package com.xzy.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.util.HttpUtil;
import com.xzy.weather.util.PermissionUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    public LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void init() {

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        getPermissions();

        Log.d(TAG, "init: ");
        
        HttpUtil.sendOkHttpRequest("https://devapi.heweather.net/v7/weather/7d?location=101010100&key=1303d8f41ae24186b5de7055a0b0e2a4",
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.d(TAG, "onResponse: " + response.body().string());
                    }
                });
    }

    /**
     * 获取初始所需权限
     */
    void getPermissions(){
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

    private void requestLocation(){
        mLocationClient.start();
    }

    /**
     * 接收位置信息，通过位置申请天气信息
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.d(TAG, "onReceiveLocation: " + bdLocation.getLatitude());
            Log.d(TAG, "onReceiveLocation: " + bdLocation.getLongitude());
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
