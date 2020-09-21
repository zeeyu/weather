package com.xzy.weather.setting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.xzy.weather.GlobalData;
import com.xzy.weather.R;
import com.xzy.weather.bean.SettingBean;
import com.xzy.weather.notification.NotificationService;

/**
 * Author:xzy
 * Date:2020/9/17 14:19
 **/
public class SettingFragment extends PreferenceFragmentCompat {

    private static final String TAG = "SettingFragment";

    public static final String UNIT_CHANGE_ACTION = "com.xzy.weather.setting.UNIT_CHANGE_ACTION";

    private SettingListPreference settingUnitPreference;
    private SettingListPreference settingUpdatePreference;
    private SwitchPreference switchPreference;

    LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting);

        SettingBean setting = GlobalData.getInstance().getSetting();

        settingUnitPreference = findPreference("setting_temp_unit");
        settingUpdatePreference = findPreference("setting_auto_update");
        switchPreference = findPreference("setting_weather_notify");

        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext());

        settingUnitPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            setting.setTempUnit(newValue.toString());
            localBroadcastManager.sendBroadcast(new Intent(UNIT_CHANGE_ACTION));

            return true;
        });

        settingUpdatePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            return true;
        });

        switchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            setting.setWeatherNotify((Boolean) newValue);
            if((Boolean) newValue) {
                Intent intent = new Intent(requireContext().getApplicationContext(), NotificationService.class);
                requireContext().startService(intent);
            } else {
                Intent intent = new Intent(requireContext().getApplicationContext(), NotificationService.class);
                requireContext().stopService(intent);
            }
            return true;
        });
    }

    @Override
    public void setDivider(Drawable divider) {
        super.setDivider(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void setDividerHeight(int height) {
        super.setDividerHeight(20);
    }
}
