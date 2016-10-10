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
public class ScenicProductResponse extends BaseResponse {


    /**
     * result : 01
     * projects : [{"project_name":"洪湖111--无预售证","project_province":"3","project_id":"59","project_city":"3002"},{"project_name":"日子_儿童节","project_province":"2","project_id":"51","project_city":"2001"},{"project_name":"月子_儿童节","project_province":"2","project_id":"50","project_city":"2001"},{"project_name":"儿童节快乐_yuezi","project_province":"3","project_id":"49","project_city":"3002"},{"project_name":"项目名称001","project_province":"1","project_id":"47","project_city":"1001"}]
     */

    @SerializedName("result")
    public String mResult;
    /**
     * project_name : 洪湖111--无预售证
     * project_province : 3
     * project_id : 59
     * project_city : 3002
     */

    public List<ProjectsBean> projects;


    public static class ProjectsBean implements Serializable{
        @SerializedName("project_name")
        public String mProjectName;
        @SerializedName("project_province")
        public String mProjectProvince;
        @SerializedName("project_id")
        public String mProjectId;
        @SerializedName("project_city")
        public String mProjectCity;
        @SerializedName("project_cover")
        public String mProjectCover;
    }
}
