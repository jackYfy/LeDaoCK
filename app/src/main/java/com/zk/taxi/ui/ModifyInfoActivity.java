package com.zk.taxi.ui;

import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.methodDao.UserPost;
import com.zk.taxi.tool.ToastUtils;

import butterknife.Bind;

public class ModifyInfoActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.modify_nickName) EditText modify_nickName;
    @Bind(R.id.modify_sex) EditText modify_sex;
    @Bind(R.id.modify_truename) EditText modify_truename;
    @Bind(R.id.modify_lastname) EditText modify_lastname;
    @Bind(R.id.modify_submit) AppCompatButton modify_submit;
    @Override
    public int bindLayout() {
        return R.layout.activity_modify_info;
    }

    @Override
    public void initView(View view) {
       setBackup();
        setTitle("修改信息");
        modify_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modify_submit:
                String nickname=modify_nickName.getText().toString().trim();
                String sex=modify_sex.getText().toString().trim();
                String truename=modify_truename.getText().toString().trim();
                String lastname=modify_lastname.getText().toString().trim();
              if(nickname.isEmpty()&&sex.isEmpty()&&truename.isEmpty()&&lastname.isEmpty()){
                  ToastUtils.show("不能全部为空");
              }else{
                  if(sex.equals("男")){
                     sex="0";
                  }else if(sex.equals("女")){
                      sex="1";
                  }
                  UserPost.getUpdateUserInfo(getActivity(),nickname,sex,truename,lastname);
              }
                break;
        }
    }
}
