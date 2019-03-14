package com.zk.taxi.tool;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.zk.taxi.Application;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


public class NetWorkUtils {

    private static NetworkInfo networkInfo = null;

    public static NetworkInfo getNetworkInfo() {
        if (networkInfo == null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) Application.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo;
    }

    /**
     * 判断网络是否可用
     *
     * @return
     */
    public static boolean isAvailable() {
        NetworkInfo networkInfo = getNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    /**
     * 是否连接上wifi
     * @return
     */
    public static boolean isWifi() {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查当前环境网络是否可用，不可用跳转至开启网络界面,不设置网络强制关闭当前Activity
     */
    public static void validateNetWork(final Activity act) {
        if (!isAvailable()) {
            Builder dialogBuilder = new Builder(act);
            dialogBuilder.setTitle("网络设置");
            dialogBuilder.setMessage("网络不可用，是否现在设置网络？");
            dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    act.startActivityForResult(new Intent(Settings.ACTION_SETTINGS), which);
                }
            });
            dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialogBuilder.create();
            dialogBuilder.show();
        }
    }

    /**
     * 获取手机当前ip
     *
     * @return
     */
    public static String getHostIp() {
        String ipaddress = "127.0.0.1";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
            return ipaddress;
        } catch (Exception e) {
            return ipaddress;
        }
    }
}
