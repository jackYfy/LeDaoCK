package com.zk.taxi.ui.SettingUI;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.MyJifenOrderEntity;
import com.zk.taxi.tool.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MyJifenOrderActivity extends BaseActivity {


    @Bind(R.id.my_order_recyclerView)
    RecyclerView mRecyclerView;

    private List<MyJifenOrderEntity> mDatas = new ArrayList<>();
    private CommonAdapter<MyJifenOrderEntity> mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    @Override
    public int bindLayout() {
        return R.layout.activity_my_jifen_order;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("我的订单");
        initDatas();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommonAdapter<MyJifenOrderEntity>(this, R.layout.my_jifen_order_item, mDatas)
        {
            @Override
            protected void convert(ViewHolder holder, MyJifenOrderEntity myEntity, int position) {
                holder.setText(R.id.order_number,myEntity.getOrderNumber());
                holder.setText(R.id.car_type,myEntity.getCarType());
                holder.setText(R.id.thing_count, String.valueOf(myEntity.getThingCount()));
                holder.setText(R.id.jifen_count, String.valueOf(myEntity.getJifenCount()));
            }

        };
//        mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
//        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                ToastUtils.show("pos = "+ position);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                return false;
            }
        });
    }

    private void initDatas() {
        mDatas.add(new MyJifenOrderEntity("00001","7.8折网约车券",1,50));
        mDatas.add(new MyJifenOrderEntity("00002","7.8折网约车券",1,50));
        mDatas.add(new MyJifenOrderEntity("00003","2元出租车券",1,50));
    }
}
