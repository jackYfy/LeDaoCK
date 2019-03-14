package com.zk.taxi.ui.SettingUI;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.ComplaintEntity;
import com.zk.taxi.tool.DateUtils;

import butterknife.Bind;

public class ComplaintProOneActivity extends BaseActivity {

    @Bind(R.id.complaint_carnum) AppCompatTextView complaint_carnum;
    @Bind(R.id.complaint_time) AppCompatTextView complaint_time;
    @Bind(R.id.complaint_staddress) AppCompatTextView complaint_staddress;
    @Bind(R.id.complaint_edaddress) AppCompatTextView complaint_edaddress;
    @Bind(R.id.complaint_content) AppCompatTextView complaint_content;
    @Bind(R.id.complaint_type) AppCompatTextView complaint_type;

    private ComplaintEntity entity = null;
    @Override
    public int bindLayout() {
        return R.layout.activity_complaint_pro_one;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("投诉详情");
        Intent intent = getIntent();
        if (intent.hasExtra(ComplaintEntity.TAG)){
            entity = (ComplaintEntity) intent.getSerializableExtra(ComplaintEntity.TAG);//订单信息
            complaint_carnum.setText(entity.getTaxiCard());
            complaint_time.setText(DateUtils.getTimeByUT(DateUtils.YMDHM,entity.getRegisterTime()));
            complaint_staddress.setText(entity.getTaxiAddress());
            complaint_edaddress.setText(entity.getTaxiDestination());
            complaint_content.setText(entity.getComplaint());
            complaint_type.setText(entity.getComplaintType());
        }
    }

}
