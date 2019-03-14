package com.zk.taxi.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.adpter.BaseRecyclerAdapter;
import com.zk.taxi.adpter.SmartViewHolder;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.DateUtils;
import com.zk.taxi.ui.CallCar.TaxiPayActivity;
import com.zk.taxi.ui.SettingUI.MyScheduleDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MyScheduleActivity extends BaseActivity implements View.OnClickListener {

    private List<TripHistoryEntity> waitpay_list = new ArrayList<>();//未支付
    private List<TripHistoryEntity> ispay_list = new ArrayList<>();//已支付
//    private CommonAdapter<TripHistoryEntity> waitpayAdapter;
    private int pageIndex = 1;
    private static boolean isFirstEnter = true;
    private BaseRecyclerAdapter<TripHistoryEntity> mAdapter;
    TripHistoryEntity not_pay=null;
    @Bind(R.id.listView) RecyclerView listView;
    @Bind(R.id.srl) SmartRefreshLayout refreshLayout ;
    @Bind(R.id.recyclerview_mid) RecyclerView recyclerview_mid;

    @Bind(R.id.his_type) AppCompatTextView his_type;//车类型
    @Bind(R.id.his_wait_pay) AppCompatTextView his_wait_pay;//待支付
    @Bind(R.id.his_cancel) AppCompatTextView his_cancel;//订单已取消
    @Bind(R.id.his_forpingjia) AppCompatTextView his_forpingjia;//未评价
    @Bind(R.id.his_order_time) AppCompatTextView his_order_time;//时间
    @Bind(R.id.his_startaddress) AppCompatTextView his_startaddress;//起点
    @Bind(R.id.his_endaddress) AppCompatTextView his_endaddress;//终点
    @Bind(R.id.not_paylinear) LinearLayout not_paylinear;
    @Bind(R.id.not_title) AppCompatTextView not_title;
    @Override
    public int bindLayout() {
        return R.layout.activity_my_schedule;
    }

    @Override
    public void initView(View view) {
         setBackup();
        setTitle("我的行程");
        not_paylinear.setOnClickListener(this);
        getData();
        initIsPayAdapter();
    }

    private void getData() {
        TakeCarDao.getTripHistory(getActivity(),pageIndex,mhandler);
    }
    private Handler mhandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
          case TakeCarDao.TRIPHISTORY:
              List<TripHistoryEntity> tripEntity=(List<TripHistoryEntity>) msg.obj;
              if(tripEntity!=null&&tripEntity.size()>0){
                  for (int i = 0; i <tripEntity.size(); i++) {
                      if (tripEntity.get(i).getPayStatus()==0){
                          waitpay_list.add(tripEntity.get(i));//等待支付
                      }else {
                          ispay_list.add(tripEntity.get(i));//已支付列表
                      }
                  }
                  if(ispay_list!=null&&ispay_list.size()>0){
                      if(pageIndex==1){
                          mAdapter.refresh(ispay_list);//刷新
                      }else if(pageIndex!=1){
                          mAdapter.loadMore(ispay_list);//加载更多
                      }
                  }
                  //未完成的订单
                  if(waitpay_list!=null&&waitpay_list.size()>0) {
                      setWaitAdapter(waitpay_list);
                  }
              }
              break;
      }
  }
};
//    未完成的单
    private void setWaitAdapter(List<TripHistoryEntity> waitpay_list) {
            not_pay = waitpay_list.get(0);
            if (not_pay != null) {
                if(not_pay.getStatus()!=null) {
                    if ((not_pay.getStatus()).equals("1")||(not_pay.getStatus()).equals("2")||(not_pay.getStatus()).equals("3")) {
                        his_cancel.setVisibility(View.VISIBLE);
                        his_wait_pay.setVisibility(View.GONE);
                        his_forpingjia.setVisibility(View.GONE);
                    }else if((not_pay.getStatus()).equals("4")&&not_pay.getPayStatus()==0){
                        his_wait_pay.setVisibility(View.VISIBLE);
                        his_cancel.setVisibility(View.GONE);
                    }
                }
                not_paylinear.setVisibility(View.VISIBLE);
                not_title.setVisibility(View.VISIBLE);
                his_type.setText(not_pay.getTaxiType());
                his_order_time.setText(DateUtils.getTimeByUT(DateUtils.YMDHM, not_pay.getTaxiTime()));
                his_startaddress.setText(not_pay.getTaxiAddress());
                his_endaddress.setText(not_pay.getTaxiDestination());
            } else {
                not_paylinear.setVisibility(View.GONE);
                not_title.setVisibility(View.GONE);
            }
    }

    private void initIsPayAdapter() {
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        //第一次进入演示刷新
//        if (isFirstEnter) {
//            isFirstEnter = false;
//            refreshLayout.autoRefresh();
//        }
        recyclerview_mid.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_mid.setItemAnimator(new DefaultItemAnimator());
        recyclerview_mid.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerview_mid.setAdapter(mAdapter=new BaseRecyclerAdapter<TripHistoryEntity>(ispay_list,R.layout.order_his_item) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, TripHistoryEntity model, int position) {
                holder.text(R.id.type, model.getTaxiType());
                holder.text(R.id.order_time, DateUtils.getTimeByUT(DateUtils.YMDHM,model.getTaxiTime()));
                AppCompatTextView startaddress2=holder.getView(R.id.startaddress);
                AppCompatTextView endaddress2=holder.getView(R.id.endaddress);
                AppCompatTextView wait_pay2=holder.getView(R.id.wait_pay);
                AppCompatTextView completed2=holder.getView(R.id.completed);
                AppCompatTextView wait_pingjia2=holder.getView(R.id.forpingjia);
                startaddress2.setText(model.getTaxiAddress());
                endaddress2.setText(model.getTaxiDestination());
                wait_pay2.setVisibility(View.GONE);
                completed2.setVisibility(View.VISIBLE);
                if (model.getEvaluationcontent()==""){
                    wait_pingjia2.setVisibility(View.VISIBLE);
                }else {
                    wait_pingjia2.setVisibility(View.GONE);
                }
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1500);
                pageIndex=1;
//                getData();
                refreshLayout.resetNoMoreData();//恢复上拉状态
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageIndex++;
                getData();
                refreshlayout.finishLoadmore();
            }
        });
     mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MyScheduleActivity.this, MyScheduleDetailActivity.class);
        intent.putExtra(TripHistoryEntity.TAG, (Serializable)mAdapter.getItem(position));
        startActivity(intent);
        }
      });
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.not_paylinear:
                 if((not_pay.getStatus()).equals("4")&&not_pay.getPayStatus()==0){
                     Intent intent = new Intent(act, TaxiPayActivity.class);
                     intent.putExtra("state",not_pay);
                     startActivity(intent);
                     finish();
                 }

                 break;
         }
    }

}
