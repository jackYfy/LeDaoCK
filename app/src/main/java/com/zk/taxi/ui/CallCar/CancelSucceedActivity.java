package com.zk.taxi.ui.CallCar;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.ActivityManager;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.Methods;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class CancelSucceedActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.cancel_reason)
    AppCompatButton cancel_reason;
    @Bind(R.id.cb_terms1)
    CheckBox cb_terms1;
    @Bind(R.id.cb_terms2)
    CheckBox cb_terms2;
    @Bind(R.id.cb_terms3)
    CheckBox cb_terms3;
    @Bind(R.id.cb_terms4)
    CheckBox cb_terms4;
    @Bind(R.id.cb_terms5)
    CheckBox cb_terms5;
    @Bind(R.id.cb_terms6)
    CheckBox cb_terms6;
    @Bind(R.id.cb_terms7)
    CheckBox cb_terms7;

    private String guid = null;// 订单唯一标识

    @Override
    public int bindLayout() {
        return R.layout.activity_cancel_succeed;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("取消成功");
        ActivityManager.getAppManager().finishActivity(WaitReplyActivity.class);
        ActivityManager.getAppManager().finishActivity(WaitDriverActivity.class);
        cancel_reason.setOnClickListener(this);
        cb_terms1.setOnCheckedChangeListener(this);
        cb_terms2.setOnCheckedChangeListener(this);
        cb_terms3.setOnCheckedChangeListener(this);
        cb_terms4.setOnCheckedChangeListener(this);
        cb_terms5.setOnCheckedChangeListener(this);
        cb_terms6.setOnCheckedChangeListener(this);
        cb_terms7.setOnCheckedChangeListener(this);
        SetInit();
    }

    private void SetInit() {
        Intent intent = getIntent();
        if (!intent.hasExtra("GUID")) {
            ToastUtils.show("获取订单信息失败");
            finish();
            return;
        }
        guid=intent.getExtras().getString("GUID");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_reason:
                String textcontent =listToString(text);
                TakeCarDao.getCancelOrderReason(getActivity(),guid,textcontent);
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
    int flag_7 = 0;
    List<String> text=new ArrayList<>();//所选标签集合
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              switch (buttonView.getId()){
                  case R.id.cb_terms1:
                      if(flag_1==0) { //选中添加
                          flag_1=1;
                          text.add(Methods.getCancelReason()[0]);
                      }else if(flag_1==1){//没选中，或取消了，就移除原先添加的
                          flag_1=0;
                          if (text.contains(Methods.getCancelReason()[0])){
                              text.remove(Methods.getCancelReason()[0]);
                          }
                      }
                      break;
                  case R.id.cb_terms2:
                      if(flag_2==0){
                          flag_2=1;
                          text.add(Methods.getCancelReason()[1]);
                      }else if(flag_2==1){
                          flag_2=0;
                          if (text.contains(Methods.getCancelReason()[1])){
                              text.remove(Methods.getCancelReason()[1]);
                          }
                      }
                      break;
                  case R.id.cb_terms3:
                      if(flag_3==0){
                          flag_3=1;
                          text.add(Methods.getCancelReason()[2]);
                      }else if(flag_3==1){
                          flag_3=0;
                          if (text.contains(Methods.getCancelReason()[2])){
                              text.remove(Methods.getCancelReason()[2]);
                          }
                      }
                      break;
                  case R.id.cb_terms4:
                      if(flag_4==0){
                          flag_4=1;
                          text.add(Methods.getCancelReason()[3]);
                      }else if(flag_4==1){
                          flag_4=0;
                          if (text.contains(Methods.getCancelReason()[3])){
                              text.remove(Methods.getCancelReason()[3]);
                          }
                      }
                      break;
                  case R.id.cb_terms5:
                      if(flag_5==0){
                          flag_5=1;
                          text.add(Methods.getCancelReason()[4]);
                      }else if(flag_5==1){
                          flag_5=0;
                          if (text.contains(Methods.getCancelReason()[4])){
                              text.remove(Methods.getCancelReason()[4]);
                          }
                      }
                      break;
                  case R.id.cb_terms6:
                      if(flag_6==0){
                          flag_6=1;
                          text.add(Methods.getCancelReason()[5]);
                      }else if(flag_6==1){
                          flag_6=0;
                          if (text.contains(Methods.getCancelReason()[5])){
                              text.remove(Methods.getCancelReason()[5]);
                          }
                      }
                      break;
                  case R.id.cb_terms7:
                      if(flag_7==0){
                          flag_7=1;
                          text.add(Methods.getCancelReason()[6]);
                      }else if(flag_7==1){
                          flag_7=0;
                          if (text.contains(Methods.getCancelReason()[6])){
                              text.remove(Methods.getCancelReason()[6]);
                          }
                      }
                      break;
              }
    }

    public static String listToString(List<String> list){
        if(list==null){
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        //第一个前面不拼接","
        for(String string :list) {
            if(first) {
                first=false;
            }else{
                result.append(",");
            }
            result.append(string);
        }
        return result.toString();
    }
}
