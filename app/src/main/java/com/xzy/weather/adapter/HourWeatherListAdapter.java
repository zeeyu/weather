package com.xzy.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.R;
import com.xzy.weather.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean;

/**
 * Author:xzy
 * Date:2020/8/18 16:04
 **/
public class HourWeatherListAdapter extends RecyclerView.Adapter<HourWeatherListAdapter.ViewHolder>{

    private List<WeatherHourlyBean.HourlyBean> mHourlyBeanList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_main_hour_time)
        TextView tvTime;
        @BindView(R.id.tv_main_hour_temperature)
        TextView tvTemp;
        @BindView(R.id.tv_main_hour_wind)
        TextView tvWind;
        @BindView(R.id.iv_main_hour_weather)
        ImageView ivWeather;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public HourWeatherListAdapter(List<WeatherHourlyBean.HourlyBean> hourlyBeanList){
        mHourlyBeanList = hourlyBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_main_hour, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherHourlyBean.HourlyBean hourlyBean = mHourlyBeanList.get(position);
        holder.tvTemp.setText(hourlyBean.getTemp() + "°C");
        holder.tvTime.setText(TimeUtil.getHeFxTimeHour(hourlyBean.getFxTime()));
        holder.tvWind.setText(hourlyBean.getWindScale() + "级");
    }

    @Override
    public int getItemCount() {
        return mHourlyBeanList.size();
    }
}
