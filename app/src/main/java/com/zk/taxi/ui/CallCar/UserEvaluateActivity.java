package com.zk.taxi.ui.CallCar;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.OrderInfo;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.view.CircleImageView;
import com.zk.taxi.view.RatingBar;
import com.zk.taxi.widget.Methods;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class UserEvaluateActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.evaluate_ratingbar) com.zk.taxi.view.RatingBar evaluate_ratingbar;
    @Bind(R.id.go_evaluate)  AppCompatButton go_evaluate;
    @Bind(R.id.evaluate_driver_name) AppCompatTextView evaluate_driver_name;
    @Bind(R.id.evaluate_driver_img) CircleImageView evaluate_driver_img;
    @Bind(R.id.evaluate_taxi_num) AppCompatTextView evaluate_taxi_num;
    @Bind(R.id.evaluate_ratingbar_info) AppCompatTextView evaluate_ratingbar_info;
    @Bind(R.id.evaluate_text) EditText evaluate_text;
    @Bind(R.id.evaluate_label_1) AppCompatTextView evaluate_label_1;
    @Bind(R.id.evaluate_label_2) AppCompatTextView evaluate_label_2;
    @Bind(R.id.evaluate_label_3) AppCompatTextView evaluate_label_3;
    @Bind(R.id.evaluate_label_4) AppCompatTextView evaluate_label_4;
    @Bind(R.id.evaluate_label_5) AppCompatTextView evaluate_label_5;

    private OrderInfo orderInfo = null;// 订单
    private TripHistoryEntity tripEntity = null;// 订单
    private String guid = null;// 订单唯一标识
    private int RatingNum =0;
    @Override
    public int bindLayout() {
        return R.layout.activity_user_evaluate;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("用户评价");
        setOnMenuClickListener(menuClickListener);
        evaluate_label_1.setOnClickListener(this);
        evaluate_label_2.setOnClickListener(this);
        evaluate_label_3.setOnClickListener(this);
        evaluate_label_4.setOnClickListener(this);
        evaluate_label_5.setOnClickListener(this);
        go_evaluate.setOnClickListener(this);
        Intent intent = getIntent();
        if (!intent.hasExtra(OrderInfo.TAG)&&!intent.hasExtra(TripHistoryEntity.TAG)) {
            ToastUtils.show("初始化失败");
            finish();
            return;
        }
        if (intent.hasExtra(TripHistoryEntity.TAG)){
            tripEntity= (TripHistoryEntity) intent.getSerializableExtra(TripHistoryEntity.TAG);
            evaluate_driver_name.setText(tripEntity.getDriverName()+ "师傅");
            evaluate_taxi_num.setText(tripEntity.getTaxiCard());
            guid = tripEntity.getGuid();//订单GUID
        }else {
            orderInfo = (OrderInfo) intent.getSerializableExtra(OrderInfo.TAG);//订单信息
            evaluate_driver_name.setText(orderInfo.getDriverName() + "师傅");
            evaluate_taxi_num.setText(orderInfo.getTaxiCard());
            guid = orderInfo.getGuid();//订单GUID
        }
        evaluate_ratingbar.setOnRatingListener(new RatingBar.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                ToastUtils.show(String.valueOf(RatingScore));
                RatingNum=RatingScore;
                evaluate_ratingbar_info.setText(Methods.getEvaluates()[RatingScore]);
            }
        });
    }
    //0表示没有选择，1表示已经选择
    int flag_1 = 0;
    int flag_2 = 0;
    int flag_3 = 0;
    int flag_4 = 0;
    int flag_5 = 0;
    List<String> text=new ArrayList<>();//所选标签集合
    List<String> textid=new ArrayList<>();//所选标签id集合(1表示第一个标签，以此类推)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_evaluate:
                String content=getText(evaluate_text);
                String textcontent =text.toString();
                String textidnum=textid.toString();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.show("随便说两句咯~");
                    return;
                }
                if (content.length() > 200) {
                    ToastUtils.show("评价内容太长，请整理后再提交");
                    return;
                }
                TakeCarDao.getEvaluation(getActivity(),guid,RatingNum,content,textidnum,textcontent);
                break;
            case R.id.evaluate_label_1:
                if (flag_1==0){
                    evaluate_label_1.setActivated(true);
                    flag_1=1;
                    textid.add("1");
                    text.add("车辆整洁");
                }else if(flag_1==1){
                    evaluate_label_1.setActivated(false);
                    flag_1=0;
                    if (textid.contains("1")&&text.contains("车辆整洁")){
                        textid.remove("1");
                        text.remove("车辆整洁");
                    }
                }
                break;
            case R.id.evaluate_label_2:
                if (flag_2==0){
                    evaluate_label_2.setActivated(true);
                    flag_2=1;
                    textid.add("2");
                    text.add("认路准");
                }else if(flag_2==1){
                    evaluate_label_2.setActivated(false);
                    flag_2=0;
                    if (textid.contains("2")&&text.contains("认路准")){
                        textid.remove("2");
                        text.remove("认路准");
                    }
                }
                break;
            case R.id.evaluate_label_3:
                if (flag_3==0){
                    evaluate_label_3.setActivated(true);
                    flag_3=1;
                    textid.add("3");
                    text.add("驾驶平稳");
                }else if(flag_3==1){
                    evaluate_label_3.setActivated(false);
                    flag_3=0;
                    if (textid.contains("3")&&text.contains("驾驶平稳")){
                        textid.remove("3");
                        text.remove("驾驶平稳");
                    }
                }
                break;
            case R.id.evaluate_label_4:
                if (flag_4==0){
                    evaluate_label_4.setActivated(true);
                    flag_4=1;
                    textid.add("4");
                    text.add("态度好");
                }else if(flag_4==1){
                    evaluate_label_4.setActivated(false);
                    flag_4=0;
                    if (textid.contains("4")&&text.contains("态度好")){
                        textid.remove("4");
                        text.remove("态度好");
                    }
                }
                break;
            case R.id.evaluate_label_5:
                if (flag_5==0){
                    evaluate_label_5.setActivated(true);
                    flag_5=1;
                    textid.add("5");
                    text.add("服务棒");
                }else if(flag_5==1){
                    evaluate_label_5.setActivated(false);
                    flag_5=0;
                    if (textid.contains("5")&&text.contains("服务棒")){
                        textid.remove("5");
                        text.remove("服务棒");
                    }
                }
                break;
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
                    Intent intent = new Intent(UserEvaluateActivity.this, StrokeComplaintActivity.class);
                    if (tripEntity==null){
                        intent.putExtra(OrderInfo.TAG, orderInfo);
                    }else{
                        intent.putExtra(TripHistoryEntity.TAG, tripEntity);
                    }
                    startActivity(intent);

                    break;
            }
            return false;
        }
    };


}
