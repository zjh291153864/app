package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.OrderCancelResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zg
 * 取消订单
 */
public class OrderCancelController extends BaseController {

    public OrderCancelController(Context mContext) {
        super(mContext);
    }

    public void handleCancelOrderByNet(final NetInterface netInterface, String orderId) {

        String url = NetTag.BASEURL.concat(NetTag.ORDERCANCEL);
        Map<String, Object> map = new HashMap<>();
        map.put("order_id", orderId);
        XUtilByNet.post(url, map, new NetCallBack<OrderCancelResponse>() {

            @Override
            public void onSuccess(OrderCancelResponse result) {
                super.onSuccess(result);
                if (netInterface != null) {
                    netInterface.onNetResult(NetTag.ORDERCANCEL, result);
                }
                Log.e("result", result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                netInterface.onNetError("",ex,isOnCallback);
            }
        });
    }

}
