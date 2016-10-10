package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心修改密码控制器
 * Created by jh on 2016/7/1.
 */
public class ChagePwdController extends BaseController{
    public ChagePwdController(Context mContext) {
        super(mContext);
    }
//    检测用户原密码是否正确
    public void handleOriginalPwdByNet(final NetInterface netInterface,String id, String password){

        String url= NetTag.BASEURL.concat(NetTag.CHECKOLDPWD);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_id", id);
        map.put("old_pwd", password);
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

//    设置中心-用户修改密码
    public void handleNewPwdByNet(final NetInterface netInterface,String id, String password){

        String url= NetTag.BASEURL.concat(NetTag.CHANGEPWDBYSET);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_id", id);
        map.put("new_pwd", password);
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
            }
        });
    }
}
