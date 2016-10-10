package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by jh on 2016/7/13.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class AccessTokenResponse {
    /**
     * code : 1
     * msg : success
     * data : 57833b5c1f8158bee3e6f5bd
     */
    @SerializedName("code")
    public int mCode;
    @SerializedName("msg")
    public String mMsg;
    @SerializedName("data")
    public String mData;

    @Override
    public String toString() {
        return "AccessTokenResponse{" +
                "mCode=" + mCode +
                ", mMsg='" + mMsg + '\'' +
                ", mData='" + mData + '\'' +
                '}';
    }
}
