package com.zk.taxi.ui.SettingUI;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.DateUtils;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.ColorDialog;

import butterknife.Bind;

public class ThingLostActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.thing_lost_cartype) AppCompatTextView thing_lost_cartype;
    @Bind(R.id.thing_lost_time) AppCompatTextView thing_lost_time;
    @Bind(R.id.thing_lost_stadress) AppCompatTextView thing_lost_stadress;
    @Bind(R.id.thing_lost_edadress) AppCompatTextView thing_lost_edadress;
    @Bind(R.id.thing_lost_phone) AppCompatImageView thing_lost_phone;
    @Bind(R.id.thing_lost_etinput) EditText thing_lost_etinput;
    @Bind(R.id.thing_lost_submit) AppCompatButton thing_lost_submit;

    private TripHistoryEntity entity = null;
    @Override
    public int bindLayout() {
        return R.layout.activity_thing_lost;
    }

    @Override
    public void initView(View view) {
          setBackup();
        setTitle("物品遗失");
        thing_lost_phone.setOnClickListener(this);
        thing_lost_submit.setOnClickListener(this);
        Intent intent = getIntent();
        if (!intent.hasExtra(TripHistoryEntity.TAG)) {
            ToastUtils.show("初始化失败");
            finish();
            return;
        }
        entity = (TripHistoryEntity) intent.getSerializableExtra(TripHistoryEntity.TAG);//订单信息
        thing_lost_cartype.setText(entity.getTaxiType());
        thing_lost_time.setText(DateUtils.getTimeByUT(DateUtils.YMDHM,entity.getTaxiTime()));
        thing_lost_stadress.setText(entity.getTaxiAddress());
        thing_lost_edadress.setText(entity.getTaxiDestination());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.thing_lost_phone:
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
            case R.id.thing_lost_submit:
                String content=thing_lost_etinput.getText().toString().trim();
                String date=DateUtils.getTimeByUT(DateUtils.YMDHM,entity.getTaxiTime());
                if (content.isEmpty()){
                    ToastUtils.show("请先填写遗失物品");
                }
                TakeCarDao.getLostThing(getActivity(),date,content,entity.getTaxiCard(),entity.getGuid());
                break;
        }

    }
}
