package com.xzy.weather.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Author:xzy
 * Date:2020/9/1 16:26
 **/
public class SystemUtil {

    public static void closeSoftInput(Context context, View v){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                // 当前所连接的网络可用
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    public static boolean isGpsAvailable(Context context) {
        LocationManager lm;
        lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        //开了定位服务
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    };
}
