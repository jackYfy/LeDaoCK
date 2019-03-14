package com.zk.taxi.ui;

import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;

public class ChangePhoneIdActivity extends BaseActivity {


    @Override
    public int bindLayout() {
        return R.layout.activity_change_phone_id;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("修改绑定手机");
    }
}
