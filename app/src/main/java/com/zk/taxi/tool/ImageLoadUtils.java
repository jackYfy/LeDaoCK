package com.zk.taxi.tool;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zk.taxi.R;

import java.io.File;

/**
 * Created by winky on 2016/11/22.
 */
public class ImageLoadUtils {

    private static DisplayImageOptions options = null;
    private static ImageLoaderConfiguration config = null;

    public static ImageLoaderConfiguration getConfiguration(Context context) {
        if (config == null) {
            File cacheDir = StorageUtils.getCacheDirectory(context);  //缓存文件夹路径
            config = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 内存缓存文件的最大长宽.
                    .threadPoolSize(3) // default  线程池内加载的数量
                    .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
                    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
                    .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
                    .diskCache(new UnlimitedDiscCache(cacheDir)) // default 可以自定义缓存路径
                    .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                    .diskCacheFileCount(100)  // 可以缓存的文件数量
                    // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                    .imageDownloader(new BaseImageDownloader(context)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                    .writeDebugLogs() // 打印debug log
                    .build(); //开始构建
        }
        return config;

    }

    public static DisplayImageOptions getOptions() {
        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.default_image) // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.default_image) // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.default_image) // 设置图片加载或解码过程中发生错误显示的图片
                    .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                    .delayBeforeLoading(1000)  // 下载前的延迟时间
                    .cacheInMemory(false) // default  设置下载的图片是否缓存在内存中
                    .cacheOnDisk(false) // default  设置下载的图片是否缓存在SD卡中
                    .considerExifParams(false) // default
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565) // default 设置图片的解码类型
                    .displayer(new RoundedBitmapDisplayer(10)) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
                    .build();
        }
        return options;
    }

    public void clearMemory() {
        ImageLoader.getInstance().clearMemoryCache();  // 清除内存缓存
    }

    public void clearDisk() {
        ImageLoader.getInstance().clearDiskCache();  // 清除本地缓存
    }
}
