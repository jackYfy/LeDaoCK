package com.zk.taxi.tool;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/3/12.
 */

public class CompressPicUtils {
    static String targetPath;
    private Activity contxt;

public CompressPicUtils(Activity act){
    contxt=act;
    targetPath=FileUtils.getSdcardDir() +"/"+ act.getPackageName()+"/CompressPicture"+"/";
}

    public static String getimage(String srcPath) {

        String compressdPicPath = "";
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //设置手机800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        Log.e("TAG1","byte-0-:"+bitmap.getAllocationByteCount()/ 1024);
        //return compressImage(bitmap,srcPath);
        //压缩好比例大小后再进行质量压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>1024) {  //循环判断如果压缩后图片是否大于1M,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        Log.e("TAG1","byte-1-:"+baos.toByteArray().length/ 1024);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap1 = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        File dir = new File(targetPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File bitmapFile = new File(targetPath+ srcPath.substring(srcPath.lastIndexOf("/") + 1));
        if(!bitmapFile.exists()) {
            try {
                bitmapFile.createNewFile();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(bitmapFile);
            // 第一参数是图片格式，第二个是图片质量,保留原图像100%的品质，第三个是输出流
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            compressdPicPath=bitmapFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return compressdPicPath;
    }

}
