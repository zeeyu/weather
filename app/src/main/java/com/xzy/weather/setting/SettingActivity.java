package com.xzy.weather.setting;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.xzy.weather.R;
import com.xzy.weather.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    private static final String TAG = "SettingActivity";

    @BindView(R.id.tb_setting)
    Toolbar toolbar;

    //private static SettingBean setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new SettingFragment()).commit();

        initData();
        initView();
    }

    @Override
    protected void initData() {
        //setting = DataStoreUtil.getSettingInfo(getApplicationContext());
    }

    @Override
    protected void initView() {
        toolbar.setTitle(R.string.label_setting);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
