package com.example.wanghf.smartwaistcoat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.example.wanghf.myapplication.R;

/**
 * Created by wanghf on 2017/5/21.
 */

public class FilterFragment extends PreferenceFragment {
    private Preference filter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.filter);


        filter = findPreference("filter");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.getBoolean("filter", false)) {
            filter.setSummary("开");
        }
        else {
            filter.setSummary("关");
        }
        filter.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if ((boolean) o) {
                    filter.setSummary("开");
                }
                else {
                    filter.setSummary("关");
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
