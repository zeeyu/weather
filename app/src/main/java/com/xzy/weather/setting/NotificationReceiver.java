package com.xzy.weather.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Author:xzy
 * Date:2020/9/16 17:06
 **/
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO 通知

        Intent intent1 = new Intent(context, NotificationService.class);
        context.startService(intent1);
    }
}
