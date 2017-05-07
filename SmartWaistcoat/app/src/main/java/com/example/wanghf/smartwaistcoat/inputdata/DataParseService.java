package com.example.wanghf.smartwaistcoat.inputdata;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
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
    private LinkedBlockingQueue<Byte> bytesQueue;
    private Context context;

    public void onCreate() {
        if(byteFifo == null) {
            byteFifo = ByteFifo.getInstance();
        }
        if(queue == null){
            queue = MainApplication.getQueue();
        }
        if (bytesQueue == null) {
            bytesQueue = MainApplication.getBytes();
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
        private final int head1 = 1;
        private final int head2 = 2;
        private final int head3 = 3;
        private final int head4 = 4;
        private final int head5 = 5;
        private int dataCount = 0;
        private int state = noneHead;
        private byte[] dataItem = new byte[64];

        public void run() {
            while (!interrupted()) {
                switch (state) {
                    case noneHead:
                        try {
                            byte head = bytesQueue.poll(2500, TimeUnit.MILLISECONDS);
                            if (head > 0) {
                                state = head;
                                dataCount = 0;
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "bytesQueue is empty");
                        }
                        break;
                    case head1:
                        while (!bytesQueue.isEmpty()) {
                            if (dataCount < 7) {
                                try {
                                   dataItem[dataCount++] = bytesQueue.poll(2500, TimeUnit.MILLISECONDS);
                                } catch (Exception e) {
                                    Log.i(TAG, "bytesQueue is empty");
                                }
                            } else {
                                dataParse1(Arrays.copyOfRange(dataItem, 0, dataCount));
                                state = noneHead;
                                break;
                            }
                        }
                        break;
                    case head2:
                        while (!bytesQueue.isEmpty()) {
                            if (dataCount < 15) {
                                try {
                                    dataItem[dataCount++] = bytesQueue.poll(2500, TimeUnit.MILLISECONDS);
                                } catch (Exception e) {
                                    Log.i(TAG, "bytesQueue is empty");
                                }
                            } else {
                                dataParse2(Arrays.copyOfRange(dataItem, 0, dataCount));
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

        private void dataParse1(byte[] buffer) {

        }

        private void dataParse2(byte[] buffer) {

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
        if (!bytesQueue.isEmpty()) {
            bytesQueue.clear();
        }
        if (!queue.isEmpty())
            queue.clear();
    }
}
