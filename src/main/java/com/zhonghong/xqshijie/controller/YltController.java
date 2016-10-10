package com.zhonghong.xqshijie.controller;

import android.content.Context;

import com.jingchen.pulltorefresh.parent.ParentInterface;
import com.zhonghong.xqshijie.app.MyApplication;
import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.YltHomePageResponse;
import com.zhonghong.xqshijie.data.response.YltProductResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;
import com.zhonghong.xqshijie.util.FileCacheUtil;
import com.zhonghong.xqshijie.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jh on 2016/6/29.
 */

public class YltController extends BaseController {

    private YltHomePageResponse yltHomePageResponse;

    public YltController(Context mContext) {
        super(mContext);
    }

    public void handleYltHomePageByNet(final NetInterface netInterface, final boolean isCache, final String cacheKey, ParentInterface parent) {
        yltHomePageResponse = (YltHomePageResponse) FileCacheUtil.readObject(cacheKey, MyApplication.getAppContext());
        if (yltHomePageResponse !=null){
            if (netInterface != null ) {
                netInterface.onNetResult(NetTag.GETYLTHOMEPAGE, yltHomePageResponse);
            }
        }

//        if(parent!=null){
//            RefreshUtils.AlertWhichParentlayout(parent,RefreshUtils.NetState.NET_SHOWLOADING,null);
//        }

        String url = NetTag.BASEURL.concat(NetTag.GETYLTHOMEPAGE);
        Map<String, Object> map = new HashMap<>();
        map.put("key", "");
        XUtilByNet.post(url, map, new NetCallBack<YltHomePageResponse>() {

            @Override
            public void onSuccess(YltHomePageResponse result) {
                super.onSuccess(result);
                if (isCache&& !StringUtils.isNull(cacheKey)){
                    result.isCache = isCache;
                    FileCacheUtil.saveObject(result, cacheKey, MyApplication.getAppContext());
                }

                if (netInterface != null) {
                    netInterface.onNetResult(NetTag.GETYLTHOMEPAGE, result);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);

                if (netInterface != null) {
                    netInterface.onNetError(null, null, false);
                }
            }
        });
    }


    /**
     * 获取逸乐通产品列表
     * @param netInterface
     * @param pageSize
     * @param page
     * @param isCache
     * @param cacheKey
     */
    public void handleYltProductListByNet(final NetInterface netInterface, String pageSize, final String page, final boolean isCache, final String cacheKey,ParentInterface parent) {
        YltProductResponse yltProductResponse = (YltProductResponse) FileCacheUtil.readObject(cacheKey.concat(page), MyApplication.getAppContext());

        if (yltProductResponse!=null){
            if (netInterface != null) {
                netInterface.onNetResult(NetTag.GETYLTPRODUCTS, yltProductResponse);
            }
        }

        String url = NetTag.BASEURL.concat(NetTag.GETYLTPRODUCTS);
        Map<String, Object> map = new HashMap<>();
        map.put("key", "");
        map.put("showCount", pageSize);
        map.put("currentPage", page);
        XUtilByNet.post(url, map, new NetCallBack<YltProductResponse>() {
            @Override
            public void onSuccess(YltProductResponse result) {
                super.onSuccess(result);
                if (isCache&& !StringUtils.isNull(cacheKey)){
                    result.isCache = isCache;
                    FileCacheUtil.saveObject(result, cacheKey.concat(page), MyApplication.getAppContext());
                }

                if (netInterface != null) {
                    result.netPageNo = page;
                    netInterface.onNetResult(NetTag.GETYLTPRODUCTS, result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                boolean hadCaChed = false;
                if(yltHomePageResponse!=null){
                    hadCaChed = true;
                }
                if (netInterface != null) {
                    netInterface.onNetError(NetTag.GETYLTPRODUCTS,ex,hadCaChed);
                }
            }
        });

    }

}
