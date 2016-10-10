package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jh on 2016/7/5.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class SpecificationSelectionResponse implements Serializable{
    /**
     * project_id : 37
     * project_name : zz
     * project_cover : /upload/201605/o/201605271606561464336416272.jpg
     * houses : [{"house_area":"65.0","house_name":"测试","house_price":"10000.00","house_id":"162","project_sort":"99"," has_cert ":"1","room_card_surplus_number":"200"},{"house_area":"60.0"," house_name ":"666","house_price":"0.00","house_id":"133","project_sort":"66","has_cert":"1","room_card_surplus_number":"65535"}]
     * code : 01
     */
    @SerializedName("project_id")
    public String mProjectId;//项目ID
    @SerializedName("project_name")
    public String mProjectName;//项目名称
    @SerializedName("project_cover")
    public String mProjectCover;//项目背景图
    @SerializedName("code")
    public String mCode;//
    @SerializedName("houses")
    public List<HousesBean> mHouses;//


    /**
     * house_area : 65.0
     * house_name : 测试
     * house_price : 10000.00
     * house_id : 162
     * project_sort : 99
     *  has_cert  : 1
     * room_card_surplus_number : 200
     */
    public static class HousesBean implements Serializable{
        /**
         * house_area : 60.0
         *  house_name  : 666
         * house_price : 0.00
         * house_id : 133
         * project_sort : 66
         * has_cert : 1
         * room_card_surplus_number : 65535
         */

        @SerializedName("house_area")
        public String mHouseArea;//面积
        @SerializedName("house_name")
        public String mHouseName;//产品名称
        @SerializedName("house_price")
        public String mHousePrice;//标准总价，前台显示用，如果只有一个逸乐通，就写一个，否则前台展现范围。
        @SerializedName("house_id")
        public String mHouseId;//房源编号
        @SerializedName("project_sort")
        public String mProjectSort;//前端显示排序
        @SerializedName("has_cert")
        public String mHasCert;//是否有预售证，1代表有，0代表没有，没有的只能交定金
        @SerializedName("room_card_surplus_number")
        public String mRoomCardSurplusNumber;//剩余份数，用来判断详情页面是否可以购买，如果不可以购买，弹出已经售罄

        @Override
        public String toString() {
            return "HousesBean{" +
                    "mHouseArea='" + mHouseArea + '\'' +
                    ", mHouseName='" + mHouseName + '\'' +
                    ", mHousePrice='" + mHousePrice + '\'' +
                    ", mHouseId='" + mHouseId + '\'' +
                    ", mProjectSort='" + mProjectSort + '\'' +
                    ", mHasCert='" + mHasCert + '\'' +
                    ", mRoomCardSurplusNumber='" + mRoomCardSurplusNumber + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SpecificationSelectionResponse{" +
                "mProjectId='" + mProjectId + '\'' +
                ", mProjectName='" + mProjectName + '\'' +
                ", mProjectCover='" + mProjectCover + '\'' +
                ", mCode='" + mCode + '\'' +
                ", mHouses=" + mHouses +
                '}';
    }
}
