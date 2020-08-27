package com.xzy.weather;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.xzy.weather.city.CityManageActivity;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.util.PermissionUtil;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.iv_main_background)
    ImageView ivBackground;
    @BindView(R.id.iv_main_title_city)
    ImageView ivCity;
    @BindView(R.id.iv_main_title_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_main_title_location)
    TextView tvLocation;

    public LocationClient mLocationClient;

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
    }

    @Override
    protected void init() {

        StatusBarUtil.setTranslucentForImageView(this, 100, ivBackground);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        getPermissions();

        ivCity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CityManageActivity.class);
            intent.putExtra("CityList", new Gson().toJson(weatherNow));
            startActivity(intent);
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
           // getWeatherFromHeAPI(bdLocation.getLatitude()+ "," + bdLocation.getLongitude());
            getWeatherFromHeAPI(bdLocation.getLongitude()+ "," + bdLocation.getLatitude());
        }
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
