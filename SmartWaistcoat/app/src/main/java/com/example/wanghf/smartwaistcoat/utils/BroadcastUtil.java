package com.example.wanghf.smartwaistcoat.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by bss on 2016/2/17.
 */
public abstract class BroadcastUtil {
    private static final String TAG = "BroadcastUtil";

    public static final String ACTION_PHONE_CALL = "com.example.PHONE_CALL";
    public static final String ACTION_SEND_MESSAGE = "com.example.SEND_MESSAGE";
    public static final String ACTION_CHANGE_DATA_SOURCE = "com.example.CHANGE_DATA_SOURCE";
    public static final String ACTION_IMPULSE_UPDATE = "com.example.IMPULSE_UPDATE";

    private BroadcastUtil() {
        throw new AssertionError();
    }

    public static void makePhoneCall(Context context) {
        Intent intent = new Intent(ACTION_PHONE_CALL);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void changeDataSource(Context context, int id) {
        Intent intent = new Intent(ACTION_CHANGE_DATA_SOURCE);
        intent.putExtra("ID", id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void updateImpulse(Context context, int value) {
        Intent intent = new Intent(ACTION_IMPULSE_UPDATE);
        intent.putExtra("IMPULSE", value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
