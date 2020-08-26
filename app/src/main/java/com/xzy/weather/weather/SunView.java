package com.xzy.weather.weather;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xzy.weather.R;
import com.xzy.weather.util.BitmapUtil;
import com.xzy.weather.util.TimeUtil;

/**
 * Author:xzy
 * Date:2020/8/24 17:18
 **/
public class SunView extends View {

    private static final String TAG = "SunView";

    private static final int MSG_UPDATE = 1;

    private Paint mPaint;

    private ValueAnimator animator;
    float fraction = 0;
    boolean stop = false;

    private String sunrise;
    private String sunset;

    float width, height;
    float margin = 50;
    float left, top, right, bottom;
    float startAngle = -150, sweepAngle = 120, midAngel = -100, nowAngle = startAngle;

    volatile boolean updateCompleted = true;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case MSG_UPDATE:
                    invalidate();
                    break;
                default:
                    break;
            }
        }
    };

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //setSun("6:00", "18:00");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();
        left = 0;
        top = margin;
        right = width;
        bottom = 2.0f * height;

        drawArc(canvas);
    }

    public void setSun(String rise, String set){
        if(rise != null && set != null) {
            this.sunrise = rise;
            this.sunset = set;
        } else {
            if(this.sunrise == null || this.sunset == null){
                return;
            }
        }
        nowAngle = startAngle;

        String now = TimeUtil.getHourNow();
        float pos = TimeUtil.getIntervalHour(sunrise, now) / TimeUtil.getIntervalHour(sunrise, sunset);
        if(pos > 1.5){
            pos = 0;
        } else if(pos > 1){
            pos = 1;
        }
        midAngel = startAngle + pos * sweepAngle;
        //Log.d(TAG, "setSun :" + TimeUtil.getIntervalHour(sunrise, sunset) + " " + TimeUtil.getIntervalHour(sunrise, now) + " " + pos);

        startAnimator();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(nowAngle < midAngel) {
//                    Message message = new Message();
//                    message.what = MSG_UPDATE;
//                    handler.sendMessage(message);
//                    nowAngle += (midAngel - startAngle) * 0.02f;
//                    //Log.d(TAG, "nowAngle :" + nowAngle);
//                    try {
//                        Thread.sleep(30);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

        //invalidate();
    }

    private void drawArc(Canvas canvas){
        float x0 = (left + right) / 2.0f;
        float y0 = (top + bottom) / 2.0f;
        float a = (right - left) / 2.0f;
        float b = (bottom - top) / 2.0f;

        nowAngle = startAngle + (midAngel - startAngle) * fraction;
        //Log.d(TAG, "drawArc: " + nowAngle);

        mPaint.reset();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        int orange = getResources().getColor(R.color.orange);
        int white = getResources().getColor(R.color.white);
        mPaint.setColor(getResources().getColor(R.color.orange));

        float x1 = x0 - a * (float)Math.cos(Math.toRadians(180 + startAngle));
        float x2 =  x0 + a * (float)Math.cos(Math.toRadians(180 + startAngle));
        float y = y0 - b * (float)Math.sin(Math.toRadians(180 + startAngle));
        float x_now = x0 - a * (float)Math.cos(Math.toRadians(180 + nowAngle));
        float y_now = y0 - b * (float)Math.sin(Math.toRadians(180 + nowAngle));

        //Path path = new Path();
        //path.moveTo(x1, y);
        //path.quadTo((x_now + x1)/2, (y_now + y) / 2, x_now, y_now);
        //path.arcTo(new RectF(left, top, right, bottom), startAngle, midAngel-startAngle);

        canvas.drawArc(new RectF(left, top, right, bottom), startAngle, midAngel-startAngle, false, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        LinearGradient gradient = new LinearGradient(x_now, top, x_now, y, new int[]{orange, white} , null, Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);
        canvas.drawArc(new RectF(left, top, right, bottom), startAngle, nowAngle - startAngle, true, mPaint);
        //canvas.drawPath(path, mPaint);

        mPaint.setShader(null);
        canvas.drawLine(x1 - 10, y, x1 + 10, y, mPaint);
        canvas.drawLine(x2 - 10, y, x2 + 10, y, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
        canvas.drawArc(new RectF(left, top, right, bottom), midAngel, sweepAngle - (midAngel - startAngle), false, mPaint);

        mPaint.setColor(getResources().getColor(R.color.gray));
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(30);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(sunrise + getResources().getString(R.string.sunrise), x1 - margin, y + margin, mPaint);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(sunset + getResources().getString(R.string.sunset), x2 + margin, y + margin, mPaint);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sun);
        bitmap = BitmapUtil.resizeImage(bitmap, 30, 30);
        canvas.drawBitmap(bitmap, x_now, y_now - 15, mPaint);

    }

    public void startAnimator(){
        animator = ValueAnimator.ofFloat(0,1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = (float) animator.getAnimatedValue();
                stop = true;
                postInvalidate();
            }
        });
        animator.setDuration(2000);
        animator.start();
    }
}

