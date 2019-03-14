package com.zk.taxi.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.zk.taxi.R;
import com.zk.taxi.entity.LocationCityEntity;
import com.zk.taxi.methodDao.UserPost;
import com.zk.taxi.widget.Methods;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity implements AMapLocationListener {

    //宏定义，表示是什么消息
    public static final int REFRESH = 0x01;
    //用来显示变化的值
    private int nValue = 0;
    //定时器设置
    Timer timer = new Timer();
    //声明AMapLocationClient类对象���
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象�
    public AMapLocationClientOption mLocationOption = null;
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器����
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private String city=null;//城市名
    private List<LocationCityEntity> cityList = new ArrayList<LocationCityEntity>();//判断接口地址
    public static String URL_CAR=null;
    public static String URL_ZJ=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        nValue = 0;
//        //开启定时器
//        timer.schedule(task,1000,1000);
        initloc();
        UserPost.getAllInterface(this,handler);
    }

    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }
    private void initloc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听����
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表�
                if (isFirstLoc) {
                    //进入页面首次定位调用接口
                    city=aMapLocation.getCity();
                    isFirstLoc = false;
                }

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            nValue++;
            Message message = new Message();
            message.what = REFRESH;
            handler.sendMessage(message);
        }
    };
    //处理消息
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case REFRESH:
                    if (nValue==3){
                        Methods.toBase(WelcomeActivity.this,MainActivity.class);
                    }
                    break;
                case UserPost.INTERFACE:
                    List<LocationCityEntity> urlList=(List<LocationCityEntity>) msg.obj;
                    if (urlList != null && urlList.size() > 0) {
                        Methods.toBase(WelcomeActivity.this,MainActivity.class);
                    }
                    break;
            }
        }
    };
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
