package com.zhonghong.xqshijie.data.bean;

import java.io.Serializable;

/**
 * Created by zg on 2016/7/4.
 */
public class PaymentHistoryBean implements Serializable{
    public String  mPaymentId;  //支付id
    public String  mOrderId;    //订单id
    public String  mPayAmount;  //支付时金额
    public String mCreateTime;//创建时间
    public String mTradeNo;//支付宝交易号
    public String mNotifyTime;//支付通知时间
    public String mIsRefund;//退款会有单独借口

}
