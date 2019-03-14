package com.zk.taxi.tool;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;


public class GeocodeUtils {
    private GeocodeSearch geocodeSearch;
    private Handler handler=null;
    public static final int START_LATLNG=10001;
    public static final int END_LATLNG=10002;
    public GeocodeUtils(Activity act, Handler handler){
        this.handler=handler;
        geocodeSearch=new GeocodeSearch(act);
    }
    /**
     * 坐标转换地址
     * @param latLng
     */
    public void RegeocodeSearchedGPS(LatLng latLng, int ReCodeType){
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude,latLng.longitude), 50, geocodeSearch.GPS);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearchListener(ReCodeType));
        geocodeSearch.getFromLocationAsyn(query);
    }
    /**
     * 坐标转换地址
     * @param latLng
     */
    public void RegeocodeSearchedAMAP(LatLng latLng, int ReCodeType){
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude,latLng.longitude), 100, geocodeSearch.AMAP);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearchListener(ReCodeType));
        geocodeSearch.getFromLocationAsyn(query);
    }
    /**
     * 地址转换坐标
     * @param address
     */
    public void GeocodeSearched(String address,String CityName,int CodeType){
        GeocodeQuery query=new GeocodeQuery(address,CityName);
        try {
            geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearchListener(CodeType));
            geocodeSearch.getFromLocationName(query);
        } catch (AMapException e) {
            e.printStackTrace();
        }

    }
     class GeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener{
            int codeType;
        GeocodeSearchListener(int codeType){
            this.codeType=codeType;
        }
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            if(regeocodeResult!=null) {
                Log.e("GeocodeUtils",regeocodeResult.getRegeocodeAddress().toString());
                Message msg = new Message();
                msg.what = codeType;
                msg.obj = regeocodeResult;
                handler.sendMessage(msg);
            }else{
                ToastUtils.show("逆地理编码无返回结果");
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            if(geocodeResult!=null) {
                Message msg = new Message();
                msg.what = codeType;
                msg.obj = geocodeResult;
                handler.sendMessage(msg);
            }
        }
}
}
