package com.example.wanghf.smartwaistcoat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidplot.Plot;
import com.androidplot.util.PlotStatistics;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYPlot;
import com.example.wanghf.myapplication.R;
import com.example.wanghf.smartwaistcoat.controller.MainController;
import com.example.wanghf.smartwaistcoat.inputdata.WaistcoatData;
import com.example.wanghf.smartwaistcoat.utils.BroadcastUtil;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int HISTORY_SIZE = 50;

    // 画曲线相关
    private XYPlot impulsePlot;
    private XYPlot xinlvPlot;
    private XYPlot strikePlot;
    private SimpleXYSeries impulseSeries;
    private SimpleXYSeries ecgSeries;
    private SimpleXYSeries strikeSeries;

    private TextView textViewDianliang;
    private TextView textViewxueyang;
    private TextView textViewWendu;
    private TextView textViewXinlv;
    private ImageButton buttonDisplayCurve;
    private ImageButton buttonDisplayTable;

    private String callNumber = "";
    private String msgNumber1 = "";
    private String msgNumber2 = "";
    private String msgNumber3 = "";

    private boolean showingCurve;
    private boolean curveActive = true;
    private boolean showingTable;
    private boolean tableActive;
    private MainController mainController;
    private Context context;

    private Redrawer redrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        mainController = new MainController(context, MainApplication.getQueue());

        initPlots();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainController.onResume();

        redrawer.start();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastUtil.ACTION_PHONE_CALL);
        intentFilter.addAction(BroadcastUtil.ACTION_SEND_MESSAGE);
        intentFilter.addAction(BroadcastUtil.ACTION_TABLES_UPDATE);
        intentFilter.addAction(BroadcastUtil.ACTION_ECG_UPDATE);
        intentFilter.addAction(BroadcastUtil.ACTION_STRIKE_UPDATE);
        intentFilter.addAction(BroadcastUtil.ACTION_IMPEDANCE_UPDATE);
        LocalBroadcastManager.getInstance(context).registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        redrawer.pause();

        LocalBroadcastManager.getInstance(context).unregisterReceiver(myReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        redrawer.finish();
    }

    private void initPlots() {
        impulsePlot = (XYPlot) findViewById(R.id.main_plot_impulse);
        xinlvPlot = (XYPlot) findViewById(R.id.main_plot_ecg);
        strikePlot = (XYPlot) findViewById(R.id.main_plot_strike);

        ecgSeries = new SimpleXYSeries("ECG");
        ecgSeries.useImplicitXVals();
        impulseSeries = new SimpleXYSeries("SPO2");
        impulseSeries.useImplicitXVals();
        strikeSeries = new SimpleXYSeries("GST");
        strikeSeries.useImplicitXVals();

        xinlvPlot.addSeries(ecgSeries, new LineAndPointFormatter(
                Color.rgb(200, 100, 100), null, null, null));
        impulsePlot.addSeries(impulseSeries, new LineAndPointFormatter(
                Color.rgb(100, 100, 200), null, null, null));
        strikePlot.addSeries(strikeSeries, new LineAndPointFormatter(
                Color.rgb(150, 200, 100), null, null, null));
        xinlvPlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED);
        xinlvPlot.setDomainStepMode(StepMode.INCREMENT_BY_VAL);
        xinlvPlot.setDomainStepValue(HISTORY_SIZE);
//        xinlvPlot.setRangeBoundaries(16776000, 16776500, BoundaryMode.FIXED);

//        impulsePlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.GROW);
//        xinlvPlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED);
//        xinlvPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1000);

        final PlotStatistics histStats = new PlotStatistics(10000, false);

        impulsePlot.addListener(histStats);
        xinlvPlot.addListener(histStats);
        strikePlot.addListener(histStats);

        redrawer = new Redrawer(
                Arrays.asList(new Plot[]{impulsePlot, strikePlot, xinlvPlot}),
                250, false);

        // 按钮
        buttonDisplayCurve = (ImageButton) findViewById(R.id.button_switch_display);
        buttonDisplayTable = (ImageButton) findViewById(R.id.button_source);

        textViewDianliang = (TextView) findViewById(R.id.text_num_dianliang);
        textViewWendu = (TextView) findViewById(R.id.text_num_wendu);
        textViewXinlv = (TextView) findViewById(R.id.text_num_xinlv);
        textViewxueyang = (TextView) findViewById(R.id.text_num_xueyangzhi);
    }

    /**
     * 切换曲线和表格
     */
    public void onClickShowCurve(View view) {
        findViewById(R.id.linear_table).setVisibility(View.GONE);
        findViewById(R.id.linear_curve).setVisibility(View.VISIBLE);

        if (tableActive) {
            buttonDisplayCurve.setImageDrawable(getResources().getDrawable(R.drawable.begin));
            buttonDisplayTable.setImageDrawable(getResources().getDrawable(R.drawable.table));
            tableActive = false;
            curveActive = true;
            return;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int id = Integer.parseInt(sharedPreferences.getString("source_id", "1"));
        // 停止数据
        if (showingTable) {
            BroadcastUtil.stopData(context, id);
            showingTable = false;
        }

        // 正在显示，停止数据
        if (showingCurve) {
            buttonDisplayCurve.setImageDrawable(getResources().getDrawable(R.drawable.begin));
            showingCurve = false;
            BroadcastUtil.stopData(context, id);
        } else {
            buttonDisplayCurve.setImageDrawable(getResources().getDrawable(R.drawable.stop));
            showingCurve = true;
            BroadcastUtil.receiveData(context, id);
        }
    }

    public void onClickShowTable(View view) {
        findViewById(R.id.linear_table).setVisibility(View.VISIBLE);
        findViewById(R.id.linear_curve).setVisibility(View.GONE);

        if (curveActive) {
            curveActive = false;
            tableActive = true;
            buttonDisplayTable.setImageDrawable(getResources().getDrawable(R.drawable.begin));
            buttonDisplayCurve.setImageDrawable(getResources().getDrawable(R.drawable.curve));
            return;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int id = Integer.parseInt(sharedPreferences.getString("source_id", "1"));

        // 停止
        if (showingCurve) {
            BroadcastUtil.stopData(context, id);
            showingCurve = false;
        }

        if (showingTable) {
            buttonDisplayTable.setImageDrawable(getResources().getDrawable(R.drawable.begin));
            showingTable = false;
            BroadcastUtil.stopData(context, id);
        } else {
            buttonDisplayTable.setImageDrawable(getResources().getDrawable(R.drawable.stop));
            showingTable = true;
            BroadcastUtil.receiveData(context, id);
        }
    }

    /**
     * 设置
     */
    public void onClickSettings(View view) {
        Intent intent = new Intent(context, SettingActivity.class);
        startActivity(intent);
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

            if (action.equals(BroadcastUtil.ACTION_TABLES_UPDATE)) {
                WaistcoatData waistcoatData = (WaistcoatData) intent.getSerializableExtra("TABLES");
                textViewWendu.setText(waistcoatData.getWendu() + "");
                textViewxueyang.setText(waistcoatData.getXueyang() + "");
                textViewDianliang.setText(waistcoatData.getDianliang() + "");
                textViewXinlv.setText(waistcoatData.getXinlv() + "");
                Log.i(TAG, "tables");
            }

            if (action.equals(BroadcastUtil.ACTION_ECG_UPDATE)) {
                int data = intent.getIntExtra("ECG", 0);

                if (ecgSeries.size() > HISTORY_SIZE) {
                    ecgSeries.removeFirst();
                }

                ecgSeries.addLast(null, data);

                Log.i(TAG, "ecg" + data);
            }

            if (action.equals(BroadcastUtil.ACTION_STRIKE_UPDATE)) {
//                int data = intent.getIntExtra("STRIKE", 0);
//
//                if (strikeSeries.size() > HISTORY_SIZE) {
//                    strikeSeries.removeFirst();
//                }
//
//                strikeSeries.addLast(null, data);
//
//                strikeSeries.setTitle("GST");
//                Log.i(TAG, "strike" + data);
            }

            if (action.equals(BroadcastUtil.ACTION_IMPEDANCE_UPDATE)) {
//                int data = intent.getIntExtra("IMPEDANCE", 0);
//
//                if (impulseSeries.size() > HISTORY_SIZE) {
//                    impulseSeries.removeFirst();
//                }
//
//                impulseSeries.addLast(null, data);
//                impulseSeries.setTitle("阻抗");
//
//                Log.i(TAG, "impedance" + data);
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
}
