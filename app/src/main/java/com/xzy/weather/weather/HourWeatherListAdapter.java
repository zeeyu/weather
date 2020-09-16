package com.xzy.weather.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.GlobalData;
import com.xzy.weather.R;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.util.HeWeatherUtil;
import com.xzy.weather.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:xzy
 * Date:2020/8/18 16:04
 **/
public class HourWeatherListAdapter extends RecyclerView.Adapter<HourWeatherListAdapter.ViewHolder>{

    private static final String TAG = "HourWeatherListAdapter";

    private Context mContext;

    private List<MyWeatherBean> mHourlyList;
    private boolean[] init;
    private int maxTemp = -10000;
    private int minTemp = 10000;

    private float[] left;
    private float[] right;
    private float[] point;

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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    HourWeatherListAdapter(List<MyWeatherBean> hourlyList) {
        mHourlyList = hourlyList;
        for(int i = 0; i < hourlyList.size(); i++) {
            maxTemp = Math.max(Integer.parseInt(hourlyList.get(i).getTemp()), maxTemp);
            minTemp = Math.min(Integer.parseInt(hourlyList.get(i).getTemp()), minTemp);
        }
        init = new boolean[hourlyList.size()];
        left = new float[hourlyList.size()];
        right = new float[hourlyList.size()];
        point = new float[hourlyList.size()];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_main_hour, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyWeatherBean hourlyBean = mHourlyList.get(position);

        String unit = GlobalData.getInstance().getSetting().getTempUnit();
        if("°C".equals(unit)) {
            holder.tvTemp.setText(String.format(mContext.getString(R.string.temperature), hourlyBean.getTemp(), unit));
        } else {
            holder.tvTemp.setText(String.format(mContext.getString(R.string.temperature), HeWeatherUtil.formatTempC(hourlyBean.getTemp()), unit));
        }

        holder.tvTime.setText(hourlyBean.getTime());
        holder.tvWind.setText(String.format(mContext.getString(R.string.wind_scale), hourlyBean.getWindScale()));

        drawLine(holder, position);

        String weather = hourlyBean.getText();
        int id = mContext.getResources().getIdentifier("ic_" + StringUtil.getWeatherName(weather), "drawable", "com.xzy.weather");
        holder.ivWeather.setImageDrawable(mContext.getResources().getDrawable(id));
    }

    private float mid = -1;   //两个view连线的中点坐标
    private void drawLine(@NonNull HourWeatherListAdapter.ViewHolder holder, int position){
         //Log.d(TAG, "drawLine: " + position);

        if(!init[position]) {
            init[position] = true;

            float temp = Integer.parseInt(mHourlyList.get(position).getTemp());
            float pos;
            float nextMid = -1;
            if(maxTemp == minTemp) {
                pos = 0;
            } else {
                pos = (maxTemp - temp) / (maxTemp - minTemp);
            }
            if(position != mHourlyList.size() - 1) {
                float nextTemp = Integer.parseInt(mHourlyList.get(position + 1).getTemp());
                float nextPos = (maxTemp - nextTemp) / (maxTemp - minTemp);
                nextMid = Math.abs((nextPos + pos)/2);
            }
            left[position] = mid;
            right[position] = nextMid;
            point[position] = pos;
            mid = nextMid;
        }

        holder.viewLine.setParam(position, left[position], right[position], point[position], position == 0, position == 0, position == mHourlyList.size() - 1);
        //holder.viewLine.setParam(position, mid, nextMid, pos, position == 0, position == 0, position == mHourlyList.size()-1);
    }

    @Override
    public int getItemCount() {
        return mHourlyList.size();
    }
}
