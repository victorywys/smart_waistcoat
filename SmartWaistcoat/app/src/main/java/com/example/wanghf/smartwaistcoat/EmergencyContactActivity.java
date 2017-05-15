package com.example.wanghf.smartwaistcoat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.wanghf.myapplication.R;
import com.example.wanghf.smartwaistcoat.utils.BroadcastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by wanghf on 2017/4/18.
 */

public class EmergencyContactActivity extends Activity {

    private EditText editTextCall;
    private EditText editTextMSG1;
    private EditText editTextMSG2;
    private EditText editTextMSG3;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contact);

        context = this;

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new EmergencyFragment())
                .commit();
    }


}
