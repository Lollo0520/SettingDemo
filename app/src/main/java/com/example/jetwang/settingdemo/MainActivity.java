package com.example.jetwang.settingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wifi_connection);
//        ButterKnife.bind(this);

    }

    /*@OnClick({R.id.wifi_indicator, R.id.mini_u_indicator, R.id.volume_sb, R.id.brightness_sb, R.id.brightness_auto_cb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wifi_indicator:
                Intent intent = new Intent(this, WifiConnectionActivity.class);
                startActivity(intent);
                break;
            case R.id.mini_u_indicator:
                break;
            case R.id.volume_sb:
                break;
            case R.id.brightness_sb:
                break;
            case R.id.brightness_auto_cb:
                break;
        }
    }*/
}
