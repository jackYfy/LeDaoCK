package com.zk.taxi.AmapHelper;

import android.content.Context;
import android.graphics.Color;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.zk.taxi.tool.ToastUtils;

/**
 * Created by 94943 on 2017/10/19.
 */

public class MapLocationUtils implements LocationSource, AMapLocationListener {
    private LocationChangListener listener;
    private MyLocationStyle myLocationStyle;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private Context mContext;
    private MyLocationStyle getMyLocationStyle() {
        if (myLocationStyle == null) {
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.interval(2000);
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));//范围圆圈边框色
            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//范围圆圈填充色
        }
        return myLocationStyle;
    }
//有定位图标
    public void startLocation(AMap aMap, Context context) {
        this.mContext = context;
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.028762,113.122717),14));
        aMap.setMyLocationStyle(getMyLocationStyle());
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }
    //自定义定位图标
    public void startLocation(AMap aMap, Context context,MyLocationStyle myLocationStyle) {
        this.mContext = context;
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.028762,113.122717),14));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);// 不显示定位蓝点，设置为true表示启动显示点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }
    public void setLocationListener(LocationChangListener listener) {
        this.listener = listener;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(mContext);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式AMapLocationMode.Hight_Accuracy
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(2000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }


    @Override
    public void deactivate() {

    }

    public void onDestroy() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    public void onResume() {
        if (mlocationClient != null) {
            mlocationClient.startLocation();
        }
    }

    public void onPause() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();//停止定位
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                listener.Location(aMapLocation);
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            }
        } else {
            ToastUtils.show("定位失败，请检查是否开启定位权限！");
        }
    }

    public interface LocationChangListener {
        public void Location(AMapLocation location);
    }
}
