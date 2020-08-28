package com.xzy.weather.city;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xzy.weather.R;
import com.xzy.weather.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityManageActivity extends BaseActivity {

    @BindView(R.id.tb_city_manage)
    Toolbar toolbar;
    @BindView(R.id.rv_city_manage)
    RecyclerView recyclerView;
    @BindView(R.id.btn_city_manage_add)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        initView();
    }

    @Override
    protected void initView() {

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(getString(R.string.label_city_manage));


    }
}
