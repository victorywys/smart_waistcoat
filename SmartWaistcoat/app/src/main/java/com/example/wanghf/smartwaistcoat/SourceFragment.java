package com.example.wanghf.smartwaistcoat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.example.wanghf.myapplication.R;

/**
 * Created by wanghf on 2017/5/15.
 */

public class SourceFragment extends PreferenceFragment {

    private Preference dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.source);


        initViews();
        initListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        dataSource = findPreference("data_source");
        dataSource.setSummary(sharedPreferences.getString("data_source", "null"));

    }

    private void initListeners() {
        dataSource.setOnPreferenceChangeListener(onDataSourceChange);
    }

    protected Preference.OnPreferenceChangeListener onDataSourceChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                dataSource.setSummary(String.valueOf(newValue));
            } catch (Exception ex) {
                showTip(ex.getMessage());
            }
            return true;
        }
    };


    private void showTip(String str) {

    }
}
