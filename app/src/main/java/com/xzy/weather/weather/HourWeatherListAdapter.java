package com.xzy.weather.weather;

import android.content.Context;
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
import com.xzy.weather.bean.MyWeatherNowBean;
import com.xzy.weather.util.StringUtil;
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

    private static final String TAG = "HourWeatherListAdapter";

    private Context mContext;

    private List<MyWeatherBean> mHourlyList;
    boolean[] init;
    int maxTemp = -10000;
    int minTemp = 10000;


    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_main_hour_time)
        TextView tvTime;
        @BindView(R.id.tv_main_hour_temperature)
        TextView tvTemp;
        @BindView(R.id.tv_main_hour_wind)
        TextView tvWind;
        @BindView(R.id.iv_main_hour_weather)
        ImageView ivWeather;
        @BindView(R.id.view_main_hour_line)
        HourWeatherLineView viewLine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public HourWeatherListAdapter(List<MyWeatherBean> hourlyList){
        mHourlyList = hourlyList;
        for(int i = 0; i < hourlyList.size(); i++){
            maxTemp = Math.max(Integer.valueOf(hourlyList.get(i).getTemp()), maxTemp);
            minTemp = Math.min(Integer.valueOf(hourlyList.get(i).getTemp()), minTemp);
        }
        init = new boolean[hourlyList.size()];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_main_hour, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyWeatherBean hourlyBean = mHourlyList.get(position);
        holder.tvTemp.setText(hourlyBean.getTemp() + "°C");
        holder.tvTime.setText(hourlyBean.getTime());
        holder.tvWind.setText(hourlyBean.getWindScale() + "级");

        if(!init[position]) {
            drawLine(holder, position);
            init[position] = true;
        } else {
            holder.viewLine.invalidate();
        }

        String weather = hourlyBean.getText();
        int id = mContext.getResources().getIdentifier("ic_" + StringUtil.getWeatherName(weather), "drawable", "com.xzy.weather");
        holder.ivWeather.setImageDrawable(mContext.getResources().getDrawable(id));
    }

    float mid = -1;   //两个view连线的中点坐标
    protected void drawLine(@NonNull HourWeatherListAdapter.ViewHolder holder, int position){
        //Log.d(TAG, "drawLine: " + position);

        float temp = Integer.valueOf(mHourlyList.get(position).getTemp());
        float pos = -1;
        float nextMid = -1;
        if(maxTemp == minTemp){
            pos = 0;
        } else {
            pos = (maxTemp - temp) / (maxTemp - minTemp);
        }
        if(position != mHourlyList.size() - 1){
            float nextTemp = Integer.valueOf(mHourlyList.get(position + 1).getTemp());
            float nextPos = (maxTemp - nextTemp) / (maxTemp - minTemp);
            nextMid = Math.abs((nextPos + pos)/2);
        }

        holder.viewLine.setParam(position, mid, nextMid, pos, position == 0, position == 0, position == mHourlyList.size()-1);
        mid = nextMid;
    }

    @Override
    public int getItemCount() {
        return mHourlyList.size();
    }
}
