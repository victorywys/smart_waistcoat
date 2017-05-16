package com.example.wanghf.smartwaistcoat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.example.wanghf.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by wanghf on 2017/4/18.
 */

public class DeviceActivity extends Activity {

    private Context context;

    private EditText editTextSSID;
    private EditText editTextPSW;
    private EditText editTextIPAddress;
    private EditText editTextPort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device);

        context = this;

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new DeviceFragment())
                .commit();

//        SharedPreferences sharedPreferences = getSharedPreferences("device", MODE_PRIVATE);
//        String a = sharedPreferences.getString("device_ip", "no");
//        Log.i("DEVICE", a);
    }
}
