package com.zk.taxi.ui.SettingUI;

import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;

public class MoneyQuanActivity extends BaseActivity {


    @Override
    public int bindLayout() {
        return R.layout.activity_money_quan;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("优惠券");
    }
}
