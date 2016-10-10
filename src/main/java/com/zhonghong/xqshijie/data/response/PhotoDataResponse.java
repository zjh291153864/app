package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jh on 2016/7/11.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class PhotoDataResponse extends BaseResponse implements Serializable {


    /**
     * result : 01
     * project_photo : [{"name":"效果图","data":[{"picture_title":"效果图1","picture_url":"/upload/201605/o/201605191445021463640302728.jpg"},{"picture_title":"效果图2","picture_url":"/upload/201605/o/201605191445341463640334395.jpg"},{"picture_title":"效果图3","picture_url":"/upload/201605/o/201605191445471463640347508.jpg"}],"type":"1"},{"name":"实景图","data":[{"picture_title":"","picture_url":"/upload/201605/o/201605191446141463640374985.jpg"},{"picture_title":"","picture_url":"/upload/201605/o/201605191446141463640374438.jpg"}],"type":"2"},{"name":"户型图","data":[{"picture_title":"","picture_url":"/upload/201605/o/201605191446311463640391447.jpg"},{"picture_title":"","picture_url":"/upload/201605/o/201605191446311463640391487.jpg"},{"picture_title":"","picture_url":"/upload/201605/o/201605191446311463640391265.jpg"}],"type":"3"},{"name":"区位图","data":[{"picture_title":"","picture_url":"/upload/201605/o/201605191446401463640400892.jpg"}],"type":"4"},{"name":"沙盘图","data":[{"picture_title":"","picture_url":"/upload/201605/o/201605191446481463640408916.jpg"}],"type":"5"},{"name":"样板间","data":[{"picture_title":"","picture_url":"/upload/201605/o/201605191447261463640446372.jpg"},{"picture_title":"","picture_url":"/upload/201605/o/201605191447271463640447851.jpg"}],"type":"6"},{"name":"配套图","data":[{"picture_title":"","picture_url":"/upload/201605/o/201605191447411463640461535.jpg"},{"picture_title":"","picture_url":"/upload/201605/o/201605191447461463640466165.jpg"},{"picture_title":"","picture_url":"/upload/201605/o/201605191447461463640466305.jpg"}],"type":"7"}]
     */
    @SerializedName("result")
    private String mResult;
    /**
     * name : 效果图
     * data : [{"picture_title":"效果图1","picture_url":"/upload/201605/o/201605191445021463640302728.jpg"},{"picture_title":"效果图2","picture_url":"/upload/201605/o/201605191445341463640334395.jpg"},{"picture_title":"效果图3","picture_url":"/upload/201605/o/201605191445471463640347508.jpg"}]
     * type : 1
     */
    @SerializedName("project_photo")
    private List<ProjectPhotoBean> mProjectPhotoBean;

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        this.mResult = result;
    }

    public List<ProjectPhotoBean> getProject_photo() {
        return mProjectPhotoBean;
    }

    public void setProject_photo(List<ProjectPhotoBean> project_photo) {
        this.mProjectPhotoBean = project_photo;
    }

    public static class ProjectPhotoBean {
        @SerializedName("name")
        private String mName;
        @SerializedName("type")
        private String mType;

        /**
         * picture_title : 效果图1
         * picture_url : /upload/201605/o/201605191445021463640302728.jpg
         */
        @SerializedName("data")
        private List<DataBean> data;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            this.mName = name;
        }

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            this.mType = type;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            @SerializedName("picture_title")
            private String picture_title;
            @SerializedName("picture_url")
            private String picture_url;

            public String getPicture_title() {
                return picture_title;
            }

            public void setPicture_title(String picture_title) {
                this.picture_title = picture_title;
            }

            public String getPicture_url() {
                return picture_url;
            }

            public void setPicture_url(String picture_url) {
                this.picture_url = picture_url;
            }
        }
    }
}
