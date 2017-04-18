package com.example.wanghf.smartwaistcoat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.androidplot.xy.XYPlot;
import com.example.wanghf.myapplication.R;
import com.example.wanghf.smartwaistcoat.controller.MainController;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private XYPlot impulsePlot;

    private boolean showingCurve;
    private MainController mainController;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        mainController = new MainController(context, MainApplication.getQueue());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainController.onResume();
    }

    private void initPlots() {
        impulsePlot = (XYPlot) findViewById(R.id.main_plot_impulse);
    }

    public void onClickShowCurve(View view) {
        if (showingCurve) {
            findViewById(R.id.linear_curve).setVisibility(View.GONE);
            findViewById(R.id.linear_table).setVisibility(View.VISIBLE);
            showingCurve = false;
        }
        else {
            findViewById(R.id.linear_table).setVisibility(View.GONE);
            findViewById(R.id.linear_curve).setVisibility(View.VISIBLE);
            showingCurve = true;
        }
    }

    public void onClickShowAcceleration(View view) {

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
}
