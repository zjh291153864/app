package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by zg on 16/7/18.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class OrderCloselResponse {

    /**
     * result : 01
     * msg : 手机号已注册
     */
    @SerializedName("result")
    public String mResult;//返回响应

    @SerializedName("msg")
    public String mMsg;//返回消息
}
