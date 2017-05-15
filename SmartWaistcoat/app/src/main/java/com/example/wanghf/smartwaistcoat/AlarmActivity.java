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

//        initViews();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new AlarmFragment())
                .commit();

    }

}
