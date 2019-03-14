package com.zk.taxi.ui;

import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.widget.Methods;

import butterknife.Bind;

public class ChangePhoneNumActivity extends BaseActivity implements View.OnClickListener {

@Bind(R.id.chang_phone_submitnext) AppCompatButton chang_phone_submitnext;
    @Override
    public int bindLayout() {
        return R.layout.activity_change_phone_num;
    }

    @Override
    public void initView(View view) {
           setBackup();
           setTitle("修改绑定手机");
        chang_phone_submitnext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chang_phone_submitnext:
                Methods.toBase(this,ChangePhoneIdActivity.class);
                break;
        }
    }
}
