package com.xzy.weather.city;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.bean.MyLocationBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:xzy
 * Date:2020/9/2 10:27
 **/
public class CitySearchListAdapter extends RecyclerView.Adapter<CitySearchListAdapter.ViewHolder> {

    private List<MyLocationBean> mList;
    private OnItemClickListener mListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(android.R.id.text1)
        TextView text1;

        ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    CitySearchListAdapter(List<MyLocationBean> list){
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text1.setText(mList.get(position).getFullAddress());
        holder.itemView.setOnClickListener(v -> {
            mListener.OnClick(position);
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void onDataChanged(List<MyLocationBean> list){
        mList = list;
        notifyDataSetChanged();
    }

    void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    interface OnItemClickListener {
        void OnClick(int position);
    }
}
