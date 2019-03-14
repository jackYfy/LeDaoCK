package com.zk.taxi.ui;

import android.app.DownloadManager;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import com.zk.taxi.tool.ToastUtils;
import com.zk.taxi.widget.ColorDialog;

import java.io.File;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class DownLoadService extends Service {


    private DownloadManager dm;//**系统下载管理器*/
    private long enqueue;///**系统下载器分配的唯一下载任务id，可以通过这个id查询或者处理下载任务*/
    private String downloadUrl=null;/**下载地址 */
    private String DOWNLOADPATH = "/com.zk.taxi/apk/";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
            return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        downloadUrl = intent.getStringExtra("downloadurl");//获取下载地址
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ DOWNLOADPATH;//获取存放APK路径
        File file = new File(path);
        if(file.exists()){
            deleteAllFiles(file);//删除该文件夹下所有文件
            //deleteFileWithPath(path);//删除该文件
        }
        initDownManager();
        return Service.START_STICKY;
    }

    private void initDownManager() {
        final String packageName = "com.zk.taxi";
        int state =getPackageManager().getApplicationEnabledSetting(packageName);
        //检测下载管理器是否被禁用
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED //不可用状态
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER // 隐藏应用图标,用户禁止启动该应用程序
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            ColorDialog dialog = new ColorDialog(this);
            dialog.setColor("#ffffff");//#8ECB54
            dialog.setAnimationEnable(true);
            dialog.setTitle("温馨提示");
            dialog.setContentText("系统下载管理器被禁止，需手动打开");
            dialog.setPositiveListener("取消", new ColorDialog.OnPositiveListener() {
                @Override
                public void onClick(ColorDialog dialog) {
                    dialog.dismiss();
                }
            }).setNegativeListener("确定", new ColorDialog.OnNegativeListener() {
                @Override
                public void onClick(ColorDialog dialog) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + packageName));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        startActivity(intent);
                    }
                    dialog.dismiss();
                }
            }).show();
        } else {
            dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            //设置下载地址
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(downloadUrl));
            //设置下载文件的类型
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI); //  设置允许使用的网络类型，这里是移动网络和wifi都可以
            down.setDestinationInExternalPublicDir(DOWNLOADPATH,"LeDao.apk");// 设置下载后文件存放的位置
            down.setTitle("乐道");
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE| DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);// 下载时，通知栏显示途中
            down.allowScanningByMediaScanner();//设置为可被媒体扫描器找到
            down.setVisibleInDownloadsUi(true);// 显示下载界面
            down.setAllowedOverRoaming(false);
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadUrl));
            down.setMimeType(mimeString);
//            dm.enqueue(down);// 将下载请求放入队列
            //执行下载，并返回任务唯一id
            enqueue = dm.enqueue(down);
            registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));//注册下载广播
        }
    }

    //广播监听下载的各个状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(dm.getUriForDownloadedFile(downId)!=null){
                   ToastUtils.show("下载完成");
                    //自动安装apk
                    installAPK(context,getRealFilePath(context,dm.getUriForDownloadedFile(downId)));
                }else{
                    ToastUtils.show("下载失败");
                }
                //停止服务并关闭广播
                DownLoadService.this.stopSelf();
            }
        }
        private void installAPK(Context context, String path) {
            File file = new File(path);
            if(file.exists()){
                openFile(file,context);
            }else{
                ToastUtils.show("下载失败");
            }
        }
    };


    public String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    /**
     *重点在这里
     */
    public void openFile(File apkfile, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        //android 6.0
        if(Build.VERSION.SDK_INT>=23){
            Uri uriForFile = FileProvider.getUriForFile(context,"com.zk.taxi.myfileprovider", apkfile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uriForFile,"application/vnd.android.package-archive");
        }/*//大于android 6.0的，7.0可以用
        else if(Build.VERSION.SDK_INT > 23){
            Uri apkUri =FileProvider.getUriForFile(context,"com.zk.taxi.myfileprovider", apkfile);
            if (apkfile.exists()) {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            }
        } */  //小于android 6.0的系统
        else{
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");//getMIMEType(var0)
        }
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            ToastUtils.show("没有找到打开此类文件的程序");
        }
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        //unregisterContentObserver();
        super.onDestroy();
    }


    public  static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }
}
