package com.example.wanghf.smartwaistcoat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.wanghf.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by wanghf on 2017/4/18.
 */

public class EmergencyContactActivity extends Activity {

    private EditText editTextCall;
    private EditText editTextMSG1;
    private EditText editTextMSG2;
    private EditText editTextMSG3;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contact);

        context = this;

        initViews();
    }

    private void initViews() {
        editTextCall = (EditText) findViewById(R.id.contact_text_call);
        editTextMSG1 = (EditText) findViewById(R.id.contact_text_message1);
        editTextMSG2 = (EditText) findViewById(R.id.contact_text_message2);
        editTextMSG3 = (EditText) findViewById(R.id.contact_text_message3);

        Properties prop = loadConfig(context, Environment.getExternalStorageDirectory()+ "/AAA/person.properties");
        if (prop == null) {
            return;
        }

        String call = prop.get("CALL").toString();
        String msg1 = prop.get("MSG1").toString();
        String msg2 = prop.get("MSG2").toString();
        String msg3 = prop.get("MSG3").toString();

        editTextCall.setText(call);
        editTextMSG1.setText(msg1);
        editTextMSG2.setText(msg2);
        editTextMSG3.setText(msg3);
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickSaveUpdate(View view) {
        Properties prop = loadConfig(context, Environment.getExternalStorageDirectory()+ "/AAA/contact.properties");
        String call = editTextCall.getText().toString();
        String msg1 = editTextMSG1.getText().toString();
        String msg2 = editTextMSG2.getText().toString();
        String msg3 = editTextMSG3.getText().toString();
        if (prop == null) {
            // 配置文件不存在的时候创建配置文件 初始化配置信息
            prop = new Properties();
        }
        prop.put("CALL", call);
        prop.put("MSG1", msg1);
        prop.put("MSG2", msg2);
        prop.put("MSG3", msg3);

        saveConfig(context, Environment.getExternalStorageDirectory() + "/AAA/contact.properties", prop);
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
