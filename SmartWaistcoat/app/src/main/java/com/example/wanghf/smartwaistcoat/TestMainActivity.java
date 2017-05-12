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

    final int RIGHT = 0;
    final int LEFT = 1;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_main);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new TestMainFragment())
                .commit();

        gestureDetector = new GestureDetector(TestMainActivity.this, onGestureListener);
    }

    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e2.getX();

            // 右滑
            if (x > 0) {
                finish();
            }
            return true;
        }
    };
}
