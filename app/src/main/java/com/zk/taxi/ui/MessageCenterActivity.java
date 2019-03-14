package com.zk.taxi.ui;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.ui.CallCar.UserEvaluateActivity;
import com.zk.taxi.widget.Methods;

import butterknife.Bind;

public class MessageCenterActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.recent_message)
    RelativeLayout recent_message;
    @Bind(R.id.advcation)
    AppCompatTextView advcation;

    @Override
    public int bindLayout() {
        return R.layout.activity_message_center;
    }

    @Override
    public void initView(View view) {
          setBackup();
          setTitle("消息中心");
        recent_message.setOnClickListener(this);
        advcation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recent_message:
                Methods.toBase(this,UserMessageActivity.class);
                break;
            case R.id.advcation:
                Methods.toBase(this,UserEvaluateActivity.class);
                break;
        }
    }
}
