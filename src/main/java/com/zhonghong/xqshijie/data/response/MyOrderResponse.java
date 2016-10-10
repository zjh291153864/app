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
public class MyOrderResponse {

    @SerializedName("result")
    public String mResult;
    @SerializedName("orders")
    public List<OrderBean> mOrderBean;
    public static class OrderBean implements Serializable {
        @SerializedName("project_cover")
        public String mProjectCover;
        @SerializedName("project_id")
        public String mProjectId;
        @SerializedName("project_name")
        public String mProjectName;
        @SerializedName("house_price")
        public String mHousePrice;
        @SerializedName("house_id")
        public String mHouseId;
        @SerializedName("has_cert")
        public String mHasCert;
        @SerializedName("house_thumb")
        public String mHouseThumb;
        @SerializedName("house_name")
        public String mHouseName;
        @SerializedName("house_area")
        public String mHouseArea;
        @SerializedName("room_card_surplus_number")
        public String mRoomCardSurplusNumber;
        @SerializedName("create_time")
        public String mCreateTime;
        @SerializedName("cusomter_name")
        public String mCusomterName;
        @SerializedName("customer_id")
        public String mCustomerId;
        @SerializedName("order_id")
        public String mOrderId;
        @SerializedName("item_id")
        public String mItemId;
        @SerializedName("order_display_id")
        public String mOrderDisplayId;
        @SerializedName("order_total")
        public String mOrderTotal;
        @SerializedName("order_status")
        public String mOrderTatus;
        @SerializedName("all_pay_price")
        public String mAllPayPrice;
        @SerializedName("final_price")
        public String mFinalPrice;
        @SerializedName("difference_date")
        public String mDifferenceData;//当前时间和下单时间的差值
    }
}
