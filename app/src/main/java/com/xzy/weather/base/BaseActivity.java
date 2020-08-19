package com.xzy.weather.base;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Author:xzy
 * Date:2020/8/17 14:01
 **/
public class BaseActivity extends AppCompatActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        init();
    }

    public void init() {

    }
}
