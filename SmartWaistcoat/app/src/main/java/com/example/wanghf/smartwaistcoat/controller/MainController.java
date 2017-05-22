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
    private LinkedBlockingQueue ecgQueue;
    private LinkedBlockingQueue spoQueue;
    private LinkedBlockingQueue gsenQueue;
    private List<Integer> spoList;
    private List<Integer> ecgList;
    private List<Integer> gsenList;
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

    public MainController(Context context, LinkedBlockingQueue queue, LinkedBlockingQueue ecgQueue,
                          LinkedBlockingQueue gsenQueue) {
        this.context = context;
        this.spoQueue = queue;
        this.ecgQueue = ecgQueue;
        this.gsenQueue = gsenQueue;
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
        int maxSpo;
        int minSpo;
        int maxEcg;
        int minEcg;
        int maxGsen;
        int minGsen;
        @Override
        public void run() {
            spoQueue.clear();
            ecgQueue.clear();
            gsenQueue.clear();
            spoList = new ArrayList<>();
            ecgList = new ArrayList<>();
            gsenList = new ArrayList<>();

            while (ControllerThread.this.running) {
                try {
                    if (spoQueue.size() > 0) {
                        int spo = (int) spoQueue.take();
//                        spoList.add(spo);
//                        int size = spoList.size();
//                        maxSpo = Integer.MIN_VALUE;
//                        minSpo = Integer.MAX_VALUE;
//
//                        for (int i = 0; i < size; i++) {
//                            if (spoList.get(i) > maxSpo) {
//                                maxSpo = spoList.get(i);
//                            }
//                            else if (spoList.get(i) < minSpo) {
//                                minSpo = spoList.get(i);
//                            }
//                        }

//                        if (spoList.size() >= 1000) {
//                            spo = spoList.remove(0);
                            BroadcastUtil.updateImpedance(context, spo, 0, 0);
//                        }
                    }

                    if (ecgQueue.size() > 0) {
                        int ecg = (int) ecgQueue.take();
//                        ecgList.add(ecg);
//                        int size = ecgList.size();
//                        maxEcg = Integer.MIN_VALUE;
//                        minEcg = Integer.MAX_VALUE;
//
//                        for (int i = 0; i < size; i++) {
//                            if (ecgList.get(i) > maxEcg) {
//                                maxEcg = ecgList.get(i);
//                            }
//                            else if (ecgList.get(i) < minEcg) {
//                                minEcg = ecgList.get(i);
//                            }
//                        }
                        BroadcastUtil.updateECG(context, ecg, 0, 0);
//
//                        if (ecgList.size() >= 1000) {
//                            ecg = ecgList.remove(0);
//                            BroadcastUtil.updateECG(context, ecg, maxEcg, minEcg);
//                        }
                    }

                    if (gsenQueue.size() > 0) {
                        int ecg = (int) gsenQueue.take();
//                        gsenList.add(ecg);
//                        int size = gsenList.size();
//                        maxGsen = Integer.MIN_VALUE;
//                        minGsen = Integer.MAX_VALUE;
//
//                        for (int i = 0; i < size; i++) {
//                            if (gsenList.get(i) > maxGsen) {
//                                maxGsen = gsenList.get(i);
//                            }
//                            else if (gsenList.get(i) < minGsen) {
//                                minGsen = gsenList.get(i);
//                            }
//                        }

//                        if (gsenList.size() >= 1000) {
//                            ecg = gsenList.remove(0);
                            BroadcastUtil.updateStrike(context, ecg, maxGsen, minGsen);
//                        }
                    }

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
