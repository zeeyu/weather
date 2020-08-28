package com.xzy.weather.base;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Author:xzy
 * Date:2020/8/27 19:13
 **/
public class BaseFragment extends Fragment {

    private Activity activity;

    @Nullable
    @Override
    public Context getContext() {
        if(activity == null){
            return MyApplication.getContext();
        }
        return activity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }
}
