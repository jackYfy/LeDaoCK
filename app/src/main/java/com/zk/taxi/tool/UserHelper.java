package com.zk.taxi.tool;

import android.text.TextUtils;

import com.zk.taxi.Config;
import com.zk.taxi.entity.UserInfo;


/**
 */
public class UserHelper {

    private static UserInfo userinfo = null;// 用户登录信息
    private static String url_service=null;
    private static String url_headimg=null;

    public static UserInfo getUserInfo() {
        synchronized (UserHelper.class) {
            if (!SPUtils.contains(UserInfo.TAG)) {
                return null;
            }
            String userJSON = (String) SPUtils.get(UserInfo.TAG, "");
            if (!TextUtils.isEmpty(userJSON)) {
                userinfo = UserInfo.parse(MD5.convertMD5(userJSON));
            }
            return userinfo;
        }
    }

    public synchronized static void saveUserInfo(final UserInfo user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SPUtils.put(UserInfo.TAG, MD5.convertMD5(user.toString()));// 保存登录信息
                SPUtils.put(Config.SP_ACCOUNT, MD5.convertMD5(user.getMobile()));// 保存登录帐号
            }
        }).start();
    }
    //保存根据当前地址获取的url列表
    public synchronized static void saveURL(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SPUtils.put(Config.URL_LOC,url);// 保存登录帐号
            }
        }).start();
    }
    //获取根据当前地址获取的url列表
    public static String getURL() {
        synchronized (UserHelper.class) {
            if (!SPUtils.contains(Config.URL_LOC)) {
                return null;
            }
            String url = (String) SPUtils.get(Config.URL_LOC,"");
            if (!TextUtils.isEmpty(url)) {
                url_service=url ;
            }
            return url_service;
        }
    }
    //删除根据当前地址获取的url列表
    public static void deleteURL() {
        synchronized (UserHelper.class) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SPUtils.remove(Config.URL_LOC);
                }
            }).start();
        }
    }

    //保存上传的头像地址
    public synchronized static void saveHeadImgURL(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SPUtils.put(Config.HEAD_IMG_URL,url);
            }
        }).start();
    }
    //获取上传的头像地址
    public static String getHeadImgURL() {
        synchronized (UserHelper.class) {
            if (!SPUtils.contains(Config.HEAD_IMG_URL)) {
                return null;
            }
            String url = (String) SPUtils.get(Config.HEAD_IMG_URL,"");
            if (!TextUtils.isEmpty(url)) {
                url_headimg=url ;
            }
            return url_headimg;
        }
    }
    /**
     * 是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        if (!SPUtils.contains(UserInfo.TAG))
            return false;
        String userJSON = (String) SPUtils.get(UserInfo.TAG, "");
        if (TextUtils.isEmpty(userJSON))
            return false;
        userinfo = UserInfo.parse(MD5.convertMD5(userJSON));
        if (userinfo == null)
            return false;
        return true;
    }

    public static void deleteUserInfo() {
        synchronized (UserHelper.class) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SPUtils.remove(UserInfo.TAG);
                }
            }).start();
        }
    }
    public static void saveEntity(final String ClassTAG,final String  str) {
        synchronized (UserHelper.class) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SPUtils.put(ClassTAG,str);
                }
            }).start();
        }
    }
    public static String getEntity(String ClassTag) {
        synchronized (UserHelper.class) {
            if (!SPUtils.contains(ClassTag)) {
                return null;
            }
            String value=(String) SPUtils.get(ClassTag, "");
            SPUtils.remove(ClassTag);
            return TextUtils.isEmpty(value)?"":value;
        }
    }
}
