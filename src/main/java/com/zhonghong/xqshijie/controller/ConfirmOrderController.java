package com.zhonghong.xqshijie.controller;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.base.BaseController;
import com.zhonghong.xqshijie.data.response.ConfirmOrderResponse;
import com.zhonghong.xqshijie.data.response.ContractListResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetCallBack;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.net.XUtilByNet;
import com.zhonghong.xqshijie.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jh on 2016/7/18.
 */
public class ConfirmOrderController extends BaseController{
    public ConfirmOrderController(Context mContext) {
        super(mContext);
    }
    //    确认定单接口
    public void handleSaveOrderByNet(final NetInterface netInterface, String customerId, String cusomterName, String cusomterPhone, String itemQuantity, int itemId, String finalPrice, String itemPrice){

        String url= NetTag.BASEURL.concat(NetTag.SAVEORIDER);
        Map<String,Object> map=new HashMap<>();
        map.put("customer_id", StringUtils.repNull(customerId));//客户id (必填)
        map.put("cusomter_name", cusomterName);//客户姓名  (非必填)
        map.put("cusomter_phone", cusomterPhone);//客户手机号  (非必填)
        map.put("item_quantity", itemQuantity);//购买数量 必填
        map.put("item_id", itemId);//商品id 必填
        map.put("final_price", finalPrice);//下单价格 必填
        map.put("item_price", itemPrice);//商品价格 必填
        XUtilByNet.post(url, map, new NetCallBack<ConfirmOrderResponse>(){
            @Override
            public void onSuccess(ConfirmOrderResponse result) {
                super.onSuccess(result);
                if (netInterface!=null){
                    netInterface.onNetResult("",result);
                }
                Log.e("result", result.toString());
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if (netInterface!=null){
                    netInterface.onNetError("",ex,isOnCallback);
                }
            }
        });
    }

    //确认订单合同展示
    public void handleContractListByNet(final NetInterface netInterface, String orderType){

        String url= NetTag.BASEURL.concat(NetTag.CONTRACTLIST);
        Map<String,Object> map=new HashMap<>();
        map.put("order_type", orderType);//订单类型>1|全款支付，2|借贷支付，3|预购支付',
        XUtilByNet.post(url, map, new NetCallBack<ContractListResponse>(){
            @Override
            public void onSuccess(ContractListResponse result) {
                super.onSuccess(result);
                if (netInterface!=null){
                    netInterface.onNetResult(NetTag.CONTRACTLIST,result);
                }
                Log.e("result", result.toString());
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if (netInterface!=null){
                    netInterface.onNetError(NetTag.CONTRACTLIST,ex,isOnCallback);
                }
            }
        });
    }
}
