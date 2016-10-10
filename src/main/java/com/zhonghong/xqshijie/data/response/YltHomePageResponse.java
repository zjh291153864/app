package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jh on 2016/6/29.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class YltHomePageResponse extends BaseResponse {
    /**
     * datas : {"productions":[{"inforurl":"resource/xqsjpc/images/index_2_0/banner1.jpg","infotitle":"逸乐通,给你不一样的度假体验"}],"banners":[{"title":"获赠产权","sort":"1","image_url":"resource/xqsjpc/images/index_2_0/banner1.jpg"},{"title":"自由入住","sort":"2","image_url":"resource/xqsjpc/images/index_2_0/banner1.jpg"},{"title":"经营收益","sort":"3","image_url":"resource/xqsjpc/images/index_2_0/banner1.jpg"},{"title":"尊享游乐","sort":"4","image_url":"resource/xqsjpc/images/index_2_0/banner1.jpg"}]}
     * result : 01
     * msg : 请求成功
     */
    @SerializedName("result")
    public String mResult;
    @SerializedName("msg")
    public String mMsg;
    @SerializedName("datas")
    public DatasBean mDatas;

    public static class DatasBean {
        /**
         * inforurl : resource/xqsjpc/images/index_2_0/banner1.jpg
         * infotitle : 逸乐通,给你不一样的度假体验
         */
        @SerializedName("productions")
        public List<ProductionsBean> mProductions;
        /**
         * title : 获赠产权
         * sort : 1
         * image_url : resource/xqsjpc/images/index_2_0/banner1.jpg
         */
        @SerializedName("banners")
        public List<BannersBean> mBanners;

        public static class ProductionsBean implements Serializable {
            @SerializedName("inforurl")
            public String mInforurl;
            @SerializedName("infotitle")
            public String mInfotitle;
        }

        public static class BannersBean implements  Serializable{
            @SerializedName("title")
            public String mTitle;
            @SerializedName("sort")
            public String mSort;
            @SerializedName("image_url")
            public String mImage_url;

        }
    }


//    /**
//     * result : 01
//     * ylt_config : {"processes":[{"image_url":"resource/xqsjpc/images/index_2_0/banner3.jpg"}],"advantages":[{"image_url":"resource/xqsjpc/images/index_2_0/banner2.jpg"}],"banners":[{"field":"project","image_url":"resource/xqsjpc/images/index_2_0/banner1.jpg","field_value":"7"}]}
//     * msg : 请求成功
//     */
//
//    @SerializedName("result")
//    public String mResult;
//    @SerializedName("msg")
//    public String mMsg;
//    @SerializedName("ylt_config")
//    public YltConfigBean mYltConfig;
//
//
//    public static class YltConfigBean implements Serializable {
//        /**
//         * image_url : resource/xqsjpc/images/index_2_0/banner3.jpg
//         */
//
//        @SerializedName("processes")
//        public List<ProcessesBean> mProcesses;
//        /**
//         * image_url : resource/xqsjpc/images/index_2_0/banner2.jpg
//         */
//
//        @SerializedName("advantages")
//        public List<AdvantagesBean> mAdvantages;
//        /**
//         * field : project
//         * image_url : resource/xqsjpc/images/index_2_0/banner1.jpg
//         * field_value : 7
//         */
//
//        @SerializedName("banners")
//        public List<BannersBean> mBanners;
//
//        public static class ProcessesBean implements Serializable {
//            @SerializedName("image_url")
//            public String mImageUrl;
//        }
//
//        public static class AdvantagesBean implements Serializable {
//            @SerializedName("image_url")
//            public String mImageUrl;
//        }
//
//        public static class BannersBean implements Serializable {
//            @SerializedName("field")
//            public String mField;
//            @SerializedName("image_url")
//            public String mImageUrl;
//            @SerializedName("field_value")
//            public String mFieldValue;
//        }
//    }

}
