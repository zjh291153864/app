package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.app.MyApplication;
import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.PhotoDataResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;
import com.zhonghong.xqshijie.util.FileCacheUtil;
import com.zhonghong.xqshijie.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class PhotoController extends BaseController {

    public PhotoController(Context mContext) {
        super(mContext);
    }

    /**
     * 分页获取新奇景区列表
     * @param netInterface
     */
    public void handlePhotoByNet(final NetInterface netInterface, String proId, final boolean isCacheObject, final String cacheKey) {
        String url = NetTag.BASEURL.concat(NetTag.GET_PROJECT_PHOTO);
        Map<String, Object> map = new HashMap<>();
        map.put("key", "");
        map.put("project_id", proId);
        XUtilByNet.post(url, map, new NetCallBack<PhotoDataResponse>() {

            @Override
            public void onSuccess(PhotoDataResponse result) {
                super.onSuccess(result);
                if (isCacheObject && !StringUtils.isNull(cacheKey)) {
                    result.isCache = isCacheObject;
                    FileCacheUtil.saveObject(result, cacheKey, MyApplication.getAppContext());
                }

                if (netInterface != null) {
                    netInterface.onNetResult(cacheKey, result);
                }
                Log.e("result", result.toString());
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
}
