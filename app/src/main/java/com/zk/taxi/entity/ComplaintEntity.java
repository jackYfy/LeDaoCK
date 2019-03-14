package com.zk.taxi.entity;

import com.google.gson.Gson;
import com.zk.taxi.tool.MD5;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/10 0010.
 */

public class ComplaintEntity implements Serializable {
    public static final String TAG = MD5.getMessageDigest(ComplaintEntity.class.getSimpleName());
    public String UserID;
    public String id;
    public String Registrant;
    public String RegisterTime;
    public String CallTime;
    public String Complainant;
    public String ComplainantPhone;
    public String TaxiCard;
    public String DriverNumber;
    public String DriverName;
    public String CompanyGuid;
    public String CompanyName;
    public String ComplaintType;
    public String Complaint;
    public String TransferCompanyTime;
    public String TransferPerson;
    public String CompanyReplyTime;
    public String CompanyNote;
    public String ReturnTime;
    public String ReturnPerson;
    public String Returning;
    public String Result;
    public String Remark;
    public String Status;
    public String Modifier;
    public String ModifyTime;
    public String TaskGuid;
    public String TaxiAddress;
    public String TaxiDestination;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistrant() {
        return Registrant;
    }

    public void setRegistrant(String registrant) {
        Registrant = registrant;
    }

    public String getRegisterTime() {
        return RegisterTime;
    }

    public void setRegisterTime(String registerTime) {
        RegisterTime = registerTime;
    }

    public String getCallTime() {
        return CallTime;
    }

    public void setCallTime(String callTime) {
        CallTime = callTime;
    }

    public String getComplainant() {
        return Complainant;
    }

    public void setComplainant(String complainant) {
        Complainant = complainant;
    }

    public String getComplainantPhone() {
        return ComplainantPhone;
    }

    public void setComplainantPhone(String complainantPhone) {
        ComplainantPhone = complainantPhone;
    }

    public String getTaxiCard() {
        return TaxiCard;
    }

    public void setTaxiCard(String taxiCard) {
        TaxiCard = taxiCard;
    }

    public String getDriverNumber() {
        return DriverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        DriverNumber = driverNumber;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getCompanyGuid() {
        return CompanyGuid;
    }

    public void setCompanyGuid(String companyGuid) {
        CompanyGuid = companyGuid;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getComplaintType() {
        return ComplaintType;
    }

    public void setComplaintType(String complaintType) {
        ComplaintType = complaintType;
    }

    public String getComplaint() {
        return Complaint;
    }

    public void setComplaint(String complaint) {
        Complaint = complaint;
    }

    public String getTransferCompanyTime() {
        return TransferCompanyTime;
    }

    public void setTransferCompanyTime(String transferCompanyTime) {
        TransferCompanyTime = transferCompanyTime;
    }

    public String getTransferPerson() {
        return TransferPerson;
    }

    public void setTransferPerson(String transferPerson) {
        TransferPerson = transferPerson;
    }

    public String getCompanyReplyTime() {
        return CompanyReplyTime;
    }

    public void setCompanyReplyTime(String companyReplyTime) {
        CompanyReplyTime = companyReplyTime;
    }

    public String getCompanyNote() {
        return CompanyNote;
    }

    public void setCompanyNote(String companyNote) {
        CompanyNote = companyNote;
    }

    public String getReturnTime() {
        return ReturnTime;
    }

    public void setReturnTime(String returnTime) {
        ReturnTime = returnTime;
    }

    public String getReturnPerson() {
        return ReturnPerson;
    }

    public void setReturnPerson(String returnPerson) {
        ReturnPerson = returnPerson;
    }

    public String getReturning() {
        return Returning;
    }

    public void setReturning(String returning) {
        Returning = returning;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getModifier() {
        return Modifier;
    }

    public void setModifier(String modifier) {
        Modifier = modifier;
    }

    public String getModifyTime() {
        return ModifyTime;
    }

    public void setModifyTime(String modifyTime) {
        ModifyTime = modifyTime;
    }

    public String getTaskGuid() {
        return TaskGuid;
    }

    public void setTaskGuid(String taskGuid) {
        TaskGuid = taskGuid;
    }

    public String getTaxiAddress() {
        return TaxiAddress;
    }

    public void setTaxiAddress(String taxiAddress) {
        TaxiAddress = taxiAddress;
    }

    public String getTaxiDestination() {
        return TaxiDestination;
    }

    public void setTaxiDestination(String taxiDestination) {
        TaxiDestination = taxiDestination;
    }


    public static ComplaintEntity parse(String json) {
        return new Gson().fromJson(json, ComplaintEntity.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
