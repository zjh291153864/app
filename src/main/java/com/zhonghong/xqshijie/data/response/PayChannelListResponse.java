package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by jh on 2016/7/19.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class PayChannelListResponse {
    /**
     * 3 : 逸乐通
     * result : 01
     * 2 : 易宝支付
     * 1 : 支付宝
     */

    @SerializedName("3")
    public String value3;
    @SerializedName("result")
    public String result;
    @SerializedName("2")
    public String value2;
    @SerializedName("1")
    public String value1;

    @Override
    public String toString() {
        return "PayChannelListResponse{" +
                "value3='" + value3 + '\'' +
                ", result='" + result + '\'' +
                ", value2='" + value2 + '\'' +
                ", value1='" + value1 + '\'' +
                '}';
    }
}
