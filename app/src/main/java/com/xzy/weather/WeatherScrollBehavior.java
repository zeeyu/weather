package com.xzy.weather;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

/**
 * Author:xzy
 * Date:2020/9/9 09:10
 **/
public class WeatherScrollBehavior extends CoordinatorLayout.Behavior {

    private float deltaY = 0;

    WeatherScrollBehavior() {

    }

    WeatherScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if(deltaY == 0){
            deltaY = dependency.getY() - child.getHeight();
        }


        return true;
    }
}
