package com.xzy.weather.base;

import android.app.Application;
import android.content.Context;

/**
 * Author:xzy
 * Date:2020/8/28 17:47
 **/
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
