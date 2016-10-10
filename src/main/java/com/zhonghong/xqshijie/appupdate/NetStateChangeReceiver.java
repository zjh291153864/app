package com.zhonghong.xqshijie.appupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.zhonghong.xqshijie.util.LogUtils;
import com.zhonghong.xqshijie.util.NetUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by jh on 2016/7/13.
 */
public class NetStateChangeReceiver extends BroadcastReceiver {
    //防止多次调用
    private boolean states = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean netConnected = NetUtils.isNetConnected();
            if (netConnected) {
                int netConnectSubType = NetUtils.getNetConnectSubType(context);
                //wifi 连接
                if (netConnectSubType == 1) {
                } else {
                }
                //流量 连接
                if (netConnectSubType == 0) {

                }
                if (states == true) {
                    states = false;
                    UpdateAppController.startUpdateServiceForDownContinue(context, UpdateConstants.RECEIVER_FLAG, UpdateConstants.RECEIVER_TEXT);
                }
                LogUtils.d("有网");
                EventBus.getDefault().post(new MessageBean(UpdateConstants.ISCONNECT));
            } else {
                LogUtils.d("无网");
                states = true;
                EventBus.getDefault().post(new MessageBean(UpdateConstants.DISCONNECT));
            }
        }
    }
}
