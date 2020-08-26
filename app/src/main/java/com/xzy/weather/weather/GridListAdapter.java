package com.xzy.weather.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.R;
import com.xzy.weather.bean.MyWeatherNowBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean;

/**
 * Author:xzy
 * Date:2020/8/24 15:12
 **/
public class GridListAdapter extends RecyclerView.Adapter<GridListAdapter.ViewHolder> {

    private Context mContext;

    private String[] types = {"风力风向", "湿度", "紫外线指数", "能见度"};
    private String[] units = {"级", "%", "级", "km"};
    private String[] data = new String[4];
    private int[] resource = {R.drawable.ic_wind, R.drawable.ic_humidity, R.drawable.ic_uv, R.drawable.ic_visibility};

    private MyWeatherNowBean mWeatherNowBean;

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_main_grid_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_main_grid_type)
        TextView tvType;
        @BindView(R.id.tv_main_grid_data)
        TextView tvData;
        @BindView(R.id.tv_main_grid_unit)
        TextView tvUnit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public GridListAdapter(MyWeatherNowBean weatherNowBean){
        this.mWeatherNowBean = weatherNowBean;

        data[0] = weatherNowBean.getWindScale();
        units[0] = "级" + weatherNowBean.getWindDir();

        data[1] = weatherNowBean.getHumidity();

        data[2] = weatherNowBean.getUv();

        data[3] = weatherNowBean.getVis();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_main_grid, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvData.setText(data[position]);
        holder.tvType.setText(types[position]);
        holder.tvUnit.setText(units[position]);
        holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(resource[position]));
    }

    @Override
    public int getItemCount() {
        return types.length;
    }

}
