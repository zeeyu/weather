package com.xzy.weather.setting;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceViewHolder;

import com.xzy.weather.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:xzy
 * Date:2020/9/9 15:49
 **/
public class SettingListPreference extends ListPreference {

    private static final String TAG = "SettingListPreference";

    @BindView(R.id.tv_select_bar_title)
    TextView tvTitle;
    @BindView(R.id.tv_select_bar_info)
    TextView tvInfo;
    private TypedArray typedArray;
    private String title;

    public SettingListPreference(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingListPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.item_select_bar);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingListPreference, defStyle, 0);
        title = typedArray.getString(R.styleable.SettingListPreference_title);
        typedArray.recycle();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        ButterKnife.bind(this, holder.itemView);

        tvTitle.setText(title);

        setValue(getValue());
    }

    @Override
    public void setValue(String value) {
        Log.d(TAG, "setValue: " + value);
        if(tvInfo != null) {
            tvInfo.setText(value);
        }
        super.setValue(value);
    }

    @Override
    public void onDetached() {
        super.onDetached();
    }
}
