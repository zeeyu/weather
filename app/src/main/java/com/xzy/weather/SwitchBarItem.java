package com.xzy.weather;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:xzy
 * Date:2020/9/9 14:43
 **/
public class SwitchBarItem extends LinearLayout {

    @BindView(R.id.tv_switch_bar_title)
    TextView tvTitle;
    @BindView(R.id.tv_switch_bar_hint)
    TextView tvHint;
    @BindView(R.id.switch_switch_bar)
    Switch aSwitch;

    public SwitchBarItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.item_switch_bar, this);

        ButterKnife.bind(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchBarItem);
        initWidget(typedArray);
    }

    private void initWidget(TypedArray typedArray) {
        String title = typedArray.getString(R.styleable.SwitchBarItem_switch_text_title);
        String hint = typedArray.getString(R.styleable.SwitchBarItem_switch_text_hint);
        tvTitle.setText(title);
        if(hint == null || "".equals(hint)) {
            tvHint.setVisibility(GONE);
        } else {
            tvHint.setText(hint);
        }
    }

    public void setTitle(String text) {
        tvTitle.setText(text);
    }

    public void setHint(String text) {
        tvHint.setText(text);
    }

    public void switchChange() {
        aSwitch.setChecked(!aSwitch.isChecked());
    }

    public boolean switchIsChecked() {
        return aSwitch.isChecked();
    }
}
