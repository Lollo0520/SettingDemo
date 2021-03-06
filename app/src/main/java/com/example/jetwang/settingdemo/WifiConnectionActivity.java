package com.example.jetwang.settingdemo;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jet Wang on 2017/2/22.
 */
public class WifiConnectionActivity extends AppCompatActivity {

    private ArrayList<ScanResult> wifiArray;
    private WiFiInfoAdapter wifiInfoAdapter;
    private ListView listWifi;

    private ProgressBar updateProgress;
    private Button updateButton;
    private String wifiPassword = null;

    private WifiManager wifiManager;
    private WifiAdmin wiFiAdmin;
    private List<ScanResult> list;
    private ScanResult mScanResult;
    private StringBuffer sb = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_wifi_connection);

        wiFiAdmin = new WifiAdmin(this);
        initLayout();

        getAllNetWorkList();
    }

    public void initLayout() {
        listWifi = (ListView) findViewById(R.id.wifi_list);
       /* RelativeLayout btnToSettingFromWiFi = (RelativeLayout) findViewById(R.id.btnToSettingFromWiFi);
        btnToSettingFromWiFi.setOnClickListener(new MyOnClickListener());*/

        // 刷新按钮和进度条
        updateProgress = (ProgressBar) findViewById(R.id.update_progress);
        updateProgress.setVisibility(View.INVISIBLE);
        updateButton = (Button) findViewById(R.id.update_btn);
        updateButton.setVisibility(View.VISIBLE);
        updateButton.setOnClickListener(new MyOnClickListener());

        Switch switchWifi = (Switch) findViewById(R.id.wifi_switch);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        switchWifi.setChecked(wifiManager.isWifiEnabled());
        switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                wifiManager.setWifiEnabled(isChecked);
                // 更新WiFi列表
                if (isChecked) {
                    listWifi.setVisibility(View.VISIBLE);
                    updateProgress.setVisibility(View.VISIBLE);
                    updateButton.setVisibility(View.INVISIBLE);
                    new Thread(new refreshWifiThread()).start();
                } else {
                    listWifi.setVisibility(View.GONE);
                }
            }
        });

    }

    final Handler refreshWifiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getAllNetWorkList();
                    updateProgress.setVisibility(View.INVISIBLE);
                    updateButton.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    };

    public class refreshWifiThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                Message message = new Message();
                message.what = 1;
                refreshWifiHandler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                /*case R.id.btnToSettingFromWiFi:
                    finish();
                    break;*/
                case R.id.update_btn:
                    updateButton.setVisibility(View.INVISIBLE);
                    updateProgress.setVisibility(View.VISIBLE);
                    new Thread(new refreshWifiThread()).start();
                    break;
                default:
                    break;
            }
        }

    }

    public void getAllNetWorkList() {

        wifiArray = new ArrayList<ScanResult>();
        if (sb != null) {
            sb = new StringBuffer();
        }
        wiFiAdmin.startScan();
        list = wiFiAdmin.getWifiList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                mScanResult = list.get(i);
               /* WifiInfo wifiInfo = new WifiInfo(mScanResult.BSSID,
                        mScanResult.SSID, mScanResult.capabilities,
                        mScanResult.level);*/
                wifiArray.add(mScanResult);
            }
//            Log.e("getAllNetWorkList","getAllNetWorkList: ssid: " + wiFiAdmin.getWifiInfo());
            Log.e("getAllNetWorkList","getAllNetWorkList: wifimanager: " + wifiManager.getConnectionInfo().getSSID());
            wifiInfoAdapter = new WiFiInfoAdapter(getApplicationContext(),
                    wifiArray, wifiManager.getConnectionInfo().getSSID());
            listWifi.setAdapter(wifiInfoAdapter);

            //
            wiFiAdmin.getConfiguration();

            listWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                String wifiItemSSID = null;

                public void onItemClick(android.widget.AdapterView<?> parent,
                                        android.view.View view, int position, long id) {

//                    Log.d(Constant.TAG, "BSSID:" + list.get(position).BSSID);

                    // 连接WiFi
                    wifiItemSSID = list.get(position).SSID;
                    int wifiItemId = wiFiAdmin.IsConfiguration("\""
                            + list.get(position).SSID + "\"");
                    if (wifiItemId != -1) {
                        if (wiFiAdmin.ConnectWifi(wifiItemId)) {
                            // 连接已保存密码的WiFi
                            Toast.makeText(getApplicationContext(), "正在连接",
                                    Toast.LENGTH_SHORT).show();
                            updateButton.setVisibility(View.INVISIBLE);
                            updateProgress.setVisibility(View.VISIBLE);
                            new Thread(new refreshWifiThread()).start();
                        }
                    } else {
                        // 没有配置好信息，配置
                        WifiPswDialog pswDialog = new WifiPswDialog(
                                WifiConnectionActivity.this,
                                new WifiPswDialog.OnCustomDialogListener() {
                                    @Override
                                    public void back(String str) {
                                        wifiPassword = str;
                                        if (wifiPassword != null) {
                                            int netId = wiFiAdmin
                                                    .AddWifiConfig(list,
                                                            wifiItemSSID,
                                                            wifiPassword);
                                            if (netId != -1) {
                                                wiFiAdmin.getConfiguration();// 添加了配置信息，要重新得到配置信息
                                                if (wiFiAdmin
                                                        .ConnectWifi(netId)) {
                                                    // 连接成功，刷新UI
                                                    updateProgress
                                                            .setVisibility(View.VISIBLE);
                                                    updateButton
                                                            .setVisibility(View.INVISIBLE);
                                                    new Thread(
                                                            new refreshWifiThread())
                                                            .start();
                                                }
                                            } else {
                                                // 网络连接错误
                                            }
                                        } else {
                                        }
                                    }
                                });
                        pswDialog.show();
                    }
                }
            });
        }
    }
}
