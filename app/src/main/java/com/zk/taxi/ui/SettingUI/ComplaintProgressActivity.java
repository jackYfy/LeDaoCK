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
import com.zk.taxi.entity.ComplaintEntity;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ComplaintProgressActivity extends BaseActivity {

    private BaseRecyclerAdapter<ComplaintEntity> mAdapter;
    @Bind(R.id.complaint_recyclerView)
    RecyclerView listView;
    @Bind(R.id.complaint_srl)
    SmartRefreshLayout refreshLayout ;

    private int pageIndex = 1;
    private List<ComplaintEntity> list = new ArrayList<>();
    @Override
    public int bindLayout() {
        return R.layout.activity_complaint_progress;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("全部投诉");
        TakeCarDao.getComplaintReturn(getActivity(),pageIndex,mhandler);
        setAdpter();
    }



    private Handler mhandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TakeCarDao.COMPLAINT:
                    List<ComplaintEntity> tripEntity=(List<ComplaintEntity>) msg.obj;
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
        listView.setAdapter(mAdapter=new BaseRecyclerAdapter<ComplaintEntity>(list,R.layout.order_his_item) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, ComplaintEntity model, int position) {
                holder.text(R.id.type, model.getTaxiCard());
                holder.text(R.id.order_time, DateUtils.getTimeByUT(DateUtils.YMDHM,model.getRegisterTime()));
                AppCompatTextView startaddress2=holder.getView(R.id.startaddress);
                AppCompatTextView endaddress2=holder.getView(R.id.endaddress);
                AppCompatTextView wait_pay2=holder.getView(R.id.wait_pay);
                AppCompatTextView completed2=holder.getView(R.id.completed);
                AppCompatTextView canceled2=holder.getView(R.id.canceled);
                AppCompatTextView wait_pingjia2=holder.getView(R.id.forpingjia);
                startaddress2.setText(model.getTaxiAddress());
                endaddress2.setText(model.getTaxiDestination());
                wait_pay2.setVisibility(View.GONE);
                completed2.setVisibility(View.GONE);
                wait_pingjia2.setVisibility(View.GONE);
                canceled2.setVisibility(View.GONE);
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
                TakeCarDao.getComplaintReturn(getActivity(),pageIndex,mhandler);
                    refreshlayout.finishLoadmore();
            }
        });
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ComplaintProgressActivity.this, ComplaintProOneActivity.class);
                intent.putExtra(ComplaintEntity.TAG, (Serializable)mAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
