package com.zk.taxi.ui.CallCar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.zk.taxi.AmapHelper.AMapRouteUtils;
import com.zk.taxi.AmapHelper.MapLocationUtils;
import com.zk.taxi.BaseActivity;
import com.zk.taxi.Config;
import com.zk.taxi.Interface;
import com.zk.taxi.R;
import com.zk.taxi.entity.DriverGPS;
import com.zk.taxi.entity.OrderCarStatusEntity;
import com.zk.taxi.entity.OrderInfo;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.http.HttpResponse;
import com.zk.taxi.http.HttpUtils;
import com.zk.taxi.http.Response;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.DateUtils;
import com.zk.taxi.tool.GpsToGaodeUtil;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.ColorDialog;
import com.zk.taxi.widget.LoadingDialog;
import com.zk.taxi.widget.Methods;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;

public class WaitDriverActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener, AMapRouteUtils.RouteResultListener, RouteSearch.OnRouteSearchListener {

    @Bind(R.id.call_phone)
    LinearLayout call_phone;
    @Bind(R.id.wait_gotrip)
    AppCompatTextView wait_gotrip;
    @Bind(R.id.driver_name)
    AppCompatTextView driver_name;
    @Bind(R.id.taxi_number)
    AppCompatTextView taxi_number;
    @Bind(R.id.alldistance)
    AppCompatTextView alldistance;
    @Bind(R.id.allmoney)
    AppCompatTextView allmoney;
    @Bind(R.id.alltime)
    AppCompatTextView alltime;

    private MapView mapView;//地图控件
    private AMap aMap;//地图控制器对象�
    private Bundle savedInstanceState;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private LatLng mLocLatLng = null;// 当前经纬度坐标�
    private static LoadingDialog loadingDialog;
    private MapLocationUtils mapLocationUtils;
    private LatLonPoint meLatpoint = null;//起点
    private LatLonPoint driverLatpoint = null;//司机当前位置
    private LatLonPoint endPoint=null;//目的地
    private LatLng startlang=null;
    private AMapRouteUtils mapRouteUtils;
    private int typeByCar;// 出租车类型
    private String guid = null;// 订单唯一标识
    private String taxiguid=null;//车辆guid
    private String startAddress = null;// 开始地址
    private String endAddress = null;// 结束地址
    private OrderInfo orderInfo = null;// 订单
    private TripHistoryEntity stateEntity = null;// 订单状态对应的数据
    private RouteSearch routeSearch;
    private double startstlatlng;//起点纬度
    private double startedlatlng;//起点经度
    private double endstlatlng;//终点纬度
    private double endedlatlng;//终点经度

    @Override
    public int bindLayout() {
        return R.layout.activity_wait_driver;
    }

    @Override
    public void initView(View view) {
        setTitle("等待司机到达");
        mapView = (MapView) findViewById(R.id.waitdriver_map);
        mapView.onCreate(savedInstanceState);//必须要写
        initMap();
        initSt();
        mHandler.sendEmptyMessage(Config.DRIVERTOME);//每隔15秒刷一次司机和起点的路线规划
        mHandler.sendEmptyMessage(Config.ON_THE_CAR);//每隔5秒刷一次检测订单状态，乘客是否已上车
    }

    private void initMap() {
        loadingDialog = new LoadingDialog(act);
        loadingDialog.loading();
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        routeSearch = new RouteSearch(this);//路线规划显示在地图
        routeSearch.setRouteSearchListener(this);

        mapRouteUtils = new AMapRouteUtils(aMap, this);
        mapRouteUtils.setResultListener(this);//此次路线规划获取司机距离乘客的距离

        mapLocationUtils = new MapLocationUtils();//地图定位
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.showMyLocation(false);
        myLocationStyle.interval(2000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));//范围圆圈边框色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//范围圆圈填充色
        mapLocationUtils.startLocation(aMap, this,myLocationStyle);//不显示定位蓝点
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        mapLocationUtils.setLocationListener(new MapLocationUtils.LocationChangListener() {
            @Override
            public void Location(AMapLocation location) {
                loadingDialog.dismiss();
                //定位成功回调信息，设置相关消息
                location.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                mLocLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    isFirstLoc = false;
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocLatLng, 16));

                }
            }
        });
    }

    private void initSt() {
        Intent intent = getIntent();
        if (!intent.hasExtra(OrderInfo.TAG)&&!intent.hasExtra("state")) {
            ToastUtils.show("初始化失败,叫车失败");
            finish();
            return;
        }
        if(intent.hasExtra("state")){
            stateEntity=(TripHistoryEntity) intent.getSerializableExtra("state");//订单信息
            startstlatlng =stateEntity.getLat();//起点纬度
            startedlatlng = stateEntity.getLon();//起点经度
            endstlatlng=stateEntity.getDLat();//终点纬度
            endedlatlng=stateEntity.getDLon();//终点经度
            typeByCar =stateEntity.getTaxiTypeST();//车类型
            startAddress = stateEntity.getTaxiAddress();//起点地址
            endAddress=stateEntity.getTaxiDestination();//终点地址
            guid = stateEntity.getGuid();//订单GUID
            taxiguid=stateEntity.getTaxiGuid();//车辆guid
            driver_name.setText(stateEntity.getDriverName());
            taxi_number.setText(stateEntity.getTaxiCard());
        }else {
            startstlatlng = intent.getExtras().getDouble(Config.STLATITUDE);//起点纬度
            startedlatlng = intent.getExtras().getDouble(Config.STLONGITUDE);//起点经度
            endstlatlng = intent.getExtras().getDouble(Config.EDLATITUDE);//终点纬度
            endedlatlng = intent.getExtras().getDouble(Config.EDLONGITUDE);//终点经度
            orderInfo = (OrderInfo) intent.getSerializableExtra(OrderInfo.TAG);//订单信息
            typeByCar = intent.getExtras().getInt(Config.KEY_CALL_TYPE);//车类型
            startAddress = intent.getStringExtra(Config.ST_ADDRESS);//起点地址
            endAddress = intent.getStringExtra(Config.ED_ADDRESS);//终点地址
            guid = orderInfo.getGuid();//订单GUID
            taxiguid=orderInfo.getTaxiGuid();//车辆guid
            driver_name.setText(orderInfo.getDriverName());
            taxi_number.setText(orderInfo.getTaxiCard());
        }
        startlang=new LatLng(startstlatlng,startedlatlng);
        meLatpoint=new LatLonPoint(startstlatlng,startedlatlng);
        endPoint=new LatLonPoint(endstlatlng,endedlatlng);
        searchRouteResult(meLatpoint,endPoint);//这个是查询起始点路线数据
        setOnMenuClickListener(menuItemClickListener);
        call_phone.setOnClickListener(this);
        wait_gotrip.setOnClickListener(this);
    }

    //创建附近车辆数，等待时间marker
    private MarkerOptions markerOptions;
    private Marker disMarker;

    private void distanceMarker(LatLng latLng, String distence) {
        View view = View.inflate(getActivity(), R.layout.layout_km, null);
        AppCompatTextView distanceKM = (AppCompatTextView) view.findViewById(R.id.driver_me_km);
        distanceKM.setText(distence);
        Bitmap bitmap = DateUtils.convertViewToBitmap(view);
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        markerOptions.draggable(false);
        markerOptions.setFlat(true);
        markerOptions.visible(true);
        aMap.addMarker(markerOptions);
        disMarker = aMap.addMarker(markerOptions);
    }

    //每隔15秒查询一次车辆与起点路线数据的结果返回  （属于mapRouteUtils的结果）
    @Override
    public void RouteResult(String distence, int Minute, float money) {
//        ToastUtils.show(distence);
//        if (disMarker != null) {
//            disMarker.remove();
//        }
        distanceMarker(startlang, distence);
    }
//重写routeSearch的查询
    private void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }
    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.DRIVERTOME:
                    if (TextUtils.isEmpty(taxiguid))
                        return;
                    JSONObject json=new JSONObject();
                    json.put("taxicarid",taxiguid);
                    HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.NS_TAXI_REALTIMEGPSINFO,json.toJSONString(),new HttpResponse(){
                        @Override
                        public void result(int i, String content) {
                            List<DriverGPS> list = Response.analysis(act, content, DriverGPS.class);
                            if (list != null && list.size() > 0) {
                                DriverGPS gps = list.get(0);
                                LatLng driverlatlng = new LatLng(Double.parseDouble(gps.getLatitude()),
                                                                        Double.parseDouble(gps.getLongitude()));
                                GpsToGaodeUtil trans=new GpsToGaodeUtil(WaitDriverActivity.this);
                                LatLng dirverlat=trans.GpsToGaode(driverlatlng);
                                driverLatpoint=new LatLonPoint(dirverlat.latitude,dirverlat.longitude);
                                if (meLatpoint!=null) {
                                    //这个是每隔15秒查询一次车辆与起点的路线数据
                                    mapRouteUtils.searchRoute(driverLatpoint, meLatpoint);
                                }
                            }
                        }
                    });
                    sendEmptyMessageDelayed(Config.DRIVERTOME, 15000);
                    break;
                case Config.ON_THE_CAR:
                    TakeCarDao.getCarStatus(getActivity(),guid,mHandler);
                    sendEmptyMessageDelayed(Config.ON_THE_CAR, 5000);
                    break;
                case TakeCarDao.ORDERCARSTATUS:
                    List<OrderCarStatusEntity> statusList=(List<OrderCarStatusEntity>) msg.obj;
                    if(statusList!=null&&statusList.size()>0){
                        if (statusList.get(0).getOrderStatus().equals("2")) {
                            ToastUtils.show("乘客已上车");
                            mHandler.removeMessages(Config.DRIVERTOME);
                            mHandler.removeMessages(Config.ON_THE_CAR);
                            Intent intent = null;
                            if(typeByCar==Config.TYPE_TAXI){
                              intent = new Intent(act, TaxiTripingActivity.class);
                            }else if(typeByCar==Config.TYPE_SPECIAL) {
                               intent = new Intent(act, DriverTripActivity.class);
                            }
                            if(stateEntity!=null){
                                intent.putExtra("state",stateEntity);
                            }else{
                                intent.putExtra(OrderInfo.TAG, orderInfo);
                            }
                            intent.putExtra(Config.KEY_CALL_TYPE, typeByCar);
                            intent.putExtra(Config.ST_ADDRESS, startAddress);
                            intent.putExtra(Config.ED_ADDRESS, endAddress);
                            intent.putExtra(Config.STLATITUDE, startstlatlng);
                            intent.putExtra(Config.STLONGITUDE, startedlatlng);
                            intent.putExtra(Config.EDLATITUDE, endstlatlng);
                            intent.putExtra(Config.EDLONGITUDE, endedlatlng);
                            act.startActivity(intent);
                            act.finish();
                        }
                    }
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_phone:
                String phonenum=null;
                if(stateEntity!=null){
                    phonenum=stateEntity.getTaxiSim();
                }else{
                    phonenum=orderInfo.getTaxiSim();
                }
                ColorDialog dialog = new ColorDialog(this);
                dialog.setColor("#ffffff");//#8ECB54
                dialog.setAnimationEnable(true);
                dialog.setTitle("");
                dialog.setContentText(phonenum);
                final String finalPhonenum = phonenum;
                dialog.setPositiveListener("取消", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        ToastUtils.show("取消");
                        dialog.dismiss();
                    }
                }).setNegativeListener("呼叫", new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL); // 设置动作
                        Uri data = Uri.parse("tel:" + finalPhonenum); // 设置数据
                        intent.setData(data);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.wait_gotrip:
                Methods.toBase(getActivity(),DriverTripActivity.class);
                break;
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapView.onDestroy();
        mHandler.removeMessages(Config.DRIVERTOME);
        mHandler.removeMessages(Config.ON_THE_CAR);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TakeCarDao.getCancelOrder(getActivity(),typeByCar,startAddress,guid);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wait_driver, menu);
        return true;
    }
    Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_cancel_order:
                    ColorDialog dialog1 = new ColorDialog(getActivity());
                    dialog1.setColor("#ffffff");//#8ECB54
                    dialog1.setAnimationEnable(true);
                    dialog1.setTitle("");
                    dialog1.setContentText("是否取消订单");
                    dialog1.setPositiveListener("不取消", new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            dialog.dismiss();
                        }
                    }).setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            TakeCarDao.getCancelOrder(getActivity(),typeByCar,startAddress,guid);
                            dialog.dismiss();
                        }
                    }).show();
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
//重写routeSearch的结果返回
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if(i==1000){
            if (driveRouteResult != null && driveRouteResult.getPaths() != null && driveRouteResult.getPaths().size() > 0) {
                DrivePath drivePath = driveRouteResult.getPaths().get(0);
                DecimalFormat df = new DecimalFormat("0.0");
                float dis = (float)drivePath.getDistance()/ 1000;
                int time = (int) (drivePath.getDuration()/ 60);
                float cost = driveRouteResult.getTaxiCost();//在规划的路线里坐出租车的大概花费
                alldistance.setText("全程约"+df.format(dis)+"公里");
                allmoney.setText("需要"+time+"分钟");
                alltime.setText("大约"+df.format(cost)+"元");
            }
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
