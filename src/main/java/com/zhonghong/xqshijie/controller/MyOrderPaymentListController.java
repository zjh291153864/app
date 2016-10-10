package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.MyOrderPaymentListResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zg
 */
public class MyOrderPaymentListController extends BaseController {

    public MyOrderPaymentListController(Context mContext) {
        super(mContext);
    }

    public void handleMyOrderPaymentsListByNet(final NetInterface netInterface, String orderId) {

        String url = NetTag.BASEURL.concat(NetTag.GETMYORDERPAYMENTLIST);
        Map<String, Object> map = new HashMap<>();
        map.put("order_id", 10453+" ");
        XUtilByNet.post(url, map, new NetCallBack<MyOrderPaymentListResponse>() {

            @Override
            public void onSuccess(MyOrderPaymentListResponse result) {
                super.onSuccess(result);
                if (netInterface != null) {
                    netInterface.onNetResult(NetTag.GETMYORDERPAYMENTLIST, result);
                }
                Log.e("result", result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if (netInterface != null) {
                    netInterface.onNetError(NetTag.GETMYORDERPAYMENTLIST,ex,isOnCallback);
                }

            }
        });
    }

}
