package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.MyWalletResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zg
 * 我的钱包
 */
public class MyWalletController extends BaseController {

    public MyWalletController(Context mContext) {
        super(mContext);
    }

    public void handleMyOrderByNet(final NetInterface netInterface, String customerId) {

        String url = NetTag.BASEURL.concat(NetTag.GET_MY_WALLET);
        Map<String, Object> map = new HashMap<>();
        map.put("customer_id", customerId);
        XUtilByNet.post(url, map, new NetCallBack<MyWalletResponse>() {

            @Override
            public void onSuccess(MyWalletResponse result) {
                super.onSuccess(result);
                if (netInterface != null) {
                    netInterface.onNetResult(NetTag.GET_MY_WALLET, result);
                }
                Log.e("result", result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                netInterface.onNetError(NetTag.GET_MY_WALLET,ex,isOnCallback);
            }
        });
    }

}
