package com.xzy.weather.warning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.R;
import com.xzy.weather.bean.MyWarningBean;
import com.xzy.weather.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.heweather.com.interfacesmodule.bean.WarningBean;

/**
 * Author:xzy
 * Date:2020/8/26 14:19
 **/
public class WarningListAdapter extends RecyclerView.Adapter<WarningListAdapter.ViewHolder> {

    private Context mContext;

    private List<MyWarningBean> mWarningList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_warning_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_warning_type)
        TextView tvType;
        @BindView(R.id.tv_warning_time)
        TextView tvTime;
        @BindView(R.id.tv_warning_info)
        TextView tvInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public WarningListAdapter(List<MyWarningBean> warningBeanList){
        this.mWarningList = warningBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_warning, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyWarningBean bean = mWarningList.get(position);
        holder.tvType.setText(bean.getType() + bean.getLevel() + "预警");

        float hour = TimeUtil.getIntervalHour(
                TimeUtil.getHeFxTimeHour(bean.getPubTime()), TimeUtil.getHourNow(),
                TimeUtil.getHeFxTimeDate(bean.getPubTime()), TimeUtil.getDateNow());

        if(hour < 1){
            holder.tvTime.setText(String.format(mContext.getString(R.string.update_minute),hour*60));
        } else {
            holder.tvTime.setText(String.format(mContext.getString(R.string.update_hour),hour));
        }

        holder.tvInfo.setText(bean.getText() + "（预警信息来源: " + bean.getSender() + ")");
    }

    @Override
    public int getItemCount() {
        return mWarningList.size();
    }
}
