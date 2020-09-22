package com.xzy.weather.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.xzy.weather.util.TimeUtil;

/**
 * Author:xzy
 * Date:2020/9/21 11:05
 **/
public class WidgetUpdateService extends Service {

    private static final String TAG = "WidgetUpdateService";

    //private static final long MILLIS_PER_MINUTE = 60000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long secondsNextMinutes = TimeUtil.getMillisNextMinute();
        Intent intent1 = new Intent(this, AppWidget.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(TAG, "onStartCommand: " + secondsNextMinutes);
        if(manager != null) {
            //manager.setRepeating(AlarmManager.RTC_WAKEUP, secondsNextMinutes, MILLIS_PER_MINUTE, pi);
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + secondsNextMinutes, pi);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        Intent restartIntent = new Intent("com.xzy.weather.restart");
        restartIntent.setClassName(this, "com.xzy.weather.widget.AppWidget");
        sendBroadcast(restartIntent);
        super.onDestroy();
    }
}
