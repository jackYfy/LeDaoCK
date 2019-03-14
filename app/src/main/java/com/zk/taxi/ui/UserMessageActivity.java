package com.zk.taxi.ui;

import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;

public class UserMessageActivity extends BaseActivity {


    @Override
    public int bindLayout() {
        return R.layout.activity_user_message;
    }

    @Override
    public void initView(View view) {
          setBackup();
          setTitle("用户消息");
    }
}
