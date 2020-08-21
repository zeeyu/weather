package com.xzy.weather.weather;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean;

/**
 * Author:xzy
 * Date:2020/8/21 14:00
 **/
public class HourWeatherListDecoration extends RecyclerView.ItemDecoration {

    private List<WeatherHourlyBean.HourlyBean> mHourlyBeanList;
    private Paint mPaint;
    int maxTemp = -10000;
    int minTemp = 10000;

    public HourWeatherListDecoration(List<WeatherHourlyBean.HourlyBean> hourlyBeanList){
        mHourlyBeanList = hourlyBeanList;
        for(int i = 0; i < hourlyBeanList.size(); i++){
            maxTemp = Math.max(Integer.valueOf(hourlyBeanList.get(i).getTemp()), maxTemp);
            minTemp = Math.min(Integer.valueOf(hourlyBeanList.get(i).getTemp()), minTemp);
        }
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
    }

    float mid = -1;   //两个view连线的中点坐标
    protected void drawLine(@NonNull HourWeatherListAdapter.ViewHolder holder, int position){
        //TODO 理解valueOf源码
        float temp = Integer.valueOf(mHourlyBeanList.get(position).getTemp());
        float pos = -1;
        float nextMid = -1;
        if(maxTemp == minTemp){
            pos = 0;
        } else {
            pos = (temp - minTemp) / (maxTemp - minTemp);
        }
        if(position != mHourlyBeanList.size() - 1){
            float nextTemp = Integer.valueOf(mHourlyBeanList.get(position + 1).getTemp());
            float nextPos = (nextTemp - minTemp) / (maxTemp - minTemp);
            nextMid = Math.abs((nextPos - pos)/2);
        }

        holder.viewLine.setParam(mid, nextMid, pos, position == 0);
        mid = nextMid;
    }
}
