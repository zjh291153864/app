package com.zhonghong.xqshijie.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zhonghong.xqshijie.util.FileUtils;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import share.com.libshare.ShareHelper;

/**
 * Created by xiezl on 16/6/15.
 */
public class MyApplication extends Application {
    /**
     * activity栈保存
     */
    public List<Activity> activityStack = null;

    /**
     * 百度定位，主线程实例化
     */
    public static LocationClient mLocationClient;
    public LocationManager locaMgr;

    private static MyApplication mInstance = null;

    public static MyApplication getAppContext() {
        return mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        mInstance = this;
        // activity管理
        activityStack = new ArrayList<Activity>();
        // 异常处理
        BaseCrashHandler handler = BaseCrashHandler.getInstance();
        handler.init(this);

        // 程序异常关闭1s之后重新启动
        new RebootThreadExceptionHandler(getBaseContext());

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        locaMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //初始化XUtils3
        x.Ext.init(this);
        //设置debug模式
        x.Ext.setDebug(true);
        //share库的初始化
        ShareHelper.shareinit(this);


        initImageLoader();
    }

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;


    public void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mInstance)
                .threadPriority(Thread.NORM_PRIORITY + 2)
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(10)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(new File(FileUtils.getIconDir(this))))
                .memoryCache(new LRULimitedMemoryCache(2 * 1024 * 1024))
                .build();

        ImageLoader.getInstance().init(config);
    }


    /**
     * 退出应用
     */
    public void exitApp() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
