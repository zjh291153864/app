package com.zhonghong.xqshijie.data.bean;

import java.io.Serializable;


public class OrderBean implements Serializable {
    public String orderID;//订单ID
    public String orderNum;//订单编号
    public String orderAllpayPrice;//已支付金额
    public String orderTotal;//订单总额
    public String orderDeposit;//订单定金
    public String orderSurplus;//待付金额
    public String orderStatus;//订单状态 0 支付定金 / 1 待付余款，已付定金
    public String projectName;//项目名称
    public String projectDescription;//项目描述
//    public String ....


    @Override
    public String toString() {
        return "OrderBean{" +
                "orderID='" + orderID + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", orderAllpayPrice='" + orderAllpayPrice + '\'' +
                ", orderTotal='" + orderTotal + '\'' +
                ", orderDeposit='" + orderDeposit + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                '}';
    }
}
