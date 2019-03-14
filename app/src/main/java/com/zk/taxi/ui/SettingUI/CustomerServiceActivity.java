package com.zk.taxi.ui.SettingUI;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.DateUtils;
import com.zk.taxi.widget.Methods;

import java.util.List;

import butterknife.Bind;

public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.thing_lost) AppCompatTextView thing_lost;
    @Bind(R.id.complaint_progress) AppCompatTextView complaint_progress;
    @Bind(R.id.customer_allorder) AppCompatTextView customer_allorder;
    @Bind(R.id.customer_car_type) AppCompatTextView customer_car_type;
    @Bind(R.id.customer_time) AppCompatTextView customer_time;
    @Bind(R.id.customer_st_address) AppCompatTextView customer_st_address;
    @Bind(R.id.customer_ed_address) AppCompatTextView customer_ed_address;

    private int pageIndex = 1;
    private TripHistoryEntity entity = null;
    @Override
    public int bindLayout() {
        return R.layout.activity_customer_service;
    }

    @Override
    public void initView(View view) {
             setBackup();
        setTitle("客服");
        thing_lost.setOnClickListener(this);
        complaint_progress.setOnClickListener(this);
        customer_allorder.setOnClickListener(this);
        Intent intent = getIntent();
        if (!intent.hasExtra(TripHistoryEntity.TAG)) {
            TakeCarDao.getTripHistory(getActivity(),pageIndex,mhandler);
        }else{
            entity = (TripHistoryEntity) intent.getSerializableExtra(TripHistoryEntity.TAG);//订单信息
            customer_car_type.setText(entity.getTaxiType());
            customer_time.setText(DateUtils.getTimeByUT(DateUtils.YMDHM,entity.getTaxiTime()));
            customer_st_address.setText(entity.getTaxiAddress());
            customer_ed_address.setText(entity.getTaxiDestination());
        }
    }

    private Handler mhandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TakeCarDao.TRIPHISTORY:
                    List<TripHistoryEntity> tripEntity=(List<TripHistoryEntity>) msg.obj;
                    if(tripEntity!=null&&tripEntity.size()>0){
                        entity=tripEntity.get(0);
                        customer_car_type.setText(entity.getTaxiType());
                        customer_time.setText(DateUtils.getTimeByUT(DateUtils.YMDHM,entity.getTaxiTime()));
                        customer_st_address.setText(entity.getTaxiAddress());
                        customer_ed_address.setText(entity.getTaxiDestination());
                    }
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.thing_lost:
                Intent intent = new Intent(CustomerServiceActivity.this, ThingLostActivity.class);
                intent.putExtra(TripHistoryEntity.TAG, entity);
                startActivity(intent);
                break;
            case R.id.complaint_progress:
                Methods.toBase(this,ComplaintProgressActivity.class);
                break;
            case R.id.customer_allorder:
                Methods.toBase(this,CustomerAllOrderActivity.class);
        }
    }
}
