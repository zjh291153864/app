package com.zhonghong.xqshijie.app;

/**
 * 常量类
 *
 * @author xiezl
 */
public class Constants {
    public static final int AROUND_DURATION = 2000;//异常处理布局为默认2秒。设置为0则为实际加载的时间（可能会略短，无法看见效果)；
    //   项目版本号
    public static int VERSION = 0;
    //   手机型号
    public static String MODELS = "";
    //   手机版本
    public static int MOBALEVERSION = 0;
    public static final String LOG_TAG = "com.zhonghong.xqshijie";//日志TAG
    public static String PACKAGENAME = "com.zhonghong.xqshijie";//包名
    public static final String DBNAME = "xqshijie";//数据库名字
    public static final int DBVERSION = 1;//数据库版本
    /**
     * 子类不重写handler 开关
     */
    public static final int NOT_REWRITE_HANDLER = 0x1000;

}
