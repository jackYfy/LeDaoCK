package com.zk.taxi.http;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zk.taxi.tool.ActivityManager;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.tool.UserHelper;
import com.zk.taxi.ui.CallCar.WaitDriverActivity;
import com.zk.taxi.ui.LoginAccountActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by winky on 2016/12/12.
 */
public class Response {

    public static final String TAG = Response.class.getSimpleName();
    public static final String SUCCESS = "1";// 接口调用成功
    public static final String FAILED = "0";// 接口调用失败
    public static Gson gson = new Gson();

    public static <T> List<T> analysis(Activity act, String response, Class<T> cls) {
        try {
            String mess = URLDecoder.decode(response, "utf-8");
            JSONObject  json;
            if (!isSuccess(act, mess)) {
                return null;
            }
            HttpResult<?> result = gson.fromJson(mess, HttpResult.class);
            String data = gson.toJson(result.getData());

            json = new JSONObject(mess);
            String dataJson = json.getString("Data").replace("'\'","");
            if (!TextUtils.isEmpty(data)) {
                List<T> list = new ArrayList<T>();
                // 创建一个JsonParser 获得 解析者
                JsonParser parser = new JsonParser();
                // 通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象 获得 根节点元素
                JsonElement el = parser.parse(dataJson);
                String fristChar = dataJson.substring(0,1);
                // 对象类型即 {}表示。[]表示为数组类型
                if (fristChar.equals("{")) {
                    T t = gson.fromJson(el.getAsJsonObject().toString().replace("'\'", ""), cls);
                    list.add(t);
                } else if (fristChar.equals("[")) {
                    Iterator<JsonElement> it = el.getAsJsonArray().iterator();
                    while (it.hasNext()) {
                        JsonElement e = it.next();
                        list.add(gson.fromJson(e, cls));
                    }
                } else {
                    return null;
                }
                return list;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSuccess(Activity act, String response) {
        HttpResult<?> result = gson.fromJson(response, HttpResult.class);
        if (result.getStatus().equals(SUCCESS)) {
            return true;
        } else {
            if (result.getErrorCode().equals("30-10-10")) {
                ToastUtils.show("登录过期，请重新登录");
                UserHelper.deleteUserInfo();
                act.startActivityForResult(new Intent(act, LoginAccountActivity.class),Activity.RESULT_FIRST_USER);
                ActivityManager.getAppManager().finishActivity(WaitDriverActivity.class);
            } else {
                ToastUtils.show(result.getMessage());
            }
            return false;
        }
    }
//验证码是否发送成功
    public static boolean CodeSuccess(Activity act, String response) {
        String mess = null;
        try {
            mess = URLDecoder.decode(response, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResult<?> result = gson.fromJson(mess, HttpResult.class);
        if (result.getStatus().equals(SUCCESS)) {
            return true;
        } else {
            if (result.getErrorCode().equals("30-10-10")) {
                ToastUtils.show("登录过期，请重新登录");
                UserHelper.deleteUserInfo();
                act.startActivityForResult(new Intent(act, LoginAccountActivity.class),Activity.RESULT_FIRST_USER);
            } else {
                ToastUtils.show(result.getMessage());
            }
            return false;
        }
    }

    public static String analysisResult(String response) {
        String mess = null;
        try {
            mess = URLDecoder.decode(response, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResult<?> result = gson.fromJson(mess, HttpResult.class);
        return result.getData().toString();
    }
}
