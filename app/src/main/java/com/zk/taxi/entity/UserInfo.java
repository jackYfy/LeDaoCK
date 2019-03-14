package com.zk.taxi.entity;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 *
 */
public class UserInfo {

    public static final String TAG = UserInfo.class.getSimpleName();

    private String Token;// 登录令牌
    private long user_id;// 用户id
    private String user_name;// 用户名
    private String nick_name;// 昵称
    private String last_name;// 用户名
    private int sex;// 性别
    private String user_truename;// 实名登记
    private String email;// 邮箱
    private String mobile;// 手机
    private String common_country_id;//国家id
    private String province_id;//省份id
    private String city_id;//城市id
    private String default_lan_id;//默认ip
    private String ic_card_no;//身份证号
    private String head_pic;//头像路径

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }


    public String getUser_id() {
        return TextUtils.isEmpty(String.valueOf(user_id)) ? "abc" : String.valueOf(user_id);
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getSex() {

        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUser_truename() {
        return user_truename;
    }

    public void setUser_truename(String user_truename) {
        this.user_truename = user_truename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return TextUtils.isEmpty(mobile) ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCommon_country_id() {
        return common_country_id;
    }

    public void setCommon_country_id(String common_country_id) {
        this.common_country_id = common_country_id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getDefault_lan_id() {
        return default_lan_id;
    }

    public void setDefault_lan_id(String default_lan_id) {
        this.default_lan_id = default_lan_id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public String getIc_card_no() {
        return ic_card_no;
    }

    public void setIc_card_no(String ic_card_no) {
        this.ic_card_no = ic_card_no;
    }
    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public static UserInfo parse(String json) {
        return new Gson().fromJson(json, UserInfo.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
