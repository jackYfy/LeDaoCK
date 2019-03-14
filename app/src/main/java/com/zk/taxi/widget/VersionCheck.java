package com.zk.taxi.widget;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.zk.taxi.Config;
import com.zk.taxi.Interface;
import com.zk.taxi.entity.AppVersionEntity;
import com.zk.taxi.http.HttpResponse;
import com.zk.taxi.http.HttpUtils;
import com.zk.taxi.http.Response;
import com.zk.taxi.tool.FileUtils;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.ui.DownLoadService;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class VersionCheck {

    private Activity act = null;
    private boolean isShowHint = false;
    public File appFile;
    public String appPath;
    //文件地址
    public String filePath;

    public VersionCheck(Activity act, boolean isShowHint) {
        this.act = act;
        this.isShowHint = isShowHint;
        appPath= FileUtils.getSdcardDir()+"/"+act.getPackageName()+"/APK";
        FileUtils.createIfNotExist(appPath);
        filePath=appPath+"/"+ "LeDao.apk";
        appFile=new File(filePath);
        check();
    }
    private void check() {
        JSONObject json=new JSONObject();
        json.put("AppID", Config.APP_ID);
        json.put("ClientSoftWareVerNo",Methods.getPackageInfo(act).versionName);
        json.put("ClientSoftWareName","");
        json.put("UpdateInfo","");
        json.put("FileName","");
        HttpUtils.getTaxi(Config.URL_SERVER, Interface.NS_GETNEWVERSIONFORCURRENTVERSION, json.toJSONString(), new HttpResponse() {
            @Override
            public void result(int i, String content) {
                List<AppVersionEntity> list = Response.analysis(act, content, AppVersionEntity.class);
                if (list != null && list.size() > 0) {
                    judgment(list.get(0), act);
                } else {
                    if (isShowHint)
                        ToastUtils.show("已是最新版本");
                }
            }
        });
    }

    private void judgment(final AppVersionEntity version, final Activity act) {
        if (version == null || TextUtils.isEmpty(version.getClientSoftWareVerNo())) {
            if (isShowHint)
                ToastUtils.show("已是最新版本");
            return;
        }
        double serverVersion = Double.parseDouble(version.getClientSoftWareVerNo().replace(".", ""));
        double clientVersion = Double.parseDouble(Methods.getPackageInfo(act).versionName.replace(".", ""));
        if (clientVersion < serverVersion) {
            ColorDialog dialog = new ColorDialog(act);
            dialog.setColor("#ffffff");//#8ECB54
            dialog.setAnimationEnable(true);
            dialog.setTitle("更新提示");
            dialog.setContentText("发现新版本：V" + version.getClientSoftWareVerNo() + "\n" + version.getUpdateInfo());
            dialog.setPositiveListener("取消", new ColorDialog.OnPositiveListener() {
                @Override
                public void onClick(ColorDialog dialog) {
                    dialog.dismiss();
                }
            }).setNegativeListener("更新", new ColorDialog.OnNegativeListener() {
                @Override
                public void onClick(ColorDialog dialog) {
                    Intent service = new Intent(act,DownLoadService.class);
                    service.putExtra("downloadurl", version.getFileName());
                    ToastUtils.show("正在后台下载中，可在通知栏查看下载进度");
                    Log.i("service", "启动服务");
                    act.startService(service);
                    dialog.dismiss();
                }
            }).show();


        }
    }
}
