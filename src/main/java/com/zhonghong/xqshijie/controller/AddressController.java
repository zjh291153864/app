package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.AddressResponse;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人地址相关控制器
 * Created by jh on 2016/7/4.
 */
public class AddressController extends BaseController{
    public AddressController(Context mContext) {
        super(mContext);
    }
//    获取个人地址列表
    public void handleGetAddressListByNet(final NetInterface netInterface, String memberId){

        String url= NetTag.BASEURL.concat(NetTag.GETADDRESSLIST);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_id", memberId);
        XUtilByNet.post(url, map, new NetCallBack<AddressResponse>(){
            @Override
            public void onSuccess(AddressResponse result) {
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

//    新增个人地址
    public void handleAddressByNet(final NetInterface netInterface, String memberId, String provinceId, String cityId, String countyId, String consigneeName, String consigneeMobile, String address, String isDefault){

        String url= NetTag.BASEURL.concat(NetTag.ADDADDRESS);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("member_id",memberId);
        map.put("province_id", provinceId);
        map.put("city_id", cityId);
        map.put("county_id", countyId);
        map.put("consignee_name", consigneeName);
        map.put("consignee_mobile", consigneeMobile);
        map.put("address", address);
        map.put("member_mail_code", "0");
        map.put("is_default", isDefault);
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

//    修改个人地址
    public void handleUpdateAddressByNet(final NetInterface netInterface, String addressId, String provinceId, String cityId, String countyId, String consigneeName, String consigneeMobile, String address, String isDefault){

        String url= NetTag.BASEURL.concat(NetTag.UPDATEADDRESS);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("address_id", addressId);
        map.put("province_id", provinceId);
        map.put("city_id", cityId);
        map.put("county_id", countyId);
        map.put("consignee_name", consigneeName);
        map.put("consignee_mobile", consigneeMobile);
        map.put("address", address);
        map.put("member_mail_code", "0");
        map.put("is_default", isDefault);
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
//    删除个人地址
    public void handleDelAddressByNet(final NetInterface netInterface, String addressId){

        String url= NetTag.BASEURL.concat(NetTag.DELADDRESS);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        map.put("address_id", addressId);
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
