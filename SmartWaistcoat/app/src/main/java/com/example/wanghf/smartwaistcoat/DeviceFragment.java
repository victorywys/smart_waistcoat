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

public class DeviceFragment extends PreferenceFragment {
    private Preference deviceIP;
    private Preference devicePort;
    private Preference wifiName;
    private Preference wifiPassword;
    private Preference sourceId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.device);


        initViews();
        initListeners();
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

    }

    private void initListeners() {
        wifiName.setOnPreferenceChangeListener(onWifiNameChange);
        wifiPassword.setOnPreferenceChangeListener(onWifiPasswordChange);
        deviceIP.setOnPreferenceChangeListener(onDeviceIpChange);
        devicePort.setOnPreferenceChangeListener(onDevicePortChange);
        sourceId.setOnPreferenceChangeListener(onSourceIDChange);
    }

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

    private void showTip(String str) {

    }
}
