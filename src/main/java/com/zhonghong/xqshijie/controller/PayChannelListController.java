package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.OrderAliPayResponse;
import com.zhonghong.xqshijie.data.response.PayChannelListResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取支付渠道列表
 * Created by jh on 2016/7/19.
 */
public class PayChannelListController extends BaseController{
    public PayChannelListController(Context mContext) {
        super(mContext);
    }

    /**
     * 获取支付列表
     * @param netInterface
     */
    public void handlePayChannelListByNet(final NetInterface netInterface){

        String url= NetTag.BASEURL.concat(NetTag.PAYCHANNELLIST);
        Map<String,Object> map=new HashMap<>();
        map.put("key", "");
        XUtilByNet.post(url, map, new NetCallBack<PayChannelListResponse>(){

            @Override
            public void onSuccess(PayChannelListResponse result) {
                super.onSuccess(result);
                if (netInterface!=null){
                    netInterface.onNetResult(NetTag.PAYCHANNELLIST,result);
                }
                Log.e("result", result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if (netInterface!=null){
                    netInterface.onNetError(NetTag.PAYCHANNELLIST,ex,isOnCallback);
                }
            }
        });
    }


    /**
     * 支付记录插入（支付单号取得）
     * @param netInterface
     * @param order_id 订单ID
     * @param pay_amount 实付金额
     * @param subject 商品名称
     * @param body 商品描述（目前和商品名称一个值）
     */
    public void handleOrderAlipayByNet(final NetInterface netInterface,String order_id,String pay_amount,String subject,String body){
        String url= NetTag.BASEURL.concat(NetTag.ORDERALIPAY);
        Map<String,Object> map=new HashMap<>();
        map.put("order_id", order_id);
        map.put("pay_amount", pay_amount);
        map.put("subject", subject);
        map.put("body", body);
        XUtilByNet.post(url, map, new NetCallBack<OrderAliPayResponse>(){

            @Override
            public void onSuccess(OrderAliPayResponse result) {
                super.onSuccess(result);
                if (netInterface!=null){
                    netInterface.onNetResult(NetTag.ORDERALIPAY,result);
                }
                Log.e("result", result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if (netInterface!=null){
                    netInterface.onNetError(NetTag.ORDERALIPAY,ex,isOnCallback);
                }
            }
        });
    }
}
