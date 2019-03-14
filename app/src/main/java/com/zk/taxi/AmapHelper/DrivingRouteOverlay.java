package com.zk.taxi.AmapHelper;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveStep;
import com.zk.taxi.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */
public class DrivingRouteOverlay extends RouteOverLay {
    private DrivePath drivePath;

    public DrivingRouteOverlay(Context context, AMap amap, DrivePath path, LatLonPoint start, LatLonPoint end) {
        super(context);
        this.mAMap = amap;
        this.drivePath = path;
        startPoint = AMapServicesUtil.convertToLatLng(start);
        endPoint = AMapServicesUtil.convertToLatLng(end);
    }

    /**
     * 绘制节点和线�?
     */
    public void addToMap() {
        List<DriveStep> drivePaths = drivePath.getSteps();
        for (int i = 0; i < drivePaths.size(); i++) {
            DriveStep driveStep = drivePaths.get(i);
            LatLng latLng = AMapServicesUtil.convertToLatLng(driveStep.getPolyline().get(0));
            if (i < drivePaths.size() - 1) {
                if (i == 0) {
                    Polyline oneLine = mAMap.addPolyline(new PolylineOptions()
                            .add(startPoint, latLng).color(getDriveColor())
                            .geodesic(true)
                            .width(getDriverLineWidth()));// 把起始点和第�?��步行的起点连接起�?
                    allPolyLines.add(oneLine);
                }
                LatLng latLngEnd = AMapServicesUtil.convertToLatLng(driveStep
                        .getPolyline().get(driveStep.getPolyline().size() - 1));
                LatLng latLngStart = AMapServicesUtil
                        .convertToLatLng(drivePaths.get(i + 1).getPolyline().get(0));
                if (!(latLngEnd.equals(latLngStart))) {
                    Polyline breakLine = mAMap
                            .addPolyline((new PolylineOptions())
                                    .add(latLngEnd, latLngStart)
                                    .color(getDriveColor())
                                    .geodesic(true)
                                    .width(getDriverLineWidth()));// 把前�?��步行段的终点和后�?��步行段的起点连接起来
                    allPolyLines.add(breakLine);
                }
            } else {
                LatLng latLng1 = AMapServicesUtil.convertToLatLng(driveStep
                        .getPolyline().get(driveStep.getPolyline().size() - 1));
                Polyline endLine = mAMap.addPolyline(new PolylineOptions()
                        .add(latLng1, endPoint).color(getDriveColor())
                        .width(getDriverLineWidth()));// 把最终点和最后一个步行的终点连接起来
                allPolyLines.add(endLine);
            }
//            //设置路径中转弯处的Marker图标，若不添加可不设置
//            Marker driveMarker = mAMap.addMarker(new MarkerOptions()
//                    .position(latLng)
//                    .snippet(driveStep.getInstruction()).anchor(0.5f, 0.5f)
//                    .icon(getDriveBitmapDescriptor()));
//            stationMarkers.add(driveMarker);

            Polyline driveLine = mAMap.addPolyline((new PolylineOptions())
                    .addAll(AMapServicesUtil.convertArrList(driveStep.getPolyline()))
                    .setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.map_alr))
//                    .color(getDriveColor())
                    .geodesic(true)
                    .width(getDriverLineWidth()));
            allPolyLines.add(driveLine);
        }
        addStartAndEndMarker();
    }

    //设置路径显示的宽度
    protected float getDriverLineWidth() {
        return 20;
    }
}
