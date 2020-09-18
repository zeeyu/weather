package com.xzy.weather.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.xzy.weather.MainActivity;
import com.xzy.weather.R;
import com.xzy.weather.setting.NotificationService;

/**
 * Author:xzy
 * Date:2020/9/16 17:06
 **/
public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";

    private static final int NOTIFICATION_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("id1", "channel1", NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            notification = new NotificationCompat.Builder(context, "id1")
                    .setSmallIcon(R.drawable.icon_city)
                    .setContentTitle("aaa")
                    .setContentText("aaa")
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pi)
                    .build();
        } else {
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.icon_city)
                    .setContentIntent(pi)
                    .build();
        }
        if(manager != null) {
            manager.notify(NOTIFICATION_ID, notification);
        }

        Intent intent2 = new Intent(context, NotificationService.class);
        context.startService(intent2);
    }
}
