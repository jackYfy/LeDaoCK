package com.zk.taxi.ui;

import android.view.View;
import android.widget.RelativeLayout;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.ui.SettingUI.MoneyQuanActivity;
import com.zk.taxi.ui.SettingUI.MoneyYueActivity;
import com.zk.taxi.widget.Methods;

import butterknife.Bind;

public class MoneyActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.set_yue)
    RelativeLayout set_yue;
    @Bind(R.id.set_quan)
    RelativeLayout set_quan;

    @Override
    public int bindLayout() {
        return R.layout.activity_money;
    }

    @Override
    public void initView(View view) {
             setBackup();
        setTitle("我的钱包");
        set_yue.setOnClickListener(this);
        set_quan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_yue:
                Methods.toBase(this,MoneyYueActivity.class);
                break;
            case R.id.set_quan:
                Methods.toBase(this,MoneyQuanActivity.class);
                break;
        }
    }
}
