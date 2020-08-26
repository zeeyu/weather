package com.xzy.weather.warning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzy.weather.R;
import com.xzy.weather.base.BaseActivity;
import com.xzy.weather.bean.MyWarningBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WarningActivity extends BaseActivity {

    private static final String TAG = "WarningActivity";

    public List<MyWarningBean> warningBeanList;

    @BindView(R.id.rv_warning)
    RecyclerView rvWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String json = intent.getStringExtra("WarningList");
        warningBeanList = new Gson().fromJson(json, new TypeToken<ArrayList<MyWarningBean>>(){}.getType());
        //Log.d(TAG, json);

        rvWarning.setLayoutManager(new LinearLayoutManager(this));
        rvWarning.setAdapter(new WarningListAdapter(warningBeanList));
    }
}
