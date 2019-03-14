package com.zk.taxi.AmapHelper;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.amap.api.maps.AMap;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.text.DecimalFormat;


/**
 * Created by 94943 on 2018/1/3.
 */

public class AMapRouteUtils implements RouteSearch.OnRouteSearchListener {
    private AMap aMap;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private DriveRouteResult driveResult;// 驾车模式查询结果
    private NaviLatLng startLL;
    private NaviLatLng endLL;
    private RouteResultListener mResultListener;
    public AMapRouteUtils(AMap aMap,Context context){
        this.aMap=aMap;
        this.mContext=context;
        mRouteSearch=new RouteSearch(context);
    }
    /**
     * 开始搜索路径规划
     */
    public void searchRoute(LatLonPoint startPoint,LatLonPoint endPoint) {
        mRouteSearch.setRouteSearchListener(this);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }
    public void setResultListener(RouteResultListener listener){
        this.mResultListener=listener;
    }
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if(i==1000){
            aMap.clear();
            if (driveRouteResult != null && driveRouteResult.getPaths() != null && driveRouteResult.getPaths().size() > 0) {
                driveResult = driveRouteResult;
                DrivePath drivePath = driveRouteResult.getPaths().get(0);
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(mContext, aMap, drivePath, driveRouteResult.getStartPos(), driveRouteResult.getTargetPos());
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
//                drivingRouteOverlay.zoomToSpan();
                startLL = new NaviLatLng(driveRouteResult.getStartPos().getLatitude(), driveRouteResult.getStartPos().getLongitude());
                endLL = new NaviLatLng(driveRouteResult.getTargetPos().getLatitude(), driveRouteResult.getTargetPos().getLongitude());
                DecimalFormat df = new DecimalFormat("0.0");
                float dis = (float)drivePath.getDistance()/ 1000;
                int time = (int) (drivePath.getDuration()/ 60);
                float cost = driveRouteResult.getTaxiCost();//在规划的路线里坐出租车的大概花费
                if(mResultListener!=null){
                    mResultListener.RouteResult(df.format(dis),time,cost);
                }
            }
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    public interface RouteResultListener{
        public void RouteResult(String distence, int Minute, float money);
    }
}
