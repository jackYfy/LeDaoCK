package com.zk.taxi.ui;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.adpter.JifenAdapterForRv;
import com.zk.taxi.entity.JifenEntity;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.ui.SettingUI.MyJifenActivity;
import com.zk.taxi.widget.Methods;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class JifenMallActivity extends BaseActivity implements View.OnClickListener {

@Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.my_jifen) LinearLayout my_jifen;

    private LoadMoreWrapper mLoadMoreWrapper;
    private List<JifenEntity> mDatas = new ArrayList<>();
    @Override
    public int bindLayout() {
        return R.layout.activity_jifen_mall;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("积分商城");
        my_jifen.setOnClickListener(this);
        initData();
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        JifenAdapterForRv adapter = new JifenAdapterForRv(this, mDatas);
        mLoadMoreWrapper = new LoadMoreWrapper(adapter);
        mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.default_loading, mRecyclerView, false));
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
                        boolean coming = Math.random() > 0.5;
                        JifenEntity msg = null;
                        msg = new JifenEntity(coming ? 2 :0, "200积分", "有效期到2018.3.5",
                                                     coming ? 0 :8.5, coming);
                        mDatas.add(msg);
                        mLoadMoreWrapper.notifyDataSetChanged();

                    }
                }, 1000);
            }
        });
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                ToastUtils.show("Click: "+position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                ToastUtils.show("Click: "+ position);
                return false;
            }

        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_jifen:
                Methods.toBase(this, MyJifenActivity.class);
                break;
        }

    }
    private void initData() {
       mDatas.add(new JifenEntity(1,"100积分","有效期到2018.2.14",0,true));
       mDatas.add(new JifenEntity(2,"500积分","有效期到2018.2.14",0,true));
        mDatas.add(new JifenEntity(0,"500积分","有效期到2018.2.14",7.8,false));
        mDatas.add(new JifenEntity(0,"500积分","有效期到2018.2.14",8.0,false));
    }



}
