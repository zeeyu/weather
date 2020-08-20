package com.xzy.weather.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.heweather.com.interfacesmodule.bean.air.AirDailyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean;

/**
 * Author:xzy
 * Date:2020/8/20 20:01
 **/
public class DayWeatherListAdapter extends RecyclerView.Adapter<DayWeatherListAdapter.ViewHolder> {

    private List<WeatherDailyBean.DailyBean> mWeatherDailyBeanList;
    private List<AirDailyBean.DailyBean> mAirDailyBeanList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_main_day_temperature)
        TextView tvTemp;
        @BindView(R.id.tv_main_day_date)
        TextView tvDate;
        @BindView(R.id.tv_main_day_air)
        TextView tvAir;
        @BindView(R.id.iv_main_day_weather)
        ImageView ivWeather;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public DayWeatherListAdapter(List<WeatherDailyBean.DailyBean> weatherDailyBeanList, List<AirDailyBean.DailyBean> airDailyBeanList){
        mWeatherDailyBeanList = weatherDailyBeanList;
        mAirDailyBeanList = airDailyBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO 创建适配器
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return Math.min(mWeatherDailyBeanList.size(), mAirDailyBeanList.size());
    }
}
