package com.zk.taxi;

/**
 * 所有接口名定义
 */
public class Interface {

    public static final String LOGIN = "ns-login";// 登录
    public static final String NS_GETALIINTERFACE="ns-getaliinterface"; //客户端获取所有业务接口地址
    public static final String NS_GETUSERINFO = "ns-getuserinfo";// 获取个人信息
    public static final String NS_EDITUSERINFO = "ns-edituserinfo";// 修改用户资料
    public static final String UPDATEUSERINFO = "updateuserinfo";// 修改身份证信息
    public static final String NS_EDITHEADPIC="ns-editheadpic";//上传头像
    public static final String NS_CT_GETADVERTISEMENTLIST = "ns-ct.getadvertisementlist";// 获取广告列表
    public static final String NS_POSTUSERPHONEINFO = "ns-postuserphoneinfo";// 提交手机信息
    public static final String NS_QUERYDETAILTAXISTATUS = "ns-querydetailtaxistatus";// 查询附近出租车
    public static final String NS_TAXI_NEARBYSPECIALCAR = "ns-taxi.nearbyspecialcar";// 附近约租车信息列表
    public static final String NS_ORDER_CANCEL = "ns-order.cancel";// 约租车取消订单
    public static final String NS_CANCELTAXIORDER = "ns-canceltaxiorder";// 取消出租车
    public static final String NS_CANCELTAXIORDERTYPE="ns-canceltaxiordertype";//取消订单后提交取消的原因
    public static final String NS_TAXI_CREATEORDER = "ns-taxi.createorder";// 约租车创建订单
    public static final String NS_TAXI_REALTIMEGPSINFO = "ns-taxi.realtimegpsinfo";// 查询司机实时位置
    public static final String NS_QUERYCALLORDERSTATUS = "ns-querycallorderstatus";// 查询出租车订单状态
    public static final String NS_QUERYORDERSTATUS="ns-queryorderstatus";//查询订单状态，乘客是否上车，是否到达目的地
    public static final String NS_ORDER_DETAIL = "ns-order.detail";// 根据GUIID查询订单详情
    public static final String NS_EXTERNALTAXIRECORD = "ns-externaltaxirecord";// 出租车召车
    public static final String NS_GETANNOUNCEMENTLIST = "ns-ct.getannouncementlist";// 公告
    public static final String CHECKICCARDNO = "checkiccardno";// 检测是否登记身份证
    public static final String NS_SENDFINDPASSWORDVERFITYCODE = "ns-sendfindpasswordverfitycode";// 修改密码，发送验证码
    public static final String NS_SENDPHONECODE="ns-sendphonecode";//手机号发送验证码
    public static final String NS_MODIFYPOSSWORDBYPHONE = "ns-modifyposswordbyphone";// 修改密码
    public static final String NS_CALLREGISTE = "ns-callregiste";// 注册获取验证吗
    public static final String NS_REGISTE = "ns-registe";// 注册
    public static final String NS_QUERYCALLORDERHISTORYEXFORLEDAO = "ns-querycallorderhistoryexforledao";// 行程记录
    public static final String NS_MESSAGELIST = "ns-ct.messagelist";// 消息中心
    public static final String NS_FEEDBACKADD = "ns-ct.feedbackadd";// 用户评价,反馈
    public static final String NS_GETFEEDBACK="ns-ct.getfeedback";//获取反馈内容
    public static final String NS_GETNEWVERSIONFORCURRENTVERSION = "ns-getnewversionforcurrentversion";// 获取服务端app版本
    public static final String NS_CT_GETCOMPLAINTREGISTERLIST1 = "ns-ct.getcomplaintregisterlist";// 获取用户投诉信息列表

    public static final String NS_CT_GETLOSTREGISTERLIST = "ns-ct.getlostregisterlist";// 获取用户失物信息列表
    public static final String NS_CT_COMPLAINTREGISTERADD = "ns-ct.complaintregisteradd";// 用户提交投诉信息
    public static final String NS_CT_LOSTREGISTERADD = "ns-ct.lostregisteradd";// 用户提交失物信息
    public static final String SETSATISFACTION = "ns-setsatisfaction";// 订单评价
    public static final String QUERYLATESTCALLORDER="querylatestcallorder";//查询订单的状态，去往相应界面继续完成

    public static final String GETPAYSERIALNO = "getpayserialno";// 获取支付流水号
    public static final String GETWXPREPAYID = "getwxprepayid";// 微信预支付
    public static final String WXPAYCLOSE = "wxpayclose";// 关闭支付宝订单
//    public static final String CREATEORDER = "createOrder";// 支付宝支付
    public static final String UPDATEORDERBYPAY="updateorderbypay";//支付宝支付前订单更新

    public static final String ADDCOMMONROUTE = "addcommonroute";//添加常用路线
    public static final String EDITCOMMONROUTE = "editcommonroute";//编辑常用路线
    public static final String DELETECOMMONROUTE = "deletecommonroute";//删除常用路线
    public static final String GETCOMMONROUTE = "getcommonroute";//获取常用路线列表
    public static final String RIDE_CREATEORDER = "ride.createorder";//创建顺风车订单
    public static final String RIDE_TASKLIST = "ride.tasklist";//查询顺风车订单任务列表
}
