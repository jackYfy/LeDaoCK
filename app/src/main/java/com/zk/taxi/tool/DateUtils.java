package com.zk.taxi.tool;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint({"SimpleDateFormat", "DefaultLocale"})
public class DateUtils {
    public static final String TAG = DateUtils.class.getSimpleName();
    public static final String YMD = "yyyy-MM-dd";
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String HMS = "HH:mm:ss";
    public static final String HM = "HH:mm";
    public static final String YMDHM = "yyyy-MM-dd HH:mm";
    public static final String UT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String YMDHM_ = "yyyyMMddHHmm";
    public static final String WEEK = "EEEE";
    public static final String WEEK_ZHOU= "E";


    // 获取当前日期
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        return sdf.format(c.getTime());
    }
 public static String getWeeek(){
     Calendar c = Calendar.getInstance();//
     int mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
     return "星期"+mWay;
 }
    public static Calendar getTimeByHHmm(String str) {
        String[] times = str.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public static Date getTimeByMMddHHmm(String str) {
        try {
            return new SimpleDateFormat(YMDHMS).parse(Calendar.getInstance().get(Calendar.YEAR) + "-" + str + ":00");
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertByDate(String type, Date date) {
        return new SimpleDateFormat(type).format(date);
    }

    public static Date convertByStr(String type, String time) {
        try {
            return new SimpleDateFormat(type).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getTime() {
        return getTimeByType(YMDHMS);
    }

    public static String getTimeByType(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getTime(String type) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(type);
            return sdf.parse(sdf.format(System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTimeByUT(String type, String str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(type);
            return sdf.format(new SimpleDateFormat(UT).parse(str));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long getLongByUTStr(String type, String str) {
        try {
            return new SimpleDateFormat(type).parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return intToLong(0);
        }
    }

    // java Timestamp构造函数需传入Long型
    public static long intToLong(int i) {
        long result = (long) i;
        result *= 1000;
        return result;
    }

    public static String getStrByStr(String type, String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(str);
    }

    public static String getYMDHM_ByYMDHM(String str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YMDHM_);
            return sdf.format(new SimpleDateFormat(YMDHM).parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //view 转bitmap
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
