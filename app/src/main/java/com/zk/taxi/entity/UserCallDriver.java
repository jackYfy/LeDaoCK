package com.zk.taxi.entity;

import com.google.gson.Gson;

import java.io.Serializable;

public class UserCallDriver implements Serializable {
	private static final long serialVersionUID = 3063403804392450792L;
	// 用户手机号 可以为null
	private String CustomerTel;
	// 用户姓名 可以为null
	private String CustomerName;
	// 用户性别 0/1 0—男 1—女
	private int Sex;
	// 用户乘车地址 可以为null
	private String TaxiAddress;
	// 经度
	private double Lon;
	// 纬度
	private double Lat;
	private String RequestTime;// 要车时间
	private int TaxiType;// 的士类型，2普通出租车app召车，7快捷出租车app召车

	public String getCustomerTel() {
		return CustomerTel;
	}

	public void setCustomerTel(String customerTel) {
		CustomerTel = customerTel;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public String getTaxiAddress() {
		return TaxiAddress;
	}

	public void setTaxiAddress(String taxiAddress) {
		TaxiAddress = taxiAddress;
	}

	public double getLon() {
		return Lon;
	}

	public void setLon(double lon) {
		Lon = lon;
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		Lat = lat;
	}

	public String getRequestTime() {
		return RequestTime;
	}

	public void setRequestTime(String requestTime) {
		RequestTime = requestTime;
	}

	public int getTaxiType() {
		return TaxiType;
	}

	public void setTaxiType(int taxiType) {
		TaxiType = taxiType;
	}

	public static UserCallDriver parse(String json) {
		return new Gson().fromJson(json, UserCallDriver.class);
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
