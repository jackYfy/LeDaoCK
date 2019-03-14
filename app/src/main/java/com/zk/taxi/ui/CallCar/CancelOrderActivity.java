package com.zk.taxi.ui.CallCar;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.Config;
import com.zk.taxi.R;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.ToastUtils;

import butterknife.Bind;

public class CancelOrderActivity extends BaseActivity implements View.OnClickListener {

@Bind(R.id.not_cancel)
    AppCompatButton not_cancel;
    @Bind(R.id.yes_cancel)
    AppCompatButton yes_cancel;
    private int typeByCar ;// 出租车类型
    private String guid = null;// 订单唯一标识
    private String startAddress = null;// 开始地址
    @Override
    public int bindLayout() {
        return R.layout.activity_cancel_order;
    }

    @Override
    public void initView(View view) {
            setTitle("取消订单");
            setBackup();
        initac();

    }

    private void initac() {
        Intent intent = getIntent();
        if (!intent.hasExtra(Config.KEY_CALL_TYPE) || !intent.hasExtra(Config.KEY_CALL_GUID)
                    || !intent.hasExtra(Config.ST_ADDRESS)){
            ToastUtils.show("传入数据为空");
            finish();
            return;
        }
        typeByCar = intent.getExtras().getInt(Config.KEY_CALL_TYPE);
        guid = intent.getStringExtra(Config.KEY_CALL_GUID);
        startAddress=intent.getStringExtra(Config.ST_ADDRESS);
        not_cancel.setOnClickListener(this);
        yes_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.not_cancel:
                 finish();
                break;
            case R.id.yes_cancel:
                TakeCarDao.getCancelOrder(getActivity(),typeByCar,startAddress,guid);

                break;
        }
    }
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return false;
//    }
}
