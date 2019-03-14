package com.zk.taxi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zk.taxi.R;
import com.zk.taxi.entity.EventBean;
import com.zk.taxi.tool.ActivityManager;
import com.zk.taxi.widget.Methods;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.regis_next)
    AppCompatButton regis_next;
    @Bind(R.id.regis_back)
    LinearLayout regis_back;
    @Bind(R.id.regist_input_phone)
    EditText regist_input_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ActivityManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        ininThis();
    }

    private void ininThis() {
        regis_next.setOnClickListener(this);
        regis_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regis_back:
                finish();
                break;
            case R.id.regis_next:
               String phone=regist_input_phone.getText().toString().trim();
                EventBus.getDefault().postSticky(new EventBean(phone,3));
                Methods.toBase(this,VCodeActivity.class);
                break;
        }

    }
}
