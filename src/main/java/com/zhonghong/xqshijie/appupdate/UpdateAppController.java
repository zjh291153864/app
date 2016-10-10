package com.zhonghong.xqshijie.appupdate;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.UpdateAppResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;

import java.util.HashMap;
import java.util.Map;

/**
 * 软件版本相关控制器
 * Created by jh on 2016/7/4.
 */
public class UpdateAppController extends BaseController {
    public UpdateAppController(Context mContext) {
        super(mContext);
    }

    //    获取软件信息
    public void handleGetUpdateAppByNet(final NetInterface netInterface, String nowVersion) {

        String url = NetTag.BASEURL + NetTag.GET_APP_NEW_VERSION;
        Map<String, Object> map = new HashMap<>();
        map.put("key", "");
        map.put("app_version", nowVersion);
        XUtilByNet.post(url, map, new NetCallBack<UpdateAppResponse>() {
            @Override
            public void onSuccess(UpdateAppResponse result) {
                super.onSuccess(result);
                if (netInterface != null) {
                    netInterface.onNetResult("", result);
                }
                Log.e("result", result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if (netInterface != null) {
                    netInterface.onNetError("", ex, isOnCallback);
                }
            }
        });
    }

    /**
     * 开启服务去获取软件更新信息
     *
     * @param context
     */
    public static void startUpdateService(Context context) {
        Intent service = new Intent();
        service.setClass(context, UpdateAppService.class);
        context.startService(service);
    }

    /**
     * 开启服务去断点下载更新
     *
     * @param context
     */
    public static void startUpdateServiceForDownContinue(Context context, String flag, String value) {
        Intent service = new Intent();
        service.setClass(context, UpdateAppService.class);
        service.putExtra(flag, value);
        context.startService(service);
    }

    /**
     * 开启服务去下载软件
     *
     * @param context
     */
    public static void startUpdateServiceForDown(Context context, String flag, boolean value) {
        Intent service = new Intent();
        service.setClass(context, UpdateAppService.class);
        service.putExtra(flag, value);
        context.startService(service);
    }

    /**
     * 停止服务
     */
    public static void stopUpdateService(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UpdateAppService.class);
        context.stopService(intent);
    }

    /**
     * 注册接收网络改变广播
     */
    public static void registerNetStateReceiver(Context context, NetStateChangeReceiver mNetChangedReceiver) {
        if (mNetChangedReceiver != null) {
            IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(mNetChangedReceiver, filter);
        }

    }

    /**
     * 解注册广播
     */
    public static void unRegistrNetStateReceiver(Context context, NetStateChangeReceiver mWifiReceiver) {
        if (mWifiReceiver != null) {
            context.unregisterReceiver(mWifiReceiver);
        }
    }

    /**
     * 开启提示下载的ActivityDialog
     *
     * @param context
     * @param b
     * @param content
     */
    public static void startUpdateActivityDialog(Context context, boolean b, String content) {
        Intent intentUpdate = new Intent(context, UpdateAppDialogActivity.class);
        intentUpdate.putExtra(UpdateConstants.ACTIVITY_FLAG, b);
        intentUpdate.putExtra(UpdateConstants.ACTIVITY_CONTENT, content);
        intentUpdate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentUpdate);
    }

}
