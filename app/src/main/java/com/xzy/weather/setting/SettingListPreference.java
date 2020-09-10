package com.xzy.weather.setting;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.xzy.weather.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:xzy
 * Date:2020/9/9 15:49
 **/
public class SettingListPreference extends ListPreference {

    @BindView(R.id.tv_select_bar_title)
    TextView tvTitle;
    @BindView(R.id.tv_select_bar_info)
    TextView tvInfo;

    public SettingListPreference(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingListPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.item_select_bar);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectBarItem, defStyle, 0);
        initWidget(typedArray);
    }

    private void initWidget(TypedArray typedArray) {
        tvTitle.setText(typedArray.getString(R.styleable.SelectBarItem_select_text_title));
        tvInfo.setText(typedArray.getString(R.styleable.SelectBarItem_select_text_info));
        typedArray.recycle();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        ButterKnife.bind(holder.itemView);
    }

    @Override
    public void setValue(String value) {
        tvInfo.setText(value);
        super.setValue(value);
    }


}
