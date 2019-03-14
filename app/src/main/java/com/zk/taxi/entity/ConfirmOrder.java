package com.zk.taxi.entity;

import com.google.gson.Gson;

import java.io.Serializable;

public class ConfirmOrder implements Serializable  {
    private static final long serialVersionUID = 1422343320430883869L;
    public String guid;//订单guid信息
    public String CustomerTel;//用户手机号 可以为null
    public String CustomerName;//用户姓名 可以为null
    public int Sex;//用户性别 0/1 0—男 1—女
    public String TaxiAddress;//用户乘车地址 可以为null
    public String RequestTime;//要车时间2014-09-13 14:00:36
    public int TaxiType;//的士类型

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCustomerTel() {
        return CustomerTel;
    }

    public void setCustomerTel(String customerTel) {
        CustomerTel = customerTel;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getTaxiAddress() {
        return TaxiAddress;
    }

    public void setTaxiAddress(String taxiAddress) {
        TaxiAddress = taxiAddress;
    }

    public String getRequestTime() {
        return RequestTime;
    }

    public void setRequestTime(String requestTime) {
        RequestTime = requestTime;
    }

    public int getTaxiType() {
        return TaxiType;
    }

    public void setTaxiType(int taxiType) {
        TaxiType = taxiType;
    }
    public static ConfirmOrder parse(String json) {
        return new Gson().fromJson(json, ConfirmOrder.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
