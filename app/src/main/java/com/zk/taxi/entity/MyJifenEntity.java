package com.zk.taxi.entity;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/12/22.
 */

public class MyJifenEntity {
    private String orderName;
    private String myJifen;
    private String time;

    public MyJifenEntity() {
    }
    public MyJifenEntity(String orderName, String myJifen, String time)
    {
        this.orderName = orderName;
        this.myJifen = myJifen;
        this.time = time;
    }
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getMyJifen() {
        return myJifen;
    }

    public void setMyJifen(String myJifen) {
        this.myJifen = myJifen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public static MyJifenEntity parse(String json) {
        return new Gson().fromJson(json, MyJifenEntity.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
