package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zg on 16/6/28.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class MyWalletResponse {

    @SerializedName("result")
    public String mResult;
    @SerializedName("count")
    public String mCount;
    @SerializedName("cashs")
    public List<Cashs> mCashs;
    public static class Cashs implements Serializable {
        @SerializedName("create_time")
        public String mCreateTime;
        @SerializedName("money")
        public String mMoney;
    }
}
