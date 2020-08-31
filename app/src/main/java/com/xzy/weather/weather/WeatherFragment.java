package com.xzy.weather.weather;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xzy.weather.R;
import com.xzy.weather.base.BaseFragment;
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.bean.MyWarningBean;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.bean.MyWeatherNowBean;
import com.xzy.weather.util.DataStoreUtil;
import com.xzy.weather.util.TimeUtil;
import com.xzy.weather.warning.WarningActivity;
import com.xzy.weather.weather.DayWeatherListAdapter;
import com.xzy.weather.weather.DayWeatherListDecoration;
import com.xzy.weather.weather.GridListAdapter;
import com.xzy.weather.weather.GridListDecoration;
import com.xzy.weather.weather.HourWeatherListAdapter;
import com.xzy.weather.weather.HourWeatherListDecoration;
import com.xzy.weather.weather.SunView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.heweather.com.interfacesmodule.bean.WarningBean;
import interfaces.heweather.com.interfacesmodule.bean.air.AirDailyBean;
import interfaces.heweather.com.interfacesmodule.bean.air.AirNowBean;
import interfaces.heweather.com.interfacesmodule.bean.base.Lang;
import interfaces.heweather.com.interfacesmodule.bean.geo.GeoBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class WeatherFragment extends BaseFragment {

    private static final String TAG = "WeatherFragment";

    public MyWeatherNowBean weatherNow = new MyWeatherNowBean();
    public List<MyWeatherBean> weatherDailyList = new ArrayList<>();
    public List<MyWeatherBean> weatherHourlyList = new ArrayList<>();
    public List<MyWarningBean> warningList = new ArrayList<>();
    public MyLocationBean location = new MyLocationBean();

    private AtomicInteger atomicInteger = new AtomicInteger();  //和风天气请求事件计数，所有事件完成后更新界面

    @BindView(R.id.tv_main_info_temperature)
    TextView tvTemp;
    @BindView(R.id.tv_main_info_max)
    TextView tvTempMax;
    @BindView(R.id.tv_main_info_min)
    TextView tvTempMin;
    @BindView(R.id.tv_main_info_air)
    TextView tvAir;
    @BindView(R.id.tv_main_info_type)
    TextView tvType;
    @BindView(R.id.ll_main_warning)
    LinearLayout llWarning;
    @BindView(R.id.tv_main_warning_type)
    TextView tvWarningType;
    @BindView(R.id.tv_main_warning_time)
    TextView tvWarningTime;
    @BindView(R.id.rv_main_hour)
    RecyclerView rvHour;
    @BindView(R.id.rv_main_day)
    RecyclerView rvDay;
    @BindView(R.id.rv_main_grid)
    RecyclerView rvGrid;
    @BindView(R.id.view_main_sun)
    SunView viewSun;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public WeatherFragment(MyLocationBean location){
        this.location = location;
    }

    void initData(){

        Log.d(TAG, "initData");
        String time = DataStoreUtil.getLocationInfoUpdateTime(getContext(), location.getId());
        if(TimeUtil.getIntervalHour(TimeUtil.getHourNow(), TimeUtil.getTimeHour(time), TimeUtil.getDateNow(), TimeUtil.getTimeDate(time)) < 0.1f){
            weatherNow = DataStoreUtil.getWeatherNow(getContext(), location.getId());
            weatherHourlyList = DataStoreUtil.getWeather24h(getContext(), location.getId());
            weatherDailyList = DataStoreUtil.getWeather7d(getContext(), location.getId());
            warningList = DataStoreUtil.getWarning(getContext(), location.getId());
            updateView();
        } else {
            for(int i = 0; i < 7; i++){
                weatherDailyList.add(new MyWeatherBean());
            }
            for(int i = 0; i < 24; i++){
                weatherHourlyList.add(new MyWeatherBean());
            }
            getWeatherFromHeAPI();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initData();
        Log.d(TAG, "onViewCreated" + location);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateView(){
        Log.d(TAG, "updateView");

        if(warningList == null || warningList.size() == 0){
            llWarning.setVisibility(View.GONE);
        } else {
            updateWarningView();
        }

        updateWeatherNowView();
        updateWeather7dView();
        updateWeather24hView();
        updateWeatherGridView();

        storageData();
    }

    synchronized private void storageData(){
        DataStoreUtil.setLocationInfoUpdateTime(getContext(), location.getId());
        DataStoreUtil.setWarning(getContext(), location.getId(), warningList);
        DataStoreUtil.setWeather7d(getContext(), location.getId(), weatherDailyList);
        DataStoreUtil.setWeather24h(getContext(), location.getId(), weatherHourlyList);
        DataStoreUtil.setWeatherNow(getContext(), location.getId(), weatherNow);
    }

    private void updateWeatherNowView(){
        tvTemp.setText(weatherNow.getTemp());
        tvType.setText(weatherNow.getText());
        tvTempMax.setText(weatherDailyList.get(0).getTempMax() + "°C");
        tvTempMin.setText(weatherDailyList.get(0).getTempMin() + "°C");
        tvAir.setText("空气" + weatherNow.getAir());
        viewSun.setSun(weatherNow.getSunrise(), weatherNow.getSunset());
    }

    private void updateWeather24hView(){
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHour.setNestedScrollingEnabled(false);
        rvHour.setLayoutManager(layoutManager1);
        rvHour.setAdapter(new HourWeatherListAdapter(weatherHourlyList));
        rvHour.addItemDecoration(new HourWeatherListDecoration());
        rvHour.setItemViewCacheSize(weatherHourlyList.size());
    }

    private void updateWeather7dView(){
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvDay.setNestedScrollingEnabled(false);
        rvDay.setLayoutManager(layoutManager2);
        rvDay.setAdapter(new DayWeatherListAdapter(weatherDailyList));
        rvDay.addItemDecoration(new DayWeatherListDecoration());
    }

    private void updateWeatherGridView(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvGrid.setLayoutManager(gridLayoutManager);
        rvGrid.setAdapter(new GridListAdapter(weatherNow));
        rvGrid.post(() -> rvGrid.addItemDecoration(new GridListDecoration(rvGrid.getMeasuredWidth(), rvGrid.getChildAt(0).getWidth())));
    }

    private void updateWarningView(){
        MyWarningBean bean = warningList.get(0);

        tvWarningType.setText(bean.getType() + bean.getLevel() + "预警");

        float hour = TimeUtil.getIntervalHour(
                TimeUtil.getTimeHour(bean.getPubTime()), TimeUtil.getHourNow(),
                TimeUtil.getTimeDate(bean.getPubTime()), TimeUtil.getDateNow());

        if(hour < 1){
            tvWarningTime.setText(String.format(getResources().getString(R.string.update_minute),(int)(hour*60)));
        } else {
            tvWarningTime.setText(String.format(getResources().getString(R.string.update_hour),hour));
        }

        llWarning.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), WarningActivity.class);
            intent.putExtra("WarningList", new Gson().toJson(warningList));
            startActivity(intent);
        });
    }

    /**
     * 从和风天气获取天气信息
     */
    private void getWeatherFromHeAPI(){

        String location = this.location.getId();

        HeConfig.init(getString(R.string.HE_ID), getString(R.string.HE_KEY));
        HeConfig.switchToDevService();

        atomicInteger.set(6);

        HeWeather.getWeatherNow(getContext(), location, new HeWeather.OnResultWeatherNowListener(){

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Weather now onError:", throwable);
            }

            @Override
            public void onSuccess(WeatherNowBean weatherNowBean) {
                Log.d(TAG, "Weather now onSuccess:" + new Gson().toJson(weatherNowBean));
                WeatherNowBean.NowBaseBean weatherNowBaseBean = weatherNowBean.getNow();

                weatherNow.setTemp(weatherNowBaseBean.getTemp());
                weatherNow.setText(weatherNowBaseBean.getText());
                weatherNow.setHumidity(weatherNowBaseBean.getHumidity());
                weatherNow.setVis(weatherNowBaseBean.getVis());
                weatherNow.setWindDir(weatherNowBaseBean.getWindDir());
                weatherNow.setWindScale(weatherNowBaseBean.getWindScale());

                if(atomicInteger.getAndDecrement() == 1) {
                    updateView();
                }
            }
        });

        HeWeather.getAirNow(getContext(), location, Lang.ZH_HANS, new HeWeather.OnResultAirNowListener(){

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Air now onError:", throwable);
            }

            @Override
            public void onSuccess(AirNowBean airNowBean) {
                Log.d(TAG, "Air now onSuccess:" + new Gson().toJson(airNowBean));
                AirNowBean.NowBean nowBean = airNowBean.getNow();

                weatherNow.setAir(nowBean.getCategory());

                if(atomicInteger.getAndDecrement() == 1) {
                    updateView();
                }
            }
        });

        HeWeather.getWeather24Hourly(getContext(), location, new HeWeather.OnResultWeatherHourlyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Weather hourly onError:", throwable);
            }

            @Override
            public void onSuccess(WeatherHourlyBean weatherHourlyBean) {
                Log.d(TAG, "Weather hourly onSuccess:" + new Gson().toJson(weatherHourlyBean));
                List<WeatherHourlyBean.HourlyBean> beanList = weatherHourlyBean.getHourly();
                for(int i = 0; i < beanList.size(); i++){
                    WeatherHourlyBean.HourlyBean bean = beanList.get(i);

                    weatherHourlyList.get(i).setTime(TimeUtil.getTimeHour(bean.getFxTime()));
                    weatherHourlyList.get(i).setTemp(bean.getTemp());
                    weatherHourlyList.get(i).setWindScale(bean.getWindScale());
                    weatherHourlyList.get(i).setText(bean.getText());
                }

                if(atomicInteger.getAndDecrement() == 1) {
                    updateView();
                }
            }
        });

        HeWeather.getWeather7D(getContext(), location, new HeWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Weather7d onError:", throwable);
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                Log.d(TAG, "Weather7d onSuccess:" + new Gson().toJson(weatherDailyBean));

                List<WeatherDailyBean.DailyBean> beanList = weatherDailyBean.getDaily();
                for(int i = 0; i < beanList.size(); i++){

                    WeatherDailyBean.DailyBean bean = beanList.get(i);

                    if(i == 0) {
                        weatherNow.setUv(bean.getUvIndex());
                        weatherNow.setSunrise(bean.getSunrise());
                        weatherNow.setSunset(bean.getSunset());
                    }

                    weatherDailyList.get(i).setDate(bean.getFxDate());
                    weatherDailyList.get(i).setTempMax(bean.getTempMax());
                    weatherDailyList.get(i).setTempMin(bean.getTempMin());
                    weatherDailyList.get(i).setText(bean.getTextDay());
                }

                if(atomicInteger.getAndDecrement() == 1) {
                    updateView();
                }
            }
        });

        HeWeather.getAir5D(getContext(), location, Lang.ZH_HANS, new HeWeather.OnResultAirDailyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Air5d onError:", throwable);
            }

            @Override
            public void onSuccess(AirDailyBean airDailyBean) {
                Log.d(TAG, "Air5d onSuccess:" + new Gson().toJson(airDailyBean));
                List<AirDailyBean.DailyBean> beanList = airDailyBean.getAirDaily();
                for(int i = 0; i < beanList.size(); i++){
                    AirDailyBean.DailyBean bean = beanList.get(i);

                    weatherDailyList.get(i).setAir(bean.getCategory());
                }

                if(atomicInteger.getAndDecrement() == 1) {
                    updateView();
                }
            }
        });

        HeWeather.getWarning(getContext(), location, new HeWeather.OnResultWarningListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Warning onError:", throwable);
            }

            @Override
            public void onSuccess(WarningBean warningBean) {
                Log.d(TAG, "Warning onSuccess:" + new Gson().toJson(warningBean));

                for(WarningBean.WarningBeanBase base : warningBean.getBeanBaseList()){

                    MyWarningBean bean = new MyWarningBean();
                    bean.setPubTime(base.getPubTime());
                    bean.setLevel(base.getLevel());
                    bean.setText(base.getText());
                    bean.setType(base.getTypeName());
                    bean.setTitle(base.getTitle());
                    bean.setSender(base.getSender());

                    warningList.add(bean);
                }

                if(atomicInteger.getAndDecrement() == 1) {
                    updateView();
                }
            }
        });
    }
}
