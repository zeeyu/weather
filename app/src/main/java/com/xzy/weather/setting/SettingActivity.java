package com.xzy.weather.setting;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.xzy.weather.R;
import com.xzy.weather.SwitchBarItem;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.bean.SettingBean;
import com.xzy.weather.util.DataStoreUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tb_setting)
    Toolbar toolbar;

//    @BindView(R.id.item_notify)
//    SwitchBarItem itemNotify;
//    @BindView(R.id.item_temperature)
//    SelectBarItem itemTemp;
//    @BindView(R.id.item_update)
//    SelectBarItem itemUpdate;

    private SettingBean mSettingBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void initData() {
        mSettingBean = DataStoreUtil.getSettingInfo(getApplicationContext());
    }

    @Override
    protected void initView() {
        toolbar.setTitle(R.string.label_setting);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

}
