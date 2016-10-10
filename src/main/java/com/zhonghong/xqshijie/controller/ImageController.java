package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.AccessTokenResponse;
import com.zhonghong.xqshijie.data.response.ImageResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 头像上传
 * Created by jh on 2016/7/13.
 */
public class ImageController extends BaseController {
    public ImageController(Context mContext) {
        super(mContext);
    }

    //    取得access_Token
    public void handleAccessTokenByNet(final NetInterface netInterface, String username, String password) {

        String url = NetTag.ACCESSTOKENN;
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("channel", "xqshijie");
        map.put("userType", "5");
        XUtilByNet.post(url, map, new NetCallBack<AccessTokenResponse>() {
            @Override
            public void onSuccess(AccessTokenResponse result) {
                super.onSuccess(result);
                if (netInterface != null) {
                    netInterface.onNetResult("", result);
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

    //    上传头像
    public void handleUpLoadImgByNet(final NetInterface netInterface, String accessToken, File photo) {

        String url = NetTag.UPLOADIMAGE + accessToken;
        Map<String, Object> map = new HashMap<>();
        map.put("channel", "xqshijie");
        map.put("uploadType", "photo");
        map.put("theFile", photo);
        map.put("sizeList", "");
        XUtilByNet.upLoadFile(url, map, new NetCallBack<ImageResponse>() {
            @Override
            public void onSuccess(ImageResponse result) {
                super.onSuccess(result);
                if (netInterface != null) {
                    netInterface.onNetResult("", result);
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
