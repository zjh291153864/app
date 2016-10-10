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
public class HomePageResponse extends BaseResponse {

    /**
     * result : 01
     * index_config : {"ylts":[{"image_url":"resource/xqsjmob_2/images/index/index_icon1.png"},{"image_url":"resource/xqsjmob_2/images/index/index_icon2.png"},{"image_url":"resource/xqsjmob_2/images/index/index_icon3.png"},{"image_url":"resource/xqsjmob_2/images/index/index_icon4.png"}],"projects":[{"id":"2","title":"卧虎藏龙拍摄地 上影安吉","image_url":"resource/xqsjpc/images/index_2_0/index_pic01.jpg","provinc":"浙江","city":"湖州"},{"id":"8","title":"馨居之地 长白水镇","image_url":"resource/xqsjpc/images/index_2_0/index_pic07.jpg","provinc":"吉林","city":"延边"},{"id":"3","title":"东方生态城 济南鹊山","image_url":"resource/xqsjpc/images/index_2_0/index_pic10.jpg","provinc":"山东","city":"济南"},{"id":"1","title":"游遍自家风景 御马坊","image_url":"resource/xqsjpc/images/index_2_0/index_pic04.jpg","provinc":"北京","city":""}],"domain":[{"domain_name":"https://m.xqshijie.com/"}],"informations":[{"id":"55","time":"2016-07-05 14:30:06","title":"北京御马坊开盘再创1亿辉煌！","image_url":"upload/image/20160705/1467700096592436.png","link_url":"help/noticeDetail/55.html"}],"themeParks":[{"title":"首都时尚","image_url":"resource/xqsjpc/images/index_2_0/index_pic16.jpg","project_id":"9"},{"title":"齐鲁古韵","image_url":"resource/xqsjpc/images/index_2_0/index_pic17.jpg","project_id":"8"},{"title":"竹海茶山","image_url":"resource/xqsjpc/images/index_2_0/index_pic18.jpg","project_id":"2"},{"title":"冰封雪原 童话世界","image_url":"resource/xqsjpc/images/index_2_0/index_pic19.jpg","project_id":"1"},{"title":"南海升起 不夜城","image_url":"resource/xqsjpc/images/index_2_0/index_pic20.jpg","project_id":"3"}],"addresss":[{"title":"北京","image_url":"resource/xqsjmob_2/images/index/pic_beijing.png","cityId":"","provincId":"1"},{"title":"浙江","image_url":"resource/xqsjmob_2/images/index/pic_zhejiang.png","cityId":"","provincId":"11"},{"title":"吉林","image_url":"resource/xqsjmob_2/images/index/pic_jilin.png","cityId":"","provincId":"7"},{"title":"山东","image_url":"resource/xqsjmob_2/images/index/pic_shandong.png","cityId":"","provincId":"15"},{"title":"海南","image_url":"resource/xqsjmob_2/images/index/pic_hainan.png","cityId":"","provincId":"21"}],"banners":[{"field":"project","image_url":"resource/xqsjpc/images/index_2_0/banner1.jpg","field_value":"7"},{"field":"link","image_url":"resource/xqsjpc/images/index_2_0/banner2.jpg","field_value":"https://www.xqshijie.com/active/fanghu.html"},{"field":"link","image_url":"resource/xqsjpc/images/index_2_0/banner3.jpg","field_value":"https://www.xqshijie.com/help/noticeDetail/35.html"}]}
     * msg : 请求成功
     */

    @SerializedName("result")
    public String mResult;
    @SerializedName("index_config")
    public ObjectBean mObject;
    @SerializedName("msg")
    public String mMsg;


    public static class ObjectBean implements Serializable {
        /**
         * image_url : resource/xqsjmob_2/images/index/index_icon1.png
         */

        @SerializedName("ylts")
        public List<YltsBean> mYlts;
        /**
         * id : 2
         * title : 卧虎藏龙拍摄地 上影安吉
         * image_url : resource/xqsjpc/images/index_2_0/index_pic01.jpg
         * provinc : 浙江
         * city : 湖州
         */

        @SerializedName("projects")
        public List<ProjectsBean> mProjectsBeanList;
        /**
         * id :
         * time :
         * title :
         * image_url :
         */
        @SerializedName("domain")
        public List<DomainBean> mDomainBeanList;
        /**
         * id : 55
         * time : 2016-07-05 14:30:06
         * title : 北京御马坊开盘再创1亿辉煌！
         * image_url : upload/image/20160705/1467700096592436.png
         * link_url : help/noticeDetail/55.html
         */

        @SerializedName("informations")
        public List<InformationsBean> mInformationsBeenList;
        /**
         * title : 首都时尚
         * image_url : resource/xqsjpc/images/index_2_0/index_pic16.jpg
         * project_id : 9
         */

        @SerializedName("themeParks")
        public List<ThemeParksBean> mThemeParksBeanList;
        /**
         * title : 北京
         * image_url : resource/xqsjmob_2/images/index/pic_beijing.png
         * cityId :
         * provincId : 1
         */

        @SerializedName("addresss")
        public List<AddresssBean> mAddresssBeanList;
        /**
         * field : project
         * image_url : resource/xqsjpc/images/index_2_0/banner1.jpg
         * field_value : 7
         */

        @SerializedName("banners")
        public List<BannersBean> mBannersBeanList;


        public static class YltsBean implements Serializable{
            @SerializedName("image_url")
            public String mImageUrl;

        }

        public static class ProjectsBean implements Serializable {
            @SerializedName("id")
            public String mId;
            @SerializedName("title")
            public String mTitle;
            @SerializedName("image_url")
            public String mImageUrl;
            @SerializedName("provinc")
            public String mProvinc;
            @SerializedName("city")
            public String mCity;

        }

        public static class DomainBean implements Serializable{
            @SerializedName("domain_name")
            public String mDomainName;

        }

        public static class InformationsBean implements Serializable {
            @SerializedName("id")
            public String mId;
            @SerializedName("time")
            public String mTime;
            @SerializedName("title")
            public String mTitle;
            @SerializedName("image_url")
            public String mImageUrl;
            @SerializedName("link_url")
            public String mLinkUrl;
        }

        public static class ThemeParksBean implements Serializable {
            @SerializedName("title")
            public String mTitle;
            @SerializedName("image_url")
            public String mImageUrl;
            @SerializedName("project_id")
            public String mProjectId;
        }

        public static class AddresssBean implements Serializable {
            @SerializedName("title")
            public String mTitle;
            @SerializedName("image_url")
            public String mImageUrl;
            @SerializedName("cityId")
            public String mCityId;
            @SerializedName("provincId")
            public String mProvincId;
        }

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
