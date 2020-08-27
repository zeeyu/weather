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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:xzy
 * Date:2020/8/27 16:04
 **/
public class CityManageListAdapter extends RecyclerView.Adapter<CityManageListAdapter.ViewHolder> {

    private Context mContext;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_city_manage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
