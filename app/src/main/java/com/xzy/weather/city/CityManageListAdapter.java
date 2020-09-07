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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:xzy
 * Date:2020/8/27 16:04
 **/
public class CityManageListAdapter extends RecyclerView.Adapter<CityManageListAdapter.ViewHolder> {

    public static final int MODE_NORMAL = 0;
    public static final int MODE_EDIT = 1;
    private int mode = MODE_NORMAL;

    private List<MyLocationBean> locationList;
    private List<MyWeatherNowBean> weatherNowList;
    private List<MyWeatherBean> weatherList;

    private List<MyLocationBean> tmpLocationList = new ArrayList<>();
    private List<MyWeatherNowBean> tmpWeatherNowList = new ArrayList<>();
    private List<MyWeatherBean> tmpWeatherList = new ArrayList<>();

    private OnItemClickListener listener;

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
        @BindView(R.id.iv_city_manage_remove)
        ImageView ivRemove;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    CityManageListAdapter(List<MyLocationBean> locationList, List<MyWeatherNowBean> weatherNowList, List<MyWeatherBean> weatherList){
        this.locationList = locationList;
        this.weatherNowList = weatherNowList;
        this.weatherList = weatherList;
        tmpLocationList.addAll(locationList);
        tmpWeatherList.addAll(weatherList);
        tmpWeatherNowList.addAll(weatherNowList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_city_manage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTemp.setText(tmpWeatherNowList.get(position).getTemp());
        holder.tvType.setText(tmpWeatherNowList.get(position).getText());
        holder.tvAir.setText(tmpWeatherList.get(position).getAir());
        holder.tvMax.setText(tmpWeatherList.get(position).getTempMax());
        holder.tvMin.setText(tmpWeatherList.get(position).getTempMin());
        holder.tvTime.setText(tmpWeatherList.get(position).getTime());
        holder.tvLocation.setText(locationList.get(position).getCity() + " " + locationList.get(position).getName());
        holder.ivRemove.setOnClickListener(v -> deleteItem(position));

        if(mode == MODE_NORMAL){
            if(holder.ivRemove.getVisibility() == View.VISIBLE){
                holder.ivRemove.setVisibility(View.GONE);
            }
        } else {
            if(holder.ivRemove.getVisibility() == View.GONE) {
                holder.ivRemove.setVisibility(View.VISIBLE);
            }
        }
        if(listener != null){
            holder.itemView.setOnLongClickListener(v -> {
                if(mode == MODE_NORMAL){
                    mode = MODE_EDIT;
                    notifyDataSetChanged();
                }
                listener.onLongClick(holder, position);
                return false;
            });
        }
    }

    private void deleteItem(int position){
        tmpWeatherNowList.remove(position);
        tmpWeatherList.remove(position);
        tmpLocationList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void delete() {
        locationList.clear();
        weatherList.clear();
        weatherNowList.clear();
        locationList.addAll(tmpLocationList);
        weatherList.addAll(tmpWeatherList);
        weatherNowList.addAll(tmpWeatherNowList);
        mode = MODE_NORMAL;
    }

    public void redo() {
        tmpLocationList.clear();
        tmpWeatherList.clear();
        tmpWeatherNowList.clear();
        tmpLocationList.addAll(locationList);
        tmpWeatherList.addAll(weatherList);
        tmpWeatherNowList.addAll(weatherNowList);
        mode = MODE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return tmpLocationList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    interface OnItemClickListener {
        void onLongClick(ViewHolder viewHolder, int position);
    }
}
