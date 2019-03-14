package com.zk.taxi;


import com.zk.taxi.tool.MD5;
import com.zk.taxi.tool.UserHelper;

public class Config {
    // 客户端数据库名、版本号
    public static final String DATABASE_NAME = "ledaopassenger.db";
    public static final int DATABASE_VERSION = 1;
    // server interface
    public static final boolean debug = true;// 是否调试环境
    public static final String APP_ID = "2016011800000002";// 乐道appid
    // 正式环境
    public static final String URL_PRODUCE = "http://webapi.app.zkits.cn:8085/clientapp/default.ashx";
    // 调试环境
//    public static final String URL_TEST = "http://192.168.10.211:8085/ClientApp/default.ashx";//zhongkaizn.f3322.net
    public static final String URL_TEST ="http://192.168.10.211:8084/default.ashx"; //登录和和获取接口列表地址，以及用户中心等
    public static final String URL_SERVER = debug ? URL_TEST : URL_PRODUCE;
    public static final String URL_CALL_CAR= UserHelper.getURL();//根据当前所在城市所获取的接口地址
    // 广告图片url
    public static final String URL_PICTURE = "http://webapi.app.zkits.cn:8085/AppIntroduce/LeDao/ad/";
    // 服务条款url
    public static final String URL_DISCLAIMER = "http://zhongkai.f3322.net:8085/AppIntroduce/LeDao/Disclaimer.aspx";
    // 计价标准url
    public static final String URL_VALUATION = "http://webapi.app.zkits.cn:8085/AppIntroduce/LeDao/Valuation.aspx";
    //支付宝回调地址
    public static final String URL_ALIPAYS="http://openapi.app.fscsps.com:8081/ClientBizServer/AlipayNotify.ashx";
    //支付
    public static final int ALI_PAY=3001;
    public static final int WEIXIN_PAY=3002;
    // server interface attrs
    public static final String ATTRS_APPID = "appid";
    public static final String ATTRS_METHOD = "methodname";
    public static final String ATTRS_USERID = "user_id";
    public static final String ATTRS_POSTDATA = "postdata";
    public static final String ATTRS_SIGN = "sign";
    public static final String ATTRS_FILE = "File";
    public static final String ATTRS_TOKEN = "token";
    public static final int PAGE_SIZE = 10;
    public static final String SP_ACCOUNT = "ACCOUNT";//登录后缓存账号信息
    public static final String URL_LOC = "URLLOC";//缓存当前地点所获取的url
    public static final String HEAD_IMG_URL = "HEADIMGURL";//缓存头像地址
    // 标记是否第一次启动应用的key
    public static final String IS_FRIST = MD5.getMessageDigest("is_frist");
    // 两次返回退出应用
    public static long LAST_TIME ;
    public static final int INTERVAL_TIME = 2000;// 触发间隔

    // 接收定位消息成功
    public static final int LOCAION_RECEIVER = 10001;
    // 百度地图地理编码成功
    public static final int BAIDU_GEO = 10002;
    public static final int BAIDU_REVERSEGEO = 10003;
    public static final int BAIDU_POI = 10004;
    public static final int BAIDU_SUGGESTION = 10005;

    public static final int HANDLER_ADVERT = 10008;// 加载页广告切换handler的what

    public static final int WEB_SERVICE_PROVISION = 10009; // 服务条款
    public static final int WEB_VALUATION = 10010; // 计价标准
    public static final int WEB_REGISTERED = 10011; // 注册前服务条款

    public static final int CALL_COUNT_TIME = 10012;
    public static final int CALL_NEAR_CAR = 10013;

    public static final int VERSIONCHECK = 10014;//检查更新
    public static final int DRIVERTOME = 10015;//司机位置
    public static final int ON_THE_CAR= 10016;//乘客已上车状态
    public static final int ON_END_POINT= 10017;//到达目的地状态
    public static final int TOTALAMOUNT=10018;//总金额
    // 车辆类型id
    public static final int TYPE_TAXI = 2;// 出租车
    public static final int TYPE_SPECIAL = 7;// 快捷出租车
    public static final int TYPE_FREERIDE = 8;//顺风车
    // 约租车
    public static final String SPECIAL_TIMELY = "0";// 及时约租车
    public static final String SPECIAL_APPOINTMENT = "1";// 延迟约租车
    // emailbox id
    public static final int BOX_LOST = 11;
    public static final int BOX_COMPLAINT = 12;
    // SildingItem
    public static final int SILD_TRIPHISTORY = 21;
    public static final int SILD_MESSAGECENTER = 22;
    public static final int SILD_SETTING = 23;
    public static final int SILD_USEREVALUATE = 24;
    public static final int SILD_VALUATION = 25;
    public static final int SILD_MAILBOX = 26;
    public static final int SILD_SHARE = 27;
    //update_ui
    public static final int UPDATE_UI = 28;
    // ------------------request and result code------------------//start
    // 出租车
    public static final int CODE_POI_START = 20001;
    public static final int CODE_POI_END = 20002;
    public static final int CODE_COMPLAINTS_REQUEST = 20005;
    public static final int CODE_COMPLAINTS_RESULT = 20006;
    public static final int CODE_LOST_REQUEST = 20007;
    public static final int CODE_LOST_RESULT = 20008;
    // 行程
    public static final int CODE_TRIP_REQUEST = 20009;
    public static final int CODE_TRIP_RESULT = 20010;
    // 个人中心
    public static final int CODE_INDIVIDUAL_REQUEST = 20011;
    // 投诉和失物
    public static final int CODE_LOST = 20012;
    public static final int CODE_COMPLAINTS = 20013;
    //替他人下单
    public static final int CODE_OTHER_PASSENGER = 20014;

    public static final int CODE_ROUTE_REQUEST = 20015;
    // ------------------request and result code------------------//end

    // ------------------Intent key------------------//start
    // 出租车转搜索页面
    public static final String KEY_POI_SEARCH = "POI_SEARCH";
    // 当前地址
    public static final String KEY_CUR_ADDRESS = "CUR_ADDRESS";
    // result 带回key
    public static final String KEY_RESULT_POIINFO = "RESULT_POIINFO";
    // webview入口key
    public static final String KEY_WEBVIEW = "WEB_VIEW";
    // callthecar区分出租车类型
    public static final String KEY_CALL_TYPE = "CALL_TYPE";
    public static final String KEY_CALL_POIINFO = "CALL_POIINFO";
    public static final String KEY_CALL_GUID = "CALL_GUID";
    public static final String STLATITUDE = "stlatitude";//上车点纬度
    public static final String STLONGITUDE = "stlongitude";//上车点经度
    public static final String EDLATITUDE = "edlatitude";//下车点纬度
    public static final String EDLONGITUDE = "edlongitude";//下车点经度
    public static final String ST_ADDRESS = "st_address";
    public static final String ED_ADDRESS = "ed_address";
    // 修改资料
    public static final String KEY_MODIFY_NICKNAME = "NickName";// 对应接口传输的key,不要修改
    public static final String KEY_MODIFY_LASTNAME = "UserLastName";// 对应接口传输的key,不要修改
    public static final String KEY_TRIP_HISTORY = "TRIP_HISTORY";
    // 约租车 及时打车或预约车 的key
    public static final String KEY_SPECIAL_CRATEORDER = "SPECIAL_CRATEORDER";
    //路线编辑标题
    public static final String KEY_ROUTE_EDIT_TITLE = "ROUTE_EDIT_TITLE";
    //顺风车匹配订单日期
    public static final String KEY_ORDER_MATCHING_TIME = "ORDER_MATCHING_TIME";
    // ------------------Intent key------------------//end

    // ------------------SharedPreferences的key------------------//start
    // 保存当前城市的key
    public static final String SP_CUR_CITY = MD5.getMessageDigest("CUR_CITY");
    public static final String SP_ACCOUTS = MD5.getMessageDigest("ACCOUTS");
    // ------------------SharedPreferences的key------------------//end
}
