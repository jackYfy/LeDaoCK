package com.zk.taxi.ui.CallCar;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.Config;
import com.zk.taxi.R;
import com.zk.taxi.entity.OrderInfo;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.DateUtils;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.Methods;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class StrokeComplaintActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.stroke_time)
    AppCompatTextView stroke_time;
    @Bind(R.id.stroke_startaddress)
    AppCompatTextView stroke_startaddress;
    @Bind(R.id.stroke_endaddress)
    AppCompatTextView stroke_endaddress;
    @Bind(R.id.ct1)
    CheckBox ct1;
    @Bind(R.id.ct2)
    CheckBox ct2;
    @Bind(R.id.ct3)
    CheckBox ct3;
    @Bind(R.id.ct4)
    CheckBox ct4;
    @Bind(R.id.ct5)
    CheckBox ct5;
    @Bind(R.id.ct6)
    CheckBox ct6;
    @Bind(R.id.stroke_content)
    EditText stroke_content;
    @Bind(R.id.stroke_submit)
    AppCompatButton stroke_submit;

    private OrderInfo orderInfo = null;//正在进行订单信息
    private TripHistoryEntity entity = null;//已完成订单信息
    private String guid = null;// 订单唯一标识
    private String carnumber=null;//车牌号
    private String startAddress = null;// 开始地址
    private String endAddress = null;// 结束地址
    @Override
    public int bindLayout() {
        return R.layout.activity_stroke_complaint;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("行程投诉");
        Intent intent = getIntent();
        if (!intent.hasExtra(OrderInfo.TAG)&&!intent.hasExtra(TripHistoryEntity.TAG)) {
            ToastUtils.show("初始化失败");
            finish();
            return;
        }
        if (intent.hasExtra(TripHistoryEntity.TAG)){
            entity= (TripHistoryEntity) intent.getSerializableExtra(TripHistoryEntity.TAG);
            Log.d("POST", "entity:"+entity);
            stroke_time.setText(DateUtils.getTimeByUT(DateUtils.YMDHM,entity.getCreateTime()));
            stroke_startaddress.setText(entity.getTaxiAddress());
            stroke_endaddress.setText(entity.getTaxiDestination());
            guid=entity.getGuid();
            carnumber=entity.getTaxiCard();
        }else {
            orderInfo = (OrderInfo) intent.getSerializableExtra(OrderInfo.TAG);//订单信息
            startAddress = intent.getStringExtra(Config.ST_ADDRESS);//起点地址
            endAddress=intent.getStringExtra(Config.ED_ADDRESS);//终点地址
            Log.d("POST", "orderInfo:"+orderInfo);
            stroke_time.setText(orderInfo.getTaxiTime());
            stroke_startaddress.setText(startAddress);
            stroke_endaddress.setText(endAddress);
            guid=orderInfo.getGuid();
            carnumber=orderInfo.getTaxiCard();
        }
        stroke_submit.setOnClickListener(this);
        ct1.setOnCheckedChangeListener(this);
        ct2.setOnCheckedChangeListener(this);
        ct3.setOnCheckedChangeListener(this);
        ct4.setOnCheckedChangeListener(this);
        ct5.setOnCheckedChangeListener(this);
        ct6.setOnCheckedChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stroke_submit:
                String textet=stroke_content.getText().toString().trim();
                String contenttype =CancelSucceedActivity.listToString(ckcontent);
                Log.d("POST", "textet:"+textet+","+"contenttype:"+contenttype);
                if (!textet.isEmpty()&&!contenttype.isEmpty()) {
                    TakeCarDao.getStrokeComplaint(getActivity(),contenttype,textet,carnumber,guid);
                }else{
                    ToastUtils.show("请选择投诉类型和内容");
                }
                break;
        }
    }
    //0表示没有选择，1表示已经选择
    int flag_1 = 0;
    int flag_2 = 0;
    int flag_3 = 0;
    int flag_4 = 0;
    int flag_5 = 0;
    int flag_6 = 0;
    List<String> ckcontent=new ArrayList<>();//所选标签集合
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.ct1:
                if(flag_1==0) { //选中添加
                    flag_1=1;
                    ckcontent.add(Methods.getStrokeType()[0]);
                }else if(flag_1==1){//没选中，或取消了，就移除原先添加的
                    flag_1=0;
                    if (ckcontent.contains(Methods.getStrokeType()[0])){
                        ckcontent.remove(Methods.getStrokeType()[0]);
                    }
                }
                break;
            case R.id.ct2:
                if(flag_2==0){
                    flag_2=1;
                    ckcontent.add(Methods.getStrokeType()[1]);
                }else if(flag_2==1){
                    flag_2=0;
                    if (ckcontent.contains(Methods.getStrokeType()[1])){
                        ckcontent.remove(Methods.getStrokeType()[1]);
                    }
                }
                break;
            case R.id.ct3:
                if(flag_3==0){
                    flag_3=1;
                    ckcontent.add(Methods.getStrokeType()[2]);
                }else if(flag_3==1){
                    flag_3=0;
                    if (ckcontent.contains(Methods.getStrokeType()[2])){
                        ckcontent.remove(Methods.getStrokeType()[2]);
                    }
                }
                break;
            case R.id.ct4:
                if(flag_4==0){
                    flag_4=1;
                    ckcontent.add(Methods.getStrokeType()[3]);
                }else if(flag_4==1){
                    flag_4=0;
                    if (ckcontent.contains(Methods.getStrokeType()[3])){
                        ckcontent.remove(Methods.getStrokeType()[3]);
                    }
                }
                break;
            case R.id.ct5:
                if(flag_5==0){
                    flag_5=1;
                    ckcontent.add(Methods.getStrokeType()[4]);
                }else if(flag_5==1){
                    flag_5=0;
                    if (ckcontent.contains(Methods.getStrokeType()[4])){
                        ckcontent.remove(Methods.getStrokeType()[4]);
                    }
                }
                break;
            case R.id.ct6:
                if(flag_6==0){
                    flag_6=1;
                    ckcontent.add(Methods.getStrokeType()[5]);
                }else if(flag_6==1){
                    flag_6=0;
                    if (ckcontent.contains(Methods.getStrokeType()[5])){
                        ckcontent.remove(Methods.getStrokeType()[5]);
                    }
                }
                break;
        }
    }
}
