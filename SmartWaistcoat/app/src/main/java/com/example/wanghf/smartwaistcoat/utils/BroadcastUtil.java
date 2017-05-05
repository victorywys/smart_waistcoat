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

    private BroadcastUtil() {
        throw new AssertionError();
    }

    public static void makePhoneCall(Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
