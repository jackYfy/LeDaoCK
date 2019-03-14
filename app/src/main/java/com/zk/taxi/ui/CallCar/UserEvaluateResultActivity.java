package com.zk.taxi.ui.CallCar;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.view.CircleImageView;
import com.zk.taxi.widget.Methods;

import butterknife.Bind;

public class UserEvaluateResultActivity extends BaseActivity {

    @Bind(R.id.ratingbar) com.zk.taxi.view.RatingBar ratingbar;
    @Bind(R.id.evaluatedetail_driver_name) AppCompatTextView evaluatedetail_driver_name;
    @Bind(R.id.evaluatedetail_driver_img) CircleImageView evaluatedetail_driver_img;
    @Bind(R.id.evaluatedetail_car_num) AppCompatTextView evaluatedetail_car_num;
    @Bind(R.id.evaluatedetail_star_content) AppCompatTextView evaluatedetail_star_content;
    @Bind(R.id.evaluatedetail_content) AppCompatTextView evaluatedetail_content;
    @Bind(R.id.evaluatedetail_label_1) AppCompatTextView label_1;
    @Bind(R.id.evaluatedetail_label_2) AppCompatTextView label_2;
    @Bind(R.id.evaluatedetail_label_3) AppCompatTextView label_3;
    @Bind(R.id.evaluatedetail_label_4) AppCompatTextView label_4;
    @Bind(R.id.evaluatedetail_label_5) AppCompatTextView label_5;
    private TripHistoryEntity entity = null;

    @Override
    public int bindLayout() {
        return R.layout.activity_user_evaluate_result;
    }

    @Override
    public void initView(View view) {
         setBackup();
        setTitle("用户评价");
        ratingbar.setClickable(false);
        setOnMenuClickListener(menuClickListener);
        Intent intent = getIntent();
        if (!intent.hasExtra(TripHistoryEntity.TAG)) {
            ToastUtils.show("初始化失败");
            finish();
            return;
        }
        entity = (TripHistoryEntity) intent.getSerializableExtra(TripHistoryEntity.TAG);//订单信息
        evaluatedetail_driver_name.setText(entity.getDriverName()+"师傅");
        evaluatedetail_car_num.setText(entity.getTaxiCard());
        evaluatedetail_content.setText(entity.getEvaluationcontent());
        evaluatedetail_star_content.setText(Methods.getEvaluates()[Integer.parseInt(entity.getEvaluationlevel())]);
        ratingbar.setStar(Integer.parseInt(entity.getEvaluationlevel()),false);
        String tabId=entity.getTabId();
        tabId=tabId.substring(1,tabId.length()-1);
        if (tabId.contains("1")){
            label_1.setVisibility(View.VISIBLE);
        }
        if(tabId.contains("2")){
            label_2.setVisibility(View.VISIBLE);
        }
        if(tabId.contains("3")){
            label_3.setVisibility(View.VISIBLE);
        }
        if(tabId.contains("4")){
            label_4.setVisibility(View.VISIBLE);
        }
        if(tabId.contains("5")){
            label_5.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complaint, menu);
        return true;
    }
    Toolbar.OnMenuItemClickListener menuClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_complaint:
                    Intent intent = new Intent(UserEvaluateResultActivity.this, StrokeComplaintActivity.class);
                    intent.putExtra(TripHistoryEntity.TAG, entity);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };
}
