package com.xzy.weather.warning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

    private List<MyWarningBean> warningBeanList;

    @BindView(R.id.rv_warning)
    RecyclerView rvWarning;
    @BindView(R.id.tb_warning)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String json = intent.getStringExtra("WarningList");
        warningBeanList = new Gson().fromJson(json, new TypeToken<ArrayList<MyWarningBean>>(){}.getType());
        initView();
    }

    @Override
    protected void initView() {
        rvWarning.setLayoutManager(new LinearLayoutManager(this));
        rvWarning.setAdapter(new WarningListAdapter(warningBeanList));
        rvWarning.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(20,20,20,20);
            }
        });

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(getString(R.string.label_warning));
    }
}
