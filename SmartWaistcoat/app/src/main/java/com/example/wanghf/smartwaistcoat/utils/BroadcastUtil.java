package com.example.wanghf.smartwaistcoat.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.ParcelUuid;
import android.support.v4.content.LocalBroadcastManager;

import com.example.wanghf.smartwaistcoat.inputdata.WaistcoatData;

/**
 * Created by bss on 2016/2/17.
 */
public abstract class BroadcastUtil {
    private static final String TAG = "BroadcastUtil";

    public static final String ACTION_PHONE_CALL = "com.example.PHONE_CALL";
    public static final String ACTION_SEND_MESSAGE = "com.example.SEND_MESSAGE";
    public static final String ACTION_RECEIVE_DATA = "com.example.REVEIVE_DATA";
    public static final String ACTION_STOP_DATA = "com.example.STOP_DATA";
    public static final String ACTION_TABLES_UPDATE = "com.example.TABLES_UPDATE";
    public static final String ACTION_ECG_UPDATE = "com.example.ECG_UPDATE";
    public static final String ACTION_STRIKE_UPDATE = "com.example.STRIKE_UPDATE";
    public static final String ACTION_IMPEDANCE_UPDATE = "com.example.IMPEDANCE_UPDATE";

    private BroadcastUtil() {
        throw new AssertionError();
    }

    public static void makePhoneCall(Context context) {
        Intent intent = new Intent(ACTION_PHONE_CALL);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void receiveData(Context context, int id) {
        Intent intent = new Intent(ACTION_RECEIVE_DATA);
        intent.putExtra("ID", id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void stopData(Context context, int id) {
        Intent intent = new Intent(ACTION_STOP_DATA);
        intent.putExtra("ID", id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void updateImpedance(Context context, int value) {
        Intent intent = new Intent(ACTION_IMPEDANCE_UPDATE);
        intent.putExtra("IMPEDANCE", value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void updateStrike(Context context, int value) {
        Intent intent = new Intent(ACTION_STRIKE_UPDATE);
        intent.putExtra("STRIKE", value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void updateDataTable(Context context, WaistcoatData waistcoatData) {
        Intent intent = new Intent(ACTION_TABLES_UPDATE);
        intent.putExtra("TABLES", waistcoatData);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void updateECG(Context context, int ecg) {
        Intent intent = new Intent(ACTION_ECG_UPDATE);
        intent.putExtra("ECG", ecg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
