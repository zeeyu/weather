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
 * Date:2020/9/25 17:40
 **/
public class CityHistoryListAdapter extends RecyclerView.Adapter<CityHistoryListAdapter.ViewHolder> {

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

    CityHistoryListAdapter(List<MyLocationBean> locationList){
        mLocationList = locationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_city_history, parent, false);
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

    public interface OnItemClickListener {
        void OnClick(int position);
    }
}
