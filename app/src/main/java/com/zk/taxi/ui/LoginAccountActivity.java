package com.zk.taxi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zk.taxi.Config;
import com.zk.taxi.R;
import com.zk.taxi.entity.EventBean;
import com.zk.taxi.tool.ActivityManager;
import com.zk.taxi.tool.MD5;
import com.zk.taxi.tool.SPUtils;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.Methods;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.zk.taxi.R.id.login_forget_pass;

public class LoginAccountActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.cb_terms)
    CheckBox CheckboxTerms;
    @Bind(R.id.user)
    EditText user;//用户名
    @Bind(R.id.next)
    AppCompatButton next;//下一步
    @Bind(R.id.zhuce)
    TextView zhuce;//注册
    @Bind(R.id.text_terms)
    TextView text_terms;//服务
    @Bind(R.id.ac_back)
    LinearLayout ac_back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);
        ActivityManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        String username = (String) SPUtils.get(Config.SP_ACCOUNT, "");
        user.setText(MD5.convertMD5(username));
        next.setOnClickListener(this);
        zhuce.setOnClickListener(this);
        text_terms.setOnClickListener(this);
        ac_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_terms:

                break;
            case R.id.zhuce:
               Methods.toBase(this,RegistActivity.class);
                break;
            case login_forget_pass:

                break;
            case R.id.next:
                if(!CheckboxTerms.isChecked()){
                    ToastUtils.show("请阅读并同意服务条款");
                    return;
                }
                String name = user.getText().toString();
/*
* EventBus传值给下个界面
* 如有A，B, 两个Activity , App当前处在 A Activity 现在要向B传递一个值并且启动B Activity，
* 正常情况下在A 是发送 EventBus.getDefault().post(new Event());
* 但是有一个问题，此时B还没有启动， B也没办法成功接收这个事件。
* 所以这时需要用postSticky 粘性事件发布；
* 在B界面也需要粘性接受或注册（@Subscribe(sticky = true, threadMode = ThreadMode.MAIN) ／／这种写法达到粘性的目的
*          或 以Sticky的形式注册EventBus.getDefault().registerSticky(this);）
*
*  Event是类似广播的通信，哪个页面接收数据，哪个页面就要在oncreat()里注册和ondestory()里销毁
*  一般是更新或传值给当前页面和已打开的的页面
* */
                EventBus.getDefault().postSticky(new EventBean(name,1));
                Methods.toBase(this,LoginPasswordActivity.class);
                break;
            case R.id.ac_back:
                Methods.toMain(this);
                break;
        }
    }


}
