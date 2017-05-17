package com.example.wanghf.smartwaistcoat.inputdata;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.wanghf.smartwaistcoat.MainApplication;
import com.example.wanghf.smartwaistcoat.utils.BroadcastUtil;
import com.example.wanghf.smartwaistcoat.utils.ByteUtil;
import com.example.wanghf.smartwaistcoat.utils.FileUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wanghf on 2017/4/10.
 */

public class WiFiConnectService extends Service {
    private static final String TAG = "WiFiConnectService";

    private int PORT = 10181;
    private String IP = "192.168.21.3";

    private final Context context;
    private ConnectThread connectThread;                 // 监听端口
    private volatile ConnectedThread connectedThread;    // 通信
    private LinkedBlockingQueue<Byte> revBytes;
    private FileUtil fileUtil = new FileUtil();

    private HashMap<Integer, byte[]> sourceMap = new HashMap<Integer, byte[]>() {
        {
            put(1, new byte[]{(byte) 0x51, (byte) 0x81, (byte) 0x0d, (byte) 0x0a});
            put(2, new byte[]{(byte) 0x52, (byte) 0x81, (byte) 0x0d, (byte) 0x0a});
            put(3, new byte[]{(byte) 0x53, (byte) 0x81, (byte) 0x0d, (byte) 0x0a});
            put(4, new byte[]{(byte) 0x54, (byte) 0x81, (byte) 0x0d, (byte) 0x0a});
            put(5, new byte[]{(byte) 0x55, (byte) 0x81, (byte) 0x0d, (byte) 0x0a});
            put(6, new byte[]{(byte) 0x56, (byte) 0x81, (byte) 0x0d, (byte) 0x0a});

            put(7, new byte[]{(byte) 0x58, (byte) 0x81, (byte) 0x0d, (byte) 0x0a});
            put(8, new byte[]{(byte) 0x58, (byte) 0x83, (byte) 0x0d, (byte) 0x0a});
            put(9, new byte[]{(byte) 0x58, (byte) 0x85, (byte) 0x0d, (byte) 0x0a});
            put(10, new byte[]{(byte) 0x59, (byte) 0x81, (byte) 0x0d, (byte) 0x0a});
            put(11, new byte[]{(byte) 0x59, (byte) 0x83, (byte) 0x0d, (byte) 0x0a});
            put(12, new byte[]{(byte) 0x59, (byte) 0x85, (byte) 0x0d, (byte) 0x0a});
        }
    };

    private HashMap<Integer, byte[]> stopMap = new HashMap<Integer, byte[]>() {
        {
            put(1, new byte[]{(byte) 0x51, (byte) 0x82, (byte) 0x0d, (byte) 0x0a});
            put(2, new byte[]{(byte) 0x52, (byte) 0x82, (byte) 0x0d, (byte) 0x0a});
            put(3, new byte[]{(byte) 0x53, (byte) 0x82, (byte) 0x0d, (byte) 0x0a});
            put(4, new byte[]{(byte) 0x54, (byte) 0x82, (byte) 0x0d, (byte) 0x0a});
            put(5, new byte[]{(byte) 0x55, (byte) 0x82, (byte) 0x0d, (byte) 0x0a});
            put(6, new byte[]{(byte) 0x56, (byte) 0x82, (byte) 0x0d, (byte) 0x0a});

            put(7, new byte[]{(byte) 0x58, (byte) 0x82, (byte) 0x0d, (byte) 0x0a});
            put(8, new byte[]{(byte) 0x58, (byte) 0x84, (byte) 0x0d, (byte) 0x0a});
            put(9, new byte[]{(byte) 0x58, (byte) 0x86, (byte) 0x0d, (byte) 0x0a});
            put(10, new byte[]{(byte) 0x59, (byte) 0x82, (byte) 0x0d, (byte) 0x0a});
            put(11, new byte[]{(byte) 0x59, (byte) 0x84, (byte) 0x0d, (byte) 0x0a});
            put(12, new byte[]{(byte) 0x59, (byte) 0x86, (byte) 0x0d, (byte) 0x0a});
        }
    };

    public static Thread heartBeatThread = null;

    /**
     * Constructor
     */
    public WiFiConnectService() {
        context = this;
    }

    @Override
    public void onCreate() {
        if (revBytes == null) {
            revBytes = MainApplication.getBytes();
        }

        initConnectInfo();

        if (connectThread == null) {
            connectThread = new ConnectThread();
            connectThread.start();
        }
        super.onCreate();
    }

    /**
     * 初始化ip和端口
     */
    private void initConnectInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        IP = sharedPreferences.getString("device_ip", "10.10.100.254");
        PORT = Integer.parseInt(sharedPreferences.getString("device_port", "8899"));
    }

    /**
     * Server socket 所在线程
     */
    private class ConnectThread extends Thread {
        private Socket socket;

        public void run() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try {
                socket = new Socket(InetAddress.getByName(IP), PORT);
            } catch (Exception e) {
                editor.putBoolean("connect_state", false);
                editor.apply();
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (Exception ex) {
                    return;
                }
            }
            editor.putBoolean("connect_state", true);
            editor.apply();
            connectedThread = new ConnectedThread(socket);
            connectedThread.start();
        }

        void cancel() {
            Log.d(TAG, "connectedthread is canceled12333");
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
            if (revBytes != null) {
                revBytes.clear();
            }
        }
    }

    /**
     * 与板卡通信的 socket 所在线程
     */
    public class ConnectedThread extends Thread {
        private Socket mSocket;
        private InputStream inputStream;
        private OutputStream outputStream;

        ConnectedThread(Socket socket) {
            if ( socket == null ) {
                return;
            }
            mSocket = socket;
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (Exception ignore) {

            }
            IntentFilter intentFilter = new IntentFilter(BroadcastUtil.ACTION_RECEIVE_DATA);
            intentFilter.addAction(BroadcastUtil.ACTION_STOP_DATA);
            LocalBroadcastManager.getInstance(context).registerReceiver(instructionListener,intentFilter);
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bufferLength;

            while (!interrupted()) {
                try {
                    bufferLength = inputStream.read(buffer);
                } catch (Exception e) {
                    break;
                }
                if (bufferLength > 0) {
//                    byte[] save = new byte[bufferLength];
//                    for (int i = 0; i<bufferLength; i++) {
//                        save[i] = buffer[i];
//                    }
//                    Log.i(TAG, new String(buffer, 0, bufferLength));
//                    fileUtil.write2SDFromInputByte("AAB", "ll.txt", save.clone());
                    for (int i = 0; i < bufferLength; i++) {
                            try {
                                revBytes.put(buffer[i]);
                            } catch (Exception e) {
                            Log.i(TAG, "InterruptedException");
                        }
                    }
                }
            }
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("connect_state", false);
            editor.apply();
            cancel();
        }

        /**
         * 发送心跳 ， 30s
         */
        void startHBeatThread() {
            // stop old
            stopHeartBeatThread();
            // start new
            heartBeatThread = new Thread() {
                public void run() {
                    while(!Thread.currentThread().isInterrupted()) {
                        try {
                            Log.i(TAG, "heartBeat sent!");
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        } catch (Exception ignore) {
                            ignore.printStackTrace();
                        }
                    }
                }
            };
            heartBeatThread.start();
        }

        private boolean isHeartBeatAlive() {
            return heartBeatThread.isAlive();
        }

        void stopHeartBeatThread() {
            if (heartBeatThread != null && heartBeatThread.isAlive()) {
                try {
                    heartBeatThread.interrupt();
                } catch (Exception ignore) { }
            }
        }

        /**
         * tcp写数据
         */
        private synchronized void outWrite(byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (Exception e) {
                Log.i(TAG,"heartBeat outWrite Exception");
            }
        }

        private void receiveData(int id) {
            outWrite(sourceMap.get(id));
        }

        private void stopData(int id) {
            outWrite(stopMap.get(id));
        }

        void cancel() {
            connectThread.cancel();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputStream = null;
            }
            if (mSocket != null) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mSocket = null;
            }
            if (revBytes != null) {
                revBytes.clear();
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(instructionListener);
        }

        /**
         * 数据源
         */
        private BroadcastReceiver instructionListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BroadcastUtil.ACTION_RECEIVE_DATA)) {
                    int id = intent.getIntExtra("ID", 7);
                    receiveData(id);
                }
                else if (intent.getAction().equals(BroadcastUtil.ACTION_STOP_DATA)) {
                    int id = intent.getIntExtra("ID", 7);
                    stopData(id);
                }
            }
        };
    }

    public class ServiceBinder extends Binder {
        public WiFiConnectService getService() {
            return WiFiConnectService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "on destroy");
        super.onDestroy();
        if (connectedThread != null && connectedThread.isAlive()) {
            connectedThread.interrupt();
            connectedThread.cancel();
        }
        if (connectThread != null && connectThread.isAlive()) {
            connectThread.interrupt();
            connectThread.cancel();
        }
    }
}
