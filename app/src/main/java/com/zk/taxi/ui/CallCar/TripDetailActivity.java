package com.zk.taxi.ui.CallCar;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.Config;
import com.zk.taxi.R;
import com.zk.taxi.entity.OrderInfo;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.DateUtils;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.ColorDialog;

import butterknife.Bind;

public class TripDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.this_order_number)
    AppCompatTextView this_order_number;
    @Bind(R.id.this_order_time)
    AppCompatTextView this_order_time;
    @Bind(R.id.this_order_drivername)
    AppCompatTextView this_order_drivername;
    @Bind(R.id.this_order_carnumber)
    AppCompatTextView this_order_carnumber;
    @Bind(R.id.this_order_phonenumber)
    LinearLayout this_order_phonenumber;
    @Bind(R.id.this_order_staddress)
    AppCompatTextView this_order_staddress;
    @Bind(R.id.this_order_edaddress)
    AppCompatTextView this_order_edaddress;
    @Bind(R.id.this_order_cancelorder)
    AppCompatTextView this_order_cancelorder;

    private int typeByCar;// 出租车类型
    private String startAddress = null;// 开始地址
    private String endAddress = null;// 结束地址
    private OrderInfo orderInfo = null;// 订单
    private TripHistoryEntity stateEntity = null;// 订单状态对应的数据
    private String phone_number=null;
    private String guid = null;// 订单唯一标识
    @Override
    public int bindLayout() {
        return R.layout.activity_trip_detail;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("更多");
        setOnMenuClickListener(menuItemClickListener);
        Intent intent = getIntent();
        if (!intent.hasExtra(OrderInfo.TAG)&&!intent.hasExtra("state")) {
            ToastUtils.show("初始化失败");
            finish();
            return;
        }
        if(intent.hasExtra("state")){
            stateEntity=(TripHistoryEntity) intent.getSerializableExtra("state");//订单信息
            typeByCar =stateEntity.getTaxiTypeST();//车类型
            startAddress = stateEntity.getTaxiAddress();//起点地址
            endAddress=stateEntity.getTaxiDestination();//终点地址
            guid=stateEntity.getGuid();
            this_order_number.setText(String.valueOf(stateEntity.getOrderId()));
            this_order_time.setText(DateUtils.getTimeByUT(DateUtils.YMDHM,stateEntity.getTaxiTime()));
            this_order_drivername.setText(stateEntity.getDriverName());
            this_order_carnumber.setText(stateEntity.getTaxiCard());
        }else {
            orderInfo = (OrderInfo) intent.getSerializableExtra(OrderInfo.TAG);//订单信息
            typeByCar = intent.getExtras().getInt(Config.KEY_CALL_TYPE);//车类型
            startAddress = intent.getStringExtra(Config.ST_ADDRESS);//起点地址
            endAddress = intent.getStringExtra(Config.ED_ADDRESS);//终点地址
            guid=orderInfo.getGuid();
            this_order_number.setText(String.valueOf(orderInfo.getOrderId()));
            this_order_time.setText(DateUtils.getTimeByUT(DateUtils.YMDHM,orderInfo.getTaxiTime()));
            this_order_drivername.setText(orderInfo.getDriverName());
            this_order_carnumber.setText(orderInfo.getTaxiCard());
        }
        this_order_staddress.setText(startAddress);
        this_order_edaddress.setText(endAddress);
        this_order_phonenumber.setOnClickListener(this);
        this_order_cancelorder.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.this_order_phonenumber:
                String phonenum=null;
                if(stateEntity!=null){
                    phonenum=stateEntity.getTaxiSim();
                }else{
                    phonenum=orderInfo.getTaxiSim();
                }
                ColorDialog dialog = new ColorDialog(this);
                dialog.setColor("#ffffff");//#8ECB54
                dialog.setAnimationEnable(true);
                dialog.setTitle("");
                dialog.setContentText(phonenum);
                final String finalPhonenum = phonenum;
                dialog.setPositiveListener("取消", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        ToastUtils.show("取消");
                        dialog.dismiss();
                    }
                }).setNegativeListener("呼叫", new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL); // 设置动作
                        Uri data = Uri.parse("tel:" + finalPhonenum); // 设置数据
                        intent.setData(data);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.this_order_cancelorder:
                ColorDialog dialog1 = new ColorDialog(this);
                dialog1.setColor("#ffffff");//#8ECB54
                dialog1.setAnimationEnable(true);
                dialog1.setTitle("");
                dialog1.setContentText("是否取消订单");
                dialog1.setPositiveListener("不取消", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        dialog.dismiss();
                    }
                }).setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        TakeCarDao.getCancelOrder(getActivity(),typeByCar,startAddress,guid);
                        dialog.dismiss();
                    }
                }).show();
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complaint, menu);
        return true;
    }
    Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_complaint:
                    Intent intent = new Intent(TripDetailActivity.this, StrokeComplaintActivity.class);
                    if(stateEntity!=null){
                        intent.putExtra(TripHistoryEntity.TAG,stateEntity);
                    }else{
                        intent.putExtra(OrderInfo.TAG, orderInfo);
                    }
                    intent.putExtra(OrderInfo.TAG, orderInfo);
                    intent.putExtra(Config.ST_ADDRESS, startAddress);
                    intent.putExtra(Config.ED_ADDRESS, endAddress);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };


}
