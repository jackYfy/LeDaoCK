package com.zk.taxi.entity;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class FeedBackEntity  implements Serializable {

    public String Content;//反馈内容
    public String IsueeName;//反馈问题类型名称
    public String CarTypeName; //反馈车辆类型名称
    public String CreatTime;//时间


    public String getCreatTime() {
        return CreatTime;
    }

    public void setCreatTime(String creatTime) {
        CreatTime = creatTime;
    }
    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getIsueeName() {
        return IsueeName;
    }

    public void setIsueeName(String isueeName) {
        IsueeName = isueeName;
    }

    public String getCarTypeName() {
        return CarTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        CarTypeName = carTypeName;
    }



    public static FeedBackEntity parse(String json) {
        return new Gson().fromJson(json, FeedBackEntity.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
