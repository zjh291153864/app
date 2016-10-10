package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by jh on 2016/7/10.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class UpdateAppResponse implements Serializable{
    /**
     * result : 01
     * update_force : 0
     * update_url : http://192.168.1.100:8080/xqsj/xqsj.apk
     * update_content : 更新内容
     * update_version : 1.0
     * msg : 有新版本更新
     */

    @SerializedName("result")
    public String mResult;
    @SerializedName("update_force")
    public String mUpdate_force;
    @SerializedName("update_url")
    public String mUpdate_url;
    @SerializedName("update_content")
    public String mUpdate_content;
    @SerializedName("update_version")
    public String mUpdate_version;
    @SerializedName("msg")
    public String mMsg;

    @Override
    public String toString() {
        return "UpdateAppResponse{" +
                "mResult='" + mResult + '\'' +
                ", mUpdate_force='" + mUpdate_force + '\'' +
                ", mUpdate_url='" + mUpdate_url + '\'' +
                ", mUpdate_content='" + mUpdate_content + '\'' +
                ", mUpdate_version='" + mUpdate_version + '\'' +
                ", mMsg='" + mMsg + '\'' +
                '}';
    }
}
