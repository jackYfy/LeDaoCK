package com.zk.taxi.entity;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/7 0007.
 */

public class OrderCarStatusEntity implements Serializable {
    public String Guid; // 订单GUID
    public String OrderStatus;//订单状态，0.车机抢单成功，1.App抢单成功，2.乘客已上车，3.到达目的地，4.订单取消

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public static OrderCarStatusEntity parse(String json) {
        return new Gson().fromJson(json, OrderCarStatusEntity.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
