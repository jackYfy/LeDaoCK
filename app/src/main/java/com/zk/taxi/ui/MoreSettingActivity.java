package com.zk.taxi.ui;

import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.RelativeLayout;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.EventBean;
import com.zk.taxi.entity.UserInfo;
import com.zk.taxi.tool.ActivityManager;
import com.zk.taxi.tool.UserHelper;
import com.zk.taxi.ui.SettingUI.AboutActivity;
import com.zk.taxi.ui.SettingUI.ContactUsActivity;
import com.zk.taxi.ui.SettingUI.SettingAddressActivity;
import com.zk.taxi.ui.SettingUI.SettingFeedActivity;
import com.zk.taxi.ui.SettingUI.ShareActivity;
import com.zk.taxi.widget.Methods;
import com.zk.taxi.widget.VersionCheck;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;

public class MoreSettingActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.user_center_exit)
    AppCompatButton user_center_exit;
    @Bind(R.id.set_password)
    RelativeLayout set_password;
    @Bind(R.id.set_address)
    RelativeLayout set_address;
    @Bind(R.id.set_update)
    RelativeLayout set_update;
    @Bind(R.id.set_help)
    RelativeLayout set_help;
    @Bind(R.id.set_feedback)
    RelativeLayout set_feedback;
    @Bind(R.id.set_contact)
    RelativeLayout set_contact;
    @Bind(R.id.set_about)
    RelativeLayout set_about;
    @Bind(R.id.set_share)
    RelativeLayout set_share;


    @Override
    public int bindLayout() {
        return R.layout.activity_more_setting;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("设置");
        user_center_exit.setOnClickListener(this);
        set_password.setOnClickListener(this);
        set_address.setOnClickListener(this);
        set_update.setOnClickListener(this);
        set_help.setOnClickListener(this);
        set_feedback.setOnClickListener(this);
        set_contact.setOnClickListener(this);
        set_about.setOnClickListener(this);
        set_share.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_center_exit:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UserHelper.deleteUserInfo();// 删除本地登录用户信息
                        Methods.toLogin(MoreSettingActivity.this);
                        ActivityManager.getAppManager().finishOther(MoreSettingActivity.this);
                        finish();
                    }
                }, 1000);
                break;
            case R.id.set_password:
                UserInfo user = UserHelper.getUserInfo();
                String phone=user.getMobile();
                if(phone!=null) {
                    EventBus.getDefault().postSticky(new EventBean(phone,4));
                    Methods.toBase(this, VCodeActivity.class);
                }
                break;
            case R.id.set_address:
                 Methods.toBase(this, SettingAddressActivity.class);
                break;
            case R.id.set_update:
                new VersionCheck(getActivity(),false);
                break;
            case R.id.set_help:

                break;
            case R.id.set_feedback:
                Methods.toBase(this, SettingFeedActivity.class);
                break;
            case R.id.set_contact:
                Methods.toBase(this, ContactUsActivity.class);
                break;
            case R.id.set_about:
                Methods.toBase(this, AboutActivity.class);
                break;
            case R.id.set_share:
                Methods.toBase(this, ShareActivity.class);
                break;
        }
    }
}
