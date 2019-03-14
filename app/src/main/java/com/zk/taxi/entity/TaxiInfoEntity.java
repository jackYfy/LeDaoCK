package com.zk.taxi.entity;

import java.io.Serializable;

public class TaxiInfoEntity implements Serializable {
	public static final String TAG = TaxiInfoEntity.class.getSimpleName();
	private static final long serialVersionUID = 2296969728891943666L;
	public String TaxiCard;// 车牌号码
	public String TaxiLevel;// 车辆等级
	public double Lon;// 经度
	public double Lat;// 纬度
	public String TaxiSIM;//车牌
	public String CompanyName;//出租车公司
	public String DriverName;//司机名
	public String DriverStar;//星级
	public String TaxiGuid;//唯一id
	public int Direction;//方向

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		Lat = lat;
	}

	public String getTaxiCard() {
		return TaxiCard;
	}

	public void setTaxiCard(String taxiCard) {
		TaxiCard = taxiCard;
	}

	public String getTaxiLevel() {
		return TaxiLevel;
	}

	public void setTaxiLevel(String taxiLevel) {
		TaxiLevel = taxiLevel;
	}

	public double getLon() {
		return Lon;
	}

	public void setLon(double lon) {
		Lon = lon;
	}

	public String getTaxiSIM() {
		return TaxiSIM;
	}

	public void setTaxiSIM(String taxiSIM) {
		TaxiSIM = taxiSIM;
	}

	public String getCompanyName() {
		return CompanyName;
	}

	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}

	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	public String getDriverStar() {
		return DriverStar;
	}

	public void setDriverStar(String driverStar) {
		DriverStar = driverStar;
	}

	public String getTaxiGuid() {
		return TaxiGuid;
	}

	public void setTaxiGuid(String taxiGuid) {
		TaxiGuid = taxiGuid;
	}

	public int getDirection() {
		return Direction;
	}

	public void setDirection(int direction) {
		Direction = direction;
	}

}
