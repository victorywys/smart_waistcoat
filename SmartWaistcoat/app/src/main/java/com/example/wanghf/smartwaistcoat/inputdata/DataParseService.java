package com.example.wanghf.smartwaistcoat.inputdata;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.wanghf.smartwaistcoat.MainApplication;
import com.example.wanghf.smartwaistcoat.utils.ByteUtil;
import com.example.wanghf.smartwaistcoat.utils.FileUtil;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghf on 2017/4/10.
 */

public class DataParseService extends Service {
    //广播相关
    public final static String ACTION_GPGGADATA_AVAILABLE = "test.gps.ACTION_GPGGADATA_AVAILABLE";
    public final static String ACTION_GPTRADATA_AVAILABLE = "test.gps.ACTION_GPTRADATA_AVAILABLE";
    public final static String ACTION_PARSECOUNT_AVAILABLE = "plana.gps.ACTION_PARSECOUNT_AVAILABLE";
    public final static String EXTRA_DATA = "aplanparse.EXTRA_DATA";

    private final String TAG = "PlanAParseService";
    private ByteFifo byteFifo = null;
    private TranslateThread translateThread = null;
    private ByteUtil byteUtil = new ByteUtil();
    private FileUtil fileUtil = new FileUtil();
    private String dir = "AAA";
    private String fileName = "gnss.txt";
    private LinkedBlockingQueue<WaistcoatData> queue;
    private LinkedBlockingQueue<Byte> bytes;
    private Context context;

    public void onCreate() {
        if(byteFifo == null) {
            byteFifo = ByteFifo.getInstance();
        }
        if(queue == null){
            queue = MainApplication.getQueue();
        }
        if (bytes == null) {
            bytes = MainApplication.getBytes();
        }
        if (translateThread == null) {
            translateThread = new TranslateThread();
        }
        context = this;
        if(!translateThread.isAlive()){
            translateThread.start();
            Log.d(TAG, "PARSE THREAD START");
        }
        super.onCreate();
    }
    private class TranslateThread extends Thread {
        private final int noneHead = 0;
        private final int gpsState = 1;
        private final int navState = 2;
        private final int gsensState = 3;
        private int gpsCount = 0;
        private int navCount = 0;
        private int state = noneHead;
        private byte[] gpsItem = new byte[512];
        private byte[] navItem = new byte[128];
        private byte gsensHead = (byte) 0x05;
        private byte[] navHead = ByteUtil.getBytes("NAV");

        public void run() {
            while (!interrupted()) {
                switch (state) {
                    case noneHead:
                        try {
                            byte head = bytes.poll(2500, TimeUnit.MILLISECONDS);
                            if (head == 0x5) {
                                byte head1 = bytes.poll(2500, TimeUnit.MILLISECONDS);
                                byte head2 = bytes.poll(2500, TimeUnit.MILLISECONDS);
                                byte head3 = bytes.poll(2500, TimeUnit.MILLISECONDS);
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "bytes is empty");
                        }
                        break;
                    case gpsState:
                        while (!bytes.isEmpty()) {
                            if (gpsCount < 500) {
                                try {
                                    if ((gpsItem[gpsCount++] = bytes.poll(2500, TimeUnit.MILLISECONDS)) == (byte) 0xfd)
                                        if ((gpsItem[gpsCount++] = bytes.poll(2500, TimeUnit.MILLISECONDS)) == (byte) 0xfd)
                                            if ((gpsItem[gpsCount++] = bytes.poll(2500, TimeUnit.MILLISECONDS)) == (byte) 0xfd)
                                                if ((gpsItem[gpsCount++] = bytes.poll(2500, TimeUnit.MILLISECONDS)) == (byte) 0xfd) {
                                                    if (gpsCount > 160 && gpsCount < 240) {
                                                        dataParse(Arrays.copyOfRange(gpsItem, 0, gpsCount));
                                                    }
                                                    state = noneHead;
                                                    break;
                                                }
                                } catch (Exception e) {
                                    Log.i(TAG, "bytes is empty");
                                }
                            } else {
                                state = noneHead;
                                break;
                            }
                        }
                        break;
                    case navState:
                        while (!bytes.isEmpty()) {
                            if (navCount < 120) {
                                try {
                                    if ((navItem[navCount++] = bytes.poll(2500, TimeUnit.MILLISECONDS)) == (byte) 0xd)
                                        if ((navItem[navCount++] = bytes.poll(2500, TimeUnit.MILLISECONDS)) == (byte) 0xa) {
                                            navParse(navItem, navCount);
                                            state = noneHead;
                                            break;
                                        }
                                } catch (Exception e) {
                                    Log.i(TAG, "bytes is empty");
                                }
                            } else {
                                state = noneHead;
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        private void dataParse(byte[] dataBuffer) {
            byte[] gpsByte = new byte[256];
            byte[] obdByte = new byte[64];
            int countObd = 4;
            int countGps = 0;
            int countBuffer = 0;
            for (int i = 0; i < 4; i++) {
                obdByte[i] = (byte) 0xfe;
            }
            int length = dataBuffer.length - 2;
            while (true) {
                if ((countBuffer < length) && (gpsByte[countGps++] = dataBuffer[countBuffer++]) == (byte) 0xfe) {
                    if ((gpsByte[countGps++] = dataBuffer[countBuffer++]) == (byte) 0xfe)
                        if ((gpsByte[countGps++] = dataBuffer[countBuffer++]) == (byte) 0xfe)
                            if ((gpsByte[countGps++] = dataBuffer[countBuffer++]) == (byte) 0xfe) {
                                gpsByteParse(gpsByte, countGps - 4);
                                while (true) {
                                    if (countObd < 58) {
                                        if ((obdByte[countObd++] = dataBuffer[countBuffer++]) == (byte) 0xfd)
                                            if ((obdByte[countObd++] = dataBuffer[countBuffer++]) == (byte) 0xfd)
                                                if ((obdByte[countObd++] = dataBuffer[countBuffer++]) == (byte) 0xfd)
                                                    if ((obdByte[countObd++] = dataBuffer[countBuffer++]) == (byte) 0xfd) {
                                                        if (countObd > 20) {
                                                            byte[] obdSave = obdByte.clone();
                                                            fileUtil.write2SDFromInputByte(dir, fileName, obdSave);
                                                        }
                                                        break;
                                                    }
                                    } else {
                                        break;
                                    }
                                }
                                break;
                            }
                } else if (countBuffer >= length) {
                    break;
                }
            }
        }

        /**
         * GPS解析
         */
        private void gpsByteParse(byte[] gpsBuffer, int bufferLength) {
            String info;
            String gpggaData;
            String gptraData;
            try {
                info = new String(gpsBuffer, 0, bufferLength);
                info = "$GPT" + info;
                StringTokenizer gpsTake = new StringTokenizer(info, "$");
                if (gpsTake.hasMoreTokens()) {
                    gptraData = gpsTake.nextToken();
                } else {
                    return;
                }
                if (gpsTake.hasMoreTokens()) {
                    gpggaData = gpsTake.nextToken();
                } else {
                    return;
                }
                gpggaAndGptraParse(gpggaData, gptraData);
                fileUtil.write2SDFromInputString(dir, fileName, info);       //存数据
            } catch (Exception e) {
                Log.i(TAG, "parseByteException");
                e.printStackTrace();
            }
        }

        private void gpggaAndGptraParse(String gpgga, String gptra) {
            String strAngle;
            double[] xy;
            double lati;
            double longi;
            double angle;
            double time;
            String[] splitGpgga = gpgga.split(",");
            String[] splitGptra = gptra.split(",");
            if (splitGpgga.length > 6 && splitGptra.length > 6) {
                try {
                    lati = Double.parseDouble(splitGpgga[2]);
                    longi = Double.parseDouble(splitGpgga[4]);
                    time = Float.parseFloat(splitGpgga[1]);
                } catch (NumberFormatException e) {
                    return;
                }
                time = ((int) time / 100) * 60.0f + time % 100.0f;
                strAngle = splitGptra[2];

            }
        }

        /**
         * 惯导数据
         */
        private void navParse(byte[] navItem, int bufferLength) {
            try {
                String nav = new String(navItem, 0, bufferLength);
                String lat;
                String lon;
                String heading;
                nav = "$NAV" + nav;
                String[] navSplit = nav.split(",");
                if (navSplit.length > 10) {
                    lat = navSplit[5];
                    lon = navSplit[6];
                    heading = navSplit[9];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public class ServiceBinder extends Binder {
        public DataParseService getService() {
            return DataParseService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    public void onDestroy() {
        if (translateThread != null) {
            translateThread.interrupt();
        }
        if (!bytes.isEmpty()) {
            bytes.clear();
        }
        if (!queue.isEmpty())
            queue.clear();
    }
}
