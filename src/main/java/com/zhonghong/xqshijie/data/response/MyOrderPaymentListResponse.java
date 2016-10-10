package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zg
 */
@HttpResponse(parser = JsonResponseParser.class)
public class MyOrderPaymentListResponse {

    @SerializedName("result")
    public String mResult;
    @SerializedName("payments")
    public List<PaymentsBean> mPaymentsBean;
    public static class PaymentsBean implements Serializable {
        @SerializedName("payment_id")
        public String mPaymentId;
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
