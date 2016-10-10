package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.jingchen.pulltorefresh.parent.ParentInterface;
import com.zhonghong.xqshijie.app.MyApplication;
import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.ScenicBannersResponse;
import com.zhonghong.xqshijie.data.response.ScenicProductResponse;
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
public class ScenicController extends BaseController {

    private ScenicProductResponse mScenicProdRspList;
    private ScenicProductResponse mScenicProdforaddress;
    private ScenicBannersResponse scenicBannersResponse;

    public ScenicController(Context mContext) {
        super(mContext);
    }

    /**
     * 分页获取新奇景区列表
     *
     * @param netInterface
     * @param size
     * @param pages
     */
    public void handleScenicByNet(final NetInterface netInterface, String size, final String pages, final boolean isCache, final String cacheKey, ParentInterface parent) {
        /**
         * 首先从缓存中获取对象
         */
        mScenicProdRspList = (ScenicProductResponse) FileCacheUtil.readObject(cacheKey.concat(pages), MyApplication.getAppContext());
        if (mScenicProdRspList != null) {
            if (netInterface != null) {
                netInterface.onNetResult(NetTag.GETSCENICPRODUCT, mScenicProdRspList);
            }
        }


        /**
         * 再次从网络中获取
         */
        String url = NetTag.BASEURL.concat(NetTag.GETSCENICPRODUCT);
        Map<String, Object> map = new HashMap<>();
        map.put("key", "");
        map.put("currentPage", pages);
        map.put("showCount", size);
        XUtilByNet.post(url, map, new NetCallBack<ScenicProductResponse>() {

            @Override
            public void onSuccess(ScenicProductResponse result) {
                super.onSuccess(result);
                /**
                 * 保存缓存对象
                 */
                if (isCache && !StringUtils.isNull(cacheKey)) {
                    result.isCache = isCache;
                    FileCacheUtil.saveObject(result, cacheKey.concat(pages), MyApplication.getAppContext());
                }

                if (netInterface != null) {
                    netInterface.onNetResult(NetTag.GETSCENICPRODUCT, result);
                }
                Log.e("result", result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("result", "onError...");
                Log.e("result", "isOnCallback..." + isOnCallback);
                netInterface.onNetError(NetTag.GETSCENICPRODUCT, ex, checKingCaChe());
                ex.printStackTrace();
            }
        });
    }

    /**
     * 获取景区的轮播图
     *
     * @param netInterface
     * @param isCache
     * @param cacheKey
     */

    public void handleScenicBannerByNet(final NetInterface netInterface, final boolean isCache, final String cacheKey) {
        /**
         * 首先从缓存中获取对象
         */
        scenicBannersResponse = (ScenicBannersResponse) FileCacheUtil.readObject(cacheKey, MyApplication.getAppContext());
        if (scenicBannersResponse != null) {
            if (netInterface != null) {
                netInterface.onNetResult(NetTag.GETSCENICBANNER, scenicBannersResponse);
            }
        }


        String url = NetTag.BASEURL.concat(NetTag.GETSCENICBANNER);
        Map<String, Object> map = new HashMap<>();
        map.put("key", "");
        XUtilByNet.post(url, map, new NetCallBack<ScenicBannersResponse>() {

            @Override
            public void onSuccess(ScenicBannersResponse result) {
                super.onSuccess(result);
                /**
                 * 保存缓存对象
                 */
                if (isCache && !StringUtils.isNull(cacheKey)) {
                    result.isCache = isCache;
                    FileCacheUtil.saveObject(result, cacheKey, MyApplication.getAppContext());
                }

                if (netInterface != null) {
                    netInterface.onNetResult(NetTag.GETSCENICBANNER, result);
                }
                Log.e("result", result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("result", "onError...");
                Log.e("result", "isOnCallback..." + isOnCallback);
                netInterface.onNetError(NetTag.GETSCENICBANNER, ex, checKingCaChe());
                ex.printStackTrace();
            }
        });
    }


    /**
     * 根据目的地分页获取新奇景区列表
     *
     * @param netInterface
     * @param size
     * @param pages
     * @param mProvinceId
     * @param mCityId
     * @param isCache
     * @param cacheKey
     */
    public void handleScenicForAddressByNet(final NetInterface netInterface, String size, final String pages, final String mProvinceId, String mCityId, final boolean isCache, final String cacheKey) {
        /**
         * 首先从缓存中获取对象
         */
        mScenicProdforaddress = (ScenicProductResponse) FileCacheUtil.readObject(cacheKey.concat(StringUtils.repNull(mProvinceId)).concat(pages), MyApplication.getAppContext());
        if (mScenicProdforaddress != null) {
            if (netInterface != null) {
                netInterface.onNetResult(NetTag.GETPROBYREGION, mScenicProdforaddress);
            }
        }

        /**
         * 再次从网络中获取
         */
        String url = NetTag.BASEURL.concat(NetTag.GETPROBYREGION);
        Map<String, Object> map = new HashMap<>();
        map.put("key", "");
        map.put("currentPage", pages);
        map.put("showCount", size);
        map.put("project_province", mProvinceId);
        map.put("project_city", StringUtils.repNull(mCityId));
        XUtilByNet.post(url, map, new NetCallBack<ScenicProductResponse>() {

            @Override
            public void onSuccess(ScenicProductResponse result) {
                super.onSuccess(result);
                /**
                 * 保存缓存对象
                 */
                if (isCache && !StringUtils.isNull(cacheKey)) {
                    result.isCache = isCache;
                    FileCacheUtil.saveObject(result, cacheKey.concat(StringUtils.repNull(mProvinceId)).concat(pages), MyApplication.getAppContext());
                }

                if (netInterface != null) {
                    netInterface.onNetResult(NetTag.GETPROBYREGION, result);
                }

                Log.e("result", result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("result", "onError...");
                Log.e("result", "isOnCallback..." + isOnCallback);
                netInterface.onNetError(NetTag.GETPROBYREGION, ex, checKingCaChe());
                ex.printStackTrace();
            }
        });
    }

    private boolean checKingCaChe() {
        if (scenicBannersResponse == null && mScenicProdforaddress == null && mScenicProdRspList == null) {
            return false;
        } else {
            return true;
        }
    }
}
