package com.zk.taxi.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.poisearch.util.CityModel;
import com.amap.poisearch.util.CityUtil;
import com.bumptech.glide.Glide;
import com.zk.taxi.AmapHelper.AMapPlanUtils;
import com.zk.taxi.AmapHelper.MapLocationUtils;
import com.zk.taxi.Config;
import com.zk.taxi.R;
import com.zk.taxi.entity.Guid;
import com.zk.taxi.entity.LocationCityEntity;
import com.zk.taxi.entity.TaxiInfoEntity;
import com.zk.taxi.entity.TripHistoryEntity;
import com.zk.taxi.entity.UserInfo;
import com.zk.taxi.methodDao.TakeCarDao;
import com.zk.taxi.methodDao.UserPost;
import com.zk.taxi.tool.ActivityManager;
import com.zk.taxi.tool.GeocodeUtils;
import com.zk.taxi.tool.GpsToGaodeUtil;
import com.zk.taxi.tool.PermissionUtil;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.tool.UserHelper;
import com.zk.taxi.ui.CallCar.ChooseCityActivity;
import com.zk.taxi.ui.CallCar.ChoosePoiActivity;
import com.zk.taxi.ui.CallCar.DriverTripActivity;
import com.zk.taxi.ui.CallCar.TaxiPayActivity;
import com.zk.taxi.ui.CallCar.TaxiTripingActivity;
import com.zk.taxi.ui.CallCar.WaitDriverActivity;
import com.zk.taxi.ui.CallCar.WaitReplyActivity;
import com.zk.taxi.ui.SettingUI.CustomerServiceActivity;
import com.zk.taxi.view.CircleImageView;
import com.zk.taxi.widget.ColorDialog;
import com.zk.taxi.widget.CustomDialog;
import com.zk.taxi.widget.LoadingDialog;
import com.zk.taxi.widget.Methods;
import com.zk.taxi.widget.VersionCheck;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.amap.poisearch.searchmodule.ISearchModule.IDelegate.DEST_POI_TYPE;
import static com.amap.poisearch.searchmodule.ISearchModule.IDelegate.START_POI_TYPE;
import static com.zk.taxi.tool.PermissionUtil.SDK_PERMISSION_REQUEST;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AMap.OnMarkerClickListener,  AMap.OnCameraChangeListener, AMapPlanUtils.RouteResultListener {
    @Bind(R.id.main_toolbar) Toolbar toolbar;
    @Bind(R.id.drawer_layout1) DrawerLayout drawer;
    @Bind(R.id.apptitle) TextView apptitle;//标题
    @Bind(R.id.appcity) TextView appcity;//选择城市
    @Bind(R.id.plan_tab) TabLayout mTabLayout;
    @Bind(R.id.user_phone) AppCompatTextView user_phone;//用户电话
    @Bind(R.id.user_center_more) AppCompatTextView user_center_more;//更多设置
    @Bind(R.id.xingcheng) AppCompatTextView xingcheng;//行程
    @Bind(R.id.money) AppCompatTextView money;//钱包
    @Bind(R.id.customer_service) AppCompatTextView customer_service;//客服
    @Bind(R.id.integral_mall) AppCompatTextView integral_mall;//商城
    @Bind(R.id.main_menu_head_layout) RelativeLayout menu_head;
    @Bind(R.id.main_menu_head_userimage)
    CircleImageView main_menu_head_userimage;
    @Bind(R.id.real_time) AppCompatTextView real_time;
    @Bind(R.id.appoint_time) AppCompatTextView appoint_time;
    @Bind(R.id.call_car) AppCompatButton call_car;//确认召车
    @Bind(R.id.meun_loc) AppCompatImageView meun_loc;
    @Bind(R.id.startAd) AppCompatTextView startAd;
    @Bind(R.id.endAd)  AppCompatTextView endAd;
    @Bind(R.id.staddress) RelativeLayout staddress;
    @Bind(R.id.edaddress) RelativeLayout edaddress;
    @Bind(R.id.forecast_km) AppCompatTextView forecast_km;
    @Bind(R.id.forecast_time) AppCompatTextView forecast_time;
    @Bind(R.id.forecast_money) AppCompatTextView forecast_money;
    @Bind(R.id.forecast) RelativeLayout forecast;


    public static final String []sTitle = new String[]{"出租车","网约车"};

    private MapView mapView;//地图控件
    private AMap aMap;//地图控制器对象�
    private Bundle savedInstanceState;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private LatLng mLocLatLng =null;// 当前定位经纬度坐标�
    private String searchPlace=null;//城市��
    private CityModel cityModel=new CityModel();
    private GeocodeUtils geocodeUtils=null;
    private LatLng mapCenterLatLng =null;//地图中心点
    private LatLng EndLatLng =null;//地图中心点
    private int typecar=2;
    private PermissionUtil mPermissionUtil;
    private MapLocationUtils mapLocationUtils;
    private AMapPlanUtils mapRouteUtils;
    private static LoadingDialog loadingDialog;
    private String start=null;//上车点地址
    private String end=null;//下车点地址
    public static String URL_SERVICE=null;
    TripHistoryEntity stateEntity=null;
    /*
    * 如果想创建统一风格页面你需要做的
    * 1，创建新activity
    * 2，extends BaseActivity,并且重写他的2个方法bindLayout()，initView(View view)。
    *    以及设置setBackup();setTitle("***");详情可见已有activity
    * 3，在布局界面，跟布局需要添加 android:fitsSystemWindows="true"
    *    以及<include layout="@layout/tool_bar"/> toorbar控件
    * 4，在清单文件，添加注册，及样式android:theme="@style/AppTheme.NoActionBar"
    *某些页面不需要统一风格，可以自己写，有点相同，以上第四点；
    * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermissionUtil=new PermissionUtil(this);//确认必要权限是否开启
        ActivityManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);

        mapView = (MapView) findViewById(R.id.main_map);
        mapView.onCreate(savedInstanceState);//必须要写
        initMap();//加载地图功能
        new VersionCheck(MainActivity.this, false);//检查更新
        UserPost.getAllInterface(this,mHandler);//首先需要获取服务器url（然后根据定位地址选择相应服务器URL）
        if (UserHelper.getUserInfo()!=null){
            TakeCarDao.getOrderStates(this,mHandler);//查询订单的状态，是否有未完成订单并去往相应界面继续完成
        }
        initView();//初始化控件和侧边栏
        geocodeUtils=new GeocodeUtils(this,mHandler);//坐标反地理编码获取地址
    }

    private void initMap() {
        loadingDialog=new LoadingDialog(this);
        loadingDialog.loading();
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        mapLocationUtils=new MapLocationUtils();
        mapLocationUtils.startLocation(aMap,this);
        mapRouteUtils = new AMapPlanUtils(aMap, this);
        mapRouteUtils.setResultListener(this);//获取路线信息
        mapLocationUtils.setLocationListener(new MapLocationUtils.LocationChangListener() {
            @Override
            public void Location(AMapLocation location) {
                loadingDialog.dismiss();
                mLocLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                searchPlace=location.getCity();//获取所在城市信息
                cityModel= CityUtil.getCityByCode(getApplicationContext(), location.getCityCode());
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    isFirstLoc = false;
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocLatLng, 16));
                    appcity.setText(searchPlace);
                }
            }
        });
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
    }

    private void initView() {
        toolbar.setTitle(" ");//设置主标题
        setSupportActionBar(toolbar);
        //设置返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mPermissionUtil.checkPermissions(PermissionUtil.needPermissions);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
//                // 得到contentView
//                View content = drawer.getChildAt(0);
//                int offset = (int) (drawerView.getWidth() * slideOffset);
//                content.setTranslationX(offset);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                if (Methods.checkLogin(MainActivity.this)) {//检查是否登录，已登录能打开侧边个人信息栏
                    drawer.closeDrawers();
                    return;
                }
                invalidateOptionsMenu();
            }
            //这个方法词状态改变都会回调一次,下面列出有哪些状态:1.STATE_DRAGGING ---> 当手指拖动drawer移动的时候2.STATE_IDLE -------> 当手指不在屏幕上,且DrawerLayout处于稳定状态3.STATE_SETTLING ---> 当drawer自动移动的状态(如拉一半放开后自动收回/张开时)
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (newState == 1) {
                    if (Methods.checkLogin(MainActivity.this)) {
                        drawer.closeDrawers();
                        return;
                    }
                }
                if (newState == 2) {
                    if (Methods.checkLogin(MainActivity.this)) {
                        drawer.closeDrawers();
                        return;
                    }
                }
            }
        };
        drawer.setDrawerListener(toggle);
        //将侧边栏顶部延伸至status bar
        drawer.setFitsSystemWindows(true);
        //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
        drawer.setClipToPadding(false);
        toggle.syncState();
//        修改左侧栏开关图标
        toggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.user_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Methods.checkLogin(MainActivity.this)) {
                    drawer.closeDrawers();
                    return;
                }else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        appcity.setOnClickListener(this);
        user_center_more.setOnClickListener(this);
        xingcheng.setOnClickListener(this);
        money.setOnClickListener(this);
        customer_service.setOnClickListener(this);
        integral_mall.setOnClickListener(this);
        menu_head.setOnClickListener(this);
        call_car.setOnClickListener(this);
        appoint_time.setOnClickListener(this);
        real_time.setOnClickListener(this);
        meun_loc.setOnClickListener(this);
        staddress.setOnClickListener(this);
        edaddress.setOnClickListener(this);
        setTab();

        if (UserHelper.getHeadImgURL()!=null){
            Glide.with(MainActivity.this).load(UserHelper.getHeadImgURL()).into(main_menu_head_userimage);
        }
        UserInfo user= UserHelper.getUserInfo();
        if (user != null) {
//            user_name.setText(user.getLast_name() + (user.getSex() == 0 ? "先生" : "女士"));
            user_phone.setText(user.getMobile());
        }
    }
    //选择出租车，网约车
    private void setTab() {
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(sTitle[1]));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中了tab的逻辑
                if(tab.getPosition()==0){
                    typecar=Config.TYPE_TAXI;
                    if(URL_SERVICE!=null||UserHelper.getURL()!=null){
                        refreshNearCar(typecar,mapCenterLatLng);
                    }
                }else if(tab.getPosition()==1){
                    typecar=Config.TYPE_SPECIAL;
                    if (URL_SERVICE!=null||UserHelper.getURL()!=null) {
                        refreshNearCar(typecar, mapCenterLatLng);
                    }
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选中tab的逻辑
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //再次选中tab的逻辑
            }
        });
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
//        startAd.setText("正在选取位置。。。");

    }
/*获取地图移动中心点位置*/
    LatLng oldCenterLat=null;
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mapCenterLatLng = cameraPosition.target;
        mHandler.postDelayed(new RegeocodeRunnable(mapCenterLatLng,GeocodeUtils.START_LATLNG),0);//反地理编码获取地址
        //地图中心点移动超过100米，才刷新附近车辆
        if(oldCenterLat!=null){
            if(AMapUtils.calculateLineDistance(oldCenterLat, cameraPosition.target) >=100) {
                if(URL_SERVICE!=null||UserHelper.getURL()!=null) {
                    refreshNearCar(typecar, mapCenterLatLng);
                }
        }
        }else if(URL_SERVICE!=null||UserHelper.getURL()!=null){
            refreshNearCar(typecar,mapCenterLatLng);
        }
      oldCenterLat=mapCenterLatLng;
    }

    //查询附近出租车
    private void refreshNearCar(int carType,LatLng latLng) {
        //获取参数：类型，当前点位，通过接口查询，解析返回的数据，通过Handler在返回此页面，用mHandler接收数据
            if (carType==2){
                TakeCarDao.getNearTaxiInfo(this,Config.TYPE_TAXI,latLng,mHandler);
            }else if (carType==7){
                TakeCarDao.getNearCarInfo(this,Config.TYPE_SPECIAL,latLng,mHandler);
            }
    }

    //接收接口返回的数据
    private Handler mHandler=new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GeocodeUtils.START_LATLNG:  //移动地图时反地理编码获取地图中心点地址
                    RegeocodeResult startResult = (RegeocodeResult) msg.obj;
                    if (startResult.getRegeocodeAddress().getPois().size()>0){
                        start= startResult.getRegeocodeAddress().getPois().get(0).getTitle();
                        startAd.setText(start+"附近");
                    }else {
                        start= startResult.getRegeocodeAddress().getFormatAddress();//这种返回时格式化地址，有些地址太长
                        if (start.length() < 10) {
                        startAd.setText(start);
                            } else {
                              start = start.substring(6);//去掉多余的省市名
                              startAd.setText(start);
                             }
                    }
                    break;
                case TakeCarDao.NEARTAXI: //获取解析之后的附近出租车数据
                    List<TaxiInfoEntity> list= (List<TaxiInfoEntity>) msg.obj;
                    aMap.clear();
                    if (list != null && list.size() > 0) {
                        for (TaxiInfoEntity info : list) {
                            MarkerOptions markerOptions= new MarkerOptions();
                            LatLng carlat=new LatLng(info.getLat(),info.getLon());
                            GpsToGaodeUtil trans=new GpsToGaodeUtil(MainActivity.this);
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
                    aMap.clear();
                    if (listCar != null && listCar.size() > 0) {
                        for (TaxiInfoEntity info : listCar) {
                            MarkerOptions markerOptions= new MarkerOptions();
                            LatLng carlat=new LatLng(info.getLat(),info.getLon());
                            GpsToGaodeUtil trans=new GpsToGaodeUtil(MainActivity.this);
                            markerOptions.position(trans.GpsToGaode(carlat));
                            markerOptions.title("车牌号:"+info.getTaxiCard()+"\n司机："+info.getDriverName());
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.loc1)));
                            markerOptions.draggable(false);
                            markerOptions.setFlat(true);
                            markerOptions.visible(true);
                            aMap.addMarker(markerOptions);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"附近暂无车辆",Toast.LENGTH_LONG).show();
                    }
                    break;
                case  TakeCarDao.CREATETAXI:
                    List<Guid> createlist= (List<Guid>) msg.obj;
                    if (createlist != null && createlist.size() > 0) {
                        Intent intent = new Intent(MainActivity.this, WaitReplyActivity.class);
                        intent.putExtra(Config.STLATITUDE, mapCenterLatLng.latitude);
                        intent.putExtra(Config.STLONGITUDE, mapCenterLatLng.longitude);
                        intent.putExtra(Config.EDLATITUDE, EndLatLng.latitude);
                        intent.putExtra(Config.EDLONGITUDE, EndLatLng.longitude);
                        intent.putExtra(Config.ST_ADDRESS, start);
                        intent.putExtra(Config.ED_ADDRESS, end);
                        intent.putExtra(Config.KEY_CALL_TYPE, Config.TYPE_TAXI);
                        intent.putExtra(Config.KEY_CALL_GUID, createlist.get(0).getOrderGuid());
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),"创建订单出错",Toast.LENGTH_LONG).show();
                    }
                    break;
                case UserPost.INTERFACE:
                    List<LocationCityEntity> urlList=(List<LocationCityEntity>) msg.obj;
                    if (urlList != null && urlList.size() > 0) {
                        if (searchPlace!=null) {
                            if (searchPlace.equals("佛山市")) {
                                URL_SERVICE=urlList.get(0).getInterface_add();
                            } else if (searchPlace.equals("湛江市")) {
                                URL_SERVICE=urlList.get(1).getInterface_add();
                            } else {
                                Toast.makeText(getApplicationContext(),"当前定位不在佛山市或湛江市内",Toast.LENGTH_LONG).show();
                            }
                            Log.d("POST", "URL_SERVICE:" + URL_SERVICE);
                            UserHelper.saveURL(rtrim(URL_SERVICE));
//                            ToastUtils.show(UserHelper.getURL());
                            Toast.makeText(getApplicationContext(),UserHelper.getURL(),Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case TakeCarDao.ORDERSTATE:
                    List<TripHistoryEntity> orderList=(List<TripHistoryEntity>) msg.obj;
                    if (orderList!=null&&orderList.size()>0) {
                        stateEntity = orderList.get(0);
                        String state= stateEntity.getOrderStatus();
                        if (state!= null) {
                            if (state.equals("0") || state.equals("1")) {
                                Intent intent = new Intent(MainActivity.this, WaitDriverActivity.class);
                                intent.putExtra("state", stateEntity);
                                startActivity(intent);
                            } else if (state.equals("2")) {
                                Intent intent = null;
                                if (typecar == Config.TYPE_TAXI) {
                                    intent = new Intent(MainActivity.this, TaxiTripingActivity.class);
                                } else if (typecar == Config.TYPE_SPECIAL) {
                                    intent = new Intent(MainActivity.this, DriverTripActivity.class);
                                }
                                intent.putExtra("state", stateEntity);
                                startActivity(intent);
                            } else if (state.equals("3") && stateEntity.getPayStatus() == 0) {//到达目的地但并未支付
                                Intent intent = new Intent(MainActivity.this, TaxiPayActivity.class);
                                intent.putExtra("state", stateEntity);
                                startActivity(intent);
                            }
                        }else {
                            return;
                        }
                    }
                    break;
            }
        }
    };
    public static String rtrim(String value){
        if(value==null) return null;
        return value.replaceAll("\\s+$",""); // 去掉右侧末尾的空格
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appcity:
                    Intent intent = new Intent(MainActivity.this, ChooseCityActivity.class);
                    intent.putExtra(ChooseCityActivity.CURR_CITY_KEY, cityModel.getCity());
                    startActivityForResult(intent, REQUEST_CHOOSE_CITY);
                    MainActivity.this.overridePendingTransition(R.anim.slide_in_up, 0);
                break;
            case R.id.meun_loc:
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocLatLng, 16));
                appcity.setText(searchPlace);
                break;
            case R.id.real_time:
                    real_time.setBackgroundResource(R.drawable.tab_background_selected);
                    appoint_time.setBackgroundResource(R.drawable.tab_background_unselected);
                    real_time.setTextColor(getResources().getColor(R.color.news_color1));
                    appoint_time.setTextColor(getResources().getColor(R.color.ababab));
                break;
            case R.id.appoint_time:
                    appoint_time.setBackgroundResource(R.drawable.tab_background_selected);
                    real_time.setBackgroundResource(R.drawable.tab_background_unselected);
                    real_time.setTextColor(getResources().getColor(R.color.ababab));
                    appoint_time.setTextColor(getResources().getColor(R.color.news_color1));
                break;
            case R.id.xingcheng:
                    Methods.toBase(this,MyScheduleActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.money:
                    Methods.toBase(this,MoneyActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.customer_service:
                    Methods.toBase(this,CustomerServiceActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.integral_mall:
                    Methods.toBase(this,JifenMallActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.user_center_more:
                    Methods.toBase(this,MoreSettingActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.main_menu_head_layout:
                Methods.toBase(this,MyInfoActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.staddress:
                    Intent intentst = new Intent(MainActivity.this, ChoosePoiActivity.class);
                    intentst.putExtra(ChoosePoiActivity.POI_TYPE_KEY, START_POI_TYPE);
                    intentst.putExtra(ChoosePoiActivity.CITY_KEY, cityModel);
                    startActivityForResult(intentst, REQUEST_CHOOSE_START_POI);
                    MainActivity.this.overridePendingTransition(R.anim.slide_in_up, 0);
                break;
            case R.id.edaddress:
                    Intent intented = new Intent(MainActivity.this, ChoosePoiActivity.class);
                    intented.putExtra(ChoosePoiActivity.POI_TYPE_KEY, DEST_POI_TYPE);
                    intented.putExtra(ChoosePoiActivity.CITY_KEY, cityModel);
                    startActivityForResult(intented, REQUEST_CHOOSE_DEST_POI);
                    MainActivity.this.overridePendingTransition(R.anim.slide_in_up, 0);
                break;
            case R.id.call_car:
                //如果还有未支付的订单，召车前会弹出窗口确认并前往完成
                if(stateEntity==null||("").equals(stateEntity)) {
                    if (Methods.checkLogin(MainActivity.this)) {
                        Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        if (mapCenterLatLng != null && EndLatLng != null) {
                            TakeCarDao.getCreateTaxi(this, mapCenterLatLng, EndLatLng, start, end, Config.TYPE_TAXI, mHandler);
                        } else {
                            Toast.makeText(getApplicationContext(), "请检查地址是否输入", Toast.LENGTH_LONG).show();
                        }
                    }
                } else{
                    if((stateEntity.getOrderStatus()).equals("3")&&stateEntity.getPayStatus()==0){
                        NoPayDialog(stateEntity);
                    }
                }


                break;
        }
    }

    //如果还有未支付的订单，召车前会弹出窗口确认并前往完成
    private void NoPayDialog(final TripHistoryEntity stateEntity) {
                 ColorDialog dialog = new ColorDialog(this);
                 dialog.setColor("#ffffff");//#8ECB54
                 dialog.setAnimationEnable(true);
                 dialog.setTitle("");
                 dialog.setContentText("您还有一个订单没有完成，请您前往确认，当期情况下不能召车");
                 dialog.setPositiveListener("取消", new ColorDialog.OnPositiveListener() {
                     @Override
                     public void onClick(ColorDialog dialog) {
                         dialog.dismiss();
                     }
                 }).setNegativeListener("前往", new ColorDialog.OnNegativeListener() {
                     @Override
                     public void onClick(ColorDialog dialog) {
                         Intent intent = new Intent(MainActivity.this, TaxiPayActivity.class);
                         intent.putExtra("state",stateEntity);
                         startActivity(intent);
                         dialog.dismiss();
                     }
                 }).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    private static final int REQUEST_CHOOSE_CITY = 1;
    private static final int REQUEST_CHOOSE_START_POI = 2;
    private static final int REQUEST_CHOOSE_DEST_POI = 3;
    private static final int MIN_START_DEST_DISTANCE = 1000;
    private PoiItem mStartPoi=null;
    private PoiItem mDestPoi;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CHOOSE_CITY == requestCode) {
            if (resultCode == RESULT_OK) {
                CityModel cityModel = data.getParcelableExtra(ChooseCityActivity.CURR_CITY_KEY);
                appcity.setText(cityModel.getCity());
                LatLng marker1 = new LatLng(cityModel.getLat(),cityModel.getLng());
                //设置中心点
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
            }
        }
        if (REQUEST_CHOOSE_START_POI == requestCode) {
            if (resultCode == RESULT_OK) {
                try {
                    PoiItem poiItem = data.getParcelableExtra(ChoosePoiActivity.POIITEM_OBJECT);
                    mStartPoi = poiItem;
                    startAd.setText(poiItem.getTitle());
                    //设置中心点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude())));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"请选择正确的目的地",Toast.LENGTH_LONG).show();
                }
            }
        }
        if (REQUEST_CHOOSE_DEST_POI == requestCode) {
            if (resultCode == RESULT_OK) {
                try {
                    PoiItem poiItem = data.getParcelableExtra(ChoosePoiActivity.POIITEM_OBJECT);
                    EndLatLng=new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                    float res[] = new float[1];
                    if (mStartPoi==null){
                        Location.distanceBetween(poiItem.getLatLonPoint().getLatitude(),
                                poiItem.getLatLonPoint().getLongitude(), mLocLatLng.latitude,
                                mLocLatLng.longitude, res);
                    }else {
                        Location.distanceBetween(poiItem.getLatLonPoint().getLatitude(),
                                poiItem.getLatLonPoint().getLongitude(), mStartPoi.getLatLonPoint().getLatitude(),
                                mStartPoi.getLatLonPoint().getLongitude(), res);
                    }
                    if (res[0] <= MIN_START_DEST_DISTANCE) {
                        Toast.makeText(getApplicationContext(),"距离过近，请重新选择目的地",Toast.LENGTH_LONG).show();
                        return;
                    }
                    mDestPoi = poiItem;
                    endAd.setText(poiItem.getTitle());
                    end=poiItem.getTitle();

//                    MarkerOptions markerOptions= new MarkerOptions();
//                    markerOptions.position(EndLatLng);
//                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.loc1)));
//                    markerOptions.draggable(false);
//                    markerOptions.setFlat(true);
//                    markerOptions.visible(true);
//                    aMap.addMarker(markerOptions);
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenterLatLng, 14));
                    forecast.setVisibility(View.VISIBLE);
                    if(mStartPoi==null){ //输入了目的地就规划算出预计里程时间金额
                        mapRouteUtils.searchRoute(new LatLonPoint(mapCenterLatLng.latitude,mapCenterLatLng.longitude), poiItem.getLatLonPoint());
                    }else {
                        mapRouteUtils.searchRoute(mStartPoi.getLatLonPoint(), poiItem.getLatLonPoint());
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"请选择正确的目的地",Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notification) {
//            Methods.toNotice(act);
            Toast.makeText(getApplicationContext(),"消息中心",Toast.LENGTH_LONG).show();
            Methods.toBase(this,MessageCenterActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //显示toolbar menu的icon
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    //android 6.0及以上版本需要手动开启存储权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        if (requestCode == SDK_PERMISSION_REQUEST) {
            if (!mPermissionUtil.verifyPermissions(grantResults)) {
                Toast.makeText(getApplicationContext(),"有部分权限已被拒绝。",Toast.LENGTH_LONG).show();
                //用户不同意，向用户展示该权限作用
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        CustomDialog.Builder builer = new CustomDialog.Builder(this);
                        builer.setTitle("提示");
                        builer.setMessage("安卓手机 6.0及以上系统"+"\n"+"1.应用更新需要赋予访问存储的权限;"
                                                  +"\n" + "2.下单需要上传手机GPS点位，否则无法确定位置不能下单;"
                                                  + "  不开启权限将无法正常工作！"
                                                  +"\n"+ "3.可在弹出框时点击始终允许或进入设置权限管理中找到应用开启权限。"
                                                  +"\n"+ "4.开启权限方可继续使用");
                        builer.setPositiveButton("关闭窗口退出", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                dialog.dismiss();
                            }
                        });
                        CustomDialog dialog = builer.create();
                        dialog.show();
                        return;
                }
            } else {
                Toast.makeText(getApplicationContext(),"权限已通过",Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - Config.LAST_TIME) > Config.INTERVAL_TIME) {
                Toast.makeText(getApplicationContext(),"再按一次退出应用",Toast.LENGTH_LONG).show();
                Config.LAST_TIME = System.currentTimeMillis();
            } else {
                // 关闭所有activity即可。杀掉进程会造成android系统认为是系统意外崩溃，在后台自动重启
                ActivityManager.getAppManager().finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    protected void onResume() {
        super.onResume();
        if (UserHelper.getHeadImgURL()!=null){
            Glide.with(MainActivity.this).load(UserHelper.getHeadImgURL()).into(main_menu_head_userimage);
        }
    }

    @Override
    public void RouteResult(String distence, int Minute, float money) {
        forecast_km.setText("预估距离:"+distence+"km");
        forecast_time.setText("预估时间:"+String.valueOf(Minute)+"min");
        forecast_money.setText("预估金额:"+String.valueOf(money)+"元");
    }

    class RegeocodeRunnable implements Runnable{
        LatLng codeData;
        int type;
        RegeocodeRunnable(LatLng data,int type){
            this.codeData=data;
            this.type=type;
        }
        @Override
        public void run() {
            geocodeUtils.RegeocodeSearchedAMAP(codeData,type);
        }
    }
}
