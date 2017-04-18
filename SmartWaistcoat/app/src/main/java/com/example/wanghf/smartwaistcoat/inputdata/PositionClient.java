package com.example.wanghf.smartwaistcoat.inputdata;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * Created by SmallLong on 2016/7/4.
 */
public abstract class PositionClient implements Runnable {
    private final static String TAG = "PositionClient";
    private Context mContext;
    private Socket mSocket;
    private String server;
    private int port;
    private SocketTransceiver socketTransceiver;
    private boolean connected = false;
    private volatile boolean closed = false;
    final Handler handler = new Handler();

    private OnDataRecvedListener onDataRecvedListener;
    private boolean tryingConnect = false;

    public PositionClient(Context context, String ip, int port) {
        this.mContext = context;
        this.server = ip;
        this.port = port;
    }

    public void connect() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            mSocket = new Socket();
            mSocket.connect(new InetSocketAddress(InetAddress.getByName(this.server), this.port), 3000);
            mSocket.setKeepAlive(true);
            mSocket.setSoTimeout(3000);
            //mSocket.setSoTimeout(5000);
            socketTransceiver = new SocketTransceiver(mSocket) {
                @Override
                public void onReceive(InetAddress addr, String s) {
                   // Log.i(TAG, "recv: " + s);
                    PositionClient.this.onReceive(this, s);
                    onDataRecvedListener.newDataCome(s);
                }

                @Override
                public void onDisconnect(InetAddress addr) {
                    Log.d(TAG, "Disconnected");
                    PositionClient.this.onDisconnect(this);
                }
            };
            socketTransceiver.start();
            connected = true;
            tryingConnect = false;
        } catch (Exception e) {
            Log.i(TAG, "连接到PositionCollector失败");
            //e.printStackTrace();
            this.onConnectFailed();
            //连接失败,需要重连;
            //final Handler handler = new Handler(mContext.getMainLooper());
            //final Handler handler = new Handler();
            //int runCount = 0;// 全局变量，用于判断是否是第一次执行
            Runnable runnable = new Runnable(){
                @Override
                public void run() {
                    Log.i(TAG, "开始重连");
                    Thread connectThread = new Thread() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    };
                    connectThread.start();
                }
            };
            handler.postDelayed(runnable, 5000);// 打开定时器，执行操作
        }

    }

    public void doConnect(){
        try {
            if (mSocket != null) {
                try {
                    mSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //mSocket = new Socket(this.server, this.port);
            mSocket = new Socket();
            mSocket.connect(new InetSocketAddress(InetAddress.getByName(this.server), this.port), 3000);
            mSocket.setKeepAlive(true);
            mSocket.setSoTimeout(3000);
            if (socketTransceiver != null) {
                this.disconnect();
            }
            socketTransceiver = new SocketTransceiver(mSocket) {
                @Override
                public void onReceive(InetAddress addr, String s) {
                    //Log.i(TAG, "recv: " + s);
                    PositionClient.this.onReceive(this, s);
                    onDataRecvedListener.newDataCome(s);
                }

                @Override
                public void onDisconnect(InetAddress addr) {
                    Log.d(TAG, "Disconnected");
                    PositionClient.this.onDisconnect(this);
                }
            };
            socketTransceiver.start();
            connected = true;
            tryingConnect = false;
        } catch (Exception e) {
            //e.printStackTrace();
            Log.i(TAG, "连接到PositionCollector失败");
            this.onConnectFailed();
            //连接失败,需要重连;
            //final Handler handler = new Handler(mContext.getMainLooper());
            //final Handler handler = new Handler();
            //int runCount = 0;// 全局变量，用于判断是否是第一次执行
            Runnable runnable = new Runnable(){
                @Override
                public void run() {
                    Log.i(TAG, "开始重连");
                    Thread connectThread = new Thread() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    };
                    connectThread.start();

                }
            };
            handler.postDelayed(runnable, 5000);// 打开定时器，执行操作
        }
    }

    /**
     * 断开连接
     * <p>
     * 连接断开，回调{@code onDisconnect()}
     */
    public void disconnect() {
        if (socketTransceiver != null) {
            socketTransceiver.stop();
            socketTransceiver = null;
        }
    }

    /**
     * 判断是否连接
     *
     * @return 当前处于连接状态，则返回true
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * 获取Socket收发器
     *
     * @return 未连接则返回null
     */
    public SocketTransceiver getTransceiver() {
        if (isConnected()) {
            return socketTransceiver;
        } else {
            return null;
        }
    }

    /**
     * 连接建立
     *
     * @param transceiver
     *            SocketTransceiver对象
     */
    public abstract void onConnect(SocketTransceiver transceiver);

    /**
     * 连接建立失败
     */
    public abstract void onConnectFailed();

    /**
     * 接收到数据
     * <p>
     * 注意：此回调是在新线程中执行的
     *
     * @param transceiver
     *            SocketTransceiver对象
     * @param s
     *            字符串
     */
    public abstract void onReceive(SocketTransceiver transceiver, String s);

    /**
     * 连接断开
     * <p>
     * 注意：此回调是在新线程中执行的
     *
     * @param transceiver
     *            SocketTransceiver对象
     */
    public abstract void onDisconnect(SocketTransceiver transceiver);

    public void setOnDataRecvedListener(OnDataRecvedListener onDataRecvedListener) {
        this.onDataRecvedListener = onDataRecvedListener;
    }

    public void tryConnect() {
        tryingConnect = true;
        doConnect();
    }

    public boolean isConnecting() {
        return tryingConnect;
    }

}
