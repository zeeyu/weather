package com.xzy.weather.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.xzy.weather.R;

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

        //Log.d(TAG, "onDraw " + index + " " + left + " " + right + " " + position);

        float width = getWidth();
        float height = getHeight() - 4 * CIRCLE_RADIUS;

        mPaint.setAlpha(255);
        canvas.drawCircle(width / 2, CIRCLE_RADIUS * 2 + height * position, CIRCLE_RADIUS, mPaint);

        mPaint.setAlpha(100);
        if(!isStart) {
            canvas.drawLine(0, CIRCLE_RADIUS * 2 + height * left, width / 2 - CIRCLE_RADIUS, CIRCLE_RADIUS * 2 + height * position, mPaint);
        }

        if(!isEnd) {
            canvas.drawLine(width / 2 + CIRCLE_RADIUS, CIRCLE_RADIUS * 2 + height * position, width, CIRCLE_RADIUS * 2 + height * right, mPaint);
        }

        if(isNow){
            canvas.drawCircle(width/2, CIRCLE_RADIUS * 2 + height * position, CIRCLE_RADIUS * 2, mPaint);
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
