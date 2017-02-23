package com.example.jetwang.settingdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Jet Wang on 2017/2/23.
 */

public class WifiPswDialog extends Dialog {
    private Button cancelButton;
    private Button okButton;
    private EditText pswEdit;
    private OnCustomDialogListener customDialogListener;

    public WifiPswDialog(Context context, OnCustomDialogListener customListener) {
        super(context);
        customDialogListener = customListener;

    }

    /**
     * 定义dialog的回调事件
     */
    public interface OnCustomDialogListener {
        void back(String str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_config_dialog);
        setTitle("输入密码");
        pswEdit = (EditText) findViewById(R.id.password_et);
        cancelButton = (Button) findViewById(R.id.cancel_btn);
        okButton = (Button) findViewById(R.id.confirm_btn);
        cancelButton.setOnClickListener(buttonDialogListener);
        okButton.setOnClickListener(buttonDialogListener);

    }

    private View.OnClickListener buttonDialogListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cancel_btn) {
                pswEdit = null;
                customDialogListener.back(null);
                cancel();// 自动调用dismiss();
            } else {
                customDialogListener.back(pswEdit.getText().toString());
                dismiss();
            }
        }
    };

}
