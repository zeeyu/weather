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

    public HourWeatherListDecoration(){
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
}
