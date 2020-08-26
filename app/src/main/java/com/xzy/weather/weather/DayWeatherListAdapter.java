package com.xzy.weather.weather;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.R;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.util.StringUtil;
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

    private static final String TAG = "DayWeatherListAdapter";

    private Context mContext;
    private List<MyWeatherBean> mWeatherDailyList;

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

    public DayWeatherListAdapter(List<MyWeatherBean> weatherDailyList){
        this.mWeatherDailyList = weatherDailyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_main_day, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) throws Resources.NotFoundException {
        MyWeatherBean weatherDailyBean = mWeatherDailyList.get(position);

        holder.tvTemp.setText(weatherDailyBean.getTempMax() + "°" +"/" + weatherDailyBean.getTempMin() + "°");
        holder.tvAir.setText(weatherDailyBean.getAir());

        String fxDate = weatherDailyBean.getDate();
        String[] date = fxDate.split("-");
        holder.tvDate.setText(date[1] + "月" + date[2] + "日" + TimeUtil.getWeek(TimeUtil.strToDate(fxDate)));

        String weather = weatherDailyBean.getText();
       // Log.d(TAG, "weather: " + weather);
        int id = mContext.getResources().getIdentifier("ic_" + StringUtil.getWeatherName(weather), "drawable", "com.xzy.weather");
        holder.ivWeather.setImageDrawable(mContext.getResources().getDrawable(id));
    }

    @Override
    public int getItemCount() {
        return mWeatherDailyList.size();
    }
}
