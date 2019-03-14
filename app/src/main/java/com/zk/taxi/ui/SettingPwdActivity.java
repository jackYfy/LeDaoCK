package com.zk.taxi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zk.taxi.R;
import com.zk.taxi.entity.EventBean;
import com.zk.taxi.methodDao.UserPost;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingPwdActivity extends AppCompatActivity implements View.OnClickListener {
@Bind(R.id.et_passwd)
    EditText et_passwd;
    @Bind(R.id.iv_showPassword)
    ImageView iv_showPassword;
    @Bind(R.id.set_back)
    LinearLayout set_back;
    @Bind(R.id.set_password_next)
    AppCompatButton set_password_next;
    private Boolean showPassword = true;

    private String phone=null;
    private String code=null;
    private int type=0;
    private String newpassword=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pwd);
        ButterKnife.bind(this);
        initn();
        EventBus.getDefault().register(this);
    }

    private void initn() {
        iv_showPassword.setImageDrawable(getResources().getDrawable(R.mipmap.no_eye));
        iv_showPassword.setOnClickListener(this);
        set_back.setOnClickListener(this);
        set_password_next.setOnClickListener(this);
    }
    /**
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setOnEvent(EventBean event) {
        phone=event.getMsg();
        type=event.getType();
        code=event.getCode();
    };
    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.iv_showPassword:
            if (showPassword) {// 显示密码
                iv_showPassword.setImageDrawable(getResources().getDrawable(R.mipmap.yes_eye));
                et_passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                et_passwd.setSelection(et_passwd.getText().toString().length());
                showPassword = !showPassword;
            } else {// 隐藏密码
                iv_showPassword.setImageDrawable(getResources().getDrawable(R.mipmap.no_eye));
                et_passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                et_passwd.setSelection(et_passwd.getText().toString().length());
                showPassword = !showPassword;
            }
            break;
        case R.id.set_back:
            finish();
            break;
        case R.id.set_password_next:
            newpassword=et_passwd.getText().toString().trim();
            //1:EventBus传递账号到密码页登录，2:忘记密码，3:注册，4:修改密码
            if(type==2){
                UserPost.Modifypossword(this,phone,newpassword,code);
            }else if(type==3){
                UserPost.Regist(this,phone,code,newpassword);
            }else if(type==4){
                UserPost.Modifypossword(this,phone,newpassword,code);
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
