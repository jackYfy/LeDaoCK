package com.zk.taxi.ui.SettingUI;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zk.taxi.BaseActivity;
import com.zk.taxi.R;
import com.zk.taxi.entity.FeedBackEntity;
import com.zk.taxi.methodDao.UserPost;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SettingFeedActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.linearlayout)
    LinearLayout linearlayout;
    @Bind(R.id.feedback_content)
    EditText feedback_content;
    @Bind(R.id.feedback_submit)
    AppCompatButton feedback_submit;
    @Bind(R.id.feedlistview)
    RecyclerView recyclerview_mid;

    private CommonAdapter<FeedBackEntity> mAdapter;
    private List<FeedBackEntity> back_list = new ArrayList<>();
    @Override
    public int bindLayout() {
        return R.layout.activity_setting_feed;
    }

    @Override
    public void initView(View view) {
              setBackup();
              setTitle("意见反馈");
        feedback_submit.setOnClickListener(this);
        tabLayout.addTab(tabLayout.newTab().setText("发表反馈"));
        tabLayout.addTab(tabLayout.newTab().setText("我的反馈"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中了tab的逻辑
                if(tab.getPosition()==0){
                    linearlayout.setVisibility(View.VISIBLE);
                    recyclerview_mid.setVisibility(View.GONE);
                }else if(tab.getPosition()==1){
                    linearlayout.setVisibility(View.GONE);
                    recyclerview_mid.setVisibility(View.VISIBLE);
                  UserPost.getFeedBack(getActivity(),mHandler);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选中tab的逻辑
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
               //再次选中tab的逻辑

            }
        });
    }

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UserPost.FEEDBACK:
                    List<FeedBackEntity> feedEntity=(List<FeedBackEntity>) msg.obj;
                    if(feedEntity!=null&&feedEntity.size()>0){
                        back_list.clear();
                        back_list.addAll(feedEntity);
                        setAdapter();
                    }
                    break;
            }
        }
    };

    private void setAdapter() {
        recyclerview_mid.setHasFixedSize(true);
        recyclerview_mid.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_mid.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerview_mid.setAdapter(new CommonAdapter<FeedBackEntity>(this, R.layout.feedback_item, back_list){

            @Override
            protected void convert(ViewHolder holder, FeedBackEntity feedBackEntity, int position) {
                AppCompatTextView content=holder.getView(R.id.feed_content);
                AppCompatTextView time=holder.getView(R.id.feed_time);
                content.setText(feedBackEntity.getContent());
                time.setText(feedBackEntity.getCreatTime());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.feedback_submit:
                 String content=feedback_content.getText().toString().trim();
                UserPost.getFeedBackAdd(getActivity(),content);
                break;
        }
    }
}
