package com.zk.taxi.adpter;

import android.content.Context;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zk.taxi.entity.JifenEntity;

import java.util.List;

/**
 * Created by zhy on 15/9/4.
 */
public class JifenAdapterForRv extends MultiItemTypeAdapter<JifenEntity>
{
    public JifenAdapterForRv(Context context, List<JifenEntity> datas)
    {
        super(context, datas);

        addItemViewDelegate(new JinfenCarItemDelagate());
        addItemViewDelegate(new JinfenTaxiItemDelagate());
    }
}
