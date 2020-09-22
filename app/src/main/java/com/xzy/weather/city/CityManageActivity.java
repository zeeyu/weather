package com.xzy.weather.city;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xzy.weather.R;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.bean.MyWeatherNowBean;
import com.xzy.weather.util.DataStoreUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityManageActivity extends BaseActivity {

    private static final String TAG = "CityManageActivity";

    @BindView(R.id.tb_city_manage)
    Toolbar tbNormal;
    @BindView(R.id.tb_city_manage_edit)
    Toolbar tbEdit;
    @BindView(R.id.rv_city_manage)
    RecyclerView recyclerView;
    @BindView(R.id.btn_city_manage_add)
    FloatingActionButton fab;
    @BindView(R.id.iv_city_manage_ok)
    ImageView ivOk;
    @BindView(R.id.iv_city_manage_cancel)
    ImageView ivCancel;

    private List<MyLocationBean> locationList;
    volatile private List<MyWeatherBean> weatherList = new ArrayList<>();
    volatile private List<MyWeatherNowBean> weatherNowList = new ArrayList<>();

    CityManageListAdapter adapter;
    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void initData(){
        locationList = DataStoreUtil.getLocationList(this);
        //Log.d(TAG, "initData " + new Gson().toJson(locationList));
        for(MyLocationBean locationBean : locationList){
            weatherList.add(DataStoreUtil.getWeather7d(this, locationBean.getId()).get(0));
            weatherNowList.add(DataStoreUtil.getWeatherNow(this, locationBean.getId()));
        }
    }

    @Override
    protected void initView() {

        Drawable icBack = getResources().getDrawable(R.drawable.ic_back);
        icBack.setBounds(0, 0, 100, 100);
        tbNormal.setNavigationIcon(icBack);
        tbNormal.setNavigationOnClickListener(v -> finish());
        tbNormal.setTitle(getString(R.string.label_city_manage));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemTouchHelper = new ItemTouchHelper(new MyCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter = new CityManageListAdapter(locationList, weatherNowList, weatherList);
        adapter.setOnItemClickListener((viewHolder, position) -> {
            switchToEditMode();
            Log.d(TAG, "onLongClick: " + position);
            if(position != 0){
                itemTouchHelper.startDrag(viewHolder);
            }
        });
        recyclerView.addItemDecoration(new CityManageListDecoration());
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CityManageActivity.this, CityAddActivity.class);
            startActivityForResult(intent, 1);
        });

        ivOk.setOnClickListener(v -> {
            adapter.delete();
            setResult(1);
            switchToNormalMode();
            DataStoreUtil.setLocationList(this, locationList);
        });

        ivCancel.setOnClickListener(v -> {
            adapter.redo();
            switchToNormalMode();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == 1){
                setResult(2);
                finish();
            }
        }
    }

    private void switchToEditMode(){
        fab.hide();
        tbNormal.setVisibility(View.INVISIBLE);
        tbEdit.setVisibility(View.VISIBLE);
    }

    private void switchToNormalMode(){
        fab.show();
        tbEdit.setVisibility(View.GONE);
        tbNormal.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private class MyCallback extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if(fromPosition == 0 || toPosition == 0){
                return false;
            }
            if(fromPosition < toPosition) {
                for(int i = fromPosition; i < toPosition; i++){
                    Collections.swap(locationList, i, i+1);
                    Collections.swap(weatherList, i, i+1);
                    Collections.swap(weatherNowList, i, i+1);
                }
            } else {
                for(int i = fromPosition; i > toPosition; i--){
                    Collections.swap(locationList, i, i-1);
                    Collections.swap(weatherList, i, i-1);
                    Collections.swap(weatherNowList, i, i-1);
                }
            }
            adapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    }
}
