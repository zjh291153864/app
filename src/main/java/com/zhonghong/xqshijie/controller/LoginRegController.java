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
 * Created by xiezl on 16/6/16.
 * 登陆注册控制器
 */
public class LoginRegController extends BaseController{

    public LoginRegController(Context mContext) {
        super(mContext);
    }

//    验证手机号是否注册
    public void handlePhoneByNet(final NetInterface netInterface,String phone){

        String url= NetTag.BASEURL.concat(NetTag.CHECKMOBILE);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_mobile", phone);
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

//    注册用户-获取手机验证码
    public void handleRegisteredCodesByNet(final NetInterface netInterface, String phone){

        String url= NetTag.BASEURL.concat(NetTag.SENDSMSONREGISTER);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_mobile", phone);
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

//    注册
    public void handleRegisterByNet(final NetInterface netInterface,String phone,String password,String code){

        String url= NetTag.BASEURL.concat(NetTag.REGISTER);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_mobile", phone);
        map.put("member_password", password);
        map.put("validate_code", code);
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

//    登陆
    public void handleLoginByNet(final NetInterface netInterface,String phone,String password){

        String url= NetTag.BASEURL.concat(NetTag.LOGIN);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_mobile", phone);
        map.put("member_password", password);
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

//    忘记密码-获取手机验证码
    public void handleForgitPwdCodesByNet(final NetInterface netInterface,String phone){

        String url= NetTag.BASEURL.concat(NetTag.SENDSMSONFORGITPWD);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_mobile", phone);
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

//    忘记密码-用户修改密码
    public void handleForgitPwdByNet(final NetInterface netInterface,String phone,String password,String code){

        String url= NetTag.BASEURL.concat(NetTag.CHANGEPWDBYFOTGIT);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_mobile", phone);
        map.put("new_pwd", password);
        map.put("validate_code", code);
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
