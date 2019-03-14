package com.zk.taxi.ui;

import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.methodDao.UserPost;

import butterknife.Bind;

public class InfoRealNameActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.real_submit)
    AppCompatButton real_submit;
    @Bind(R.id.real_name)
    EditText real_name;
    @Bind(R.id.real_cardnum)
    EditText real_cardnum;

    @Override
    public int bindLayout() {
        return R.layout.activity_info_real_name;
    }

    @Override
    public void initView(View view) {
       setBackup();
        setTitle("实名认证");
        real_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.real_submit:
                 String name=real_name.getText().toString().trim();
                String cardnum=real_cardnum.getText().toString().trim();
                UserPost.getInfoUpdate(this,name,cardnum);
                break;
        }
    }

}
