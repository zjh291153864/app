package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.data.response.SpecificationSelectionResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取规格弹层控制器
 * Created by jh on 2016/7/5.
 */
public class SpecificationSelectionController extends BaseController{
    public SpecificationSelectionController(Context mContext) {
        super(mContext);
    }

    //    获取规格弹层选择页
    public void handleGetByProjectIdByNet(final NetInterface netInterface, String projectId){

        String url= NetTag.BASEURL.concat(NetTag.GITBYPROJECTID);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("project_id", "37");
        XUtilByNet.post(url, map, new NetCallBack<SpecificationSelectionResponse>(){

            @Override
            public void onSuccess(SpecificationSelectionResponse result) {
                super.onSuccess(result);
                if (netInterface!=null){
                    netInterface.onNetResult("",result);
                }
                Log.e("result", result.toString());
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if (netInterface!=null){
                    netInterface.onNetError("",ex,isOnCallback);
                }
            }
        });
    }
}
