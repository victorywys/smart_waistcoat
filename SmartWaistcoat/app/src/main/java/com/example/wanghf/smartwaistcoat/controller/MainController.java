package com.example.wanghf.smartwaistcoat.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.wanghf.smartwaistcoat.inputdata.WaistcoatData;
import com.example.wanghf.smartwaistcoat.utils.BroadcastUtil;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wanghf on 2017/4/17.
 */

public class MainController {
    private static final String TAG = "MainController";

    private Context context;
    private LinkedBlockingQueue queue;
    private ControllerThread controllerThread;

    private boolean alarmXinlv;
    private boolean alarmWendu;
    private boolean alarmXueyang;
    private boolean alarmYali;
    private boolean alarmZukang;
    private boolean alarmZhenling;
    private boolean alarmDuanxin;
    private boolean alarmDianhua;

    public MainController(Context context, LinkedBlockingQueue queue) {
        this.context = context;
        this.queue = queue;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        alarmXinlv = sharedPreferences.getBoolean("xinlv", false);
        alarmWendu = sharedPreferences.getBoolean("wendu", false);
        alarmXueyang = sharedPreferences.getBoolean("xueyang", false);
        alarmYali = sharedPreferences.getBoolean("yali", false);
        alarmZukang = sharedPreferences.getBoolean("zukang", false);
        alarmZhenling = sharedPreferences.getBoolean("zhenling", false);
        alarmDuanxin = sharedPreferences.getBoolean("duanxin", false);
        alarmDianhua = sharedPreferences.getBoolean("dianhua", false);
    }

    public void onResume() {
        controllerThread = new ControllerThread();
        controllerThread.start();
    }

    private class ControllerThread extends Thread implements Runnable {
        volatile boolean running = true;
        @Override
        public void run() {
            queue.clear();

            while (ControllerThread.this.running) {
                try {
                    WaistcoatData waistcoatData = (WaistcoatData) queue.take();

                }
                catch (Exception e) {
                    Log.i(TAG, "controller exception!");
                }
            }
        }
    }

    private void alarmDecision(WaistcoatData data) {

    }


    public void setAlarmDianhua(boolean alarmDianhua) {
        this.alarmDianhua = alarmDianhua;
    }

    public void setAlarmDuanxin(boolean alarmDuanxin) {
        this.alarmDuanxin = alarmDuanxin;
    }

    public void setAlarmWendu(boolean alarmWendu) {
        this.alarmWendu = alarmWendu;
    }

    public void setAlarmXinlv(boolean alarmXinlv) {
        this.alarmXinlv = alarmXinlv;
    }

    public void setAlarmYali(boolean alarmYali) {
        this.alarmYali = alarmYali;
    }

    public void setAlarmXueyang(boolean alarmXueyang) {
        this.alarmXueyang = alarmXueyang;
    }

    public void setAlarmZukang(boolean alarmZukang) {
        this.alarmZukang = alarmZukang;
    }

    public void setAlarmZhenling(boolean alarmZhenling) {
        this.alarmZhenling = alarmZhenling;
    }


    private WaistcoatData firFilter(WaistcoatData data) {
        WaistcoatData outData = new WaistcoatData();
        return outData;
    }

    /**
     * 报警电话
     */
    private void makeCall() {
        BroadcastUtil.makePhoneCall(context);
    }

    private void sendMsg(String msg) {
        BroadcastUtil.sendMessage(context, msg);
    }

}
