package com.zk.taxi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/19.
 */

public class AppVersionEntity implements Serializable {
    private static final long serialVersionUID = 3313625317055212982L;

    private String CommonClientSoftWareID;//为空
    private String APPID;//应用ID
    private String ClientSoftWareVerNo;//当前软件版本号
    private String ClientSoftWareName;//软件名称,为空
    private String UpdateInfo;//新版本信息，为空
    private String FileName;//下载地址, 为空

    public String getCommonClientSoftWareID() {
        return CommonClientSoftWareID;
    }

    public void setCommonClientSoftWareID(String commonClientSoftWareID) {
        CommonClientSoftWareID = commonClientSoftWareID;
    }

    public String getAPPID() {
        return APPID;
    }

    public void setAPPID(String aPPID) {
        APPID = aPPID;
    }

    public String getClientSoftWareVerNo() {
        return ClientSoftWareVerNo;
    }

    public void setClientSoftWareVerNo(String clientSoftWareVerNo) {
        ClientSoftWareVerNo = clientSoftWareVerNo;
    }

    public String getClientSoftWareName() {
        return ClientSoftWareName;
    }

    public void setClientSoftWareName(String clientSoftWareName) {
        ClientSoftWareName = clientSoftWareName;
    }

    public String getUpdateInfo() {
        return UpdateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        UpdateInfo = updateInfo;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}
