package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zg on 16/7/5.
 * 订单支付信息
 */
@HttpResponse(parser = JsonResponseParser.class)
public class OrderPaymentsResponse {

    @SerializedName("result")
    public String mResult;
    @SerializedName("payments")
    public List<OrderPaymentsBean> mOrderPaymentsBean;
    public static class OrderPaymentsBean implements Serializable {
        @SerializedName("payment_id")
        public String mPaymentsId;
        @SerializedName("order_id")
        public String mOrderId;
        @SerializedName("pay_amount")
        public String mPayAmount;
        @SerializedName("trade_no")
        public String mTradeNo;
        @SerializedName("create_time")
        public String mCreateTime;
        @SerializedName("notify_time")
        public String mNotifyTime;
        @SerializedName("is_refund")
        public String mIsRefund;
    }
}
