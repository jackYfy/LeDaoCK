package com.zk.taxi.entity;

import com.google.gson.Gson;
import com.zk.taxi.tool.MD5;

import java.io.Serializable;

public class OrderInfo implements Serializable {

	public static final String TAG = MD5.getMessageDigest(OrderInfo.class.getSimpleName());
	/**
	 *
	 */
	private static final long serialVersionUID = 8740273565701626396L;
	public String Guid; // 订单GUID
	public String CustomerTel;// 用户手机号
	public String CustomerName; // 用户姓名
	public int Sex; // 性别
	public String TaxiCard; // 车牌号码
	public String TaxiSim; // 车载电话号码
	public String TaxiResult;// 抢单结果 0 失败 1成功
	public String TaxiTime;// 召车时间
	public String RequestTime;// 约车时间
	public String DriverName;// 司机姓名
	public String Destination;// 目的地
	public double ELon;// 目的地经纬度
	public double ELat;// 目的地经纬度
	public String TaxiAddress;// 乘车地址
	public double SLon;// 乘车地址经纬度
	public double SLat;// 乘车地址经纬度
	public String TaxiGuid;// 车辆id
	public Long OrderId;//订单号

	public String getGuid() {
		return Guid;
	}

	public void setGuid(String guid) {
		Guid = guid;
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

	public String getTaxiCard() {
		return TaxiCard;
	}

	public void setTaxiCard(String taxiCard) {
		TaxiCard = taxiCard;
	}

	public String getTaxiSim() {
		return TaxiSim;
	}

	public void setTaxiSim(String taxiSim) {
		TaxiSim = taxiSim;
	}

	public String getTaxiResult() {
		return TaxiResult;
	}

	public void setTaxiResult(String taxiResult) {
		TaxiResult = taxiResult;
	}

	public String getTaxiTime() {
		return TaxiTime;
	}

	public void setTaxiTime(String taxiTime) {
		TaxiTime = taxiTime;
	}

	public String getRequestTime() {
		return RequestTime;
	}

	public void setRequestTime(String requestTime) {
		RequestTime = requestTime;
	}

	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	public String getDestination() {
		return Destination;
	}

	public void setDestination(String destination) {
		Destination = destination;
	}

	public String getTaxiAddress() {
		return TaxiAddress;
	}

	public void setTaxiAddress(String taxiAddress) {
		TaxiAddress = taxiAddress;
	}

	public double getELon() {
		return ELon;
	}

	public void setELon(double eLon) {
		ELon = eLon;
	}

	public double getELat() {
		return ELat;
	}

	public void setELat(double eLat) {
		ELat = eLat;
	}

	public double getSLon() {
		return SLon;
	}

	public void setSLon(double sLon) {
		SLon = sLon;
	}

	public double getSLat() {
		return SLat;
	}

	public void setSLat(double sLat) {
		SLat = sLat;
	}

	public String getTaxiGuid() {
		return TaxiGuid;
	}

	public void setTaxiGuid(String taxiGuid) {
		TaxiGuid = taxiGuid;
	}
	public Long getOrderId() {
		return OrderId;
	}

	public void setOrderId(Long orderId) {
		OrderId = orderId;
	}

	public static OrderInfo parse(String json) {
		return new Gson().fromJson(json, OrderInfo.class);
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
