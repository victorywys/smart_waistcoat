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

    private final double[] b = new double[]{1, 0.7 ,1};

    public MainController(Context context, LinkedBlockingQueue queue, LinkedBlockingQueue ecgQueue,
                          LinkedBlockingQueue gsenQueue) {
        this.context = context;
        this.spoQueue = queue;
        this.ecgQueue = ecgQueue;
        this.gsenQueue = gsenQueue;
    }

    public void onResume() {
        controllerThread = new ControllerThread();
        controllerThread.start();
    }

    private class ControllerThread extends Thread implements Runnable {
        volatile boolean running = true;

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
                        BroadcastUtil.updateImpedance(context, spo);
                    }

                    if (ecgQueue.size() > 0) {
                        int ecg = (int) ecgQueue.take();
                        BroadcastUtil.updateECG(context, ecg);
                    }

                    if (gsenQueue.size() > 0) {
                        int ecg = (int) gsenQueue.take();
                        BroadcastUtil.updateStrike(context, ecg);
                    }

                }
                catch (Exception e) {
                    Log.i(TAG, "controller exception!" + e.toString());
                }
            }
        }
    }

    private double firFilterECG(double in) {
        double out = 0;
        out = in * b[0];
        int n = b.length;
        for (int i = 1; i < n; i++) {
            out += b[i] * ecgList.get(n - i + 1);
        }
        return out;
    }

    private int firFilterSpo(int in) {
        return 0;
    }

    private int firFilterGsen(int in) {
        return 0;
    }

}
