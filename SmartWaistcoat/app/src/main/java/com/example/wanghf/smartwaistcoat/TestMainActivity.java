package com.example.wanghf.smartwaistcoat;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.wanghf.myapplication.R;

/**
 * Created by wanghf on 2017/5/11.
 */

public class TestMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_main);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new TestMainFragment())
                .commit();
    }

}
