package com.xzy.weather.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import com.xzy.weather.util.TimeUtil;

/**
 * Author:xzy
 * Date:2020/9/16 17:09
 **/
public class NotificationService extends Service {

    private static final String TAG = "NotificationService";
    int count = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long secondsNextEarlyMorning = TimeUtil.getMillisNextEarlyMorning(9);
        Intent intent1 = new Intent(this, NotificationReceiver.class);
        //Log.d(TAG, "onStartCommand: " + secondsNextEarlyMorning);
        PendingIntent pi = PendingIntent.getBroadcast(this, count++, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        if(manager != null) {
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + secondsNextEarlyMorning, pi);
        }
        return START_STICKY;
    }
}
