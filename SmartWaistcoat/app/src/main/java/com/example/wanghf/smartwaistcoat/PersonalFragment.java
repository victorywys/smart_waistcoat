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

public class PersonalFragment extends PreferenceFragment {
    private Preference userName;
    private Preference userGender;
    private Preference userAge;
    private Preference userNumber;
    private Preference userHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.personal);


        initViews();
        initListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

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
    }

    private void initListeners() {

        userName.setOnPreferenceChangeListener(onUserNameChange);
        userAge.setOnPreferenceChangeListener(onUserAgeChange);
        userGender.setOnPreferenceChangeListener(onUserGenderChange);
        userHistory.setOnPreferenceChangeListener(onUserHistoryChange);
        userNumber.setOnPreferenceChangeListener(onUserNumberChange);
    }

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

    private void showTip(String str) {

    }
}
