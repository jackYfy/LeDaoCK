package com.zk.taxi.ui.SettingUI;

import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;

public class ContactUsActivity extends BaseActivity {


    @Override
    public int bindLayout() {
        return R.layout.activity_contact_us;
    }

    @Override
    public void initView(View view) {
          setTitle("联系我们");
        setBackup();

    }
}
