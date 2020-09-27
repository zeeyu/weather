package com.xzy.weather.city;

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
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.bean.MyWeatherNowBean;
import com.xzy.weather.util.HeWeatherUtil;
import com.xzy.weather.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:xzy
 * Date:2020/8/27 16:04
 **/
public class CityManageListAdapter extends RecyclerView.Adapter<CityManageListAdapter.ViewHolder> {

    //private static final String TAG = "CityManageListAdapter";

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
        @BindView(R.id.tv_city_manage_unit)
        TextView tvUnit;
        @BindView(R.id.iv_city_manage_weather)
        ImageView ivWeather;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Context mContext;

    private static final int MODE_NORMAL = 0;
    private static final int MODE_EDIT = 1;
    private int mode = MODE_NORMAL;

    private List<MyLocationBean> locationList;
    private List<MyWeatherNowBean> weatherNowList;
    private List<MyWeatherBean> weatherList;

    private List<MyLocationBean> tmpLocationList = new ArrayList<>();
    private List<MyWeatherNowBean> tmpWeatherNowList = new ArrayList<>();
    private List<MyWeatherBean> tmpWeatherList = new ArrayList<>();

    private OnItemClickListener listener;

    CityManageListAdapter(List<MyLocationBean> locationList, List<MyWeatherNowBean> weatherNowList, List<MyWeatherBean> weatherList) {
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
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_city_manage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvType.setText(weatherNowList.get(position).getText());
        holder.tvAir.setText(weatherList.get(position).getAir());
        holder.tvTime.setText(weatherList.get(position).getTime());
        holder.tvLocation.setText(locationList.get(position).getBriefAddress());
        holder.ivRemove.setOnClickListener(v -> deleteItem(position));

        if(position != 0) {
            holder.ivLocal.setVisibility(View.GONE);
        }

        String weather = weatherNowList.get(position).getText();
        int id = mContext.getResources().getIdentifier("background_" + StringUtil.getWeatherBackgroundName(weather), "drawable", "com.xzy.weather");
        holder.ivWeather.setImageDrawable(mContext.getResources().getDrawable(id));

        String unit = GlobalData.getInstance().getSetting().getTempUnit();
        //Log.d(TAG, "onBindViewHolder: " + unit);
        holder.tvUnit.setText(unit);
        if("°C".equals(unit)) {
            holder.tvTemp.setText(weatherNowList.get(position).getTemp());
            holder.tvMax.setText(String.format(mContext.getString(R.string.temperature), weatherList.get(position).getTempMax(), unit));
            holder.tvMin.setText(String.format(mContext.getString(R.string.temperature), weatherList.get(position).getTempMin(), unit));
        } else {
            holder.tvTemp.setText(HeWeatherUtil.formatTempC(weatherList.get(position).getTemp()));
            holder.tvMax.setText(String.format(mContext.getString(R.string.temperature), weatherList.get(position).getTempMax(), unit));
            holder.tvMin.setText(String.format(mContext.getString(R.string.temperature), weatherList.get(position).getTempMin(), unit));
        }

        if(mode == MODE_NORMAL) {
            if(holder.ivRemove.getVisibility() == View.VISIBLE) {
                holder.ivRemove.setVisibility(View.GONE);
            }
        } else {
            if(holder.ivRemove.getVisibility() == View.GONE) {
                holder.ivRemove.setVisibility(View.VISIBLE);
            }
        }
        if(listener != null){
            holder.itemView.setOnLongClickListener(v -> {
                if(mode == MODE_NORMAL) {
                    mode = MODE_EDIT;
                    notifyDataSetChanged();
                }
                listener.onLongClick(holder, position);
                return false;
            });
        }
    }

    private void deleteItem(int position) {
        weatherNowList.remove(position);
        weatherList.remove(position);
        locationList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    void redo() {
        locationList.clear();
        weatherList.clear();
        weatherNowList.clear();
        locationList.addAll(tmpLocationList);
        weatherList.addAll(tmpWeatherList);
        weatherNowList.addAll(tmpWeatherNowList);
        mode = MODE_NORMAL;
    }

    void delete() {
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
        return locationList.size();
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    interface OnItemClickListener {
        void onLongClick(ViewHolder viewHolder, int position);
    }
}
