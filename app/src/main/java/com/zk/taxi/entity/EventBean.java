package com.zk.taxi.entity;

/**
 * Created by Administrator on 2017/11/30.
 */

public class EventBean {
    private  String msg;
    private int type;//修改密码或找回密码类型 1:EventBus传递账号到密码页登录，2:忘记密码，3:注册，4:修改密码
    private  String code;
    public EventBean(String msg,int type){
        this.msg = msg;
        this.type=type;
    }
    public EventBean(String msg,int type,String code){
        this.msg = msg;
        this.type=type;
        this.code=code;
    }
    public String getMsg(){
        return msg;
    }
    public int getType(){
        return type;
    }
    public String getCode(){
        return code;
    }
}
