package com.zk.taxi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zk.taxi.R;
import com.zk.taxi.entity.EventBean;
import com.zk.taxi.entity.UserInfo;
import com.zk.taxi.methodDao.UserPost;
import com.zk.taxi.tool.ActivityManager;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.tool.UserHelper;
import com.zk.taxi.widget.Methods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.passwordbig)
    TextInputLayout passwordbig;
    @Bind(R.id.password)
    EditText pwd;//密码
    @Bind(R.id.login)
    AppCompatButton login;//登录
    @Bind(R.id.login_forget_pass)
    TextView login_forget_pass;//忘记密码
    @Bind(R.id.pw_back)
    LinearLayout pw_back;

    private String phoneNum=null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);
        ButterKnife.bind(this);
        ActivityManager.getAppManager().addActivity(this);
        inati();
        EventBus.getDefault().register(this);
    }



    private void inati() {
        passwordbig.setPasswordVisibilityToggleEnabled(true);
        passwordbig.setPasswordVisibilityToggleDrawable(R.drawable.password_visible_invisible);
        login.setOnClickListener(this);
        login_forget_pass.setOnClickListener(this);
        pw_back.setOnClickListener(this);

    }
    /**
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setOnEvent(EventBean event) {
        phoneNum=event.getMsg();
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_forget_pass:
                EventBus.getDefault().postSticky(new EventBean(phoneNum,2));
                Methods.toBase(this,VCodeActivity.class);
                break;
            case R.id.login:
                getLogin();
                break;
            case R.id.pw_back:
                finish();
                break;
        }

    }
    public void getLogin() {
        String password = pwd.getText().toString();
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(password)) {
            ToastUtils.show("请输入正确的用户名及密码");
            return;
        }
        UserPost.isLogin(this,handler,phoneNum,password);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case UserPost.LOGIN:
                    List<UserInfo> list= (List<UserInfo>) msg.obj;
                    if(list!=null&&list.size()>0) {
                            UserHelper.saveUserInfo(list.get(0));
                        if(list.get(0).getHead_pic()!=null){
                            UserHelper.saveHeadImgURL(list.get(0).getHead_pic());
                        }
                        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginPasswordActivity.this, MainActivity.class);
                            startActivity(intent);
                        ActivityManager.getAppManager().finishActivity(LoginPasswordActivity.this);
                        ActivityManager.getAppManager().finishActivity(LoginAccountActivity.class);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
