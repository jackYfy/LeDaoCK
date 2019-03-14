package com.zk.taxi.entity;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/12/21.
 */

public class JifenEntity {

    private int money;
    private String jifen;
    private String youxiaoqi;
    private double zhekou;
    private boolean istaxi;

    public JifenEntity() {
    }
    public JifenEntity(int money, String jifen, String youxiaoqi,double zhekou,boolean istaxi)
    {
        this.money = money;
        this.jifen = jifen;
        this.youxiaoqi = youxiaoqi;
        this.zhekou=zhekou;
        this.istaxi=istaxi;
    }
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getYouxiaoqi() {
        return youxiaoqi;
    }

    public void setYouxiaoqi(String youxiaoqi) {
        this.youxiaoqi = youxiaoqi;
    }

    public double getZhekou() {
        return zhekou;
    }

    public void setZhekou(double zhekou) {
        this.zhekou = zhekou;
    }
    public boolean istaxi() {
        return istaxi;
    }

    public void setIstaxi(boolean istaxi) {
        this.istaxi = istaxi;
    }
    public static JifenEntity parse(String json) {
        return new Gson().fromJson(json, JifenEntity.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
