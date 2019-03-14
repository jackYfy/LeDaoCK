package com.zk.taxi.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.Methods;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.chang_phone_nocode)
    TextView chang_phone_nocode;
    @Bind(R.id.chang_phone_code)
    EditText chang_phone_code;
    @Bind(R.id.chang_phone_more)
    TextView chang_phone_more;
    @Override
    public int bindLayout() {
        return R.layout.activity_change_phone;
    }

    @Override
    public void initView(View view) {
         setBackup();
        setTitle("修改绑定手机");
        chang_phone_nocode.setOnClickListener(this);
        chang_phone_more.setOnClickListener(this);
        chang_phone_code.addTextChangedListener(watcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chang_phone_more:

                break;
            case R.id.chang_phone_nocode:
//                Methods.toBase(this,ChangePhoneNumActivity.class);
                break;
        }
    }
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            String  str = s.toString();
            Pattern p = Pattern.compile("(?<![0-9])([0-9]{4,6})(?![0-9])");
//            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(str);
            if(m.find()){
                Methods.toBase(getActivity(),ChangePhoneNumActivity.class);
            }else {
                ToastUtils.show("请输入正确的验证码");
            }

        }
    };
}
