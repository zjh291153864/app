package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by jh on 2016/7/18.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ConfirmOrderResponse implements Serializable{
    /**
     * result : 01
     * data : {"order_id":12128,"order_display_id":"XQSJFQ-160718152422012128","all_pay_price":"0","order_total":"10000","deposit":"5000"}
     * msg : 提交成功
     */
    @SerializedName("result")
    public String mResult;//01或者04；01=成功；04=失败
    /**
     * order_id : 12128
     * order_display_id : XQSJFQ-160718152422012128
     * all_pay_price : 0
     * order_total : 10000
     * deposit : 5000
     */
    @SerializedName("data")
    public DataBean mData;//返回数据
    @SerializedName("msg")
    public String mMsg;//消息

    public static class DataBean implements Serializable{
        @SerializedName("order_id")
        public String mOrderId;//订单id
        @SerializedName("order_display_id")
        public String mOrderDisplayId;//订单号
        @SerializedName("all_pay_price")
        public String mAllPayPrice;//已支付金额
        @SerializedName("order_total")
        public String mOrderTotal;//订单总额
        @SerializedName("deposit")
        public String mDeposit;//定金

        @Override
        public String toString() {
            return "DataBean{" +
                    "mOrderId='" + mOrderId + '\'' +
                    ", mOrderDisplayId='" + mOrderDisplayId + '\'' +
                    ", mAllPayPrice='" + mAllPayPrice + '\'' +
                    ", mOrderTotal='" + mOrderTotal + '\'' +
                    ", mDeposit='" + mDeposit + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ConfirmOrderResponse{" +
                "mResult='" + mResult + '\'' +
                ", mData=" + mData +
                ", mMsg='" + mMsg + '\'' +
                '}';
    }
}
