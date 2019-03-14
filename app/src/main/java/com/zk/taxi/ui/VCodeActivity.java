package com.zk.taxi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zk.taxi.R;
import com.zk.taxi.entity.EventBean;
import com.zk.taxi.methodDao.UserPost;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.LoadingDialog;
import com.zk.taxi.widget.Methods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
/*
* 忘记密码和修改密码页面
* */
public class VCodeActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.submit_again)
    TextView submit_again;
    @Bind(R.id.no_vcode)
    TextView no_vcode;
    @Bind(R.id.in_back)
    LinearLayout in_back;
    @Bind(R.id.code_phone)
    AppCompatTextView code_phone;
    @Bind(R.id.input_code)
    EditText input_code;
    @Bind(R.id.code_next)
    AppCompatButton code_next;

    private String phoneNumber=null;
    private int type=0;
    private String edtext=null;
    private static LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcode);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initThis();

    }

    private void initThis() {
        submit_again.setOnClickListener(this);
        in_back.setOnClickListener(this);
        code_next.setOnClickListener(this);
        if(phoneNumber!=null){
            if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
                ToastUtils.show("您输入的手机号有误");
                return;
            }
            if(type==2||type==4){
                UserPost.getSendPhoneCode(this,phoneNumber);
            }else {
                UserPost.getRegistVCode(this,phoneNumber);
            }
        }
    }
    /**
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setOnEvent(EventBean event) {
        phoneNumber=event.getMsg();
        type=event.getType();
        code_phone.setText("验证码已发送至手机"+phoneNumber);
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_again:
                if(type==2||type==4){
                    UserPost.getSendPhoneCode(this,phoneNumber);
                }else {
                    UserPost.getRegistVCode(this,phoneNumber);
                }
                break;
            case R.id.in_back:
                finish();
                break;
            case R.id.code_next:
                edtext=input_code.getText().toString().trim();
                if (edtext!=null||!edtext.isEmpty()) {
                    EventBus.getDefault().postSticky(new EventBean(phoneNumber, type, edtext));
                    Methods.toBase(this, SettingPwdActivity.class);
                }else {
                    ToastUtils.show("请输入验证码");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
