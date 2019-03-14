package com.zk.taxi.entity;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.zk.taxi.tool.MD5;

import java.io.Serializable;

public class TripHistoryEntity implements Serializable {
	public static final String TAG = MD5.getMessageDigest(TripHistoryEntity.class.getSimpleName());
	private static final long serialVersionUID = -4581261299876759808L;
	public String Guid; // 订单GUID
	public String TaxiGuid;//车辆guid
	public String CustomerTel; // 用户手机号
	public String CustomerName; // 用户姓名
	public String DriverStar;
	public String DriverName;
	public int Sex; // 性别
	public String TaxiCard; // 车牌号码
	public String TaxiSim; // 车载电话号码
	public String TaxiResult;// 抢单结果 0失败、1成功
	public String TaxiTime;// 召车时间
	public String TaxiAddress;// 用户乘车地址
	public String TaxiType;// 2普通出租车 4约租车 7快捷出租车
	public String TaxiDestination;// 目的地
	public String CreateTime;// 订单创建时间
	public String orderevaluationid;// 订单评价id
	public String evaluationlevel;// 评价等级
	public String evaluationcontent;// 评价内容
	public String evaluationlcreatetime;// 评价时间
	public double Lon;// 经度
	public double Lat;// 纬度
	public double DLon;// 目的地纬度
	public double DLat;// 目的地经度
	public String UpTime;// 上车时间
	public String DownTime;// 下车时间

	public int PayStatus; //支付状态 1已支付，0未支付
	public String TabId;//评价标签id （多个用,分隔）
	public String TabName;//评价标签名称（多个用,分隔）
	public String Payment; //支付方式
	public String OrderId; //订单id
	public String PayAmount; //支付金额
	public String OrderStatus;//订单状态，0.车机抢单成功，1.App抢单成功，2.乘客已上车，3.到达目的地，4.订单取消，5订单已完成
	public String Status;// 0.等待接单,1.用户取消, 2.司机取消, 3.系统取消,4.接单成功,5.接单失败(1,2,3都是订单取消状态)

	public TripHistoryEntity() {
	}
	public TripHistoryEntity(String TaxiType, String TaxiTime, String TaxiAddress,
							 String TaxiDestination,String evaluationcontent,int PayStatus)
	{
		this.TaxiType = TaxiType;
		this.TaxiTime = TaxiTime;
		this.TaxiAddress = TaxiAddress;
		this.TaxiDestination = TaxiDestination;
		this.evaluationcontent = evaluationcontent;
		this.PayStatus = PayStatus;
	}
	public int getPayStatus() {
		return PayStatus;
	}

	public void setPayStatus(int payStatus) {
		PayStatus = payStatus;
	}

	public String getTabId() {
		return TabId;
	}

	public void setTabId(String tabId) {
		TabId = tabId;
	}

	public String getTabName() {
		return TabName;
	}

	public void setTabName(String tabName) {
		TabName = tabName;
	}
	public String getUpTime() {
		return UpTime;
	}

	public void setUpTime(String upTime) {
		UpTime = upTime;
	}

	public String getDownTime() {
		return DownTime;
	}

	public void setDownTime(String downTime) {
		DownTime = downTime;
	}

	public String getDriverStar() {
		return DriverStar;
	}

	public void setDriverStar(String driverStar) {
		DriverStar = driverStar;
	}

	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

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

	public String getTaxiAddress() {
		return TaxiAddress;
	}

	public void setTaxiAddress(String taxiAddress) {
		TaxiAddress = taxiAddress;
	}

	public String getTaxiType() {
		int type = Integer.parseInt(TaxiType);
		String taxiStr = "出租车";
		switch (type) {
			case 2:
				taxiStr = "普通出租车";
				break;
			case 4:
				taxiStr = "约租车";
				break;
			case 7:
				taxiStr = "快捷出租车";
				break;
		}
		return taxiStr;
	}
	public int getTaxiTypeST(){
		int TaxiTypeIT = Integer.parseInt(TaxiType);
		return TaxiTypeIT;
	}

	public void setTaxiType(String taxiType) {
		TaxiType = taxiType;
	}

	public String getTaxiDestination() {
		return TaxiDestination;
	}

	public void setTaxiDestination(String taxiDestination) {
		TaxiDestination = taxiDestination;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getOrderevaluationid() {
		return orderevaluationid;
	}

	public void setOrderevaluationid(String orderevaluationid) {
		this.orderevaluationid = orderevaluationid;
	}

	public String getEvaluationlevel() {
		return evaluationlevel;
	}

	public void setEvaluationlevel(String evaluationlevel) {
		this.evaluationlevel = evaluationlevel;
	}

	public String getEvaluationcontent() {
		return TextUtils.isEmpty(evaluationcontent) ? "" : evaluationcontent;
	}

	public void setEvaluationcontent(String evaluationcontent) {
		this.evaluationcontent = evaluationcontent;
	}

	public String getEvaluationlcreatetime() {
		return evaluationlcreatetime;
	}

	public void setEvaluationlcreatetime(String evaluationlcreatetime) {
		this.evaluationlcreatetime = evaluationlcreatetime;
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

	public String getPayment() {
		return Payment;
	}

	public void setPayment(String payment) {
		Payment = payment;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getPayAmount() {
		return PayAmount;
	}

	public void setPayAmount(String payAmount) {
		PayAmount = payAmount;
	}

	public String getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}

	public String getTaxiGuid() {
		return TaxiGuid;
	}

	public void setTaxiGuid(String taxiGuid) {
		TaxiGuid = taxiGuid;
	}
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public static TripHistoryEntity parse(String json) {
		return new Gson().fromJson(json, TripHistoryEntity.class);
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
