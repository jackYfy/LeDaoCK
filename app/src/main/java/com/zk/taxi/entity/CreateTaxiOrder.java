package com.zk.taxi.entity;

import com.google.gson.Gson;

import java.io.Serializable;

public class CreateTaxiOrder implements Serializable {
	private static final long serialVersionUID = 7786280669373875618L;
	public String guid;// 订单guid信息
	public String CustomerTel;// 用户手机号
	public String CustomerName;// 用户姓名
	public int Sex;// 用户性别 0/1 0—男 1—女
	public String TaxiAddress;// 用户乘车地址
	public String RequestTime;// 要车时间
	public int TaxiType;// 的士类型 2普通出租车app召车，7快捷出租车app召车
	public double Lon;// 经度
	public double Lat;// 纬度
	public String CreateTime;// 订单创建时间
	public String Destination;// 目的地
	public int FlowType;// 1
	public String Direction;//
	public int status;// 0
	public double DLon;// 目的地纬度
	public double DLat;// 目的地经度

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

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

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getDestination() {
		return Destination;
	}

	public void setDestination(String destination) {
		Destination = destination;
	}

	public int getFlowType() {
		return FlowType;
	}

	public void setFlowType(int flowType) {
		FlowType = flowType;
	}

	public String getDirection() {
		return Direction;
	}

	public void setDirection(String direction) {
		Direction = direction;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getDLon() {
		return DLon;
	}

	public void setDLon(double dLon) {
		DLon = dLon;
	}

	public double getDLat() {
		return DLat;
	}

	public void setDLat(double dLat) {
		DLat = dLat;
	}

	public static CreateTaxiOrder parse(String json) {
		return new Gson().fromJson(json, CreateTaxiOrder.class);
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
