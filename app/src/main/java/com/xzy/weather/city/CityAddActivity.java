package com.xzy.weather.city;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.xzy.weather.R;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.component.SearchEditText;
import com.xzy.weather.util.DataStoreUtil;
import com.xzy.weather.util.HeWeatherUtil;
import com.xzy.weather.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityAddActivity extends BaseActivity {

    private static final String TAG = "CityAddActivity";

    public static final int BUTTON_WIDTH = 100;

    private static final int MAX_CITY_NUM = 10;

    @BindView(R.id.ed_city_add_search)
    SearchEditText edSearch;
    @BindView(R.id.tb_city_add)
    Toolbar toolbar;
    @BindView(R.id.btn_city_add_cancel)
    Button btnCancel;

    @BindView(R.id.ll_city_add)
    LinearLayout ll;
    @BindView(R.id.ll_city_add_search)
    LinearLayout llSearch;
    @BindView(R.id.fl_city_add_body)
    FrameLayout frame;

    TopViewHolder topViewHolder;
    HistoryViewHolder historyViewHolder;
    SearchListViewHolder searchListViewHolder;
    SearchFailedViewHolder searchFailedViewHolder;

    CitySearchListAdapter searchAdapter;

    LinearLayout.LayoutParams params1;
    LinearLayout.LayoutParams params2;

    private LocationClient mLocationClient;
    MyLocationBean local = new MyLocationBean();
    List<MyLocationBean> topCityList = new ArrayList<>();
    List<MyLocationBean> searchCityList= new ArrayList<>();
    List<MyLocationBean> historyCityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_add);
        ButterKnife.bind(this);
        inflateViews();
        initData();
    }

    protected void inflateViews(){
        LayoutInflater inflater = LayoutInflater.from(this);
        topViewHolder = new TopViewHolder(inflater.inflate(R.layout.include_city_add_top, frame, false));
        historyViewHolder = new HistoryViewHolder(inflater.inflate(R.layout.include_city_add_history, frame, false));
        searchListViewHolder = new SearchListViewHolder(inflater.inflate(R.layout.include_city_add_search_list, frame, false));
        searchFailedViewHolder = new SearchFailedViewHolder(inflater.inflate(R.layout.include_city_add_search_failed, frame, false));
    }

    @Override
    protected void initData() {
        getHeTopCity();

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        requestLocation();

        updateHistoryList();
        initView();
    }

    @Override
    protected void initView() {
        ll.setOnClickListener(v -> SystemUtil.closeSoftInput(getApplicationContext(), v));
        changeView(topViewHolder.llTop);

        params1 = (LinearLayout.LayoutParams) edSearch.getLayoutParams();
        params2 = (LinearLayout.LayoutParams) btnCancel.getLayoutParams();

        Drawable icBack = getResources().getDrawable(R.drawable.ic_back);
        icBack.setBounds(0, 0, 30, 30);
        toolbar.setNavigationIcon(icBack);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(getString(R.string.label_city_add));

        searchAdapter = new CitySearchListAdapter(searchCityList);
        searchListViewHolder.rvSearch.setAdapter(searchAdapter);
        searchAdapter.setOnItemClickListener(position -> updateCityList(searchCityList.get(position)));
        searchListViewHolder.rvSearch.setLayoutManager(new LinearLayoutManager(this));

        edSearch.setOnTextChangedListener(() -> {
            if(edSearch.getText() == null) {
                return;
            }
            String location = edSearch.getText().toString();
            if(edSearch.length() == 0){
                changeView(historyViewHolder.llHistory);
                return;
            }
            getHeSearchCity(location);
        });

        edSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                params1.width = llSearch.getWidth() - BUTTON_WIDTH;
                params2.width = BUTTON_WIDTH;
                edSearch.setLayoutParams(params1);
                btnCancel.setLayoutParams(params2);
                changeView(historyViewHolder.llHistory);
            }
        });

        btnCancel.setOnClickListener(v -> {
            params1.width = llSearch.getWidth();
            params2.width = 0;
            edSearch.setLayoutParams(params1);
            edSearch.setText("");
            edSearch.clearFocus();
            btnCancel.setLayoutParams(params2);
            changeView(topViewHolder.llTop);
            SystemUtil.closeSoftInput(getApplicationContext(), v);
        });
    }

    protected void changeView(View view) {
        //Log.d(TAG, "changeView: " + view + " " + view.getParent());
        frame.removeAllViews();
        frame.addView(view);
        //Log.d(TAG, "changeView: " + view + " " + view.getParent());
    }

    protected void updateTopList() {
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 3);
        topViewHolder.rvTop.setLayoutManager(manager);
        CityTopListAdapter adapter = new CityTopListAdapter(topCityList);
        adapter.setOnItemClickListener(position -> updateCityList(topCityList.get(position)));
        topViewHolder.rvTop.setAdapter(adapter);
        topViewHolder.rvTop.post(() -> topViewHolder.rvTop.addItemDecoration(new CityTopListDecoration(topViewHolder.rvTop.getWidth(), topViewHolder.rvTop.getChildAt(0).getWidth())));
    }

    protected void updateHistoryList() {
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 5);
        historyCityList = DataStoreUtil.getCityHistory(getApplicationContext());
        CityHistoryListAdapter adapter = new CityHistoryListAdapter(historyCityList);
        adapter.setOnItemClickListener(position -> updateCityList(historyCityList.get(position)));
        historyViewHolder.rvHistory.setLayoutManager(manager);
        historyViewHolder.rvHistory.setAdapter(adapter);
        historyViewHolder.tvClear.setOnClickListener(v -> {
            DataStoreUtil.setCityHistory(getApplicationContext(), null);
            historyCityList = new ArrayList<>();
            adapter.notifyDataSetChanged();
            historyViewHolder.rvHistory.invalidate();
        });
    }

    protected void updatePopup() {
        Log.d(TAG, "updatePopup: " + new Gson().toJson(searchCityList));
        searchAdapter.onDataChanged(searchCityList);
        if(searchCityList.size() == 0){
            changeView(searchFailedViewHolder.llSearchFailed);
        } else {
            changeView(searchListViewHolder.llSearchList);
        }
    }

    public void updateCityList(MyLocationBean newCity) {
        List<MyLocationBean> cityList = DataStoreUtil.getLocationList(getApplicationContext());

        if(cityList == null) {
            cityList = new ArrayList<>();
        }

        if(cityList.size() >= MAX_CITY_NUM) {
            Toast.makeText(this, getString(R.string.city_max), Toast.LENGTH_SHORT).show();
            return;
        }

        for(MyLocationBean city : cityList) {
            if(city.getId().equals(newCity.getId())) {
                Toast.makeText(this, getString(R.string.city_added), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        cityList.add(newCity);
        DataStoreUtil.setLocationList(getApplicationContext(), cityList);

        for(MyLocationBean location : historyCityList) {
            if(newCity.getId().equals(location.getId())) {
                historyCityList.remove(location);
                break;
            }
        }
        historyCityList.add(newCity);
        DataStoreUtil.setCityHistory(getApplicationContext(), historyCityList);

        setResult(1);
        finish();
    }

    public void getHeTopCity(){
        HeWeatherUtil.getGeoTopCity(getApplicationContext(), topCityList, this::updateTopList);
    }

    synchronized public void getHeSearchCity(String location) {
        if(location == null || "".equals(location)){
            return;
        }
        HeWeatherUtil.getGeoCityLookup(getApplicationContext(), location, searchCityList, this::updatePopup);
    }

    public void getHeLocation(String location) {
        HeWeatherUtil.getGeoCityLookup(getApplicationContext(), location, local, () -> {
            if(local == null) {
                topViewHolder.tvLocal.setText(getString(R.string.get_location_failed));
            } else {
                topViewHolder.tvLocal.setText(String.format(getString(R.string.location), local.getCity(), local.getName()));
                topViewHolder.tvLocal.setOnClickListener(v -> updateCityList(local));
            }
        });
    }

    private void requestLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 接收位置信息
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            getHeLocation(bdLocation.getLongitude() + "," + bdLocation.getLatitude());
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        //llSearch.requestFocus();
        super.onConfigurationChanged(newConfig);
    }

    static class TopViewHolder {
        @BindView(R.id.ll_city_add_top)
        LinearLayout llTop;
        @BindView(R.id.rv_city_add_top)
        RecyclerView rvTop;
        @BindView(R.id.tv_city_add_local)
        TextView tvLocal;

        TopViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    static class HistoryViewHolder {
        @BindView(R.id.ll_city_add_history)
        LinearLayout llHistory;
        @BindView(R.id.rv_city_add_history)
        RecyclerView rvHistory;
        @BindView(R.id.tv_city_history_clear)
        TextView tvClear;

        HistoryViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    static class SearchListViewHolder {
        @BindView(R.id.ll_city_add_search_list)
        LinearLayout llSearchList;
        @BindView(R.id.rv_city_add_search)
        RecyclerView rvSearch;

        SearchListViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    static class SearchFailedViewHolder {
        @BindView(R.id.ll_city_add_search_failed)
        LinearLayout llSearchFailed;

        SearchFailedViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
