package com.zk.taxi.adpter;

import android.view.View;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zk.taxi.R;
import com.zk.taxi.entity.JifenEntity;
import com.zk.taxi.tool.ToastUtils;

/**
 * Created by zhy on 16/6/22.
 */
public class JinfenCarItemDelagate implements ItemViewDelegate<JifenEntity>
{

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.jifen_car_item;
    }

    @Override
    public boolean isForViewType(JifenEntity item, int position)
    {
        return !item.istaxi();
    }

    @Override
    public void convert(ViewHolder holder, JifenEntity jifenItem, int position)
    {
        holder.setText(R.id.zhekou, String.valueOf(jifenItem.getZhekou()));
        holder.setText(R.id.car_jifen, jifenItem.getJifen());
        holder.setText(R.id.car_time,jifenItem.getYouxiaoqi());
        holder.setText(R.id.car_zhekou1,String.valueOf(jifenItem.getZhekou())+"折");
        holder.setOnClickListener(R.id.car_jifen, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("我是积分，你要去哪");
            }
        });
    }
}
