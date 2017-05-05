package com.example.wanghf.smartwaistcoat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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

        editTextSSID = (EditText) findViewById(R.id.device_text_wifi_name);
        editTextPSW = (EditText) findViewById(R.id.device_text_wifi_password);
        editTextIPAddress = (EditText) findViewById(R.id.device_text_ip_address);
        editTextPort = (EditText) findViewById(R.id.device_text_port);

        // 读取配置文件
        initText();
    }

    private void initText() {
        Properties prop = loadConfig(context, Environment.getExternalStorageDirectory()+ "/AAA/device.properties");

        if (prop == null) {
            return;
        }

        String ssid = prop.get("SSID").toString();
        String pswd = prop.get("PASSWORD").toString();
        String ip = prop.get("IP").toString();
        String port = prop.get("PORT").toString();

        editTextSSID.setText(ssid);
        editTextPSW.setText(pswd);
        editTextIPAddress.setText(ip);
        editTextPort.setText(port);
    }

    public void onClickBack(View view) {
//        KeyEvent newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
//                KeyEvent.KEYCODE_BACK);
//        onKeyDown(KeyEvent.KEYCODE_BACK, newEvent);
        finish();
    }

    public void onClickSaveUpdate(View view) {
        Properties prop = loadConfig(context, Environment.getExternalStorageDirectory()+ "/AAA/device.properties");
        String ssid = editTextSSID.getText().toString();
        String pswd = editTextPSW.getText().toString();
        String ip = editTextIPAddress.getText().toString();
        String port = editTextPort.getText().toString();
        if (prop == null) {
            // 配置文件不存在的时候创建配置文件 初始化配置信息
            prop = new Properties();
        }
        prop.put("SSID", ssid);
        prop.put("PASSWORD", pswd);
        prop.put("IP", ip);
        prop.put("PORT", port);
        saveConfig(context, Environment.getExternalStorageDirectory() + "/AAA/device.properties", prop);
    }

    private Properties loadConfig(Context context, String file) {
        Properties properties = new Properties();
        try {
            FileInputStream s = new FileInputStream(file);
            properties.load(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    private boolean saveConfig(Context context, String file, Properties properties) {
        try {
            File fil = new File(file);
            if (!fil.exists())
                fil.createNewFile();
            FileOutputStream s = new FileOutputStream(fil);
            properties.store(s, "");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
