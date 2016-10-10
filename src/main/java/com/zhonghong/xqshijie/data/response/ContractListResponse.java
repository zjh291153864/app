package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by jh on 2016/7/19.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ContractListResponse {
    /**
     * result : 01
     * contracts : {"banners":[{"field":"1","field_value":"《认购协议书》"}]}
     * msg : 请求成功
     */
    @SerializedName("result")
    public String mResult;
    @SerializedName("contracts")
    public ContractsBean contracts;
    @SerializedName("msg")
    public String msg;
    public static class ContractsBean {
        /**
         * field : 1
         * field_value : 《认购协议书》
         */
        @SerializedName("banners")
        public List<BannersBean> banners;
        public static class BannersBean {
            @SerializedName("field")
            public String field;
            @SerializedName("field_value")
            public String field_value;
            @SerializedName("image_url")
            public String mUrl;

            @Override
            public String toString() {
                return "BannersBean{" +
                        "field='" + field + '\'' +
                        ", field_value='" + field_value + '\'' +
                        ", mUrl='" + mUrl + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ContractsBean{" +
                    "banners=" + banners +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ContractListResponse{" +
                "mResult='" + mResult + '\'' +
                ", contracts=" + contracts +
                ", msg='" + msg + '\'' +
                '}';
    }
}
