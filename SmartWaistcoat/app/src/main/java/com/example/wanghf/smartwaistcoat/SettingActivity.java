package com.example.wanghf.smartwaistcoat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.example.wanghf.myapplication.R;
import com.example.wanghf.smartwaistcoat.controller.MainController;
import com.example.wanghf.smartwaistcoat.utils.BroadcastUtil;

/**
 * Created by wanghf on 2017/5/14.
 */

public class SettingActivity extends Activity {

    private TextView textView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        context = this;

        textView = (TextView) findViewById(R.id.text_connect_state);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("connect_state", false)) {
            textView.setText("连接状态: 已连接");
        }
        else {
            textView.setText("连接状态: 未连接");
        }

    }

    /**
     * 个人信息
     */
    public void onClickSettingPersonInfo(View view) {
        startActivity(new Intent(SettingActivity.this, PersonalInfoActivity.class));
    }

    /**
     * 紧急通信
     */
    public void onClickEmergencyContact(View view) {
        startActivity(new Intent(SettingActivity.this, EmergencyContactActivity.class));
    }

    /**
     * 设置报警信息
     */
    public void onClickAlarm(View view) {
        startActivity(new Intent(SettingActivity.this, AlarmActivity.class));
    }

    public void onClickReconnect(View view) {
        BroadcastUtil.reconnect(context);
    }

    /**
     * 设备信息
     */
    public void onClickDevice(View view) {
        startActivity(new Intent(SettingActivity.this, DeviceActivity.class));
    }

    /**
     * 选择数据源
     */
    public void onClickDataSource(View view) {
        startActivity(new Intent(SettingActivity.this, SourceActivity.class));
    }

    public void onClickFilter(View view) {
        startActivity(new Intent(SettingActivity.this, FilterActivity.class));
    }
}
