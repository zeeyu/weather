package com.xzy.weather.weather;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_main_day, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherDailyBean.DailyBean weatherDailyBean = mWeatherDailyBeanList.get(position);
        AirDailyBean.DailyBean airDailyBean = mAirDailyBeanList.get(position);
        holder.tvTemp.setText(weatherDailyBean.getTempMax() + "°" +"/" + weatherDailyBean.getTempMin() + "°");
        holder.tvAir.setText(airDailyBean.getCategory());

        String fxDate = weatherDailyBean.getFxDate();
        String[] date = fxDate.split("-");
        holder.tvDate.setText(date[1] + "月" + date[2] + "日" + TimeUtil.getWeek(TimeUtil.strToDate(fxDate)));
    }

    @Override
    public int getItemCount() {
        return Math.min(mWeatherDailyBeanList.size(), mAirDailyBeanList.size());
    }
}
