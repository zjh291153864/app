package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.app.MyApplication;
import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.ProjectDetailResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;
import com.zhonghong.xqshijie.util.FileCacheUtil;
import com.zhonghong.xqshijie.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zg on 16/6/16.
 */
public class ProjectDescriptionrController extends BaseController {

    public ProjectDescriptionrController(Context mContext) {
        super(mContext);
    }

    public void handleMyOrderByNet(final NetInterface netInterface, final boolean isCacheObject, String projectId, final String cacheKey) {
        ProjectDetailResponse projectDetailResponse = (ProjectDetailResponse) FileCacheUtil.readObject(cacheKey, MyApplication.getAppContext());
        if (projectDetailResponse !=null){
            if (netInterface != null) {
                netInterface.onNetResult(cacheKey, projectDetailResponse);
            }
        }
        String url = NetTag.BASEURL.concat(NetTag.GET_PROPERTY_ATTR);
        Map<String, Object> map = new HashMap<>();
        map.put("project_id",projectId);
        map.put("Key", "");
        XUtilByNet.post(url, map, new NetCallBack<ProjectDetailResponse>() {

            @Override
            public void onSuccess(ProjectDetailResponse result) {
                super.onSuccess(result);
                if (isCacheObject&& !StringUtils.isNull(cacheKey)){
                    result.isCache = isCacheObject;
                    FileCacheUtil.saveObject(result, cacheKey, MyApplication.getAppContext());
                }
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
