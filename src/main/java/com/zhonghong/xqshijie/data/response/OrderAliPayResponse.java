package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by jh on 2016/7/19.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class OrderAliPayResponse {

    @SerializedName("result")
    public String mResult;
    @SerializedName("msg")
    public String mMsg;
    @SerializedName("number")
    public String mNumber;

    @Override
    public String toString() {
        return "OrderAliPayResponse{" +
                "mResult='" + mResult + '\'' +
                ", mMsg='" + mMsg + '\'' +
                ", mNumber='" + mNumber + '\'' +
                '}';
    }
}
