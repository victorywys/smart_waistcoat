package com.example.wanghf.smartwaistcoat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.wanghf.myapplication.R;

/**
 * Created by wanghf on 2017/5/21.
 */

public class FilterActivity extends Activity {
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        context = this;

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new FilterFragment())
                .commit();

    }
}
