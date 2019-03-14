package com.zk.taxi.ui.SettingUI;

import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;

public class MoneyYueActivity extends BaseActivity {


    @Override
    public int bindLayout() {
        return R.layout.activity_money_yue;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("余额");
    }
}
