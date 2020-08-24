package com.xzy.weather.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.xzy.weather.R;

import java.util.jar.Attributes;

/**
 * Author:xzy
 * Date:2020/8/21 14:31
 **/
public class HourWeatherLineView extends View {

    private static final String TAG = "HourWeatherLineView";

    private static int CIRCLE_RADIUS = 10;

    private int index = 0;
    private float left = -1;
    private float right = -1;
    private boolean isNow = false;
    private float position = -1;
    private boolean isStart = false;
    private boolean isEnd = false;

    private Paint mPaint;

    public HourWeatherLineView(Context context, AttributeSet attrs){
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.orange_deep));
        mPaint.setStrokeWidth(3f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       // Log.d(TAG, "onDraw " + index + " " + left + " " + right + " " + position);

        int width = getWidth();
        int height = getHeight();

        mPaint.setAlpha(255);
        canvas.drawCircle(width / 2, height * position, CIRCLE_RADIUS, mPaint);

        mPaint.setAlpha(100);
        if(!isStart) {
            canvas.drawLine(0, height * left, width / 2 - CIRCLE_RADIUS, height * position, mPaint);
        }

        if(!isEnd) {
            canvas.drawLine(width / 2 + CIRCLE_RADIUS, height * position, width, height * right, mPaint);
        }

        if(isNow){
            canvas.drawCircle(width/2, height * position, CIRCLE_RADIUS * 2, mPaint);
        }
    }

    /**
     * 设置坐标参数
     * @param left 起点纵坐标
     * @param right 终点纵坐标
     * @param position 中点纵坐标
     * @param isNow 是否为当前时间点
     * @param isStart 是否为列表第一个
     * @param isEnd 是否为列表最后一个
     */
    public void setParam(int index, float left, float right, float position, boolean isNow, boolean isStart, boolean isEnd){
        this.index = index;
        this.left = left;
        this.right = right;
        this.position = position;
        this.isNow = isNow;
        this.isStart = isStart;
        this.isEnd = isEnd;
        invalidate();
    }
}
