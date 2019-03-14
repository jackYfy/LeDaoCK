package com.zk.taxi.methodDao;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import com.alibaba.fastjson.JSONObject;
import com.zk.taxi.Config;
import com.zk.taxi.Interface;
import com.zk.taxi.entity.FeedBackEntity;
import com.zk.taxi.entity.LocationCityEntity;
import com.zk.taxi.entity.UserInfo;
import com.zk.taxi.http.HttpResponse;
import com.zk.taxi.http.HttpUtils;
import com.zk.taxi.http.Response;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.tool.UserHelper;
import com.zk.taxi.widget.LoadingDialog;
import com.zk.taxi.widget.Methods;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2017/11/29.
 */

public class UserPost {
    public static final int LOGIN=1001;
    public static final int INTERFACE=2002;
    public static final int HEADIMG=2003;
    public static final int FEEDBACK=2004;
    public static final int INFOCRAD=2005;
    public static final int USERINFO=2006;
    private static LoadingDialog loadingDialog;

//获取所有地区服务器地址
    public static void  getAllInterface(final Activity act,final Handler handler){
        JSONObject json=new JSONObject();
        json.put("type",1);
        HttpUtils.getInterfaceUrl(Config.URL_SERVER, Interface.NS_GETALIINTERFACE,json.toJSONString(),new HttpResponse(){
            @Override
            public void result(int i, String content) {
                try {
                    String mess = URLDecoder.decode(content, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,LocationCityEntity.class);
                msg.what=INTERFACE;
                handler.sendMessage(msg);
            }
        });

    }
    //登录
    public static void isLogin(final Activity act, final Handler handler, String name, String password){
        loadingDialog=new LoadingDialog(act);
        loadingDialog.loading();
        JSONObject json=new JSONObject();
        json.put("loginkey",name);
        json.put("logintype","1");//1表示普通用户
        json.put("loginpassword",password);
        HttpUtils.getTaxi(Config.URL_SERVER, Interface.LOGIN,json.toJSONString(),new HttpResponse(){
            @Override
            public void result(int i, String content) {
                try {
                    String mess = URLDecoder.decode(content, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,UserInfo.class);
                msg.what=LOGIN;
                handler.sendMessage(msg);
                loadingDialog.dismiss();
            }
        });
    }
    //获取注册验证码
    public  static void  getRegistVCode(final Activity act, String phone){
        loadingDialog=new LoadingDialog(act);
        loadingDialog.loading();
        JSONObject json=new JSONObject();
        json.put("loginkey",phone);
        json.put("logintype","1");//1表示普通用户
        HttpUtils.getTaxi(Config.URL_SERVER, Interface.NS_CALLREGISTE, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                if (Response.CodeSuccess(act,content)) {
                    ToastUtils.show("验证码发送成功,请注意查收");
                    loadingDialog.dismiss();
                }else {
                    ToastUtils.show("验证码发送失败");
                    loadingDialog.dismiss();
                }
            }
        });
    }
    //注册
    public static void Regist(final Activity act, String phone,String vcode,String newpassword){
        loadingDialog=new LoadingDialog(act);
        loadingDialog.loading();
        JSONObject json=new JSONObject();
        json.put("loginkey",phone);
        json.put("logintype","1");//1表示普通用户
        json.put("loginpassword",newpassword);
        json.put("vcode",vcode);//1表示普通用户
        HttpUtils.getTaxi(Config.URL_SERVER, Interface.NS_REGISTE, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                if (Response.CodeSuccess(act,content)) {
                    ToastUtils.show("注册成功");
                }else {
                    ToastUtils.show("注册失败");
                }
                loadingDialog.dismiss();
            }
        });
    }
     //忘记密码和修改密码用的 手机号发送验证码
    public static void getSendPhoneCode(final Activity act, String phone){
        JSONObject json=new JSONObject();
        json.put("loginkey",phone);
        json.put("codetype",1);//验证码类型 0-注册 1-忘记密码 2-登录 3.司机注册
        HttpUtils.getTaxi(Config.URL_SERVER, Interface.NS_SENDPHONECODE, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                if (Response.CodeSuccess(act,content)) {
                    ToastUtils.show("验证码发送成功,请注意查收");
                }else {
                    ToastUtils.show("验证码发送失败");
                }
            }
        });
    }

 /*   //发送验证码  (手机号请求找回密码验证码)<停用>
     public static void SendCode(final Activity act, String phone){
         loadingDialog=new LoadingDialog(act);
         loadingDialog.loading();
         JSONObject json=new JSONObject();
         json.put("loginkey",phone);
         json.put("logintype","1");//1表示普通用户
         HttpUtils.getTaxi(Config.URL_SERVER, Interface.NS_SENDFINDPASSWORDVERFITYCODE, json.toJSONString(), new HttpResponse() {
             @Override
             public void result(int i, String content) {
                 if (Response.CodeSuccess(act,content)) {
                     ToastUtils.show("验证码发送成功,请注意查收");
                 }else {
                     ToastUtils.show("验证码发送失败");
                 }
                 loadingDialog.dismiss();
             }
         });
     }*/
//忘记密码 修改密码 调用
     public static void Modifypossword(final Activity act,String phone, String newpassword,String vcode){
         JSONObject json=new JSONObject();
         json.put("loginkey",phone);
         json.put("logintype","1");//1表示普通用户
         json.put("loginnewpassword ",newpassword);
         json.put("Vcode",vcode);
         HttpUtils.getTaxi(Config.URL_SERVER, Interface.NS_MODIFYPOSSWORDBYPHONE, json.toJSONString(), new HttpResponse() {
             @Override
             public void result(int i, String content) {
                 if (Response.CodeSuccess(act,content)) {
                     ToastUtils.show("密码修改成功");
                 }else {
                     ToastUtils.show("密码修改失败");
                 }
             }
         });
     }


     //上传头像
    public static void SendHeadImg(final Activity act, String fileurl, final Handler mhandler){
        fileurl=encode(fileurl);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("filedata",fileurl);
//        jsonObject.put("filename","头像");
//        jsonObject.put("datatype","图片");
        HttpUtils.getTaxiPay(act,Config.URL_SERVER, Interface.NS_EDITHEADPIC,jsonObject.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                if (Response.CodeSuccess(act,content)) {
                    ToastUtils.show("头像上传成功");
                    String uriImg=Response.analysisResult(content);
                    Message msg=new Message();
                    msg.obj= Response.analysisResult(content);
                    msg.what=HEADIMG;
                    mhandler.sendMessage(msg);
                }else {
                    ToastUtils.show("头像上传失败");
                }
            }
        });
    }
    //提交反馈
    public static void getFeedBackAdd(final Activity act,String content){
        UserInfo user = UserHelper.getUserInfo();
        JSONObject json=new JSONObject();
        json.put("appid",Config.APP_ID);
        json.put("appversion", Methods.getVersionCode(act));
        json.put("usermobile",user.getMobile());
        json.put("content",content);
        HttpUtils.getTaxi(Config.URL_SERVER, Interface.NS_FEEDBACKADD, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                if (Response.CodeSuccess(act,content)) {
                    ToastUtils.show("反馈提交成功");
                    act.finish();
                }else {
                    ToastUtils.show("提交失败");
                }
            }
        });
    }

//获取反馈列表
    public static void getFeedBack(final Activity act,final Handler mhandler){
        UserInfo user = UserHelper.getUserInfo();
        JSONObject json=new JSONObject();
        json.put("appid",Config.APP_ID);
        json.put("appversion", Methods.getVersionCode(act));
        json.put("usermobile",user.getMobile());
        HttpUtils.getTaxi(Config.URL_SERVER, Interface.NS_GETFEEDBACK, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,FeedBackEntity.class);
                msg.what=FEEDBACK;
                mhandler.sendMessage(msg);
            }
        });
    }

    //获取个人信息
    public static void getUserInfo(final Activity act,final Handler mhandler){
        UserInfo user = UserHelper.getUserInfo();
        JSONObject json=new JSONObject();
        json.put("user_id",user.getUser_id());
        HttpUtils.getTaxi(Config.URL_SERVER, Interface.NS_GETUSERINFO, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                msg.obj= Response.analysis(act,content,UserInfo.class);
                msg.what=USERINFO;
                mhandler.sendMessage(msg);
            }
        });
    }
    //修改个人信息
public static void getUpdateUserInfo(final Activity act,String NickName,String sex,String UserTrueName,String UserLastName){
    JSONObject json=new JSONObject();
    json.put("NickName",NickName);
    json.put("sex",sex);
    json.put("UserTrueName",UserTrueName);
    json.put("UserLastName",UserLastName);
    HttpUtils.getTaxiPay(act, Config.URL_SERVER, Interface.NS_EDITUSERINFO, json.toJSONString(), new HttpResponse() {
        @Override
        public void result(int i, String content) {
            if (Response.CodeSuccess(act,content)) {
                ToastUtils.show("修改成功");
                act.finish();
            }
        }
    });
}
    //检查身份证信息
    public static void getInfoCard(final Activity act,final Handler mHandler){
        UserInfo user = UserHelper.getUserInfo();
        JSONObject json=new JSONObject();
        json.put("mobile",user.getMobile());
        HttpUtils.getTaxiPay(act,Config.URL_SERVER, Interface.CHECKICCARDNO, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                Message msg=new Message();
                if (Response.CodeSuccess(act,content)) {
                    msg.obj="1";
                }else {
                    msg.obj="0";
                }
                msg.what=INFOCRAD;
                mHandler.sendMessage(msg);

            }
        });
    }

    //补充身份证信息
    public static void getInfoUpdate(final Activity act,String name,String cradnum){
        UserInfo user = UserHelper.getUserInfo();
        JSONObject json=new JSONObject();
        json.put("mobile",user.getMobile());
        json.put("user_truename",name);
        json.put("ic_card_no",cradnum);
        HttpUtils.getTaxiPay(act, Config.URL_SERVER, Interface.UPDATEUSERINFO, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                if (Response.CodeSuccess(act,content)) {
                    ToastUtils.show("提交成功");
                    act.finish();
                }
            }
        });
  }
    //将图片进行64位编码
    public static String encode(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
        String encodeString = new String(encode);
        return encodeString;
    }

}
