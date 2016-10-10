package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiezl on 16/6/28.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ScenicBannersResponse extends BaseResponse {
    /**
     * result : 01
     * xqjq_config : {"banners":[{"field":"project","image_url":"resource/xqsjpc/images/index_2_0/banner1.jpg","field_value":"7"}]}
     * msg : 请求成功
     */

    @SerializedName("result")
    public String mResult;
    @SerializedName("xqjq_config")
    public XqjqConfigBean mXqjqConfig;
    @SerializedName("msg")
    public String mMsg;


    public static class XqjqConfigBean implements Serializable {
        /**
         * field : project
         * image_url : resource/xqsjpc/images/index_2_0/banner1.jpg
         * field_value : 7
         */

        @SerializedName("banners")
        public List<BannersBean> mBanners;


        public static class BannersBean implements Serializable {
            @SerializedName("field")
            public String mField;
            @SerializedName("image_url")
            public String mImageUrl;
            @SerializedName("field_value")
            public String mFieldValue;

        }
    }

}
