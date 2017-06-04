package com.example.wanghf.smartwaistcoat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.EditText;
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

    private Context context;

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
//        xinlv.setSummary(sharedPreferences.getBoolean("xinlv", false)? "开" : "关");
        wendu = findPreference("wendu");
//        wendu.setSummary(sharedPreferences.getBoolean("wendu", false)? "开" : "关");
        xueyang = findPreference("xueyang");
//        xueyang.setSummary(sharedPreferences.getBoolean("xueyang", false)? "开" : "关");
        yali = findPreference("yali");
//        yali.setSummary(sharedPreferences.getBoolean("yali", false)? "开":"关");
        zukang = findPreference("zukang");
//        zukang.setSummary(sharedPreferences.getBoolean("zukang", false)? "开":"关");
        zhenling = findPreference("zhenling");
        zhenling.setSummary(sharedPreferences.getBoolean("zhenling", false)? "开":"关");
        duanxin = findPreference("duanxin");
        duanxin.setSummary(sharedPreferences.getBoolean("duanxin", false)? "开":"关");
        dianhua = findPreference("dianhua");
        dianhua.setSummary(sharedPreferences.getBoolean("dianhua", false)? "开":"关");
    }

    private void initListeners() {
        xinlv.setOnPreferenceChangeListener(onXinlvChange);
        xueyang.setOnPreferenceChangeListener(onXueyangChange);
        wendu.setOnPreferenceChangeListener(onWenduChange);
        yali.setOnPreferenceChangeListener(onYaliChange);
        zukang.setOnPreferenceChangeListener(onZukangChange);
        zhenling.setOnPreferenceChangeListener(onzZhenlingChange);
        duanxin.setOnPreferenceChangeListener(onDuanxinChange);
        dianhua.setOnPreferenceChangeListener(onDianhuaChange);
    }

    private Preference.OnPreferenceChangeListener onXinlvChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            final EditText editText = new EditText(getActivity());
            boolean res = (boolean) newValue;
            if (res) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("请输入正常心率范围(下限,上限)")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = editText.getText().toString();
                                String[] str = input.split(",");
                                if (str.length == 2) {
                                    SharedPreferences.Editor editor = xinlv.getEditor();
                                    editor.putInt("xinlv_low", Integer.parseInt(str[0]));
                                    editor.putInt("xinlv_high", Integer.parseInt(str[1]));
                                    editor.apply();
                                }
                                xinlv.setSummary(input);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener onXueyangChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            final EditText editText = new EditText(getActivity());
            boolean res = (boolean) newValue;
            if (res) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("请输入血氧下限值")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = editText.getText().toString();
                                xueyang.setSummary(input);
                                int val = Integer.parseInt(input);
                                SharedPreferences.Editor editor = xueyang.getEditor();
                                editor.putInt("xueyang_low", val);
                                editor.apply();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener onWenduChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            final EditText editText = new EditText(getActivity());
            boolean res = (boolean) newValue;
            if (res) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("请输入正常温度范围(下限,上限)")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = editText.getText().toString();
                                String[] str = input.split(",");
                                if (str.length == 2) {
                                    SharedPreferences.Editor editor = wendu.getEditor();
                                    editor.putInt("wendu_low", Integer.parseInt(str[0]));
                                    editor.putInt("wendu_high", Integer.parseInt(str[1]));
                                    editor.apply();
                                }
                                wendu.setSummary(input);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener onYaliChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            final EditText editText = new EditText(getActivity());
            boolean res = (boolean) newValue;
            if (res) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("请输入压力上限值")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = editText.getText().toString();
                                yali.setSummary(input);
                                int val = Integer.parseInt(input);
                                SharedPreferences.Editor editor = yali.getEditor();
                                editor.putInt("yali_high", val);
                                editor.apply();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener onZukangChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            final EditText editText = new EditText(getActivity());
            boolean res = (boolean) newValue;
            if (res) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("请输入阻抗下限值")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = editText.getText().toString();
                                zukang.setSummary(input);
                                int val = Integer.parseInt(input);
                                SharedPreferences.Editor editor = zukang.getEditor();
                                editor.putInt("zukang_low", val);
                                editor.apply();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener onzZhenlingChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if ((boolean) newValue) {
                zhenling.setSummary("开");
            }
            else {
                zhenling.setSummary("关");
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener onDuanxinChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if ((boolean) newValue) {
                duanxin.setSummary("开");
            }
            else {
                duanxin.setSummary("关");
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener onDianhuaChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if ((boolean) newValue) {
                dianhua.setSummary("开");
            }
            else {
                dianhua.setSummary("关");
            }
            return true;
        }
    };
}
