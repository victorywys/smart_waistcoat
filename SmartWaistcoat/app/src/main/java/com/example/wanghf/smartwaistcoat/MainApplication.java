package com.example.wanghf.smartwaistcoat;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.example.wanghf.smartwaistcoat.inputdata.DataParseService;
import com.example.wanghf.smartwaistcoat.inputdata.WaistcoatData;
import com.example.wanghf.smartwaistcoat.inputdata.WiFiConnectService;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wanghf on 2017/4/10.
 */

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";

    private static LinkedBlockingQueue<Byte> bytes = null;
    private static LinkedBlockingQueue<WaistcoatData> queue = null;
    private static LinkedBlockingQueue<Integer> spoQueue = null;
    private static LinkedBlockingQueue<Integer> ecgQueue = null;
    private WiFiConnectService wiFiConnectService;
    private DataParseService dataParseService;



    @Override
    public void onCreate() {
        final Context context = MainApplication.this;

        if (queue == null) {
            queue = new LinkedBlockingQueue<>();
        }

        spoQueue = new LinkedBlockingQueue<>();
        ecgQueue = new LinkedBlockingQueue<>();

        if (bytes == null) {
            bytes = new LinkedBlockingQueue<>();
        }

        bindService(new Intent(context, WiFiConnectService.class),
                wiFiConnectServiceConn, Context.BIND_AUTO_CREATE);
        bindService(new Intent(context, DataParseService.class),
                dataParseServiceConn, Context.BIND_AUTO_CREATE);
        super.onCreate();
    }

    private ServiceConnection wiFiConnectServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if (service != null) {
                wiFiConnectService = ((WiFiConnectService.ServiceBinder) service).getService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            wiFiConnectService = null;
        }
    };

    private ServiceConnection dataParseServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if (service != null) {
                dataParseService = ((DataParseService.ServiceBinder) service).getService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            dataParseService = null;
        }
    };


    public static LinkedBlockingQueue getQueue(){
        return queue;
    }

    public static LinkedBlockingQueue<Integer> getSpoQueue() {
        return spoQueue;
    }
    public static LinkedBlockingQueue<Integer> getEcgQueue() {
        return ecgQueue;
    }

    public static LinkedBlockingQueue getBytes() {
        return bytes;
    }
}
