package com.xzy.weather.weather;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.xzy.weather.R;

/**
 * Author:xzy
 * Date:2020/8/24 17:18
 **/
public class SunView extends View {

    private Paint mPaint;

    private Animator animator;

    private String sunrise;
    private String sunset;

    int width;
    int height;
    int margin = 50;

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();

        drawArc(canvas);

    }

    public void setSun(String sunrise, String sunset){
        this.sunrise = sunrise;
        this.sunset =sunset;
        invalidate();
    }

    private void drawArc(Canvas canvas){
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(getResources().getColor(R.color.orange));
        canvas.drawArc(new RectF(margin, margin, width - margin, 1.5f * height), -150, 120, false, mPaint);
    }

    private void drawText(Canvas canvas){
        if(sunrise != null && sunset != null){
            //canvas.drawText(sunrise + "日出", );
        }
    }
}

