package com.zk.taxi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/29 0029.
 */

public class OrderStateEntity implements Serializable {
    public  String Guid;// 订单GUID
    public String UserID;//用户ID
    public String CustomerTel; //用户手机号
    public String CustomerName; //用户姓名
    public int Sex; //性别 0男 1女
    public String DriverName; //司机姓名
    public String DriverSta;//司机星级
    public String Company; //所属企业
    public String TaxiCard; //车牌号码
    public  String TaxiID; //车辆Guid
    public  String TaxiSim; //车载电话号码
    public String TaxiResult;//抢单结果 0 失败 1成功
    public String TaxiTime;//召车时间(约车时间)
    public String TaxiType; //2 出租车 4 约租车
    public String CreateTime; //订单创建时间
    public String TaxiAddress; //乘车地址
    public double Lat; //上车经度
    public double Lon; //上车纬度
    public  String TaxiDestination; //目的地
    public double DLon; //目的地经度
    public double DLat; //目的地纬度
    public String UpTime; //上车时间
    public String DownTime; //下车时间
    public  String orderevaluationid; //订单ID
    public String evaluationlevel;   //评价星级 -1未评价0~5星级
    public String evaluationcontent; //评价内容
    public String evaluationlcreatetime; //订单扩展信息创建时间
    public String TabId;  //评价标签id （多个用,分隔）
    public String TabName; //评价标签名称（多个用,分隔）
    public int PayStatus; //支付状态 1已支付，0未支付
    public String Payment; //支付方式
    public String PayAmount; //支付金额
    public String OrderId; //订单id
    public String OrderStatus;//订单状态，0.车机抢单成功，1.App抢单成功，2.乘客已上车，3.到达目的地，4.订单取消，5订单已完成
}
