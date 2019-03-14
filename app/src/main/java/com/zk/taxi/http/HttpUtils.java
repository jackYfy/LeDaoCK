package com.zk.taxi.http;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zk.taxi.Config;
import com.zk.taxi.tool.MD5;
import com.zk.taxi.tool.NetWorkUtils;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.tool.UserHelper;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtils {
    public static final String TAG = HttpUtils.class.getSimpleName();
    /**
     * 异步的HTTP客户端实例
     **/
    private static AsyncHttpClient client ;
    private static final String UTF_8 = "UTF-8";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    static {
//        client = new AsyncHttpClient(8081,8081);
        client = new AsyncHttpClient();
        client.setTimeout(30000);
    }


//.do类型的 （java接口,暂没用到）
    public static void postHttpRequest(Context context, String url, RequestParams params, HttpResponse handler){
        if (!NetWorkUtils.isAvailable()) {
            ToastUtils.show("网络不可用");
            return;
        }
        handler.setCharset(UTF_8);
        client.addHeader("Content-type", CONTENT_TYPE);
        client.addHeader("Connection", "Keep-Alive");
        client.addHeader("charset", HTTP.UTF_8);
        client.post(context, url,params, handler);
    }
    //获取地址连接列表
    public static void getInterfaceUrl(String url, String method,String postdata, HttpResponse handler){
        if (!NetWorkUtils.isAvailable()) {
            ToastUtils.show("网络不可用");
            return;
        }
        try {
            postdata = URLEncoder.encode(postdata, "utf-8").toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestParams params=new RequestParams();
        params.put(Config.ATTRS_METHOD, method);
        params.put(Config.ATTRS_APPID, Config.APP_ID);
        params.put(Config.ATTRS_POSTDATA, postdata);
        client.post(url, params, handler);
    }
    //乐道接口请求 需要userid token sign签名
    public static void getTaxi(String url, String method,String postdata, HttpResponse handler) {
        if (!NetWorkUtils.isAvailable()) {
            ToastUtils.show("网络不可用");
            return;
        }
            try {
                postdata = URLEncoder.encode(postdata, "utf-8").toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        RequestParams params=new RequestParams();
        params.put(Config.ATTRS_APPID, Config.APP_ID);
        params.put(Config.ATTRS_METHOD, method);
        params.put(Config.ATTRS_USERID, (UserHelper.getUserInfo() == null) ? "abc" : UserHelper.getUserInfo().getUser_id());
        params.put(Config.ATTRS_POSTDATA, postdata);
        params.put(Config.ATTRS_SIGN, getSign(params));
        Log.d("POST",url + "?" + params.toString());
        client.post(url, params, handler);
    }
    //支付，头像用,要排序签名的都可以用这个
    public static void getTaxiPay(Context context,String url, String method,String postdata, HttpResponse handler){
        if (!NetWorkUtils.isAvailable()) {
            ToastUtils.show("网络不可用");
            return;
        }
        Values values=new Values();
        values.put(Config.ATTRS_APPID, Config.APP_ID);
        values.put(Config.ATTRS_METHOD, method);
        values.put(Config.ATTRS_USERID, (UserHelper.getUserInfo() == null) ? "abc" : UserHelper.getUserInfo().getUser_id());
        values.put(Config.ATTRS_POSTDATA, postdata);
        values.sort();//支付 签名前需排序
        values.put(Config.ATTRS_SIGN, getSign(values));
        RequestParams params=getParams(values);
        Log.d("POST","getTaxiPay:" + params.toString());
        client.post(context, url, params, handler);
    }
    //获取订单步骤状态去往相应的界面
    public static void getTaxiOrder(Context context,String url, String method,String postdata,AsyncHttpResponseHandler responseHandler){
        if (!NetWorkUtils.isAvailable()) {
            ToastUtils.show("网络不可用");
            return;
        }
        Values values=new Values();
        values.put(Config.ATTRS_APPID, Config.APP_ID);
        values.put(Config.ATTRS_METHOD, method);
        values.put(Config.ATTRS_USERID, (UserHelper.getUserInfo() == null) ? "abc" : UserHelper.getUserInfo().getUser_id());
        values.put(Config.ATTRS_POSTDATA, postdata);
        values.sort();//支付 签名前需排序
        values.put(Config.ATTRS_SIGN, getSign(values));
        RequestParams params=getParams(values);
        Log.d("POST","getTaxiPay:" + params.toString());
        client.post(context, url, params, responseHandler);
    }
    //获取天气
    private static final String APPCODE="APPCODE d28c5d7c8b4340c0b7fb93d42f9a9be6";
    public static  void getNewWeather(String url,Values values, AsyncHttpResponseHandler listener){
        if (!NetWorkUtils.isAvailable()) {
            ToastUtils.show("网络不可用");
            return;
        }
        values.sort();
        if(values.size()>0)
        {
            url=url+ "?" + values.toString();
        }
        client.addHeader("Authorization",APPCODE);
        client.get(url, listener);
    }

    /**
     * 停止请求
     *
     * @param mContext 发起请求的上下文
     */
    public static void stopRequest(Context mContext) {
        client.cancelRequests(mContext, true);
    }

    /**
     * 停止全部请求
     */
    public static void stopAllRequest() {
        client.cancelAllRequests(true);
    }

    private static String getPostData(Values values) {
        JSONObject json = new JSONObject();
        for (String key : values.keySet()) {
            try {
                json.put(key, values.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json.toJSONString();
    }
    private static String getSign(RequestParams params) {
        StringBuffer sb = new StringBuffer();
        sb.append(params.toString());
        if (UserHelper.getUserInfo() != null) {
            sb.append(UserHelper.getUserInfo().getToken());
        }
//		Log.d("TAG", "待签名：" + sb.toString());
        String sign = MD5.getMessageDigest(sb.toString());
//		Log.d("TAG", "签名：" + sign);
        return sign;
    }
    private static String getSign(Values params) {
        StringBuffer sb = new StringBuffer();
        sb.append(params.toString());
        if (UserHelper.getUserInfo() != null) {
            sb.append(UserHelper.getUserInfo().getToken());
        }
		Log.d("TAG", "待签名：" + sb.toString());
        String sign = MD5.getMessageDigest(sb.toString());
        return sign;
    }

    private static RequestParams getParams(Values values){
        RequestParams params=new RequestParams();
        String v=null;
        String k=null;
        for (Map.Entry<String, String> value:values.valueSet()){
            v=value.getValue();
            k=value.getKey();
            params.put(k,v);
        }
        return params;
    }
}

