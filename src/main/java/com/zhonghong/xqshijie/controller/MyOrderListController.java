package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.MyOrderResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zg
 */
public class MyOrderListController extends BaseController {

    public MyOrderListController(Context mContext) {
        super(mContext);
    }

    public void handleMyOrderByNet(final NetInterface netInterface, String pageSize, String page) {

        String url = NetTag.BASEURL.concat(NetTag.GETMYORDER);
        Map<String, Object> map = new HashMap<>();
        map.put("showCount", pageSize);
        map.put("currentPage", page);
        map.put("customer_id", SharedPreferencesUtil.getInstance(mContext).getStringValue(SharedPreferencesTag.MEMBER_ID));
        XUtilByNet.post(url, map, new NetCallBack<MyOrderResponse>() {

            @Override
            public void onSuccess(MyOrderResponse result) {
                super.onSuccess(result);
                if (netInterface != null) {
                    netInterface.onNetResult("", result);
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
