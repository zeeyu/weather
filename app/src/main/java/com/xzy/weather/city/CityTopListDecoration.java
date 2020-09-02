package com.xzy.weather.city;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.util.DensityUtil;

/**
 * Author:xzy
 * Date:2020/9/1 10:48
 **/
public class CityTopListDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "CityTopListDecoration";

    private int mItemWidth;
    private int mViewWidth;

    public CityTopListDecoration(int viewWidth, int itemWidth){
        mViewWidth = viewWidth;
        mItemWidth = itemWidth;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state){
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildLayoutPosition(view);

        outRect.bottom = 20;

        if(position % 3 != 0){
            outRect.left = DensityUtil.px2dip(parent.getContext().getApplicationContext(), (mViewWidth - mItemWidth * 3) / 2.0f);
            //Log.d(TAG, "getItemOffsets: " + outRect.left + " " + mViewWidth + " " + mItemWidth);
        }
    }
}
