package com.xzy.weather.weather;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xzy.weather.GlobalData;
import com.xzy.weather.R;
import com.xzy.weather.base.BaseFragment;
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.bean.MyWarningBean;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.bean.MyWeatherNowBean;
import com.xzy.weather.bean.SettingBean;
import com.xzy.weather.util.DataStoreUtil;
import com.xzy.weather.util.HeWeatherUtil;
import com.xzy.weather.util.StringUtil;
import com.xzy.weather.util.TimeUtil;
import com.xzy.weather.warning.WarningActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherFragment extends BaseFragment {

    private static final String TAG = "WeatherFragment";

    private static final int DAYS_OF_WEEK = 7;
    private static final int HOURS_OF_DAY = 24;
    private static final int EVENT_COUNT = 7;
    private static final float ALPHA_MAX = 1.0f;
    private static final float ALPHA_MIN = 0;

    public MyWeatherNowBean weatherNow;
    public List<MyWeatherBean> weatherDailyList = new ArrayList<>();
    private List<MyWeatherBean> weatherHourlyList = new ArrayList<>();
    private List<MyWarningBean> warningList = new ArrayList<>();
    private MyLocationBean location = new MyLocationBean();

    private AtomicInteger atomicInteger = new AtomicInteger();  //和风天气请求事件计数，所有事件完成后更新界面

    @BindView(R.id.nested_scrollview_main)
    NestedScrollView nestedScrollView;
    @BindView(R.id.fl_main_title)
    FrameLayout flTitle;

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

    @BindView(R.id.iv_main_background)
    ImageView ivBackground;
    @BindView(R.id.tv_main_info_temperature)
    TextView tvTemp;
    @BindView(R.id.tv_main_info_unit)
    TextView tvUnit;
    @BindView(R.id.tv_main_info_max)
    TextView tvTempMax;
    @BindView(R.id.tv_main_info_min)
    TextView tvTempMin;
    @BindView(R.id.tv_main_info_air)
    TextView tvAir;
    @BindView(R.id.tv_main_info_type)
    TextView tvType;


    SettingBean setting;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public WeatherFragment(MyLocationBean location){
        this.location = location;
    }

    private void initData(){

        Log.d(TAG, "initData");
        String time = DataStoreUtil.getLocationInfoUpdateTime(getApplicationContext(), location.getId());
        weatherNow = new MyWeatherNowBean();
        weatherDailyList.clear();
        weatherHourlyList.clear();
        warningList.clear();
        if(TimeUtil.getIntervalHour(TimeUtil.getHourNow(), TimeUtil.getTimeHour(time), TimeUtil.getDateNow(), TimeUtil.getTimeDate(time)) < 0.1f){
            weatherNow = DataStoreUtil.getWeatherNow(getApplicationContext(), location.getId());
            weatherHourlyList = DataStoreUtil.getWeather24h(getApplicationContext(), location.getId());
            weatherDailyList = DataStoreUtil.getWeather7d(getApplicationContext(), location.getId());
            warningList = DataStoreUtil.getWarning(getApplicationContext(), location.getId());
            atomicInteger.set(1);
            updateView();
        } else {
            for(int i = 0; i < DAYS_OF_WEEK; i++){
                weatherDailyList.add(new MyWeatherBean());
            }
            for(int i = 0; i < HOURS_OF_DAY; i++){
                weatherHourlyList.add(new MyWeatherBean());
            }
            getWeatherFromHeAPI();
        }

        setting = GlobalData.getInstance().getSetting();
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateView(){
        if(atomicInteger.getAndDecrement() != 1) {
            return;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                int height = flTitle.getHeight()/2;
                if (scrollY < height) {
                    float scale = (float) scrollY / height;
                    flTitle.setAlpha(ALPHA_MAX * (1 - scale));
                } else {
                    flTitle.setAlpha(ALPHA_MIN);
                }
            });
        }

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
        DataStoreUtil.setLocationInfoUpdateTime(getApplicationContext(), location.getId());
        DataStoreUtil.setWarning(getApplicationContext(), location.getId(), warningList);
        DataStoreUtil.setWeather7d(getApplicationContext(), location.getId(), weatherDailyList);
        DataStoreUtil.setWeather24h(getApplicationContext(), location.getId(), weatherHourlyList);
        DataStoreUtil.setWeatherNow(getApplicationContext(), location.getId(), weatherNow);
    }

    private void updateWeatherNowView(){
        tvTemp.setText(weatherNow.getTemp());
        tvType.setText(weatherNow.getText());
        tvUnit.setText(setting.getTempUnit());
        tvTempMax.setText(String.format(getResources().getString(R.string.temperature), weatherDailyList.get(0).getTempMax(), setting.getTempUnit()));
        tvTempMin.setText(String.format(getResources().getString(R.string.temperature), weatherDailyList.get(0).getTempMin(), setting.getTempUnit()));
        tvAir.setText(String.format(getResources().getString(R.string.air), weatherNow.getAir()));

        String weather = weatherNow.getText();
        int id = getResources().getIdentifier("background_" + StringUtil.getWeatherBackgroundName(weather), "drawable", "com.xzy.weather");
        ivBackground.setBackground(getResources().getDrawable(id));

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

        atomicInteger.set(EVENT_COUNT - 1);

        weatherNow = new MyWeatherNowBean();
        warningList.clear();

        HeWeatherUtil.getWeatherNow(getApplicationContext(), location, weatherNow, this::updateView);

        HeWeatherUtil.getAirNow(getApplicationContext(), location, weatherNow, this::updateView);

        HeWeatherUtil.getWeather24Hourly(getApplicationContext(), location, weatherHourlyList, this::updateView);

        HeWeatherUtil.getWeather7D(getApplicationContext(), location, weatherNow, weatherDailyList, this::updateView);

        HeWeatherUtil.getAir5D(getApplicationContext(), location, weatherDailyList, this::updateView);

        HeWeatherUtil.getWarning(getApplicationContext(), location, warningList, this::updateView);
    }
}
