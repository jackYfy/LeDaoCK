package com.zk.taxi.ui.SettingUI;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class CustomerAllOrderActivity extends BaseActivity {

    private BaseRecyclerAdapter<TripHistoryEntity> mAdapter;
    @Bind(R.id.recyclerview_all_order)
    RecyclerView listView;
    @Bind(R.id.all_order_srl)
    SmartRefreshLayout refreshLayout ;

    private int pageIndex = 1;
    private List<TripHistoryEntity> list = new ArrayList<>();
    @Override
    public int bindLayout() {
        return R.layout.activity_customer_all_order;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("全部订单");
        TakeCarDao.getTripHistory(getActivity(),pageIndex,mhandler);
        setAdpter();
    }
    private Handler mhandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TakeCarDao.TRIPHISTORY:
                    List<TripHistoryEntity> tripEntity=(List<TripHistoryEntity>) msg.obj;
                    if(tripEntity!=null&&tripEntity.size()>0){
                        list.addAll(tripEntity);
                        if(pageIndex==1){
                            mAdapter.refresh(list);//刷新
                        }else if(pageIndex!=1){
                            mAdapter.loadMore(list);//加载更多
                        }
                    }
                    break;
            }
        }
    };
    private void setAdpter() {
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listView.setAdapter(mAdapter=new BaseRecyclerAdapter<TripHistoryEntity>(list,R.layout.order_his_item) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, TripHistoryEntity model, int position) {
                holder.text(R.id.type, model.getTaxiType());
                holder.text(R.id.order_time, DateUtils.getTimeByUT(DateUtils.YMDHM,model.getTaxiTime()));
                AppCompatTextView startaddress2=holder.getView(R.id.startaddress);
                AppCompatTextView endaddress2=holder.getView(R.id.endaddress);
                AppCompatTextView wait_pay2=holder.getView(R.id.wait_pay);
                AppCompatTextView completed2=holder.getView(R.id.completed);
                AppCompatTextView canceled2=holder.getView(R.id.canceled);
                AppCompatTextView wait_pingjia2=holder.getView(R.id.forpingjia);
                startaddress2.setText(model.getTaxiAddress());
                endaddress2.setText(model.getTaxiDestination());
                wait_pay2.setVisibility(View.GONE);
                completed2.setVisibility(View.VISIBLE);
                if(model.getPayStatus()==0){
                    wait_pay2.setVisibility(View.VISIBLE);
                    completed2.setVisibility(View.GONE);
                    canceled2.setVisibility(View.GONE);
                }else {
                    wait_pay2.setVisibility(View.GONE);
                    completed2.setVisibility(View.VISIBLE);
                    canceled2.setVisibility(View.GONE);
                }
                if (model.getStatus()!=null) {
                    if ((model.getStatus()).equals("1")||(model.getStatus()).equals("2")||(model.getStatus()).equals("3")) {
                        wait_pay2.setVisibility(View.GONE);
                        completed2.setVisibility(View.GONE);
                        canceled2.setVisibility(View.VISIBLE);
                    }
                }
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
//                TakeCarDao.getTripHistory(getActivity(),pageIndex,mhandler);
                refreshLayout.resetNoMoreData();//恢复上拉状态
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageIndex++;
                TakeCarDao.getTripHistory(getActivity(),pageIndex,mhandler);
                refreshlayout.finishLoadmore();
            }
        });
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CustomerAllOrderActivity.this, CustomerServiceActivity.class);
                intent.putExtra(TripHistoryEntity.TAG, (Serializable)mAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }
}
