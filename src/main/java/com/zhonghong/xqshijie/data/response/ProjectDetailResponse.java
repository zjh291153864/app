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
public class ProjectDetailResponse extends BaseResponse implements Serializable{


    @SerializedName("result")
    public String mResult;

    @SerializedName("project_ylts")
    public List<ProjectYLTS> mProjectYLTS;
    @SerializedName("project_info")
    public ProjectInfo mProjectInfo;
    @SerializedName("project_board")
    public List<ProjectBoard> mProjectBoard;

    @SerializedName("project_video")
    public List<ProjectVideo> mProjectVideo;
    @SerializedName("project_bulid")
    public List<ProjectBuild> mProjectBuild;
    @SerializedName("project_photo")
    public List<ProjectPhoto> mProjectPhoto;

    public static class ProjectYLTS implements Serializable {
        @SerializedName("house_price")
        public String mHousePrice;
        @SerializedName("house_name")
        public String mHouseName;
        @SerializedName("house_area")
        public String mHouseArea;
    }

    public static class ProjectInfo implements Serializable {
        @SerializedName("project_tel")
        public String mProjectTel;
        @SerializedName("project_status")
        public String mProjectStatus;
        @SerializedName("project_name")
        public String mProjectName;
        @SerializedName("project_share_url")
        public String mProjectShareUrl;
        @SerializedName("project_province")
        public String mProjectProvince;
        @SerializedName("project_id")
        public String mProjectId;
        @SerializedName("project_strengths")
        public String mProjectStrenghts;
        @SerializedName("project_city")
        public String mProjectCity;
        @SerializedName("project_country")
        public String mProjectCountry;
        @SerializedName("project_address")
        public String mProjectAddress;
        @SerializedName("project_cover")
        public String mProjectCover;
        @SerializedName("project_label")
        public String mProjectLabel;

    }
    public static class ProjectBoard implements Serializable {
        @SerializedName("board2_desc")
        public String mBoard2Desc;
        @SerializedName("board5_title")
        public String mBoard5Title;
        @SerializedName("board2_title")
        public String mBoard2Title;
        @SerializedName("board6_title")
        public String mBoard6Title;
        @SerializedName("board5_desc")
        public String mBoard5Desc;
        @SerializedName("board6_desc")
        public String mBoard6Desc;
    }
    public static class ProjectVideo implements Serializable {
        @SerializedName("project_video_pic")
        public String mProjectVideoPic;
        @SerializedName("project_video")
        public String mProjectVideo;
    }
    public static class ProjectBuild implements Serializable {
        @SerializedName("info_name")
        public String mInfoName;
        @SerializedName("info_data")
        public List<InfoData> mInfoData;
        public  class InfoData implements Serializable {
            @SerializedName("name")
            public String mName;
            @SerializedName("value")
            public String mValue;
        }
    }
    public static class ProjectPhoto implements Serializable {
        @SerializedName("name")
        public String mName;
        @SerializedName("data")
        public List<Data> mData;
        @SerializedName("type")
        public String  mType;
        public  class Data implements Serializable {
                @SerializedName("picture_title")
                public String mPictureTitle;
                @SerializedName("picture_url")
                public String mPicUrl;
        }
    }
}
