package com.example.wanghf.smartwaistcoat;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
 * Created by wanghf on 2017/4/17.
 */

public class PersonalInfoActivity extends AppCompatActivity {

    private TextView textViewGender;
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextNum;
    private EditText editTextRecord;
    private Switch switchGender;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);

        context = this;

        initViews();
    }

    private void initViews() {
        textViewGender = (TextView) findViewById(R.id.info_text_gender);
        switchGender = (Switch) findViewById(R.id.info_switch_gender);
        editTextAge = (EditText) findViewById(R.id.info_text_age);
        editTextName = (EditText) findViewById(R.id.info_text_name);
        editTextNum = (EditText) findViewById(R.id.info_text_num);
        editTextRecord = (EditText) findViewById(R.id.info_text_record);

        switchGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    textViewGender.setText("男");
                }
                else {
                    textViewGender.setText("女");
                }
            }
        });

        Properties prop = loadConfig(context, Environment.getExternalStorageDirectory()+ "/AAA/person.properties");
        if (prop == null) {
            return;
        }

        String gender = prop.get("GENDER").toString();
        String name = prop.get("NAME").toString();
        String age = prop.get("AGE").toString();
        String num = prop.get("NUM").toString();
        String record = prop.get("RECORD").toString();

        editTextName.setText(name);
        editTextAge.setText(age);
        editTextNum.setText(num);
        editTextRecord.setText(record);

        switchGender.setChecked(gender.equals("男"));
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickSaveUpdate(View view) {
        Properties prop = loadConfig(context, Environment.getExternalStorageDirectory()+ "/AAA/person.properties");
        String gender = textViewGender.getText().toString();
        String name = editTextName.getText().toString();
        String age = editTextAge.getText().toString();
        String num = editTextNum.getText().toString();
        String record = editTextRecord.getText().toString();
        if (prop == null) {
            // 配置文件不存在的时候创建配置文件 初始化配置信息
            prop = new Properties();
        }
        prop.put("GENDER", gender);
        prop.put("NAME", name);
        prop.put("AGE", age);
        prop.put("NUM", num);
        prop.put("RECORD", record);
        saveConfig(context, Environment.getExternalStorageDirectory() + "/AAA/person.properties", prop);
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
