package com.zk.taxi.entity;

import java.io.Serializable;

public class DriverGPS implements Serializable {

	public static final String TAG = DriverGPS.class.getSimpleName();
	/**
	 *
	 */
	private static final long serialVersionUID = 2504454281240231495L;
	private String DeviceIdentity;
	private String LicenceNumber;
	private String DriverGuid;
	private String DriverName;
	private String Longitude;
	private String Latitude;
	private String FormatLon;
	private String FormatLat;
	private String FormatTime;
	private String Time;
	private String Speed;
	private String Direction;
	private String Elevation;
	private String GPSNumber;
	private String Status;
	private String AlarmStatus;
	private String Description;
	private String TotalMile;
	private String ModifyTime;
	private String IsOverSpeed;
	private String IsOutside;
	private String IsEmergency;
	private String IsCrossOver;

	public String getDeviceIdentity() {
		return DeviceIdentity;
	}

	public void setDeviceIdentity(String deviceIdentity) {
		DeviceIdentity = deviceIdentity;
	}

	public String getLicenceNumber() {
		return LicenceNumber;
	}

	public void setLicenceNumber(String licenceNumber) {
		LicenceNumber = licenceNumber;
	}

	public String getDriverGuid() {
		return DriverGuid;
	}

	public void setDriverGuid(String driverGuid) {
		DriverGuid = driverGuid;
	}

	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getFormatLon() {
		return FormatLon;
	}

	public void setFormatLon(String formatLon) {
		FormatLon = formatLon;
	}

	public String getFormatLat() {
		return FormatLat;
	}

	public void setFormatLat(String formatLat) {
		FormatLat = formatLat;
	}

	public String getFormatTime() {
		return FormatTime;
	}

	public void setFormatTime(String formatTime) {
		FormatTime = formatTime;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getSpeed() {
		return Speed;
	}

	public void setSpeed(String speed) {
		Speed = speed;
	}

	public String getDirection() {
		return Direction;
	}

	public void setDirection(String direction) {
		Direction = direction;
	}

	public String getElevation() {
		return Elevation;
	}

	public void setElevation(String elevation) {
		Elevation = elevation;
	}

	public String getGPSNumber() {
		return GPSNumber;
	}

	public void setGPSNumber(String gPSNumber) {
		GPSNumber = gPSNumber;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getAlarmStatus() {
		return AlarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		AlarmStatus = alarmStatus;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getTotalMile() {
		return TotalMile;
	}

	public void setTotalMile(String totalMile) {
		TotalMile = totalMile;
	}

	public String getModifyTime() {
		return ModifyTime;
	}

	public void setModifyTime(String modifyTime) {
		ModifyTime = modifyTime;
	}

	public String getIsOverSpeed() {
		return IsOverSpeed;
	}

	public void setIsOverSpeed(String isOverSpeed) {
		IsOverSpeed = isOverSpeed;
	}

	public String getIsOutside() {
		return IsOutside;
	}

	public void setIsOutside(String isOutside) {
		IsOutside = isOutside;
	}

	public String getIsEmergency() {
		return IsEmergency;
	}

	public void setIsEmergency(String isEmergency) {
		IsEmergency = isEmergency;
	}

	public String getIsCrossOver() {
		return IsCrossOver;
	}

	public void setIsCrossOver(String isCrossOver) {
		IsCrossOver = isCrossOver;
	}
}
