package com.xzy.weather;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.xzy.weather.adapter.HourWeatherListAdapter;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.util.PermissionUtil;

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

    public WeatherNowBean.NowBaseBean nowBaseBean;

    public List<WeatherHourlyBean.HourlyBean> hourlyBeanList;

    public List<WeatherDailyBean.DailyBean> dailyBeanList;

    public AirNowBean.NowBean nowBean;

    public List<AirDailyBean.DailyBean> airDailyBeanList;

    public List<WarningBean.WarningBeanBase> warningBeanBaseList;

    private int eventCount;       //天气信息获取计数，所有天气信息获取结束后更新UI

    public LocationClient mLocationClient;

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
    LinearLayout ll;

    @BindView(R.id.rv_main_hour)
    RecyclerView rvHour;

    @BindView(R.id.rv_main_day)
    RecyclerView rvDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void init() {

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        getPermissions();
    }

    private void updateView(){
        Log.d(TAG, "updateView");
        tvLocation.setText(locationBean.getName());
        tvTemp.setText(nowBaseBean.getTemp());
        tvType.setText(nowBaseBean.getText());
        tvTempMax.setText(dailyBeanList.get(0).getTempMax() + "°C");
        tvTempMin.setText(dailyBeanList.get(0).getTempMin() + "°C");
        tvAir.setText("空气" + nowBean.getCategory());

        if(warningBeanBaseList != null && warningBeanBaseList.size() != 0){
            ll.setVisibility(View.GONE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHour.setLayoutManager(layoutManager);
        rvHour.setAdapter(new HourWeatherListAdapter(hourlyBeanList));
        //TODO UPDATE VIEW
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
                nowBaseBean = weatherNowBean.getNow();

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
                    nowBean = airNowBean.getNow();

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
                hourlyBeanList = weatherHourlyBean.getHourly();

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
                dailyBeanList = weatherDailyBean.getDaily();

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
                airDailyBeanList = airDailyBean.getAirDaily();

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
                warningBeanBaseList = warningBean.getBeanBaseList();

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
