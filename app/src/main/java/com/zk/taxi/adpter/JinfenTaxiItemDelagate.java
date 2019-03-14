package com.zk.taxi.adpter;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zk.taxi.R;
import com.zk.taxi.entity.JifenEntity;

/**
 * Created by zhy on 16/6/22.
 */
public class JinfenTaxiItemDelagate implements ItemViewDelegate<JifenEntity>
{

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.jifen_taxi_item;
    }

    @Override
    public boolean isForViewType(JifenEntity item, int position)
    {
        return item.istaxi();
    }

    @Override
    public void convert(ViewHolder holder, JifenEntity chatMessage, int position)
    {
        holder.setText(R.id.jine, String.valueOf(chatMessage.getMoney()));
        holder.setText(R.id.have_jifen, chatMessage.getJifen());
        holder.setText(R.id.taxi_time,chatMessage.getYouxiaoqi());
        holder.setText(R.id.taxi_jine1, String.valueOf(chatMessage.getMoney())+"元");
    }
}
