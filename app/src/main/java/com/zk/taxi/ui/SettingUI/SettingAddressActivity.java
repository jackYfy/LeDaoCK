package com.zk.taxi.ui.SettingUI;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.PoiItem;
import com.amap.poisearch.searchmodule.SearchModuleDelegate;
import com.amap.poisearch.util.CityModel;
import com.amap.poisearch.util.CityUtil;
import com.amap.poisearch.util.FavAddressUtil;
import com.google.gson.Gson;
import com.zk.taxi.AmapHelper.MapLocationUtils;
import com.zk.taxi.R;
import com.zk.taxi.tool.SystemBarTintManager;
import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.ui.CallCar.SetFavAddressActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingAddressActivity extends AppCompatActivity implements View.OnClickListener {


    String home="长沙";
    String company="佛山";
    private int tag=1;

    @Bind(R.id.delete1)
    AppCompatImageView delete1;
    @Bind(R.id.delete2)
    AppCompatImageView delete2;
    @Bind(R.id.set_home)
    RelativeLayout set_home;
    @Bind(R.id.set_company)
    RelativeLayout set_company;
    @Bind(R.id.delete_address)
    AppCompatTextView delete_address;
    @Bind(R.id.back_setaddress)
    AppCompatImageView back_setaddress;
    @Bind(R.id.set_homeaddress)
    AppCompatTextView set_homeaddress;
    @Bind(R.id.set_companyaddress)
    AppCompatTextView set_companyaddress;

    @Bind(R.id.content_set)
    RelativeLayout content_set;

    private SystemBarTintManager tintManager = null;
    private boolean isSetStatus = true;//标记是否设置默认状态
    private SearchModuleDelegate mSearchModuelDeletage;
    private MapLocationUtils mapLocationUtils;
    public AMapLocationClient mLocationClient = null;
    private AMapLocation mCurrLoc = null;
    private CityModel cityModel=new CityModel();
    private PoiItem mHomePoi;
    private PoiItem mCompPoi;
    public static int MAIN_ACTIVITY_REQUEST_ADDRESS_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_address);
        ButterKnife.bind(this);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        if (isSetStatus)
            tintManager.setStatusBarTintResource(R.color.gsjtsj_title_color);// 通知栏所需颜色
        mSearchModuelDeletage = new SearchModuleDelegate();
        initView();
//        content_set.addView(mSearchModuelDeletage.getWidget(this));
    }

    private void initView() {
        back_setaddress.setOnClickListener(this);
        delete_address.setOnClickListener(this);
        delete1.setOnClickListener(this);
        delete2.setOnClickListener(this);
        set_home.setOnClickListener(this);
        set_company.setOnClickListener(this);
        initLocationStyle();
        mLocationClient.startLocation();
        mHomePoi = FavAddressUtil.getFavHomePoi(getApplicationContext());
        mCompPoi = FavAddressUtil.getFavCompPoi(getApplicationContext());
        if (mHomePoi != null) {
            set_homeaddress.setText(mHomePoi.getTitle());
        }else {
            set_homeaddress.setText("设置家的地址");
        }
        if (mCompPoi != null) {
            set_companyaddress.setText(mCompPoi.getTitle());
        }else {
            set_companyaddress.setText("设置公司的地址");
        }

    }
    private void initLocationStyle() {
        mLocationClient = new AMapLocationClient(getApplicationContext());

        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                if (mCurrLoc == null) {
                    mCurrLoc = aMapLocation;
                   cityModel= CityUtil.getCityByCode(getApplicationContext(), aMapLocation.getCityCode());
                    mSearchModuelDeletage.setCity(cityModel);
                    mSearchModuelDeletage.setCurrLoc(aMapLocation);

                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_setaddress:
                     finish();
                break;
            case R.id.delete_address:
                if(tag==1){
                    if(home!=null){
                        delete1.setVisibility(View.VISIBLE);
                    }
                    if(company!=null){
                        delete2.setVisibility(View.VISIBLE);
                    }
                    delete_address.setText("完成");
                    tag=2;
                } else if(tag==2){
                    delete1.setVisibility(View.GONE);
                    delete2.setVisibility(View.GONE);
                    delete_address.setText("删除");
                    tag=1;
                }

                break;
            case R.id.delete1:
                set_homeaddress.setText("设置家的地址");
                ToastUtils.delFavHomePoi(getApplicationContext());//移除SharedPreferences中家的地址
                break;
            case R.id.delete2:
                set_companyaddress.setText("设置公司的地址");
                ToastUtils.delFavCompPoi(getApplicationContext());
                break;
            case R.id.set_home:
                toSetFavAddressActivity(0);
                break;
            case R.id.set_company:
                toSetFavAddressActivity(1);
                break;

        }

    }

    private void toSetFavAddressActivity(int type) {
        Intent intent = new Intent();
        intent.setClass(SettingAddressActivity.this, SetFavAddressActivity.class);
        intent.putExtra(FAVTYPE_KEY, type);
        Gson gson = new Gson();
        intent.putExtra(SetFavAddressActivity.CURR_CITY_KEY, gson.toJson(mSearchModuelDeletage.getCurrCity()));
        intent.putExtra(SetFavAddressActivity.CURR_LOC_KEY, mCurrLoc);
        startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_ADDRESS_CODE);
    }

    public static final String FAVTYPE_KEY = "favtype";
    public static final String POIITEM_OBJECT = "poiitem_object";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (MAIN_ACTIVITY_REQUEST_ADDRESS_CODE == requestCode && resultCode == RESULT_OK) {
            String poiitemStr = data.getStringExtra(POIITEM_OBJECT);
            int favType = data.getIntExtra(FAVTYPE_KEY, -1);

            PoiItem poiItem = new Gson().fromJson(poiitemStr, PoiItem.class);

            if (favType == 0) {
                FavAddressUtil.saveFavHomePoi(getApplicationContext(), poiItem);
                set_homeaddress.setText(poiItem.getTitle());
            } else if (favType == 1) {
                FavAddressUtil.saveFavCompPoi(getApplicationContext(), poiItem);
                set_companyaddress.setText(poiItem.getTitle());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setTranslucentStatus() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        mCurrLoc = null;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
