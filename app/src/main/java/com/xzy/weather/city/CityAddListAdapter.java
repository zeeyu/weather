package com.xzy.weather.city;

import android.content.Context;
import android.graphics.LinearGradient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.R;
import com.xzy.weather.bean.MyLocationBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:xzy
 * Date:2020/8/31 17:32
 **/
public class CityAddListAdapter extends RecyclerView.Adapter<CityAddListAdapter.ViewHolder> {

    private Context mContext;

    private List<MyLocationBean> mLocationList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_city_add_top)
        TextView tvTop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public CityAddListAdapter(List<MyLocationBean> locationList){
        mLocationList = locationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_city_add, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mLocationList.size();
    }


}
