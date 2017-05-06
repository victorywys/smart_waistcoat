package com.example.wanghf.smartwaistcoat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.Window;

import com.androidplot.xy.XYPlot;
import com.example.wanghf.myapplication.R;
import com.example.wanghf.smartwaistcoat.controller.MainController;
import com.example.wanghf.smartwaistcoat.utils.BroadcastUtil;

import java.io.FileInputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private XYPlot impulsePlot;
    private XYPlot xinlvPlot;
    private XYPlot strikePlot;

    private String callNumber = "";
    private String msgNumber1 = "";
    private String msgNumber2 = "";
    private String msgNumber3 = "";

    private boolean showingSetting;
    private boolean showingCurve = true;
    private boolean showingSource;
    private MainController mainController;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        mainController = new MainController(context, MainApplication.getQueue());

        initPlots();

        initConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainController.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastUtil.ACTION_PHONE_CALL);
        intentFilter.addAction(BroadcastUtil.ACTION_SEND_MESSAGE);
        LocalBroadcastManager.getInstance(context).registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(context).unregisterReceiver(myReceiver);
    }

    private void initPlots() {
        impulsePlot = (XYPlot) findViewById(R.id.main_plot_impulse);
        xinlvPlot = (XYPlot) findViewById(R.id.main_plot_ecg);
        strikePlot = (XYPlot) findViewById(R.id.main_plot_strike);
    }

    private void initConfig() {
        Properties prop = loadConfig(context, Environment.getExternalStorageDirectory() + "/AAA/contact.properties");
        if (prop != null) {
            callNumber = prop.get("CALL").toString();
            msgNumber1 = prop.get("MSG1").toString();
            msgNumber2 = prop.get("MSG2").toString();
            msgNumber3 = prop.get("MSG3").toString();
        }
    }

    public void onClickShowCurve(View view) {
        if (showingSetting) {
            showingSetting = false;
            findViewById(R.id.relative_setting).setVisibility(View.GONE);
        }
        if (showingCurve) {
            findViewById(R.id.linear_curve).setVisibility(View.GONE);
            findViewById(R.id.linear_table).setVisibility(View.VISIBLE);
            showingCurve = false;
        } else {
            findViewById(R.id.linear_table).setVisibility(View.GONE);
            findViewById(R.id.linear_curve).setVisibility(View.VISIBLE);
            showingCurve = true;
        }
    }

    public void onClickSettings(View view) {
        if (showingSetting) {
            findViewById(R.id.relative_setting).setVisibility(View.GONE);
            if (showingCurve) {
                findViewById(R.id.linear_curve).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.linear_table).setVisibility(View.VISIBLE);
            }
            showingSetting = false;
        } else {
            findViewById(R.id.relative_setting).setVisibility(View.VISIBLE);
            if (showingCurve) {
                findViewById(R.id.linear_curve).setVisibility(View.GONE);
            } else {
                findViewById(R.id.linear_table).setVisibility(View.GONE);
            }
            showingSetting = true;
        }
    }

    public void onClickSettingPersonInfo(View view) {
        startActivity(new Intent(MainActivity.this, PersonalInfoActivity.class));
    }

    public void onClickEmergencyContact(View view) {
        startActivity(new Intent(MainActivity.this, EmergencyContactActivity.class));
    }

    public void onClickAlarm(View view) {
        startActivity(new Intent(MainActivity.this, AlarmActivity.class));
    }

    public void onClickDevice(View view) {
        startActivity(new Intent(MainActivity.this, DeviceActivity.class));
    }

    public void onClickChangeMode(View view) {
        BroadcastUtil.changeDataSource(context, 1);
//        if (showingSetting) {
//            showingSetting = false;
//            findViewById(R.id.relative_setting).setVisibility(View.GONE);
//        }
//        if (showingCurve) {
//            findViewById(R.id.linear_curve).setVisibility(View.GONE);
//            findViewById(R.id.linear_table).setVisibility(View.VISIBLE);
//            showingCurve = false;
//        } else {
//            findViewById(R.id.linear_table).setVisibility(View.GONE);
//            findViewById(R.id.linear_curve).setVisibility(View.VISIBLE);
//            showingCurve = true;
//        }
    }

    /**
     * 广播
     */
    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastUtil.ACTION_PHONE_CALL)) {
                call(callNumber);
            }

            if (action.equals(BroadcastUtil.ACTION_SEND_MESSAGE)) {
                doSendSMSTo(msgNumber1);
                doSendSMSTo(msgNumber2);
                doSendSMSTo(msgNumber3);
            }
        }
    };

    /**
     * 报警电话
     */
    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    /**
     * 调起系统发短信功能
     * @param phoneNumber
     */
    public void doSendSMSTo(String phoneNumber){
        String message = "something happens";
        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
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

}
