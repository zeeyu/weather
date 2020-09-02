package com.xzy.weather.city;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.R;
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.bean.MyWeatherNowBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean;

/**
 * Author:xzy
 * Date:2020/8/27 16:04
 **/
public class CityManageListAdapter extends RecyclerView.Adapter<CityManageListAdapter.ViewHolder> {

    private Context mContext;

    private List<MyLocationBean> locationList;
    private List<MyWeatherNowBean> weatherNowList;
    private List<MyWeatherBean> weatherList;

    OnItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_city_manage_temp)
        TextView tvTemp;
        @BindView(R.id.tv_city_manage_type)
        TextView tvType;
        @BindView(R.id.tv_city_manage_air)
        TextView tvAir;
        @BindView(R.id.tv_city_manage_max)
        TextView tvMax;
        @BindView(R.id.tv_city_manage_min)
        TextView tvMin;
        @BindView(R.id.tv_city_manage_location)
        TextView tvLocation;
        @BindView(R.id.tv_city_manage_time)
        TextView tvTime;
        @BindView(R.id.iv_city_manage_local)
        ImageView ivLocal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public CityManageListAdapter(List<MyLocationBean> locationList, List<MyWeatherNowBean> weatherNowList, List<MyWeatherBean> weatherList){
        this.locationList = locationList;
        this.weatherNowList = weatherNowList;
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_city_manage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTemp.setText(weatherNowList.get(position).getTemp());
        holder.tvType.setText(weatherNowList.get(position).getText());
        holder.tvAir.setText(weatherList.get(position).getAir());
        holder.tvMax.setText(weatherList.get(position).getTempMax());
        holder.tvMin.setText(weatherList.get(position).getTempMin());
        holder.tvTime.setText(weatherList.get(position).getTime());
        holder.tvLocation.setText(locationList.get(position).getCity() + " " + locationList.get(position).getName());
        if(listener != null){
            listener.onClick(position);
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    interface OnItemClickListener{
        public void onClick(int position);
    }
}
