package com.example.wanghf.smartwaistcoat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.wanghf.myapplication.R;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

//import dev.frankie.ecgwave.R;

/**
 * Created by Frankie on 2016/5/26.
 */
public class EcgView extends SurfaceView implements SurfaceHolder.Callback {

    private Context mContext;
    private SurfaceHolder surfaceHolder;
    public  boolean isRunning;
    private Canvas mCanvas;

    private double ecgMax = 32000000;//心电的最大值
    private double ecgMin = 0;
    private double ecgMaxNew = Integer.MIN_VALUE;
    private double ecgMinNew = Integer.MAX_VALUE;
    private String bgColor = "#000000";
    private int wave_speed = 25;//波速: 25mm/s
    private int sleepTime = 4; //每次锁屏的时间间距，单位:ms
    private float lockWidth;//每次锁屏需要画的
    private int ecgPerCount = 6;//每次画心电数据的个数，心电每秒有250个数据包

    private LinkedBlockingQueue<Integer> ecg0Datas = new LinkedBlockingQueue<>();

    private Paint mPaint;//画波形图的画笔
    private int mWidth;//控件宽度
    private int mHeight;//控件高度
    private double ecgYRatio;
    private int startY0;
    private Rect rect;

    private int startX;//每次画线的X坐标起点
    private double ecgXOffset;//每次X坐标偏移的像素
    private int blankLineWidth = 6;//右侧空白点的宽度

    public EcgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        rect = new Rect();
        converXOffset();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(5);

        ecgXOffset = lockWidth;
        startY0 = mHeight * (1 / 2);//波1初始Y坐标是控件高度的1/2
        ecgYRatio = mHeight / (ecgMax - ecgMin);
    }

    /**
     * 根据波速计算每次X坐标增加的像素
     *
     * 计算出每次锁屏应该画的px值
     */
    private void converXOffset() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //获取屏幕对角线的长度，单位:px
        double diagonalMm = Math.sqrt(width * width + height * height) / dm.densityDpi;//单位：英寸
        diagonalMm = diagonalMm * 2.54 * 10;//转换单位为：毫米
        double diagonalPx = width * width + height * height;
        diagonalPx = Math.sqrt(diagonalPx);
        //每毫米有多少px
        double px1mm = diagonalPx / diagonalMm;
        //每秒画多少px
        double px1s = wave_speed * px1mm;
        //每次锁屏所需画的宽度
        lockWidth = (float) (px1s * (sleepTime / 1000f));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.parseColor(bgColor));
        holder.unlockCanvasAndPost(canvas);
        startThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h ;
        isRunning = true;
        init();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopThread();
    }

    private void startThread() {
        isRunning = true;
        new Thread(drawRunnable).start();
    }

    private void stopThread(){
        isRunning = false;
    }

    Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            while(isRunning){

                startDrawWave();

            }
        }
    };

    public void startDrawWave(){
        rect.set(startX, 0, (int) (startX + lockWidth + blankLineWidth), mHeight);
        mCanvas = surfaceHolder.lockCanvas(rect);
        if(mCanvas == null) return;
        mCanvas.drawColor(Color.parseColor(bgColor));

        drawWave0();

        surfaceHolder.unlockCanvasAndPost(mCanvas);

        if(startX > mWidth) {
//            Log.i("EcgView", totalCount + "");
            startX = 0;
        }
    }

    /**
     * 画波1
     */
    private void drawWave0() {
        try {
            float mStartX = startX;
            if (ecg0Datas.size() > 500 / lockWidth) {
                Log.i("EcgView", "" + ecg0Datas.size());
                if (startX == 0) {
                    ecgMax = ecgMaxNew;
                    ecgMin = ecgMinNew;
                    ecgYRatio = mHeight / (ecgMax - ecgMin);
                    ecgMinNew = Integer.MAX_VALUE;
                    ecgMaxNew = Integer.MIN_VALUE;
                }
                for( int i=0; i<ecgPerCount; i++){
//                    Log.i("lllll", "" + startX);
                    float newX = (float) (mStartX + ecgXOffset);
                    int newY = ecgConver(ecg0Datas.poll());
//                    totalCount++;
                    mCanvas.drawLine(mStartX, startY0, newX, newY, mPaint);
                    mStartX = newX;
                    startY0 = newY;
                    startX += (int) lockWidth;
                }
            }
        } catch (NoSuchElementException e){
            e.printStackTrace();
        }
    }

    /**
     * 将心电数据转换成用于显示的Y坐标
     * @param data
     * @return
     */
    private int ecgConver(int data) {

        int newData = (int) (ecgMax - data);
        newData = (int) (newData * ecgYRatio);

        return newData;
    }

    public void addEcgData0(int data){
        if (data > ecgMaxNew) {
            ecgMaxNew = data;
        }
        else if (data < ecgMinNew) {
            ecgMinNew = data;
        }
        ecg0Datas.offer(data);
    }

    public void setLockWidth(float width) {
        lockWidth = width;
    }
}
