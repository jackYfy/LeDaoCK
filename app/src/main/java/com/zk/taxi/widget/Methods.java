package com.zk.taxi.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.zk.taxi.R;
import com.zk.taxi.tool.UserHelper;
import com.zk.taxi.ui.LoginAccountActivity;
import com.zk.taxi.ui.MainActivity;


/**
 * Created by Administrator on 2017/9/13.
 */

public class Methods {
    public static void toLogin(Activity act) {
        toBase(act, LoginAccountActivity.class);
    }

    public static void toMain(Activity act) {
        toBase(act, MainActivity.class);
        act.finish();
    }
    public static <T> void toBase(Activity act, Class<T> cls) {
        Intent intent = new Intent(act, cls);
        act.startActivity(intent);
        act.overridePendingTransition(0, R.anim.slide_out_down);
    }
    public static String[] getEvaluates() {
        String[] star = {"差评", "差评", "有待提升", "一般", "满意", "非常满意"};
        return star;
    }
    public static String[] getCancelReason() { //标记从0开始
        String[] reson = {"行程有变，暂时不需要用车", "赶时间，换用其他交通工具",
                "平台派单太远", "司机以各种原因不来接我", "联系不上司机", "司机要求加价","司机找不到上车地点"};
        return reson;
    }
    public static String[] getStrokeType() { //标记从0开始
        String[] Type = {"服务态度恶劣", "迟到",
                "故意绕路", "未上车收费", "多收附加费", "其他"};
        return Type;
    }
    public static void toCalling(Activity act, String tel) {
        // 用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        act.startActivity(intent);
    }
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            PackageInfo info = new PackageInfo();
            info.versionName = "-1";
            return info;
        }
    }
    /**
     * 检查是否登录，true 为未登录
     *
     * @param act
     * @return
     */
    public static boolean checkLogin(Activity act) {
        if (UserHelper.getUserInfo() == null) {
            toLogin(act);
            return true;
        } else {
            return false;
        }
    }

        //获取版本名
       public static String getVersionName(Context context) {
             return getPackageInfo(context).versionName;
         }

          //获取版本号
          public static int getVersionCode(Context context) {
             return getPackageInfo(context).versionCode;
         }

}
