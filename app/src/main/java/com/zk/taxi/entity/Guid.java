package com.zk.taxi.entity;

public class Guid {

	private String OrderGuid;// 叫车订单唯一
	private String PaySerialNO;// 支付唯一流水号

	public String getOrderGuid() {
		return OrderGuid;
	}

	public void setOrderGuid(String orderGuid) {
		OrderGuid = orderGuid;
	}

	public String getPaySerialNO() {
		return PaySerialNO;
	}

	public void setPaySerialNO(String paySerialNO) {
		PaySerialNO = paySerialNO;
	}

}
