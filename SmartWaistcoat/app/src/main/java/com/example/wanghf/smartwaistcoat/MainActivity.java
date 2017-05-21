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

import com.example.wanghf.myapplication.R;
import com.example.wanghf.smartwaistcoat.controller.MainController;
import com.example.wanghf.smartwaistcoat.inputdata.WaistcoatData;
import com.example.wanghf.smartwaistcoat.utils.BroadcastUtil;
import com.example.wanghf.smartwaistcoat.widget.EcgView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EcgView ecgViewUp;
    private EcgView ecgViewMid;
    private EcgView ecgViewDown;
    private TextView textViewPlotUp;
    private TextView textViewPlotMid;
    private TextView textViewPlotDown;

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

    private List<Integer> datas = new ArrayList<Integer>();
    private Queue<Integer> data0Q = new LinkedList<Integer>();

    private final int ECG_COUNT = 250;
    private final int SPO_COUNT = 250;
    private final int GSEN_COUNT = 100;
    private final int PRESS_COUNT = 10;
    private final int ZUKANG_COUNT = 1;

    private int currentECG;
    private int currentImpedance;
    private int currentStrike;
    private double avgECG;
    private double avgImpedance;
    private double avgStrike;

    private HashMap<String, Integer> sourceMap = new HashMap<String, Integer>(){
        {
            put("心电测量", 1);
            put("血氧测量", 2);
            put("G-senso测量", 3);
            put("压力测量", 4);
            put("阻抗测量", 5);
            put("01包", 6);
            put("组合包1", 7);
            put("组合包2", 8);
            put("组合包3", 9);
            put("组合包4", 10);
            put("组合包5", 11);
            put("组合包6", 12);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        mainController = new MainController(context, MainApplication.getSpoQueue(), MainApplication.getEcgQueue());

        initPlots();

//        loadDatas();
//        simulator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainController.onResume();

        currentECG = 0;
        currentImpedance = 0;
        currentStrike = 0;

        avgECG = 0;
        avgImpedance = 0;
        avgStrike = 0;

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String source = sharedPreferences.getString("data_source", "组合包1");
        BroadcastUtil.stopData(context, sourceMap.get(source));
        LocalBroadcastManager.getInstance(context).unregisterReceiver(myReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initPlots() {

        ecgViewUp = (EcgView) findViewById(R.id.ecg_view_up);
        ecgViewMid = (EcgView) findViewById(R.id.ecg_view_mid);
        ecgViewDown = (EcgView) findViewById(R.id.ecg_view_down);

        textViewPlotUp = (TextView) findViewById(R.id.text_plot_up);
        textViewPlotMid = (TextView) findViewById(R.id.text_plot_mid);
        textViewPlotDown = (TextView) findViewById(R.id.text_plot_down);

        // 按钮
        buttonDisplayCurve = (ImageButton) findViewById(R.id.button_switch_display);
        buttonDisplayTable = (ImageButton) findViewById(R.id.button_source);

        textViewDianliang = (TextView) findViewById(R.id.text_num_dianliang);
        textViewWendu = (TextView) findViewById(R.id.text_num_wendu);
        textViewXinlv = (TextView) findViewById(R.id.text_num_xinlv);
        textViewxueyang = (TextView) findViewById(R.id.text_num_xueyangzhi);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        switch (sharedPreferences.getString("data_source", "组合包1")) {
            case "心电测量":
                textViewPlotUp.setText("ECG");
                textViewPlotMid.setText("");
                textViewPlotDown.setText("");
                break;
            case "血氧测量":
                textViewPlotUp.setText("");
                textViewPlotMid.setText("SPO2");
                textViewPlotDown.setText("");
                break;
            case "G-senso测量":
                textViewPlotUp.setText("GST-X");
                textViewPlotMid.setText("GST-Y");
                textViewPlotDown.setText("GST-Z");
                break;
            case "压力测量":
                textViewPlotUp.setText("");
                textViewPlotMid.setText("压力");
                textViewPlotDown.setText("");
                break;
            case "阻抗测量":
                textViewPlotUp.setText("");
                textViewPlotMid.setText("阻抗");
                textViewPlotDown.setText("");
                break;
            case "01包":
                textViewPlotUp.setText("");
                textViewPlotMid.setText("");
                textViewPlotDown.setText("");
                break;
            case "组合包1":
                textViewPlotUp.setText("ECG");
                textViewPlotMid.setText("SPO2");
                textViewPlotDown.setText("GST-X");
                break;
            case "组合包2":
                textViewPlotUp.setText("ECG");
                textViewPlotMid.setText("SPO2");
                textViewPlotDown.setText("GST-Y");
                break;
            case "组合包3":
                textViewPlotUp.setText("ECG");
                textViewPlotMid.setText("SPO2");
                textViewPlotDown.setText("GST-Z");
                break;
            case "组合包4":
                textViewPlotUp.setText("ECG");
                textViewPlotMid.setText("阻抗");
                textViewPlotDown.setText("GST-X");
                break;
            case "组合包5":
                textViewPlotUp.setText("ECG");
                textViewPlotMid.setText("阻抗");
                textViewPlotDown.setText("GST-Y");
                break;
            case "组合包6":
                textViewPlotUp.setText("ECG");
                textViewPlotMid.setText("阻抗");
                textViewPlotDown.setText("GST-Z");
                break;
            default:

                break;
        }
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
        int id = sourceMap.get(sharedPreferences.getString("data_source", "组合包1"));
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
        int id = sourceMap.get(sharedPreferences.getString("data_source", "组合包1"));

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

            // 心电
            if (action.equals(BroadcastUtil.ACTION_ECG_UPDATE)) {
                int data = intent.getIntExtra("ECG", 0);
                int max = intent.getIntExtra("MAX", 0);
//                if (currentECG < ECG_COUNT) {
//                    avgECG = (currentECG * avgECG + data) / (++currentECG);
//                    return;
//                }
//                else if (currentECG == ECG_COUNT) {
//                    ecgViewUp.setEcgMax(2 * avgECG);
//                    currentECG++;
//                }
//                ecgViewUp.setEcgMax(max);
                ecgViewUp.addEcgData0(data);

                Log.i(TAG, "MAX" + max);
                Log.i(TAG, "ecg" + data);
            }

            // SPO2，阻抗，压力
            if (action.equals(BroadcastUtil.ACTION_IMPEDANCE_UPDATE)) {
                int data = intent.getIntExtra("IMPEDANCE", 0);
                int max = intent.getIntExtra("MAX", 0);
                int min = intent.getIntExtra("MIN", 0);
                if (data > max || data < min) {
                    Log.i("EcgView", "ddddddddddddddddddddddddddddddddddddddd");
                }
                ecgViewMid.setEcgMax(max);
                ecgViewMid.setEcgMin(min);
//                if (currentImpedance < SPO_COUNT) {
//                    avgImpedance = (currentImpedance * avgImpedance + data) / (++currentImpedance);
//                    return;
//                }
//                else if (currentImpedance == SPO_COUNT) {
//                    ecgViewMid.setEcgMax(avgImpedance * 2);
//                    currentImpedance++;
//                }
                ecgViewMid.addEcgData0(data);
//                ecgViewMid.startDrawWave();

                Log.i("EcgView", "MAX" + max);
                Log.i(TAG, "IMPEDANCE" + data);
//                Log.i(TAG, "MIN" + min);
            }

            // Gsenso
            if (action.equals(BroadcastUtil.ACTION_STRIKE_UPDATE)) {
                int data = intent.getIntExtra("STRIKE", 0);
                if (currentStrike < GSEN_COUNT) {
                    avgStrike = (currentStrike * avgStrike + data) / (++currentStrike);
                    return;
                }
                else if (currentStrike == SPO_COUNT) {
                    ecgViewDown.setEcgMax(avgStrike * 2);
                    currentStrike++;
                }
                ecgViewDown.addEcgData0(data);

                Log.i(TAG, "STRIKE" + data);
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

    /**
     * 模拟心电发送，心电数据是一秒250个包，所以
     */
    private void simulator(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(data0Q.size() > 0){
                    int data = data0Q.poll();
                    ecgViewUp.addEcgData0(data);
                    ecgViewMid.addEcgData0(data);
                    ecgViewDown.addEcgData0(data);
                }
            }
        }, 0, 4);
    }

    private void loadDatas(){
        try{
            String data0 = "";
            InputStream in = getResources().openRawResource(R.raw.ecgdata);
            int length = in.available();
            byte [] buffer = new byte[length];
            in.read(buffer);
            data0 = new String(buffer);
            in.close();
            String[] data0s = data0.split(",");
            for(String str : data0s){
                datas.add(Integer.parseInt(str));
            }

            int curAvg = 0;
            for (int i = 0; i < ECG_COUNT; i++) {
                curAvg = (curAvg * i + datas.get(i)) / (i + 1);
            }
            ecgViewUp.setEcgMax(curAvg * 2);
            ecgViewMid.setEcgMax(curAvg * 2);
            ecgViewDown.setEcgMax(curAvg * 2);

            data0Q.addAll(datas);

        }catch (Exception e){}

    }
}
