package com.zhonghong.xqshijie.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.zhonghong.xqshijie.app.MyApplication;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;


/**
 * 网络相关工具类
 */
public class NetUtils {

    /**
     * 当前无网络
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * WIFI网络开启
     */
    public static final int NETWORK_TYPE_WIFI = 1;
    /**
     * mobile网络开启：2G、2.5G、3G、3.5G等
     */
    public static final int NETWORK_TYPE_MOBILE = 2;
    /**
     * wifi和mobile网络都已开启
     */
    public static final int NETWORK_TYPE_ALL = 3;

    /**
     * 当前网络为3G网络
     */
    public static final int NETWORK_TYPE_MOBILE_3G = 10;
    /**
     * 当前网络为4G网络
     */
    public static final int NETWORK_TYPE_MOBILE_4G = 11;
    /**
     * 当前网络为2G或者其他网络
     */
    public static final int NETWORK_TYPE_MOBILE_OTHER = 12;


    /**
     * 获取当然有效的网络类型，该函数只区分WIFI和MOBILE。详细区分
     * wifi、2g、3g、4g请查看函数：<BR>
     *
     * @param context
     * @return int 网络类型
     * @see #getNetConnectSubType(Context)
     * NetUtils.getNetConnectType()<BR>
     */
    public static int getNetConnectType(Context context) {
        int res = 0;
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (((wifi != null) && wifi.isAvailable() && wifi.isConnectedOrConnecting())
                && ((mobile != null) && mobile.isAvailable() && mobile.isConnectedOrConnecting())) {
            res = NETWORK_TYPE_ALL;
            LogUtils.d("getNetConnectType:----- ConnectType = ------" + "NETWORK_TYPE_ALL");
        } else if ((wifi != null) && wifi.isAvailable() && wifi.isConnectedOrConnecting()) {
            res = NETWORK_TYPE_WIFI;
            LogUtils.d("getNetConnectType:----- ConnectType = ------" + "NETWORK_TYPE_WIFI");
        } else if ((mobile != null) && mobile.isAvailable() && mobile.isConnectedOrConnecting()) {
            res = NETWORK_TYPE_MOBILE;
            LogUtils.d("getNetConnectType:----- ConnectType = ------" + "NETWORK_TYPE_MOBILE");
        } else {
            res = NETWORK_TYPE_UNKNOWN;
            LogUtils.d("getNetConnectType:----- ConnectType = ------" + "NETWORK_TYPE_UNKNOWN");
        }
        LogUtils.d("getNetConnectType:----- end ------");
        return res;
    }


    /**
     * 获取当前有效网络类型，能够详细区分WIFI、2G、3G等网络类型。如果想只区分
     * WIFI和MOBILE，请查看函数：
     *
     * @param context
     * @return
     * @see #getNetConnectType(Context)
     * NetUtils.getNetConnectSubType()<BR>
     */
    public static int getNetConnectSubType(Context context) {
        int type = NETWORK_TYPE_UNKNOWN;
        int subtype = NETWORK_TYPE_UNKNOWN;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if ((activeNetInfo != null) && activeNetInfo.isConnectedOrConnecting()) {
            type = activeNetInfo.getType();
            LogUtils.d("getNetConnectSubType: activeNetInfo.getType() = " + type);
            if (type == ConnectivityManager.TYPE_WIFI) {
                type = NETWORK_TYPE_WIFI;
                subtype = type;
                LogUtils.d("getNetConnectSubType: subtype = " + "NETWORK_TYPE_WIFI");
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                switch (activeNetInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:// ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_CDMA:// ~ 14-64 kbps IS95A or IS95B
                    case TelephonyManager.NETWORK_TYPE_EDGE:// ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_GPRS: // ~ 100 kbps
                        subtype = NETWORK_TYPE_MOBILE_OTHER;
                        LogUtils.d("getNetConnectSubType: subtype = " + "NETWORK_TYPE_MOBILE_OTHER");
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:// ~ 400-1000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:// ~ 600-1400 kbps
//				case TelephonyManager.NETWORK_TYPE_HSDPA: // ~ 2-14 Mbps
//				case TelephonyManager.NETWORK_TYPE_HSPA: // ~ 700-1700 kbps
//				case TelephonyManager.NETWORK_TYPE_HSUPA:// ~ 1-23 Mbps
                    case TelephonyManager.NETWORK_TYPE_UMTS:// ~ 400-7000 kbps
                        subtype = NETWORK_TYPE_MOBILE_3G;
                        LogUtils.d("getNetConnectSubType: subtype = " + "NETWORK_TYPE_MOBILE_3G");
                        break;
                    // NOT AVAILABLE YET IN API LEVEL 7
                    // case Connectivity.NETWORK_TYPE_EHRPD:// ~ 1-2 Mbps
                    // case Connectivity.NETWORK_TYPE_EVDO_B:// ~ 5 Mbps
                    // case Connectivity.NETWORK_TYPE_HSPAP:// ~ 10-20 Mbps
                    // case Connectivity.NETWORK_TYPE_LTE:// ~ 10+ Mbps
                    //	 subtype = NETWORK_TYPE_MOBILE_4G;
                    //break;
                    // Unknown
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    default:
                        subtype = NETWORK_TYPE_UNKNOWN;
                        LogUtils.d("getNetConnectSubType: subtype = " + "NETWORK_TYPE_UNKNOWN");
                        break;
                }
            }
        }
        LogUtils.d("getNetConnectSubType:----- end ------");
        return subtype;
    }

    /**
     * 判断终端网络是否有效
     *
     * @return boolean TRUE:代表网络有效
     */
    public static boolean isNetConnected() {
        return getNetConnectType(MyApplication.getAppContext()) != NETWORK_TYPE_UNKNOWN;
    }

    /**
     * 获取绝对路径
     *
     * @return
     */

    /**
     * @param relativeUrl 相对路径
     * @param splitDomain 外面指定域名
     * @return
     */
    public static String getDealUrl(String relativeUrl, String splitDomain) {
        String absoluteUrl = "";
        String domain = SharedPreferencesUtil.getInstance(MyApplication.getAppContext()).getStringValue(SharedPreferencesTag.DOMAIN_URL, "https://m.xqshijie.com/");//域名
        if (!StringUtils.isNull(relativeUrl) && (relativeUrl.indexOf("http://") == -1 || relativeUrl.indexOf("https://") == -1)) {
            if (StringUtils.isNull(splitDomain)) {
                absoluteUrl = domain.concat(relativeUrl);
            }else{
                if (relativeUrl.startsWith("/")){
                    relativeUrl = relativeUrl.substring("/".length());
                }
                absoluteUrl = splitDomain.concat(relativeUrl);
            }
        } else if (!StringUtils.isNull(relativeUrl)) {
            absoluteUrl = relativeUrl;
        }
        return absoluteUrl;
    }

}

	