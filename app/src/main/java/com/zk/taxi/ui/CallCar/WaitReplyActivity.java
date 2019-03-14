package com.zk.taxi.ui.CallCar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.zk.taxi.AmapHelper.MapLocationUtils;
import com.zk.taxi.BaseActivity;
import com.zk.taxi.Config;
import com.zk.taxi.R;
import com.zk.taxi.entity.OrderInfo;
import com.zk.taxi.entity.TaxiInfoEntity;
import com.zk.taxi.http.Response;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.tool.DateUtils;
import com.zk.taxi.tool.GpsToGaodeUtil;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.view.SlideRightViewDragHelper;
import com.zk.taxi.widget.ColorDialog;
import com.zk.taxi.widget.LoadingDialog;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

public class WaitReplyActivity extends BaseActivity implements  AMap.OnMarkerClickListener {

@Bind(R.id.wait_linearlayout) LinearLayout wait_linearlayout;
    @Bind(R.id.wait_move) AppCompatImageView wait_move;
    @Bind(R.id.get_order_drag_helper) SlideRightViewDragHelper dragHelper ;
    @Bind(R.id.reply_center1) LinearLayout reply_center1;
    @Bind(R.id.get_taxi_site)
    AppCompatTextView get_taxi_site;
    @Bind(R.id.get_taxi_offsite)
    AppCompatTextView get_taxi_offsite;
    @Bind(R.id.ymd)
    AppCompatTextView ymd;
    @Bind(R.id.week_eeee)
    AppCompatTextView week_eeee;
    @Bind(R.id.hm)
    AppCompatTextView hm;
//    private TextView taxi_num;
//    private TextView wait_time;

    //宏定义，表示是什么消息
    public static final int REFRESH = 0x01;
    //用来显示变化的值
    private int nValue = 0;
    //定时器设置
    Timer timer = new Timer();
    private MapView mapView;//地图控件
    private AMap aMap;//地图控制器对象�
    private Bundle savedInstanceState;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private LatLng mLocLatLng =null;// 定位点坐标
    private LatLng stLatLng=null;//传入的起点坐标 �
    private static LoadingDialog loadingDialog;
    private MapLocationUtils mapLocationUtils;
    private View textEntryView;
    private int typeByCar ;// 出租车类型
    private String guid = null;// 订单唯一标识
    private String startAddress = null;// 开始地址
    private String endAddress = null;// 结束地址
    private double startstlatlng;//起点纬度
    private double startedlatlng;//起点经度
    private double endstlatlng;//终点纬度
    private double endedlatlng;//终点经度

    private int nearTaxi;
    private TextView number;
    private TextView time;
    private int countdowntwomin = 0;// 计时2分钟，没人接单就取消
    @Override
    public int bindLayout() {
        return R.layout.activity_wait_reply;
    }

    @Override
    public void initView(View view) {
        setTitle("等待应答");
        mapView = (MapView) findViewById(R.id.reply_map);
        mapView.onCreate(savedInstanceState);//必须要写
        doCancelOrder();
        initMap();
        Init();
        mHandler.sendEmptyMessage(Config.CALL_COUNT_TIME);
        mHandler.sendEmptyMessage(Config.CALL_NEAR_CAR);//每隔5秒刷一次检测是否被接单
    }
    private void doCancelOrder() {
        dragHelper.setOnReleasedListener(new SlideRightViewDragHelper.OnReleasedListener() {
            @Override
            public void onReleased() {
                //TODO
                CancelOrderDialog(typeByCar,startAddress,guid,1);
            }
        });
    }
    private void initMap() {
        loadingDialog=new LoadingDialog(act);
        loadingDialog.loading();
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        mapLocationUtils=new MapLocationUtils();
        mapLocationUtils.startLocation(aMap,this);
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
    private void Init() {
        Intent intent = getIntent();
        if (!intent.hasExtra(Config.KEY_CALL_TYPE) || !intent.hasExtra(Config.KEY_CALL_GUID)
                    || !intent.hasExtra(Config.STLATITUDE)|| !intent.hasExtra(Config.STLONGITUDE)
                    || !intent.hasExtra(Config.EDLATITUDE)|| !intent.hasExtra(Config.EDLONGITUDE)
                    || !intent.hasExtra(Config.ST_ADDRESS)|| !intent.hasExtra(Config.ED_ADDRESS)){
            ToastUtils.show("初始化失败,叫车失败");
            finish();
            return;
        }
        typeByCar = intent.getExtras().getInt(Config.KEY_CALL_TYPE);
        guid = intent.getStringExtra(Config.KEY_CALL_GUID);
        startstlatlng =intent.getExtras().getDouble(Config.STLATITUDE);
        startedlatlng = intent.getExtras().getDouble(Config.STLONGITUDE);
        endstlatlng=intent.getExtras().getDouble(Config.EDLATITUDE);
        endedlatlng=intent.getExtras().getDouble(Config.EDLONGITUDE);

        startAddress=intent.getStringExtra(Config.ST_ADDRESS);
        endAddress=intent.getStringExtra(Config.ED_ADDRESS);
        get_taxi_site.setText(startAddress);
        get_taxi_offsite.setText(endAddress);
        ymd.setText(DateUtils.getCurrentDate());
        week_eeee.setText(DateUtils.getTimeByType(DateUtils.WEEK));
        hm.setText(DateUtils.getTimeByType(DateUtils.HM));
        stLatLng=new LatLng(startstlatlng, startedlatlng);
        if (stLatLng==null){
            ToastUtils.show("获取起始点失败");
            return;
        }
        LatLng marker = new LatLng(startstlatlng, startedlatlng);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker));//设置中心点
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        nValue = 0;
        //开启定时器
        timer.schedule(task,1000,1000);
    }

    //创建附近车辆数，等待时间marker
    private MarkerOptions markerOptions;
    private Marker mMarker;
    private void waitWind(LatLng latLng,int conut,int timeValue) {
        View view = View.inflate(getActivity(), R.layout.layout_adrift, null);
        number= (TextView) view.findViewById(R.id.adrift_car_number);
        time= (TextView) view.findViewById(R.id.adrift_time);
        number.setText(String.valueOf(conut)+"辆");
        time.setText(ToastUtils.getTime(timeValue));
        Bitmap bitmap = DateUtils.convertViewToBitmap(view);
        markerOptions= new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        markerOptions.draggable(false);
        markerOptions.setFlat(true);
        markerOptions.visible(true);
        aMap.addMarker(markerOptions);
        mMarker=aMap.addMarker(markerOptions);
    }
    //查询附近出租车
    private void refreshNearCar(int carType,LatLng latLng) {
        //获取参数：类型，当前点位，通过接口查询，解析返回的数据，通过Handler在返回此页面，用mHandler接收数据
        if (carType==2){
            TakeCarDao.getNearTaxiInfo(this, Config.TYPE_TAXI,latLng,mHandler);
        }else if (carType==7){
            TakeCarDao.getNearTaxiInfo(this,Config.TYPE_SPECIAL,latLng,mHandler);
        }
    }
    // 请求订单状态
    private void reqByGuid() {
        TakeCarDao.getOrderStatus(getActivity(),guid,mHandler);
    }
//    // 请求网络取消订单
//    private void cancelCallingCar() {
//        TrafficNewsDao.getCancelOrder(getActivity(),typeByCar,address,guid);
//    }
    private Handler mHandler=new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.CALL_COUNT_TIME:
                    if (countdowntwomin>=120) {
                        removeMessages(Config.CALL_COUNT_TIME);
                        CancelOrderDialog(typeByCar,startAddress,guid,0);
                        return;
                    }
                    sendEmptyMessageDelayed(Config.CALL_COUNT_TIME, 1000);
                    countdowntwomin++;
                    break;
                case Config.CALL_NEAR_CAR:
                    refreshNearCar(typeByCar,new LatLng(startstlatlng, startedlatlng));// 附近车辆
                    reqByGuid();// 请求订单状态
                    sendEmptyMessageDelayed(Config.CALL_NEAR_CAR, 5000);
                    break;
                case TakeCarDao.NEARTAXI:
                    List<TaxiInfoEntity> list= (List<TaxiInfoEntity>) msg.obj;
                    nearTaxi=list.size();
                    if (list != null && list.size() > 0) {
                        for (TaxiInfoEntity info : list) {
                            MarkerOptions markerOptions= new MarkerOptions();
                            LatLng carlat=new LatLng(info.getLat(),info.getLon());
                            GpsToGaodeUtil trans=new GpsToGaodeUtil(WaitReplyActivity.this);
                            markerOptions.position(trans.GpsToGaode(carlat));
                            markerOptions.title("车牌号:"+info.getTaxiCard()+"\n司机："+info.getDriverName());
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.taxi_on2)));
                            markerOptions.draggable(false);
                            markerOptions.setFlat(true);
                            markerOptions.visible(true);
                            aMap.addMarker(markerOptions);
                        }
                    }else {
                        ToastUtils.show("附近暂无车辆");
                    }
                    break;
                case TakeCarDao.NEARCAR:
                    List<TaxiInfoEntity> listCar= (List<TaxiInfoEntity>) msg.obj;
                    if (listCar != null && listCar.size() > 0) {
                        for (TaxiInfoEntity info : listCar) {
                            MarkerOptions markerOptions= new MarkerOptions();
                            LatLng carlat=new LatLng(info.getLat(),info.getLon());
                            GpsToGaodeUtil trans=new GpsToGaodeUtil(WaitReplyActivity.this);
                            markerOptions.position(trans.GpsToGaode(carlat));
                            markerOptions.title("车牌号:"+info.getTaxiCard()+"\n司机："+info.getDriverName());
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.loc1)));
                            markerOptions.draggable(false);
                            markerOptions.setFlat(true);
                            markerOptions.visible(true);
                            aMap.addMarker(markerOptions);
                        }
                    }else {
                        ToastUtils.show("附近暂无车辆");
                    }
                    break;
                case TakeCarDao.ORDERSTATUS:
                    List<OrderInfo> orderList=(List<OrderInfo>) msg.obj;
                    if(orderList!=null&&orderList.size()>0){
                        if (orderList.get(0).getTaxiResult().equals(Response.SUCCESS)) {
                            ToastUtils.show("接单成功");
                            mHandler.removeMessages(Config.CALL_COUNT_TIME);
                            mHandler.removeMessages(Config.CALL_NEAR_CAR);
                            Intent intent = new Intent(act, WaitDriverActivity.class);
                            intent.putExtra(OrderInfo.TAG, orderList.get(0));
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
    public boolean onMarkerClick(Marker marker) {
        return true;
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
                    waitWind(stLatLng,nearTaxi,nValue);
                    if (mMarker!=null){
                        mMarker.remove();
                    }
                    break;
            }
        }
    };

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
        timer.cancel();
        timer.purge();
        mHandler.removeMessages(Config.CALL_COUNT_TIME);
        mHandler.removeMessages(Config.CALL_NEAR_CAR);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CancelOrderDialog(typeByCar,startAddress,guid,1);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
//0:是在规定时间没有匹配到司机取消，不需要取消成功后进入取消理由选择
    //1：是自己点击取消情况下，需进入理由选择界面
    public void CancelOrderDialog(final int Type, final String address, final String guid, final int way){
        ColorDialog dialog = new ColorDialog(this);
        dialog.setColor("#ffffff");//#8ECB54
        dialog.setAnimationEnable(true);
        dialog.setTitle("");
        dialog.setContentText("是否取消订单");
        dialog.setPositiveListener("不取消", new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
            }
        }).setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                if(way==0){
                    Toast.makeText(getActivity(),"暂没有匹配到司机，请稍后再下单",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    finish();
                }else{
                    TakeCarDao.getCancelOrder(getActivity(),Type,address,guid);
                    dialog.dismiss();
                }
            }
        }).show();
    }

}
