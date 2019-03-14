package com.zk.taxi.ui.CallCar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.zk.taxi.AmapHelper.AMapRouteUtils;
import com.zk.taxi.AmapHelper.MapLocationUtils;
import com.zk.taxi.BaseActivity;
import com.zk.taxi.Config;
import com.zk.taxi.Interface;
import com.zk.taxi.R;
import com.zk.taxi.entity.DriverGPS;
import com.zk.taxi.entity.OrderCarStatusEntity;
import com.zk.taxi.entity.OrderInfo;
import com.zk.taxi.http.HttpResponse;
import com.zk.taxi.http.HttpUtils;
import com.zk.taxi.http.Response;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.GpsToGaodeUtil;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.LoadingDialog;
import com.zk.taxi.widget.Methods;

import java.util.List;

import butterknife.Bind;

public class DriverTripActivity extends BaseActivity implements AMapRouteUtils.RouteResultListener {
    @Bind(R.id.bottom_money)
    RelativeLayout bottom_money;
    @Bind(R.id.trip_car_km)
    AppCompatTextView trip_car_km;
    @Bind(R.id.trip_car_money)
    AppCompatTextView trip_car_money;
    @Bind(R.id.trip_car_time)
    AppCompatTextView trip_car_time;

    private MapView mapView;//地图控件
    private AMap aMap;//地图控制器对象�
    private Bundle savedInstanceState;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private LatLng mLocLatLng =null;// 当前经纬度坐标�
    private static LoadingDialog loadingDialog;
    private MapLocationUtils mapLocationUtils;
    private AMapRouteUtils mapRouteUtils;
    private LatLonPoint meLatpoint = null;//起点
    private LatLonPoint driverLatpoint = null;//司机当前位置
    private LatLonPoint endPoint=null;//目的地
    private int typeByCar;// 出租车类型
    private String guid = null;// 订单唯一标识
    private String startAddress = null;// 开始地址
    private String endAddress = null;// 结束地址
    private OrderInfo orderInfo = null;// 订单
    private double startstlatlng;//起点纬度
    private double startedlatlng;//起点经度
    private double endstlatlng;//终点纬度
    private double endedlatlng;//终点经度
    @Override
    public int bindLayout() {
        return R.layout.activity_driver_trip;
    }

    @Override
    public void initView(View view) {
        setTitle("行程中（网约）");
        mapView = (MapView) findViewById(R.id.trip_map);
        mapView.onCreate(savedInstanceState);//必须要写
        initMap();
        initSt();
        mHandler.sendEmptyMessage(Config.DRIVERTOME);//每隔15秒刷一次司机和起点的路线规划
        mHandler.sendEmptyMessage(Config.ON_END_POINT);//每隔10秒刷一次检测订单状态，是否到达目的地

    }

    private void initSt() {
        setOnMenuClickListener(menuItemClickListener);
        bottom_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.toBase(getActivity(),TaxiTripingActivity.class);
            }
        });
        Intent intent = getIntent();
        if (!intent.hasExtra(Config.KEY_CALL_TYPE) || !intent.hasExtra(OrderInfo.TAG)
                    || !intent.hasExtra(Config.STLATITUDE)|| !intent.hasExtra(Config.STLONGITUDE)
                    || !intent.hasExtra(Config.EDLATITUDE)|| !intent.hasExtra(Config.EDLONGITUDE)
                    || !intent.hasExtra(Config.ST_ADDRESS)|| !intent.hasExtra(Config.ED_ADDRESS)) {
            ToastUtils.show("初始化失败");
            finish();
            return;
        }
        startstlatlng =intent.getExtras().getDouble(Config.STLATITUDE);//起点纬度
        startedlatlng = intent.getExtras().getDouble(Config.STLONGITUDE);//起点经度
        endstlatlng=intent.getExtras().getDouble(Config.EDLATITUDE);//终点纬度
        endedlatlng=intent.getExtras().getDouble(Config.EDLONGITUDE);//终点经度
        orderInfo = (OrderInfo) intent.getSerializableExtra(OrderInfo.TAG);//订单信息
        typeByCar = intent.getExtras().getInt(Config.KEY_CALL_TYPE);//车类型
        startAddress = intent.getStringExtra(Config.ST_ADDRESS);//起点地址
        endAddress=intent.getStringExtra(Config.ED_ADDRESS);//终点地址
        guid = orderInfo.getGuid();//订单GUID
        meLatpoint=new LatLonPoint(startstlatlng,startedlatlng);//起点
        endPoint=new LatLonPoint(endstlatlng,endedlatlng);//终点
    }

    private void initMap() {
        loadingDialog=new LoadingDialog(act);
        loadingDialog.loading();
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        mapRouteUtils = new AMapRouteUtils(aMap, this);
        mapRouteUtils.setResultListener(this);//获取司机距离乘客的距离
        mapLocationUtils = new MapLocationUtils();//地图定位
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.showMyLocation(false);
        myLocationStyle.interval(2000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));//范围圆圈边框色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//范围圆圈填充色
        mapLocationUtils.startLocation(aMap, this,myLocationStyle);//不显示定位蓝点
        mapLocationUtils.setLocationListener(new MapLocationUtils.LocationChangListener() {
            @Override
            public void Location(AMapLocation location) {
                loadingDialog.dismiss();
                //定位成功回调信息，设置相关消息
                location.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表��ͱ�
                mLocLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    isFirstLoc = false;
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocLatLng, 16));

                }
            }
        });
    }
    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.DRIVERTOME:
                    if (TextUtils.isEmpty(orderInfo.getTaxiGuid()))
                        return;
                    JSONObject json=new JSONObject();
                    json.put("taxicarid", orderInfo.getTaxiGuid());
                    HttpUtils.getTaxi(Config.URL_CALL_CAR, Interface.NS_TAXI_REALTIMEGPSINFO,json.toJSONString(),new HttpResponse(){
                        @Override
                        public void result(int i, String content) {
                            List<DriverGPS> list = Response.analysis(act, content, DriverGPS.class);
                            if (list != null && list.size() > 0) {
                                DriverGPS gps = list.get(0);
                                LatLng driverlatlng = new LatLng(Double.parseDouble(gps.getLatitude()),
                                                                        Double.parseDouble(gps.getLongitude()));
                                GpsToGaodeUtil trans=new GpsToGaodeUtil(DriverTripActivity.this);
                                LatLng dirverlat=trans.GpsToGaode(driverlatlng);
                                driverLatpoint=new LatLonPoint(dirverlat.latitude,dirverlat.longitude);
                                if (meLatpoint!=null) {
                                    //这个是每隔15秒查询一次车辆与起点的路线数据
                                    mapRouteUtils.searchRoute(driverLatpoint, endPoint);
                                }
                            }
                        }
                    });
                    sendEmptyMessageDelayed(Config.DRIVERTOME, 15000);
                    break;
                case Config.ON_END_POINT:
                    TakeCarDao.getCarStatus(getActivity(),guid,mHandler);
                    sendEmptyMessageDelayed(Config.ON_END_POINT, 10000);
                    break;
                case TakeCarDao.ORDERCARSTATUS:
                    List<OrderCarStatusEntity> statusList=(List<OrderCarStatusEntity>) msg.obj;
                    if(statusList!=null&&statusList.size()>0){
                        if (statusList.get(0).getOrderStatus().equals("3")) {
                            ToastUtils.show("到达目的地");
                            mHandler.removeMessages(Config.DRIVERTOME);
                            mHandler.removeMessages(Config.ON_THE_CAR);
                            Intent   intent = new Intent(act, TaxiCarPayActivity.class);
                            intent.putExtra(OrderInfo.TAG, orderInfo);
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
    public void RouteResult(String distence, int Minute, float money) {
        trip_car_km.setText(distence);
        trip_car_money.setText(String.valueOf(money));
        trip_car_time.setText(String.valueOf(Minute));
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
        mHandler.removeMessages(Config.ON_END_POINT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_more, menu);
        return true;
    }
    Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_order_more:
                    Intent intent = new Intent(DriverTripActivity.this, TripDetailActivity.class);
                    intent.putExtra(OrderInfo.TAG, orderInfo);
                    intent.putExtra(Config.KEY_CALL_TYPE, typeByCar);
                    intent.putExtra(Config.ST_ADDRESS, startAddress);
                    intent.putExtra(Config.ED_ADDRESS, endAddress);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };



}
