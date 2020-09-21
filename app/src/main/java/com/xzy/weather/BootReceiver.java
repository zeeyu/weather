package com.xzy.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Author:xzy
 * Date:2020/9/17 11:19
 **/
public class BootReceiver extends BroadcastReceiver {

    private final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
