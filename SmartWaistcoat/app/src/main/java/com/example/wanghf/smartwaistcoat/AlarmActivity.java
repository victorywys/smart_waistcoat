package com.example.wanghf.smartwaistcoat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.wanghf.myapplication.R;
import com.example.wanghf.smartwaistcoat.controller.MainController;

/**
 * Created by wanghf on 2017/4/18.
 */

public class AlarmActivity extends Activity {

    private Switch switchXinlv;
    private Switch switchWendu;
    private Switch switchXueyang;
    private Switch switchYali;
    private Switch switchZukang;
    private Switch switchZhenling;
    private Switch switchDuanxin;
    private Switch switchDianhua;

    private MainController mainController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        initViews();

    }

    private void initViews() {
        switchXinlv = (Switch) findViewById(R.id.alarm_switch_xinlv);
        switchWendu = (Switch) findViewById(R.id.alarm_switch_wendu);
        switchXueyang = (Switch) findViewById(R.id.alarm_switch_xueyang);
        switchYali = (Switch) findViewById(R.id.alarm_switch_yali);
        switchZukang = (Switch) findViewById(R.id.alarm_switch_zukang);
        switchZhenling = (Switch) findViewById(R.id.alarm_switch_zhenling);
        switchDuanxin = (Switch) findViewById(R.id.alarm_switch_duanxin);
        switchDianhua = (Switch) findViewById(R.id.alarm_switch_dianhua);

        switchXinlv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    public void onClickBack(View view) {
        finish();
    }
}
