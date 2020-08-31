package com.xzy.weather.weather;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author:xzy
 * Date:2020/8/27 19:15
 **/
public class WeatherFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<WeatherFragment> list;

    public WeatherFragmentPagerAdapter(@NonNull FragmentManager fm, List<WeatherFragment> list) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
