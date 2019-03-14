package com.zk.taxi.entity;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/5 0005.
 */

public class LocationCityEntity implements Serializable {
    private String city_id; //城市ID
    private String city_name; //城市名称
    private String interface_add; //接口地址
    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getInterface_add() {
        return interface_add;
    }

    public void setInterface_add(String interface_add) {
        this.interface_add = interface_add;
    }

    public static LocationCityEntity parse(String json) {
        return new Gson().fromJson(json, LocationCityEntity.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
