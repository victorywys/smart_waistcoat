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

import org.gauto.computing.AsUtils;
import org.gauto.db.DbHelper;
import org.gauto.utils.MediaUtil;

/**
 * Created by SmallLong on 2016/7/5.
 */
public class PositionService extends Service implements OnDataRecvedListener{
    private final static String TAG = "PositionService";

    private final static String ACTION_RECV_POS = "org.gauto.assertion.ACTION_POST_COLLECTOR";

    private PositionClient positionClient;
    private final static String POSITION_SERVER = "123.57.250.117";
    //private final static String POSITION_SERVER = "192.168.21.38";
    private final static int SERVER_PORT = 2333;
    private final Context context;

    private final static int SEND_PERIOD = 3;  //大概300ms 向server发送的周期
    private int periodCursor = 0;
    private String deviceId;
    private double[] xyr;


    public PositionService() {
        context = this;
    }

    @Override
    public void onCreate() {
        positionClient = new PositionClient(this, POSITION_SERVER, SERVER_PORT) {
            @Override
            public void onConnect(SocketTransceiver transceiver) {
                Log.i(TAG, "connected");
            }

            @Override
            public void onConnectFailed() {
                //Log.i(TAG, "connection failed");
                Log.i(TAG, "连接到PositionCollector失败");
            }

            @Override
            public void onReceive(SocketTransceiver transceiver, String s) {

            }

            @Override
            public void onDisconnect(SocketTransceiver transceiver) {
                //Log.w(TAG, "PositionService disconnected");
                if (!positionClient.isConnecting()) {
                    positionClient.tryConnect();
                }
            }
        };
        if (positionClient.isConnected()) {
            positionClient.disconnect();
        } else {
            try {
                positionClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
        if (positionClient != null) {
            positionClient.disconnect();
        }
        super.onDestroy();
    }

    public class ServiceBinder extends Binder {
        public PositionService getService() {
            return PositionService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RECV_POS);
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        deviceId = sharedPreferences.getString("set_device_id","null");
        positionClient.setOnDataRecvedListener(this);
        return new ServiceBinder();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_RECV_POS: {
                    //收到发来的数据
                    xyr = intent.getDoubleArrayExtra("XYR");
                    double speed = intent.getDoubleExtra("SPEED", 0.0);
                    int speedDirection = intent.getIntExtra("DIRECTION", 0);
                    int clockwise = intent.getIntExtra("CLOCKWISE", 0);
                    double cenX = intent.getDoubleExtra("CENTERX", 0.0);
                    double cenY = intent.getDoubleExtra("CENTERY", 0.0);

                    periodCursor++;
                    if (periodCursor == SEND_PERIOD) {
                        periodCursor = 0;
                        if (xyr != null && xyr.length == 3) {
                            long examId = DbHelper.getDbHelper().getExamId();
                             String msg = deviceId + "," + examId + "," + xyr[0] + "," + xyr[1] + ","
                                                    + speed + "," + xyr[2] + "," + speedDirection + ","
                                                    + clockwise + "," + cenX + "," + cenY + "\n";
                            //Log.i(TAG, "Send to position server: " + msg);

                            try {
                                //positionClient.sendMsg(msg);
                                if (null == positionClient.getTransceiver()) {
                                    return;
                                }
                                boolean success = positionClient.getTransceiver().send(msg);
                                if (!success && !positionClient.isConnecting()) {
                                    //没有正在重连
                                    positionClient.tryConnect();
                                }
                            } catch (Exception e) {
//                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void newDataCome(String data) {
        // 根据返回的数据做一些其他事情
        if (data == null) {
            return;
        }
        try {
            if (data.startsWith("ping") || data.endsWith("ping")) {
                return;
            }
            long examId = DbHelper.getDbHelper().getExamId();
            if (!data.trim().equals("")) {
                String[] msgs = data.split(";");
                double minDis = 11;
                for (String msg : msgs) {
                    String[] msgSplit = msg.split(",");
                    if (msgSplit.length == 10) {
                        // deviceId, examId, x, y, speed, angle, speedDirection, clockwise, cenx, ceny
                        if (examId == Long.valueOf(msgSplit[1])) {
                            int speedDirection = Integer.valueOf(msgSplit[6]);
                            int clockwise = Integer.valueOf(msgSplit[7]);
                            float speed = Float.valueOf(msgSplit[4]);
                            float x = Float.valueOf(msgSplit[2]);
                            float y = Float.valueOf(msgSplit[3]);
                            float angle = Float.valueOf(msgSplit[5]);
                            float cenX = Float.valueOf(msgSplit[8]);
                            float cenY = Float.valueOf(msgSplit[9]);
                            String deviceId = msgSplit[0];

                            if (AsUtils.getDistanceBetween2P(xyr[0], xyr[1], x, y) < minDis) {
                                AssertBrakeData brakeData = new AssertBrakeData();
                                brakeData.setAngle(angle);
                                brakeData.setSpeed(speed);
                                brakeData.setSpeedDirection(speedDirection);
                                brakeData.setX(x);
                                brakeData.setY(y);
                                brakeData.setClockwise(clockwise);
                                brakeData.setCenX(cenX);
                                brakeData.setCenY(cenY);
                                MediaUtil.updateNearbyCars(context, brakeData.clone());
                            }
                            MediaUtil.updateNpcCar(context, deviceId, msgSplit[0], x, y, angle);
                        }
                    }
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }
}
