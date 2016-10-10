package com.zhonghong.xqshijie.adapter;

import android.content.Context;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseCommonAdapter;
import com.zhonghong.xqshijie.base.ViewHolder;
import com.zhonghong.xqshijie.data.response.MyOrderPaymentListResponse;


public class OrderPayInfoAdapter extends BaseCommonAdapter<MyOrderPaymentListResponse.PaymentsBean> {
    private Context mContext;
    public OrderPayInfoAdapter(Context context) {
        super(context, R.layout.adapter_my_order_pay_info);
        this.mContext = context;
    }

    @Override
    public void convertView(ViewHolder holder, MyOrderPaymentListResponse.PaymentsBean paymentsBean) {
        holder.setTextView(R.id.tv_pay_money_time, paymentsBean.mCreateTime);
        holder.setTextView(R.id.tv_pay_money_count,mContext.getResources().getString(R.string.money)+paymentsBean.mPayAmount);
        holder.setTextView(R.id.tv_order_status, paymentsBean.mIsRefund);
    }
}
