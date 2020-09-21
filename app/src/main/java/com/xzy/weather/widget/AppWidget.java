package com.xzy.weather.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.xzy.weather.MainActivity;
import com.xzy.weather.R;
import com.xzy.weather.util.TimeUtil;

public class AppWidget extends AppWidgetProvider {

    private static final String TAG = "AppWidget";

    static void updateAppWidget(Context context) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        ComponentName componentName = new ComponentName(context, AppWidget.class);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_app);

        updateTime(context, views);
        updateWeather(context, views);

        appWidgetManager.updateAppWidget(componentName, views);
    }

    private static void updateTime(Context context, RemoteViews views) {

        String time = TimeUtil.getHourNow();
        String date = TimeUtil.getDateNow();
        String week = TimeUtil.getWeek(TimeUtil.strToDate(date));

        views.setTextViewText(R.id.tv_widget_time, time);
        Intent timeIntent = new Intent();
        timeIntent.setClassName("com.android.deskclock", "com.android.deskclock.DeskClock");
        PendingIntent pi1 = PendingIntent.getActivity(context, 0, timeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_widget_time, pi1);

        views.setTextViewText(R.id.tv_widget_date, date + " " + week);
        Intent calendarIntent = new Intent();
        calendarIntent.setClassName("com.android.calendar", "com.android.calendar.LaunchActivity");
        PendingIntent pi2 = PendingIntent.getActivity(context, 1, calendarIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_widget_date, pi2);
    }

    private static void updateWeather(Context context, RemoteViews views) {

        Intent weatherIntent = new Intent(context, MainActivity.class);
        PendingIntent pi3 = PendingIntent.getActivity(context, 2, weatherIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.ll_widget_weather, pi3);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive");

        String action = intent.getAction();
        if(action == null) {
            return;
        }
        switch(action) {
            case "com.xzy.weather.restart" :
            case Intent.ACTION_BOOT_COMPLETED :
                context.startService(new Intent(context, WidgetUpdateService.class));
                break;
            default :
                updateAppWidget(context);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        updateAppWidget(context);
        Intent startServiceIntent = new Intent(context, WidgetUpdateService.class);
        context.startService(startServiceIntent);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled");
        Intent startServiceIntent = new Intent(context, WidgetUpdateService.class);
        context.startService(startServiceIntent);
    }

    @Override
    public void onDisabled(Context context) {
        Intent stopServiceIntent = new Intent(context, WidgetUpdateService.class);
        context.stopService(stopServiceIntent);
    }
}

