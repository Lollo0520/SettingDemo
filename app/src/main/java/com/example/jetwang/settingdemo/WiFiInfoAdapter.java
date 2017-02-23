package com.example.jetwang.settingdemo;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jet Wang on 2017/2/23.
 */
public class WiFiInfoAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<ScanResult> wifiArray;
    private LayoutInflater mInflater = null;
    private String connectedWiFI;

    public WiFiInfoAdapter(Context context, ArrayList<ScanResult> wifiArray, String ssid) {
        this.mContext = context;
        this.wifiArray = wifiArray;
        mInflater = LayoutInflater.from(context);
        this.connectedWiFI = ssid;
    }

    @Override
    public int getCount() {
        return wifiArray != null ? wifiArray.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return wifiArray != null ? wifiArray.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScanResult scanResult = wifiArray.get(position);
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_wifi_list, null);
            holder.wifiName = (TextView) convertView.findViewById(R.id.wifi_name_tv);
            holder.connectionStatus = (TextView) convertView.findViewById(R.id.connection_state_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.wifiName.setText(scanResult.SSID);
        if (!TextUtils.isEmpty(connectedWiFI)&& TextUtils.equals(connectedWiFI, scanResult.SSID)){
            holder.connectionStatus.setText("已连接");
        } else if(scanResult.capabilities.contains("WEP")||scanResult.capabilities.contains("PSK")||
                scanResult.capabilities.contains("EAP")){
            holder.connectionStatus.setText("加密");
        }else{
            holder.connectionStatus.setText("公开");
        }

        return convertView;
    }

    static class ViewHolder{
        TextView wifiName;
        TextView connectionStatus;
    }
}
