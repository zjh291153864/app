package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.HomePageResponse;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改资料控制器
 * Created by jh on 2016/7/1.
 */
public class InformationController extends BaseController{
    public InformationController(Context mContext) {
        super(mContext);
    }
    public void handleUpdateMemberByNet(final NetInterface netInterface,String id,String avatar,String nickname,String name,String gender,String idNumber){

        String url= NetTag.BASEURL.concat(NetTag.UPDATEMEMBER);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_id", id);//用户编码
        map.put("member_avatar", avatar);//头像地址
        map.put("member_nickname", nickname);//昵称
        map.put("member_fullname", name);//姓名
        map.put("member_gender", gender);//性别
        map.put("member_id_number", idNumber);//身份证
        Log.i("aaa","member_id---"+id);
        Log.i("aaa","member_avatar---"+avatar);
        Log.i("aaa","member_nickname---"+nickname);
        Log.i("aaa","member_fullname---"+name);
        Log.i("aaa","member_gender---"+gender);
        Log.i("aaa","member_id_number---"+idNumber);
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
