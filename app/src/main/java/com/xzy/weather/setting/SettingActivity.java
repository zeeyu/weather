package com.xzy.weather.setting;

import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.xzy.weather.GlobalData;
import com.xzy.weather.R;
import com.xzy.weather.SwitchBarItem;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.bean.SettingBean;
import com.xzy.weather.util.DataStoreUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    private static final String TAG = "SettingActivity";

    public static final String UNIT_CHANGE_ACTION = "com.xzy.weather.setting.UNIT_CHANGE_ACTION";

    @BindView(R.id.tb_setting)
    Toolbar toolbar;

    //private static SettingBean setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new SettingFragment()).commit();

        initData();
        initView();
    }

    @Override
    protected void initData() {
        //setting = DataStoreUtil.getSettingInfo(getApplicationContext());
    }

    @Override
    protected void initView() {
        toolbar.setTitle(R.string.label_setting);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    public static class SettingFragment extends PreferenceFragmentCompat {

        private SettingListPreference settingUnitPreference;
        private SettingListPreference settingUpdatePreference;

        LocalBroadcastManager localBroadcastManager;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.setting);

            settingUnitPreference = findPreference("setting_temp_unit");
            settingUpdatePreference = findPreference("setting_auto_update");

            localBroadcastManager = LocalBroadcastManager.getInstance(requireContext());

            settingUnitPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                //Log.d(TAG, "onPreferenceChange" + newValue);
                SettingBean setting = GlobalData.getInstance().getSetting();
                setting.setTempUnit(newValue.toString());
                localBroadcastManager.sendBroadcast(new Intent(UNIT_CHANGE_ACTION));
                return true;
            });

            settingUpdatePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                //Log.d(TAG, "onPreferenceChange: " + newValue);
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
}
