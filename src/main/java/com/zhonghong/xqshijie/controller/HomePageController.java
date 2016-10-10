package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.jingchen.pulltorefresh.parent.ParentInterface;
import com.zhonghong.xqshijie.app.MyApplication;
import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.HomePageResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;
import com.zhonghong.xqshijie.util.FileCacheUtil;
import com.zhonghong.xqshijie.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiezl on 16/6/16.
 */
public class HomePageController extends BaseController {

    private HomePageResponse mRomePageResponse;

    public HomePageController(Context mContext) {
        super(mContext);
    }

    public void handleHomePageByNet(final NetInterface netInterface, final boolean isCache, final String cacheKey, ParentInterface parent) {
        mRomePageResponse = (HomePageResponse) FileCacheUtil.readObject(cacheKey, MyApplication.getAppContext());
        if (mRomePageResponse != null) {
            if (netInterface != null) {
                netInterface.onNetResult(NetTag.GETINDEXCONFIG, mRomePageResponse);
            }
        }
        String url = NetTag.BASEURL.concat(NetTag.GETINDEXCONFIG);
        Map<String, Object> map = new HashMap<>();
        map.put("key", "");

        XUtilByNet.post(url, map, new NetCallBack<HomePageResponse>() {

            @Override
            public void onSuccess(HomePageResponse result) {
                super.onSuccess(result);
                if (isCache && !StringUtils.isNull(cacheKey)) {
                    result.isCache = isCache;
                    FileCacheUtil.saveObject(result, cacheKey, MyApplication.getAppContext());
                }

                if (netInterface != null) {
                    netInterface.onNetResult(NetTag.GETINDEXCONFIG, result);
                }
                Log.e("result", result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if (netInterface != null) {
                    boolean isCaChed = false;
                    if (mRomePageResponse != null) {
                        isCaChed = true;
                    }
                    netInterface.onNetError(null, ex, isCaChed);
                }
            }
        });

    }

}
