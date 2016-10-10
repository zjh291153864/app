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
public class YltProductResponse extends BaseResponse{

    public YltProductResponse() {}

    @SerializedName("result")
    public String mResult;
    @SerializedName("datas")
    public List<YltProductResponse.DatasBean> mDataBeans;

    public static class DatasBean implements Serializable{
        @SerializedName("project_name")
        public String mProjectName;
        @SerializedName("room_card_surplus_number")
        public int mCardNumber;
        @SerializedName("house_price")
        public int mHousePrice;
        @SerializedName("project_id")
        public int mProjectId;
        @SerializedName("house_id")
        public int mHouseId;
        @SerializedName("project_cover")
        public String mProjectPicture;
    }
}
