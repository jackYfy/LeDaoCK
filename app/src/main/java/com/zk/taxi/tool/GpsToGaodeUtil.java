package com.zk.taxi.tool;

import android.app.Activity;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;

/**
 * Created by Administrator on 2017/7/14.
 */

public class GpsToGaodeUtil {
    private CoordinateConverter converter;
    public GpsToGaodeUtil(Activity context){
        converter=new CoordinateConverter(context);
    }
    //GPS坐标转高德
    public LatLng GpsToGaode(LatLng fromLatlng){
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(fromLatlng);
        return converter.convert();
    }
//百度坐标转高德
    public LatLng BaiduToGaode(LatLng fromLatlng){
        // CoordType. 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.BAIDU);
        // fromLatlng待转换坐标点 LatLng类型
        converter.coord(fromLatlng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return  desLatLng;
    }
}
