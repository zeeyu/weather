package com.xzy.weather;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:xzy
 * Date:2020/9/9 15:49
 **/
public class SelectBarItem extends LinearLayout {

    @BindView(R.id.tv_select_bar_title)
    TextView tvTitle;
    @BindView(R.id.tv_select_bar_info)
    TextView tvInfo;

    public SelectBarItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_select_bar, this);

        ButterKnife.bind(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectBarItem);
        initWidget(typedArray);
    }

    private void initWidget(TypedArray typedArray) {
        tvTitle.setText(typedArray.getString(R.styleable.SelectBarItem_select_text_title));
        tvInfo.setText(typedArray.getString(R.styleable.SelectBarItem_select_text_info));
    }
}
