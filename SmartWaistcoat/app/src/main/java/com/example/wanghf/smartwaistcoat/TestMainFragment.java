package com.example.wanghf.smartwaistcoat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;
import com.example.wanghf.myapplication.R;

/**
 * Created by bss on 2015/12/20.
 */
public class TestMainFragment extends PreferenceFragment {
    private static final String TAG = "TestMainFragment";

    private Toast toast;
    private Preference deviceIP;
    private Preference devicePort;
    private Preference wifiName;
    private Preference wifiPassword;
    private Preference userName;
    private Preference userGender;
    private Preference userAge;
    private Preference userNumber;
    private Preference userHistory;
    private Preference contactCall;
    private Preference contactMsg1;
    private Preference contactMsg2;
    private Preference contactMsg3;
    private Preference sourceId;
    private Preference xinlv;
    private Preference xueyang;
    private Preference wendu;
    private Preference yali;
    private Preference zukang;
    private Preference zhenling;
    private Preference duanxin;
    private Preference dianhua;

    private Context context;
    private AlertDialog.Builder tipDialog;  //提示框

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.test_main);

        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);


        initViews();
        initListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        deviceIP = findPreference("device_ip");
        deviceIP.setSummary(sharedPreferences.getString("device_ip", "127.0.0.1"));
        devicePort = findPreference("device_port");
        devicePort.setSummary(sharedPreferences.getString("device_port", "8899"));
        sourceId = findPreference("source_id");
        sourceId.setSummary(sharedPreferences.getString("source_id", "1"));
        wifiName = findPreference("wifi_name");
        wifiName.setSummary(sharedPreferences.getString("wifi_name", "wireless"));
        wifiPassword = findPreference("wifi_password");
        wifiPassword.setSummary(sharedPreferences.getString("wifi_password", "pswd"));
        userName = findPreference("user_name");
        userName.setSummary(sharedPreferences.getString("user_name", "小明"));
        userGender = findPreference("user_gender");
        if (sharedPreferences.getBoolean("user_gender", true)) {
            userGender.setSummary("男");
        }
        else {
            userGender.setSummary("女");
        }
        userNumber = findPreference("user_number");
        userNumber.setSummary(sharedPreferences.getString("user_number","1234"));
        userHistory = findPreference("user_history");
        userHistory.setSummary(sharedPreferences.getString("user_history", "无"));
        userAge = findPreference("user_age");
        userAge.setSummary(sharedPreferences.getString("user_age", "18"));
        contactCall = findPreference("contact_call");
        contactCall.setSummary(sharedPreferences.getString("contact_call", "188"));
        contactMsg1 = findPreference("contact_msg1");
        contactMsg1.setSummary(sharedPreferences.getString("contact_msg1", "188"));
        contactMsg2 = findPreference("contact_msg2");
        contactMsg2.setSummary(sharedPreferences.getString("contact_msg2", "188"));
        contactMsg3 = findPreference("contact_msg3");
        contactMsg3.setSummary(sharedPreferences.getString("contact_msg3", "188"));
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
        wifiName.setOnPreferenceChangeListener(onWifiNameChange);
        wifiPassword.setOnPreferenceChangeListener(onWifiPasswordChange);
        deviceIP.setOnPreferenceChangeListener(onDeviceIpChange);
        devicePort.setOnPreferenceChangeListener(onDevicePortChange);
        sourceId.setOnPreferenceChangeListener(onSourceIDChange);
        userName.setOnPreferenceChangeListener(onUserNameChange);
        userAge.setOnPreferenceChangeListener(onUserAgeChange);
        userGender.setOnPreferenceChangeListener(onUserGenderChange);
        userHistory.setOnPreferenceChangeListener(onUserHistoryChange);
        userNumber.setOnPreferenceChangeListener(onUserNumberChange);
        contactCall.setOnPreferenceChangeListener(onContactCallChange);
        contactMsg1.setOnPreferenceChangeListener(onContactMsg1Change);
        contactMsg2.setOnPreferenceChangeListener(onContactMsg2Change);
        contactMsg3.setOnPreferenceChangeListener(onContactMsg3Change);
    }

    /**
     *
     */
    protected Preference.OnPreferenceChangeListener onWifiNameChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                wifiName.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onWifiPasswordChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                wifiPassword.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onDeviceIpChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                deviceIP.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onSourceIDChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                sourceId.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onDevicePortChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                devicePort.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onUserAgeChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                userAge.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onUserNameChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                userName.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onUserGenderChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                if ((boolean) newValue) {
                    userGender.setSummary("男");
                }
                else {
                    userGender.setSummary("女");
                }
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onUserNumberChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                userNumber.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onUserHistoryChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                userHistory.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onContactCallChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                contactCall.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onContactMsg1Change = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                contactMsg1.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onContactMsg2Change = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                contactMsg2.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    protected Preference.OnPreferenceChangeListener onContactMsg3Change = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                contactMsg3.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };

    private void showTip(String str) {
        if (str == null) {
            str = "null";
        }
        toast.setText(str);
        toast.show();
    }
}
