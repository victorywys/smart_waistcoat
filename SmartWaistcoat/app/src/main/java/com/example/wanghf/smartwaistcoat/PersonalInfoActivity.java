package com.example.wanghf.smartwaistcoat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.wanghf.myapplication.R;

/**
 * Created by wanghf on 2017/4/17.
 */

public class PersonalInfoActivity extends AppCompatActivity {

    private TextView textViewGender;
    private Switch switchGender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);

        initViews();
    }

    private void initViews() {
        textViewGender = (TextView) findViewById(R.id.info_text_gender);
        switchGender = (Switch) findViewById(R.id.info_switch_gender);

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
    }


}
