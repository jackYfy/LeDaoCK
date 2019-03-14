package com.zk.taxi.ui.SettingUI;

import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;

public class ShareActivity extends BaseActivity {


    @Override
    public int bindLayout() {
        return R.layout.activity_share;
    }

    @Override
    public void initView(View view) {
      setBackup();
        setTitle("分享");
    }
}
