package com.xzy.weather.city;

import android.content.Context;
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
public class CityTopListAdapter extends RecyclerView.Adapter<CityTopListAdapter.ViewHolder> {

    private List<MyLocationBean> mLocationList;
    private OnItemClickListener mListener;

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_city_add_top)
        TextView tvTop;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    CityTopListAdapter(List<MyLocationBean> locationList){
        mLocationList = locationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_city_add, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTop.setText(mLocationList.get(position).getName());
        holder.itemView.setOnClickListener(v -> mListener.OnClick(position));
    }

    @Override
    public int getItemCount() {
        return mLocationList.size();
    }

    void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    interface OnItemClickListener {
        void OnClick(int position);
    }
}
