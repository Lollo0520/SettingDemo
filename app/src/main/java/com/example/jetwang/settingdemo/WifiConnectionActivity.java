package com.example.jetwang.settingdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Switch;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jet Wang on 2017/2/22.
 */
public class WifiConnectionActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.wifi_switch)
    Switch mWifiSwitch;
    @Bind(R.id.wifi_list)
    RecyclerView mWifiList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wifi_connection);
        ButterKnife.bind(this);
    }
}
