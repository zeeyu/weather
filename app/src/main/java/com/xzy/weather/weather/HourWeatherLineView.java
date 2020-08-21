package com.xzy.weather.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.jar.Attributes;

/**
 * Author:xzy
 * Date:2020/8/21 14:31
 **/
public class HourWeatherLineView extends View {

    private static final String TAG = "HourWeatherLineView";

    private static int CIRCLE_RADIUS = 5;

    private float left = -1;
    private float right = -1;
    private boolean isNow = false;
    private float position = -1;

    private Paint mPaint;

    public HourWeatherLineView(Context context, AttributeSet attrs){
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(0xff9100);
        mPaint.setStrokeWidth(1.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "onDraw" + left + " " + right + " " + position);

        int width = getWidth();
        int height = getHeight();

        if(position != -1) {
            canvas.drawCircle(width / 2, height * position, CIRCLE_RADIUS, mPaint);
        }
        if(left != -1) {
            canvas.drawLine(0, height * left, width / 2 - CIRCLE_RADIUS, height * position, mPaint);
        }
        if(right != -1) {
            canvas.drawLine(width / 2 + CIRCLE_RADIUS, height * position, width, height * right, mPaint);
        }

        if(isNow){
            mPaint.setAlpha(100);
            canvas.drawCircle(width/2, height * position, CIRCLE_RADIUS * 2, mPaint);
            mPaint.setAlpha(255);
        }
    }

    /**
     * 设置坐标参数
     * @param left 起点纵坐标
     * @param right 终点纵坐标
     * @param position 中点纵坐标
     * @param isNow 是否为当前时间点
     */
    public void setParam(float left, float right, float position, boolean isNow){
        this.left = left;
        this.right = right;
        this.position = position;
        this.isNow = isNow;
        invalidate();
    }
}
