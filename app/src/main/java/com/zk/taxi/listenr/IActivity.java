package com.zk.taxi.listenr;

import android.view.View;

/**
 *
 */
public interface IActivity {

    /**
     * 绑定布局
     *
     * @return 布局文件资源id
     */
    public int bindLayout();

    /**
     * 初始化控件
     */
    public void initView(View view);
}
