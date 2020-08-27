package com.xzy.weather.weather;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.xzy.weather.WeatherFragment;

import java.util.List;

/**
 * Author:xzy
 * Date:2020/8/27 19:15
 **/
public class WeatherFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<WeatherFragment> list;

    public WeatherFragmentPagerAdapter(@NonNull FragmentManager fm, List<WeatherFragment> list) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
