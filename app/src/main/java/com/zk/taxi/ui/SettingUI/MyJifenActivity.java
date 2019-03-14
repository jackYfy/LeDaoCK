package com.zk.taxi.ui.SettingUI;

import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.MyJifenEntity;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.Methods;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MyJifenActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.my_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.my_order)
    AppCompatTextView my_order;

    private List<MyJifenEntity> mDatas = new ArrayList<>();
    private CommonAdapter<MyJifenEntity> mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private EmptyWrapper mEmptyWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;
    @Override
    public int bindLayout() {
        return R.layout.activity_my_jifen;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("我的积分");
        my_order.setOnClickListener(this);
        initDatas();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new CommonAdapter<MyJifenEntity>(this, R.layout.my_jifen_item, mDatas)
        {
            @Override
            protected void convert(ViewHolder holder, MyJifenEntity myJifenEntity, int position) {
                holder.setText(R.id.order_name,myJifenEntity.getOrderName());
                holder.setText(R.id.chang_jifen,myJifenEntity.getMyJifen());
                holder.setText(R.id.jifen_time,myJifenEntity.getTime());
            }

        };
        mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener()
        {
            @Override
            public void onLoadMoreRequested()
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (int i = 0; i < 10; i++)
                        {
                            mDatas.add(new MyJifenEntity("成功支付订单","+"+(3+i),"2017-12-21 15:30"));
                        }
                        mLoadMoreWrapper.notifyDataSetChanged();

                    }
                }, 1000);
            }
        });

        mRecyclerView.setAdapter(mLoadMoreWrapper);
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
        mDatas.add(new MyJifenEntity("商城订单：00001","-50","2017-12-21 15:30"));
        mDatas.add(new MyJifenEntity("成功支付订单","+3","2017-12-21 15:30"));
        mDatas.add(new MyJifenEntity("成功支付订单","+3","2017-12-21 15:30"));
        mDatas.add(new MyJifenEntity("成功支付订单","+3","2017-12-21 15:30"));
        mDatas.add(new MyJifenEntity("成功支付订单","+3","2017-12-21 15:30"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_order:
                Methods.toBase(this,MyJifenOrderActivity.class);

                break;
        }
    }
}
