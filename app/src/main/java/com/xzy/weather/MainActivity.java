package com.xzy.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.bean.SettingBean;
import com.xzy.weather.city.CityManageActivity;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.setting.SettingActivity;
import com.xzy.weather.util.DataStoreUtil;
import com.xzy.weather.util.HeWeatherUtil;
import com.xzy.weather.util.PermissionUtil;
import com.xzy.weather.weather.WeatherFragment;
import com.xzy.weather.weather.WeatherFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.iv_main_title_city)
    ImageView ivCity;
    @BindView(R.id.iv_main_title_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_main_title_location)
    TextView tvLocation;

    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.ll_main_selector)
    LinearLayout llSelector;
    @BindView(R.id.appbar_main)
    AppBarLayout appBarLayout;
    @BindView(R.id.srl_main)
    SwipeRefreshLayout swipeRefreshLayout;

    SettingBean setting;

    public LocationClient mLocationClient;

    public List<MyLocationBean> locationList = new ArrayList<>();

    private List<WeatherFragment> fragments = new ArrayList<>();

    private boolean displayLocal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getPermissions();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void initData() {

        locationList = DataStoreUtil.getLocationList(this);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        requestLocation();

        setting = DataStoreUtil.getSettingInfo(getApplicationContext());

        GlobalData.getInstance().setSetting(setting);
        //getPermissions();
    }

    @Override
    protected void initView(){
        //StatusBarUtil.setTranslucentForImageView(this, 100, ivBackground);

        for(MyLocationBean location : locationList){
            fragments.add(new WeatherFragment(location));
        }

        Log.d(TAG, "initView: " + new Gson().toJson(locationList));
        DataStoreUtil.setLocationList(this, locationList);

        initSelector();
        initViewPager();

        ivCity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CityManageActivity.class);
            startActivityForResult(intent, 1);
        });

        ivSetting.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        appBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> {
            if(i >= 0){
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        });
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     * 添加ViewPager页面位置指示图标
     */
    private void initSelector(){
        for(int i = 0; i < locationList.size(); i++){
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10,10);
            params.setMargins(5,5,5,5);
            if(i == 0 && displayLocal){
                view.setBackgroundResource(R.drawable.selector_local);
            } else {
                view.setBackgroundResource(R.drawable.selector_point);
            }
            view.setLayoutParams(params);
            llSelector.addView(view);
        }
    }

    private void initViewPager(){
        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                MyLocationBean location = locationList.get(position);
                tvLocation.setText(location.getName());

                for (int i = 0; i < llSelector.getChildCount(); i++) {
                    llSelector.getChildAt(i).setSelected(false);
                }
                llSelector.getChildAt(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        viewPager.addOnPageChangeListener(listener);
        viewPager.setAdapter(new WeatherFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.postDelayed(() -> {
            listener.onPageSelected(viewPager.getCurrentItem());
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) viewPager.getLayoutParams();
            lp.topMargin = -appBarLayout.getHeight();
        }, 1000);
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
            String[] permissions = permissionList.toArray(new String[0]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
            permissionList.clear();
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
            getHeWeatherData(bdLocation.getLongitude() + "," + bdLocation.getLatitude());
            mLocationClient.stop();

            mLocationClient = null;
        }
    }

    public void getHeWeatherData(String location){

        MyLocationBean myLocationBean = new MyLocationBean();
        HeWeatherUtil.getGeoCityLookup(this.getApplicationContext(), location, myLocationBean, () -> onReceiveLocationData(myLocationBean));
    }

    /**
     * 处理和风天气返回的位置信息
     */
    public void onReceiveLocationData(MyLocationBean myLocationBean) {
        if(myLocationBean != null) {
            if (locationList == null) {
                locationList = new ArrayList<>();
            }
            if (locationList.size() == 0 || locationList.get(0).getId() == null ||!locationList.get(0).getId().equals(myLocationBean.getId())) {
                //TODO 添加对话框
                displayLocal = true;
                locationList.add(0, myLocationBean);
                fragments.add(0, new WeatherFragment(myLocationBean));
            }
        }
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == 1){
                initData();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1 :
                if(grantResults.length > 0) {
                    for(int result : grantResults) {
                        if(result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
