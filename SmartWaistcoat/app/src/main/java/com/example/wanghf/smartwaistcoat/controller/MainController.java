package com.example.wanghf.smartwaistcoat.controller;

import android.content.Context;
import android.util.Log;

import com.example.wanghf.smartwaistcoat.inputdata.WaistcoatData;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wanghf on 2017/4/17.
 */

public class MainController {
    private static final String TAG = "MainController";

    private Context context;
    private LinkedBlockingQueue queue;
    private ControllerThread controllerThread;

    public MainController(Context context, LinkedBlockingQueue queue) {
        this.context = context;
        this.queue = queue;
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
}
