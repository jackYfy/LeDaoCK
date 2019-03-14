package com.zk.taxi.ui.SettingUI;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.tool.DateUtils;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.ui.CallCar.UserEvaluateActivity;
import com.zk.taxi.ui.CallCar.UserEvaluateResultActivity;
import com.zk.taxi.widget.ColorDialog;

import java.math.BigDecimal;

import butterknife.Bind;

public class MyScheduleDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.schedule_order_number) AppCompatTextView schedule_order_number;
    @Bind(R.id.schedule_order_date) AppCompatTextView schedule_order_date;
    @Bind(R.id.schedule_driver_name) AppCompatTextView schedule_driver_name;
    @Bind(R.id.schedule_car_num) AppCompatTextView schedule_car_num;
    @Bind(R.id.schedule_start_address) AppCompatTextView schedule_start_address;
    @Bind(R.id.schedule_end_address) AppCompatTextView schedule_end_address;
    @Bind(R.id.schedule_pay_way) AppCompatTextView schedule_pay_way;
    @Bind(R.id.schedule_pay_money) AppCompatTextView schedule_pay_money;
    @Bind(R.id.schedule_dophone) LinearLayout schedule_dophone;
    @Bind(R.id.schedule_evaluate) RelativeLayout schedule_evaluate;
    private TripHistoryEntity entity = null;
    @Override
    public int bindLayout() {
        return R.layout.activity_my_schedule_detail;
    }

    @Override
    public void initView(View view) {
         setBackup();
        setTitle("行程详情");
        Intent intent = getIntent();
        if (!intent.hasExtra(TripHistoryEntity.TAG)) {
            ToastUtils.show("初始化失败");
            finish();
            return;
        }
        schedule_dophone.setOnClickListener(this);
        schedule_evaluate.setOnClickListener(this);
        entity = (TripHistoryEntity) intent.getSerializableExtra(TripHistoryEntity.TAG);//订单信息
        schedule_order_date.setText(DateUtils.getTimeByUT(DateUtils.YMDHM,entity.getCreateTime()));
        schedule_driver_name.setText(entity.getDriverName()+ "师傅");
        schedule_order_number.setText(entity.getOrderId());
        schedule_pay_way.setText(entity.getPayment());
        BigDecimal b=new  BigDecimal(entity.getPayAmount());
        double money=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        schedule_pay_money.setText(String.valueOf(money));
        schedule_car_num.setText(entity.getTaxiCard());
        schedule_start_address.setText(entity.getTaxiAddress());
        schedule_end_address.setText(entity.getTaxiDestination());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.schedule_dophone:
                ColorDialog dialog = new ColorDialog(this);
                dialog.setColor("#ffffff");//#8ECB54
                dialog.setAnimationEnable(true);
                dialog.setTitle("");
                dialog.setContentText(entity.getTaxiSim());
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
                        Uri data = Uri.parse("tel:" + entity.getTaxiSim()); // 设置数据
                        intent.setData(data);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.schedule_evaluate:
                if (entity.getEvaluationcontent()!="") {
                    Intent intent = new Intent(MyScheduleDetailActivity.this, UserEvaluateResultActivity.class);
                    intent.putExtra(TripHistoryEntity.TAG, entity);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MyScheduleDetailActivity.this, UserEvaluateActivity.class);
                    intent.putExtra(TripHistoryEntity.TAG, entity);
                    startActivity(intent);
                }
                break;
        }

    }
}
