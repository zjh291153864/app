package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jh on 2016/7/16.
 */
public class FeedBackController extends BaseController {
    public FeedBackController(Context mContext) {
        super(mContext);
    }
    //    意见反馈
    public void handleFeedBackByNet(final NetInterface netInterface, String text, String phone,String id){

        String url= NetTag.BASEURL.concat(NetTag.ADDFEEDBACK);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("text", text);
        map.put("contact_type", "1");
        map.put("contact_way", phone);
        map.put("refer", "android");
        map.put("member_id", id);
        XUtilByNet.post(url, map, new NetCallBack<LoginResponse>(){
            @Override
            public void onSuccess(LoginResponse result) {
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
