package com.zhonghong.xqshijie.appupdate;

/**
 * Created by jh on 2016/7/14.
 */
public class UpdateConstants {
    /**
     * 软件更新涉及的数据
     */
    public static final  String  RECEIVER_TEXT="wifiReceiver";
    public static final  String  RECEIVER_FLAG="receiver";
    public static final  String  ACTIVITY_FLAG="update";
    public static final  String  ACTIVITY_CONTENT="content";
    /**
     * 取消更新
     */
    public static final  String CANCEL_NETERROR="cancel";//网络失败
    public static final  String CANCEL_ISNEW="isnew";//当前最新版本
    public static final  String CANCEL_UPDATE="new";//有更新
    /**
     * 有无网络
     */
    public static final  boolean ISCONNECT=true;//网络有
    public static final  boolean DISCONNECT=false;//网络无
}
