package com.xzy.weather;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.xzy.weather.bean.MyWarningBean;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.bean.MyWeatherNowBean;
import com.xzy.weather.util.StringUtil;
import com.xzy.weather.util.TimeUtil;
import com.xzy.weather.warning.WarningActivity;
import com.xzy.weather.weather.DayWeatherListAdapter;
import com.xzy.weather.weather.DayWeatherListDecoration;
import com.xzy.weather.weather.GridListAdapter;
import com.xzy.weather.weather.GridListDecoration;
import com.xzy.weather.weather.HourWeatherListAdapter;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.util.PermissionUtil;
import com.xzy.weather.weather.HourWeatherListDecoration;
import com.xzy.weather.weather.SunView;

import java.util.ArrayList;
import java.util.List;

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

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private static final String HE_ID = "HE2008191124411841";
    private static final String HE_KEY = "1303d8f41ae24186b5de7055a0b0e2a4";

    private static final int INIT_FINISHED = 1;

    public GeoBean.LocationBean locationBean;

//    public WeatherNowBean.NowBaseBean weatherNowBaseBean;
//    public List<WeatherHourlyBean.HourlyBean> hourlyBeanList;
//    public List<WeatherDailyBean.DailyBean> weatherDailyBeanList;
//    public AirNowBean.NowBean nowBean;
//    public List<AirDailyBean.DailyBean> airDailyBeanList;
//    public List<WarningBean.WarningBeanBase> warningBeanBaseList;

    public MyWeatherNowBean weatherNow = new MyWeatherNowBean();
    public List<MyWeatherBean> weatherDailyList = new ArrayList<>();
    public List<MyWeatherBean> weatherHourlyList = new ArrayList<>();
    public List<MyWarningBean> warningList = new ArrayList<>();

    private int eventCount;       //天气信息获取计数，所有天气信息获取结束后更新UI

    public LocationClient mLocationClient;

    @BindView(R.id.iv_main_background)
    ImageView ivBackground;

    @BindView(R.id.tv_main_title_location)
    TextView tvLocation;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewSun.setSun(null, null);
    }

    @Override
    protected void init() {

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        for(int i = 0; i < 7; i++){
            weatherDailyList.add(new MyWeatherBean());
        }

        for(int i = 0; i < 24; i++){
            weatherHourlyList.add(new MyWeatherBean());
        }

        getPermissions();
    }

    private void updateView(){
        Log.d(TAG, "updateView");

        setBackground();

        tvLocation.setText(locationBean.getName());
        tvTemp.setText(weatherNow.getTemp());
        tvType.setText(weatherNow.getText());
        tvTempMax.setText(weatherDailyList.get(0).getTempMax() + "°C");
        tvTempMin.setText(weatherDailyList.get(0).getTempMin() + "°C");
        tvAir.setText("空气" + weatherNow.getAir());

        if(warningList == null || warningList.size() == 0){
            llWarning.setVisibility(View.GONE);
        } else {
            setWarning();
        }

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHour.setLayoutManager(layoutManager1);
        rvHour.setAdapter(new HourWeatherListAdapter(weatherHourlyList));
        rvHour.addItemDecoration(new HourWeatherListDecoration());
        rvHour.setItemViewCacheSize(weatherHourlyList.size());

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        rvDay.setLayoutManager(layoutManager2);
        rvDay.setAdapter(new DayWeatherListAdapter(weatherDailyList));
        rvDay.addItemDecoration(new DayWeatherListDecoration());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvGrid.setLayoutManager(gridLayoutManager);
        rvGrid.setAdapter(new GridListAdapter(weatherNow));
        rvGrid.post(new Runnable() {
            @Override
            public void run() {
                rvGrid.addItemDecoration(new GridListDecoration(rvGrid.getMeasuredWidth(), rvGrid.getChildAt(0).getWidth()));
            }
        });

        viewSun.setSun(weatherNow.getSunrise(), weatherNow.getSunset());
    }

    private void setBackground(){
        String weather = weatherNow.getText();
        int id = getResources().getIdentifier("background_" + StringUtil.getWeatherName(weather), "drawable", "com.xzy.weather");
        ivBackground.setBackground(getResources().getDrawable(id));
    }

    private void setWarning(){
        MyWarningBean bean = warningList.get(0);

        tvWarningType.setText(bean.getType() + bean.getLevel() + "预警");

        float hour = TimeUtil.getIntervalHour(
                TimeUtil.getHeFxTimeHour(bean.getPubTime()), TimeUtil.getHourNow(),
                TimeUtil.getHeFxTimeDate(bean.getPubTime()), TimeUtil.getDateNow());

        if(hour < 1){
            tvWarningTime.setText(String.format(getResources().getString(R.string.update_minute),hour*60));
        } else {
            tvWarningTime.setText(String.format(getResources().getString(R.string.update_hour),hour));
        }

        llWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WarningActivity.class);
                intent.putExtra("WarningList", new Gson().toJson(warningList));
                startActivity(intent);
            }
        });
    }

    /**
     * 获取初始所需权限
     */
    private void getPermissions(){
        PermissionUtil.addPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        PermissionUtil.addPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        PermissionUtil.addPermission(MainActivity.this, Manifest.permission.INTERNET);
        List<String> permissionList = PermissionUtil.getRequestList();
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
            permissionList.clear();
        } else {
            requestLocation();
        }
    }

    /**
     * 请求位置信息
     */
    private void requestLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 接收位置信息，通过位置申请天气信息
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.d(TAG, "Latitude: " + bdLocation.getLatitude());
            Log.d(TAG, "Longitude: " + bdLocation.getLongitude());
           // getWeatherFromHeAPI(bdLocation.getLatitude()+ "," + bdLocation.getLongitude());
            getWeatherFromHeAPI(bdLocation.getLongitude()+ "," + bdLocation.getLatitude());
        }
    }
    /**
     * 从和风天气获取天气信息
     * @param location 当前位置
     */
    private void getWeatherFromHeAPI(String location){

        HeConfig.init(HE_ID, HE_KEY);
        HeConfig.switchToDevService();

        eventCount = 7;

        HeWeather.getGeoCityLookup(MainActivity.this, location, new HeWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                Log.d(TAG, "onSuccess: " + new Gson().toJson(geoBean));

                if(geoBean.getLocationBean().size() > 0) {
                    locationBean = geoBean.getLocationBean().get(0);
                }

                if(--eventCount == 0){
                    updateView();
                }
            }
        });

        HeWeather.getWeatherNow(MainActivity.this, location, new HeWeather.OnResultWeatherNowListener(){

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

                if(--eventCount == 0){
                    updateView();
                }
            }
        });

        HeWeather.getAirNow(MainActivity.this, location, Lang.ZH_HANS, new HeWeather.OnResultAirNowListener(){

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Air now onError:", throwable);
            }

            @Override
            public void onSuccess(AirNowBean airNowBean) {
                Log.d(TAG, "Air now onSuccess:" + new Gson().toJson(airNowBean));
                    AirNowBean.NowBean nowBean = airNowBean.getNow();

                    weatherNow.setAir(nowBean.getCategory());

                if(--eventCount == 0){
                    updateView();
                }
            }
        });

        HeWeather.getWeather24Hourly(MainActivity.this, location, new HeWeather.OnResultWeatherHourlyListener() {
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

                     weatherHourlyList.get(i).setTime(TimeUtil.getHeFxTimeHour(bean.getFxTime()));
                     weatherHourlyList.get(i).setTemp(bean.getTemp());
                     weatherHourlyList.get(i).setWindScale(bean.getWindScale());
                     weatherHourlyList.get(i).setText(bean.getText());
                 }

                if(--eventCount == 0){
                    updateView();
                }
            }
        });

        HeWeather.getWeather7D(MainActivity.this, location, new HeWeather.OnResultWeatherDailyListener() {
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

                if(--eventCount == 0){
                    updateView();
                }
            }
        });

        HeWeather.getAir5D(MainActivity.this, location, Lang.ZH_HANS, new HeWeather.OnResultAirDailyListener() {
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

                if(--eventCount == 0){
                    updateView();
                }
            }
        });

        HeWeather.getWarning(MainActivity.this, location, new HeWeather.OnResultWarningListener() {
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

                if(--eventCount == 0){
                    updateView();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1 :
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }
                break;
            default:
                break;
        }
    }
}
