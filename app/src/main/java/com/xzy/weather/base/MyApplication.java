package com.xzy.weather.base;

import android.app.Application;
import android.content.Context;

import com.xzy.weather.bean.SettingBean;

/**
 * Author:xzy
 * Date:2020/8/28 17:47
 **/
public class MyApplication extends Application {

    private static Context mContext;
    //private static MyApplication app;

    //private SettingBean setting;

//    public static MyApplication getInstance() {
//        return app;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        //app = this;
        mContext = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        //app = null;
        super.onTerminate();
    }

    public static Context getContext(){
        return mContext;
    }

//    public SettingBean getSetting() {
//        return setting;
//    }
//
//    public void setSetting(SettingBean setting) {
//        this.setting = setting;
//    }
}
