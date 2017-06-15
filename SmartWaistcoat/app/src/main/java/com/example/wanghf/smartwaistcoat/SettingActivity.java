package com.example.wanghf.smartwaistcoat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wanghf.myapplication.R;
import com.example.wanghf.smartwaistcoat.controller.MainController;
import com.example.wanghf.smartwaistcoat.inputdata.WiFiConnectService;
import com.example.wanghf.smartwaistcoat.utils.BroadcastUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by wanghf on 2017/5/14.
 */

public class SettingActivity extends Activity {

    private TextView textView;
    private Context context;

//    private Thread

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

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(statusReceiver);
    }

    public void onResume() {
        super.onResume();
//        textView.setText("连接状态: 未连接");
        IntentFilter intentFilter = new IntentFilter(BroadcastUtil.ACTION_UPDATE_CONNECT);
        LocalBroadcastManager.getInstance(context).registerReceiver(statusReceiver, intentFilter);
    }

    private BroadcastReceiver statusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastUtil.ACTION_UPDATE_CONNECT)) {
                boolean connect = intent.getBooleanExtra("CONNECT", false);
                if (connect) {
                    textView.setText("连接状态: 已连接");
                }
                else  {
                    textView.setText("连接状态: 未连接");
                }
            }
        }
    };

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
//
//    Runnable myRun = new Runnable() {
//        @Override
//        public void run() {
//            BroadcastUtil.reconnect(context);
////
//            Message msg = new Message();
//            Bundle data = new Bundle();
//            data.putString("value", "请求结果");
//            msg.setData(data);
//            handler.sendMessage(msg);
//        }
//    };
//
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Bundle data = msg.getData();
//            String val = data.getString("value");
//            Log.i("mylog", "请求结果为-->" + val);
//            // TODO
//            // UI界面的更新等相关操作
//        }
//    };
}
