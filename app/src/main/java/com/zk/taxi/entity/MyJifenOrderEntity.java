package com.zk.taxi.entity;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/12/22.
 */

public class MyJifenOrderEntity {
     private String orderNumber;
    private  String carType;
    private int thingCount;
    private int jifenCount;

    public MyJifenOrderEntity() {
    }
    public MyJifenOrderEntity(String orderNumber, String carType, int thingCount,int jifenCount)
    {
        this.orderNumber = orderNumber;
        this.carType = carType;
        this.thingCount = thingCount;
        this.jifenCount=jifenCount;
    }
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public int getThingCount() {
        return thingCount;
    }

    public void setThingCount(int thingCount) {
        this.thingCount = thingCount;
    }

    public int getJifenCount() {
        return jifenCount;
    }

    public void setJifenCount(int jifenCount) {
        this.jifenCount = jifenCount;
    }

    public static MyJifenOrderEntity parse(String json) {
        return new Gson().fromJson(json, MyJifenOrderEntity.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
