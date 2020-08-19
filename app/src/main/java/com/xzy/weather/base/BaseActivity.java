package com.xzy.weather.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.LocationClient;

/**
 * Author:xzy
 * Date:2020/8/17 14:01
 **/
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public abstract void init();
}
