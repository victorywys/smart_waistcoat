package com.example.wanghf.smartwaistcoat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.wanghf.myapplication.R;

/**
 * Created by wanghf on 2017/5/15.
 */

public class EmergencyFragment extends PreferenceFragment {
    private Preference contactCall;
    private Preference contactMsg1;
    private Preference contactMsg2;
    private Preference contactMsg3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.contact);


        initViews();
        initListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        contactCall = findPreference("contact_call");
        contactCall.setSummary(sharedPreferences.getString("contact_call", "188"));
        contactMsg1 = findPreference("contact_msg1");
        contactMsg1.setSummary(sharedPreferences.getString("contact_msg1", "188"));
        contactMsg2 = findPreference("contact_msg2");
        contactMsg2.setSummary(sharedPreferences.getString("contact_msg2", "188"));
        contactMsg3 = findPreference("contact_msg3");
        contactMsg3.setSummary(sharedPreferences.getString("contact_msg3", "188"));

    }

    private void initListeners() {
        contactCall.setOnPreferenceChangeListener(onContactCallChange);
        contactMsg1.setOnPreferenceChangeListener(onContactMsg1Change);
        contactMsg2.setOnPreferenceChangeListener(onContactMsg2Change);
        contactMsg3.setOnPreferenceChangeListener(onContactMsg3Change);
    }

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

    }
}
