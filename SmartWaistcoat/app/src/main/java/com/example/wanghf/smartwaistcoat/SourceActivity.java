package com.example.wanghf.smartwaistcoat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.wanghf.myapplication.R;

/**
 * Created by wanghf on 2017/5/15.
 */

public class SourceActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SourceFragment())
                .commit();

    }

}
