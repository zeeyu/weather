package com.xzy.weather.city;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.xzy.weather.R;
import com.xzy.weather.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityAddActivity extends BaseActivity {

    @BindView(R.id.ed_city_add_search)
    SearchEditText edSearch;
    @BindView(R.id.tb_city_add)
    Toolbar toolbar;
    @BindView(R.id.btn_city_add_cancel)
    Button btnCancel;
    @BindView(R.id.ll_city_add_search)
    LinearLayout linearLayout;

    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) edSearch.getLayoutParams();
    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) btnCancel.getLayoutParams();

    public static int BUTTON_WIDTH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_add);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {

        initView();
    }

    @Override
    protected void initView() {

        Drawable icBack = getResources().getDrawable(R.drawable.ic_back);
        icBack.setBounds(0, 0, 30, 30);
        toolbar.setNavigationIcon(icBack);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(getString(R.string.label_city_add));

        edSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    params1.width = linearLayout.getWidth() - BUTTON_WIDTH;
                    params2.width = BUTTON_WIDTH;
                    edSearch.setLayoutParams(params1);
                    btnCancel.setLayoutParams(params2);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
