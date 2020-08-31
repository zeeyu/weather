package com.xzy.weather.weather;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xzy.weather.util.DensityUtil;

/**
 * Author:xzy
 * Date:2020/8/24 15:54
 **/
public class GridListDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "GridListDecoration";

    private int mItemWidth;

    private int mViewWidth;

    public GridListDecoration(int viewWidth, int itemWidth){
        mViewWidth = viewWidth;
        mItemWidth = itemWidth;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state){
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildLayoutPosition(view);

        outRect.bottom = 20;

        if(position % 2 == 1){
            outRect.left = DensityUtil.px2dip(view.getContext(), mViewWidth - mItemWidth * 2);
            //Log.d(TAG, "getItemOffsets: " + outRect.left + " " + mViewWidth + " " + mItemWidth);
        }
    }
}
