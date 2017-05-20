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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wanghf on 2017/4/17.
 */

public class MainController {
    private static final String TAG = "MainController";

    private Context context;
    private LinkedBlockingQueue queue;
    private LinkedBlockingQueue ecgQueue;
    private LinkedBlockingQueue spoQueue;
    private List<Integer> spoList;
    private ControllerThread controllerThread;

    private boolean alarmXinlv;
    private boolean alarmWendu;
    private boolean alarmXueyang;
    private boolean alarmYali;
    private boolean alarmZukang;
    private boolean alarmZhenling;
    private boolean alarmDuanxin;
    private boolean alarmDianhua;

    private final double[] b = new double[]{1, 0.7 ,1};

    public MainController(Context context, LinkedBlockingQueue queue, LinkedBlockingQueue ecgQueue) {
        this.context = context;
        this.spoQueue = queue;
        this.ecgQueue = ecgQueue;
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
        int maxSpo = 0;
        int minSpo = Integer.MAX_VALUE;
        int maxEcg = Integer.MIN_VALUE;
        @Override
        public void run() {
//            queue.clear();
            spoQueue.clear();
            spoList = new ArrayList<>();

            while (ControllerThread.this.running) {
                try {
                    int spo = (int) spoQueue.poll();
                    spoList.add(spo);
                    int size = spoList.size();
                    maxSpo = Integer.MIN_VALUE;
                    minSpo = Integer.MAX_VALUE;

                    for (int i = 0; i < size; i++) {
                        if (spoList.get(i) > maxSpo) {
                            maxSpo = spoList.get(i);
                        }
                        else if (spoList.get(i) < minSpo) {
                            minSpo = spoList.get(i);
                        }
                    }

                    if (spoList.size() >= 750) {
                        spoList.remove(0);
                    }
                    BroadcastUtil.updateImpedance(context, spo, maxSpo, minSpo);
//                    BroadcastUtil.updateECG(context, (int) ecgQueue.getFirst(), 0);

//                    if (ecgQueue.size() >=  500) {
//                        for (int i = 0; i < 500; i++) {
////                            if ((int) ecgQueue.get(i) > maxEcg) {
////                                maxEcg = ((int) ecgQueue.get(i) - maxEcg);
////                            }
//                            maxEcg += ((int)ecgQueue.get(i) - maxEcg) / (i + 1);
//                        }
//                        BroadcastUtil.updateECG(context, (int) ecgQueue.remove(), maxEcg * 3);
//                    }

                }
                catch (Exception e) {
                    Log.i(TAG, "controller exception!" + e.toString());
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


    private double firFilter(double in) {
        double out = 0;
        out = in * b[0];
        return out;
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
