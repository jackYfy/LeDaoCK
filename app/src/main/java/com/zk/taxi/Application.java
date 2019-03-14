package com.zk.taxi;

import android.os.Build;
import android.os.StrictMode;

import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zk.taxi.tool.ImageLoadUtils;

public class Application extends android.app.Application{

    private static Application application = null;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //初始化imageloader
        ImageLoader.getInstance().init(ImageLoadUtils.getConfiguration(this));
        SpeechUtility.createUtility(getApplicationContext(), "appid=59704e9f");//
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    public static Application getInstance() {
        if (application == null) {
            application = new Application();
        }
        return application;
    }
}
