package com.example.wanghf.smartwaistcoat.inputdata;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.wanghf.smartwaistcoat.MainApplication;
import com.example.wanghf.smartwaistcoat.utils.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wanghf on 2017/4/10.
 */

public class WiFiConnectService extends Service {
    private static final String TAG = "WiFiConnectService";

    private final int WAITING_TIME = 2500;
    private final int SERVER_PORT = 10181;

    // 广播相关
    public final static String ACTION_COUNT_NUMBER = "test.gps.ACTION_COUNT_NUMBER";
    public final static String ACTION_DISCONNECT_NUMBER = "test.gps.DISCONNECT_NUMBER";
    public final static String ACTION_REASON_TYPE = "test.gps.ACTION_REASON_TYPE";
    public final static String EXTRA = "test.gps.connect";
    public final static String BRAKE_SWITCHER = "org.gauto.data.BRAKE_SWITCHER";

    private final Context context;
    private ConnectThread connectThread;                 // 监听端口
    private volatile ConnectedThread connectedThread;    // 通信
    private int disconnectCount = 0;
    private String disconnectReason = "";
    private LinkedBlockingQueue<Byte> revBytes;
    private FileUtil fileUtil = new FileUtil();

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
        if (connectThread == null) {
            connectThread = new ConnectThread(SERVER_PORT);
            connectThread.start();
        }
        super.onCreate();
    }

    /**
     * Server socket 所在线程
     */
    private class ConnectThread extends Thread {
        private ServerSocket serverSocket;
        private Socket socket;

        public ConnectThread(int serverPort) {
            ServerSocket temp = null;
            try {
                temp = new ServerSocket(serverPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverSocket = temp;
        }

        public void run() {
            while (true) {
                try {
                    Log.d(TAG, "accepting");
                    socket = serverSocket.accept();
                    if (socket != null) {
                        socket.setSoTimeout(WAITING_TIME);
                    }
                    Log.d(TAG, "accepted");
                } catch (InterruptedIOException e) {
                    Log.i(TAG, "interrupted");
                    try {
                        serverSocket.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                } catch (Exception e) {
                    try {
                        Log.d(TAG, "connect err");
                        if (socket != null) {
                            socket.close();
                        }
                        continue;
                    } catch (Exception ex) {
                        return;
                    }
                }
                connectedThread = new ConnectedThread(socket);
                connectedThread.start();
                Log.d(TAG, "connected");
            }
        }

        void cancel() {
            Log.d(TAG, "connectedthread is canceled");
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
            } catch (Exception ignore) {}
            IntentFilter intentFilter = new IntentFilter(BRAKE_SWITCHER);
            LocalBroadcastManager.getInstance(context).registerReceiver(brakeListener,intentFilter);
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bufferLength,count = 0;
//            startHeartBeatThread();
            while (!interrupted()) {
                try {
                    bufferLength = inputStream.read(buffer);
                } catch (Exception e) {
                    disconnectReason = disconnectReason + "Exception:" + e.toString() + " ";
                    broadcastUpdate(ACTION_REASON_TYPE, disconnectReason);
                    break;
                }
                if (bufferLength > 0) {
                    byte[] save = new byte[bufferLength];
                    for (int i = 0; i<bufferLength; i++) {
                        save[i] = buffer[i];
                    }
                    fileUtil.write2SDFromInputByte("AAB", "origin.txt", save.clone());
                    for (int i = 0; i < bufferLength; i++) {
                        try {
                            revBytes.put(buffer[i]);
                            broadcastUpdateInt(ACTION_COUNT_NUMBER, ++count);
                        } catch (Exception e) {
                            Log.i(TAG, "InterruptedException");
                        }
                    }
                }
                if (bufferLength == -1) {
                    Log.d(TAG, "return -1");
                    disconnectReason = disconnectReason + "return -1  ";
                    broadcastUpdate(ACTION_REASON_TYPE,disconnectReason);
                    break;
                }
            }
            broadcastUpdateInt(ACTION_DISCONNECT_NUMBER, ++disconnectCount);
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
                    boolean switcher = false;
                    while(!Thread.currentThread().isInterrupted()) {
                        try {
//                            outWrite("%HEARTBEAT\r\n");           //发送心跳@
                            if (switcher) {
                                outWrite("%BRAKE ON");
                                switcher = false;
                            }else {
                                outWrite("%BRAKE OFF");
                                switcher = true;
                            }
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
        private synchronized void outWrite(String data) {
            try {
                outputStream.write(data.getBytes());
                Log.i(TAG, data);
            } catch (Exception e) {
                Log.i(TAG,"heartBeat outWrite Exception");
            }
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
            LocalBroadcastManager.getInstance(context).unregisterReceiver(brakeListener);
        }

        /**
         * 刹车指令广播
         */
        private BroadcastReceiver brakeListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BRAKE_SWITCHER)) {
                    boolean switcher = intent.getBooleanExtra("BRAKE", true);
                    if (switcher) {
                        outWrite("%BRAKE ON");
                    } else {
                        outWrite("%BRAKE OFF");
                    }
                }
            }
        };
    }

    /**
     * 广播
     */
    private void broadcastUpdate(final String action, String data) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA, data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastUpdateInt(final String action, int data) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA, data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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
