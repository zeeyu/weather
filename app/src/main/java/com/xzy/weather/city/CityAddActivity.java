package com.xzy.weather.city;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.xzy.weather.R;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.util.DataStoreUtil;
import com.xzy.weather.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.heweather.com.interfacesmodule.bean.base.Lang;
import interfaces.heweather.com.interfacesmodule.bean.base.Range;
import interfaces.heweather.com.interfacesmodule.bean.geo.GeoBean;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class CityAddActivity extends BaseActivity {

    private static final String TAG = "CityAddActivity";

    @BindView(R.id.ed_city_add_search)
    SearchEditText edSearch;
    @BindView(R.id.tb_city_add)
    Toolbar toolbar;
    @BindView(R.id.btn_city_add_cancel)
    Button btnCancel;
    @BindView(R.id.ll_city_add_search)
    LinearLayout linearLayout;

    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) edSearch.getLayoutParams();
    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) btnCancel.getLayoutParams();

    List<MyLocationBean> topCityList = new ArrayList<>();
    MyLocationBean local;

    public static int BUTTON_WIDTH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_add);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
//        String time = DataStoreUtil.getLocationInfoUpdateTime(this, location.getId());
//        if(TimeUtil.getIntervalHour(TimeUtil.getHourNow(), TimeUtil.getTimeHour(time), TimeUtil.getDateNow(), TimeUtil.getTimeDate(time)) < 0.1f){
//
//            updateList();
//        }
        //TODO
        getHeWeatherData();
        initView();
    }

    @Override
    protected void initView() {

        Drawable icBack = getResources().getDrawable(R.drawable.ic_back);
        icBack.setBounds(0, 0, 30, 30);
        toolbar.setNavigationIcon(icBack);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(getString(R.string.label_city_add));

        edSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                params1.width = linearLayout.getWidth() - BUTTON_WIDTH;
                params2.width = BUTTON_WIDTH;
                edSearch.setLayoutParams(params1);
                btnCancel.setLayoutParams(params2);
            }
        });

        btnCancel.setOnClickListener(v -> {
            params1.width = linearLayout.getWidth();
            params2.width = 0;
            edSearch.setLayoutParams(params1);
            edSearch.setText("");
            btnCancel.setLayoutParams(params2);
        });


    }

    protected void updateList(){
        //TODO
    }

    public void getHeWeatherData(){

        HeConfig.init(getString(R.string.HE_ID), getString(R.string.HE_KEY));
        HeConfig.switchToDevService();

        HeWeather.getGeoTopCity(this, 20, Range.CN, Lang.ZH_HANS, new HeWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "onError : getGeoTopCity");
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                for(GeoBean.LocationBean locationBean : locationBeanList){
                    topCityList.add(new MyLocationBean(
                            locationBean.getId(),
                            locationBean.getName(),
                            locationBean.getCountry(),
                            locationBean.getAdm2(),
                            locationBean.getAdm1(),
                            locationBean.getType(),
                            false,
                            false));
                }
                updateList();
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
