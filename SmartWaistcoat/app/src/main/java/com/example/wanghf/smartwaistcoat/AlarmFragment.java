package com.example.wanghf.smartwaistcoat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.wanghf.myapplication.R;

/**
 * Created by wanghf on 2017/5/14.
 */

public class AlarmFragment extends PreferenceFragment {
    private Preference xinlv;
    private Preference xueyang;
    private Preference wendu;
    private Preference yali;
    private Preference zukang;
    private Preference zhenling;
    private Preference duanxin;
    private Preference dianhua;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.alarm);


        initViews();
        initListeners();
    }

    private void initViews() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        xinlv = findPreference("xinlv");
        xinlv.setSummary(sharedPreferences.getBoolean("xinlv", false)? "开" : "关");
        wendu = findPreference("wendu");
        wendu.setSummary(sharedPreferences.getBoolean("wendu", false)? "开" : "关");
        xueyang = findPreference("xueyang");
        xueyang.setSummary(sharedPreferences.getBoolean("xueyang", false)? "开" : "关");
        yali = findPreference("yali");
        yali.setSummary(sharedPreferences.getBoolean("yali", false)? "开":"关");
        zukang = findPreference("zukang");
        zukang.setSummary(sharedPreferences.getBoolean("zukang", false)? "开":"关");
        zhenling = findPreference("zhenling");
        zhenling.setSummary(sharedPreferences.getBoolean("zhenling", false)? "开":"关");
        duanxin = findPreference("duanxin");
        duanxin.setSummary(sharedPreferences.getBoolean("duanxin", false)? "开":"关");
        dianhua = findPreference("dianhua");
        dianhua.setSummary(sharedPreferences.getBoolean("dianhua", false)? "开":"关");
    }

    private void initListeners() {

    }
}
