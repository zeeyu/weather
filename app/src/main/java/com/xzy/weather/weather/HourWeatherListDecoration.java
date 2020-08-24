package com.xzy.weather.weather;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.R;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean;

/**
 * Author:xzy
 * Date:2020/8/21 14:00
 **/
public class HourWeatherListDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "HourWeatherListDecor";

    private List<WeatherHourlyBean.HourlyBean> mHourlyBeanList;
//    private Paint mPaint;

    public HourWeatherListDecoration(List<WeatherHourlyBean.HourlyBean> hourlyBeanList){
        mHourlyBeanList = hourlyBeanList;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //outRect.set(15, 10, 15 ,0);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

//        int h = 0, w = 0;
//
//        for(int i = 0; i < parent.getChildCount(); i++){
//            View child = parent.getChildAt(i).findViewById(R.id.view_main_hour_line);
//            int index = parent.getChildLayoutPosition(parent.getChildAt(i));
//
//            if(i == 0){
//                h = child.getHeight();
//                w = child.getWidth();
//            }
//
//            int[] location = new int[2];
//            child.getLocationOnScreen(location);
//
//            int x0 = location[0] + w * i;
//            int y0 = child.getTop();
//            float temp = Integer.valueOf(mHourlyBeanList.get(index).getTemp());
//            float pos = ((float)maxTemp - temp)/(maxTemp - minTemp);
//
//            Log.d(TAG, "onDrawOver   " + " cx:" + (x0 + w /2) + " cy:" + (y0 + pos * h));
//
//            c.drawCircle(x0 + w /2, y0 + pos * h, 10, mPaint);
//        }
    }

//    float mid = -1;   //两个view连线的中点坐标
//    protected void drawLine(@NonNull HourWeatherListAdapter.ViewHolder holder, int position){
//        float temp = Integer.valueOf(mHourlyBeanList.get(position).getTemp());
//        float pos = -1;
//        float nextMid = -1;
//        if(maxTemp == minTemp){
//            pos = 0;
//        } else {
//            pos = (temp - minTemp) / (maxTemp - minTemp);
//        }
//        if(position != mHourlyBeanList.size() - 1){
//            float nextTemp = Integer.valueOf(mHourlyBeanList.get(position + 1).getTemp());
//            float nextPos = (nextTemp - minTemp) / (maxTemp - minTemp);
//            nextMid = Math.abs((nextPos - pos)/2);
//        }
//
//        holder.viewLine.setParam(mid, nextMid, pos, position == 0);
//        mid = nextMid;
//    }
}
