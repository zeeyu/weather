package com.xzy.weather.city;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.xzy.weather.R;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.bean.MyWeatherNowBean;
import com.xzy.weather.util.DataStoreUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityManageActivity extends BaseActivity {

    private static final String TAG = "CityManageActivity";

    @BindView(R.id.tb_city_manage)
    Toolbar toolbar;
    @BindView(R.id.rv_city_manage)
    RecyclerView recyclerView;
    @BindView(R.id.btn_city_manage_add)
    FloatingActionButton fab;

    private List<MyLocationBean> locationList;
    volatile private List<MyWeatherBean> weatherList = new ArrayList<>();
    volatile private List<MyWeatherNowBean> weatherNowList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData(){
        locationList = DataStoreUtil.getLocationList(this);
        Log.d(TAG, "initData " + new Gson().toJson(locationList));
        for(MyLocationBean locationBean : locationList){
            weatherList.add(DataStoreUtil.getWeather7d(this, locationBean.getId()).get(0));
            weatherNowList.add(DataStoreUtil.getWeatherNow(this, locationBean.getId()));
        }
        initView();
    }

    @Override
    protected void initView() {

        Drawable icBack = getResources().getDrawable(R.drawable.ic_back);
        icBack.setBounds(0, 0, 100, 100);
        toolbar.setNavigationIcon(icBack);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(getString(R.string.label_city_manage));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CityManageListAdapter adapter = new CityManageListAdapter(locationList, weatherNowList, weatherList);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CityManageActivity.this, CityAddActivity.class);
            startActivity(intent);
        });
    }
}
