package com.zk.taxi.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.zk.taxi.Application;

import java.text.DecimalFormat;

public class ToastUtils {
    public static void show(String text) {
        handler.obtainMessage(WHAT_TEXT, text).sendToTarget();
    }

    private static final int WHAT_TEXT = 10000;

    private static Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_TEXT:
                    if (toast != null) {
                        toast.cancel();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        init(msg.obj.toString()).show();
                    }
            }
            return true;
        }
    });
    //string类型转百分比
    public static String percent(String string){
        double aa=Double.parseDouble(string);
        DecimalFormat df = new DecimalFormat("0.00%");
        String r = df.format(aa);
        return r;
    }
    //double类型转百分比
    public static String percent1(Double d){
        DecimalFormat df = new DecimalFormat("0.00%");
        String r = df.format(d);
        return r;
    }

    private static Toast toast = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static Toast init(String msg) {
        Context context = Application.getInstance();
        toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView tv = new TextView(context);
        int dpPadding = 15;
        tv.setPadding(dpPadding, dpPadding, dpPadding, dpPadding);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setText(msg);
        LinearLayout mLayout = new LinearLayout(context);
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(ContextCompat.getColor(context, android.R.color.black));
        shape.setCornerRadius(10);
        shape.setStroke(1, ContextCompat.getColor(context, android.R.color.black));
        shape.setAlpha(180);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mLayout.setBackground(shape);
        }
        mLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mLayout.setLayoutParams(params);
        mLayout.setGravity(Gravity.CENTER);
        mLayout.addView(tv);
        toast.setView(mLayout);
        return toast;
    }
    public static void Speak(Context context, String txt) {
        XunFeiTTSUtils.getInstance().init(context); //初始化语音对象
        XunFeiTTSUtils.getInstance().speakText(txt); //播放语音
    }
    /**
     * 判断字符串是否为空
     * @param str 字符串
     * @return true/false
     */
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    public static String isTxt(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return "";
        }
        return str;
    }
   //秒转换为分秒
    public static String getTime(int second){
        if (second < 10) {
            return "00:0" + second;
        }
        if (second < 60) {
            return "00:" + second;
        }

        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + ":0" + second;
                }
                return "0" + minute + ":" + second;
            }
            if (second < 10) {
                return minute + ":0" + second;
            }
            return minute + ":" + second;
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        return minute + ":" + second;
    }
    //删除常用地址中的家 或公司
    public static void delete(Context context, String key) {
        if (context == null) {
            return;
        }

        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getClass().getPackage().getName(),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
    public static final String FAV_HOME_ADDRESS_KEY = "fav_home_address_key";
    public static final String FAV_COMP_ADDRESS_KEY = "fav_comp_address_key";

    public static void delFavHomePoi(Context context) {
        delete(context, FAV_HOME_ADDRESS_KEY);
    }

    public static void delFavCompPoi(Context context){
        delete(context, FAV_COMP_ADDRESS_KEY);
    }
}
