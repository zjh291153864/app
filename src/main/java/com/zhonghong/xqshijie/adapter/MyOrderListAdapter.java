package com.zhonghong.xqshijie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.ContractDownloadActivity;
import com.zhonghong.xqshijie.activity.OrderPayActivity;
import com.zhonghong.xqshijie.activity.ScenicFragmentActivity;
import com.zhonghong.xqshijie.activity.TimeLineActivity;
import com.zhonghong.xqshijie.activity.ViewContractActivity;
import com.zhonghong.xqshijie.base.BaseCommonAdapter;
import com.zhonghong.xqshijie.base.ViewHolder;
import com.zhonghong.xqshijie.controller.OrderCloseController;
import com.zhonghong.xqshijie.data.bean.OrderBean;
import com.zhonghong.xqshijie.data.response.MyOrderResponse;
import com.zhonghong.xqshijie.data.response.OrderCloselResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;
import com.zhonghong.xqshijie.util.TimeFormatUtils;
import com.zhonghong.xqshijie.widget.countdown.CountDownTask;
import com.zhonghong.xqshijie.widget.countdown.CountDownTimers;
import com.zhonghong.xqshijie.widget.dialog.NormalDialog;
import com.zhonghong.xqshijie.widget.dialog.OnBtnClickL;

import java.util.Objects;


public class MyOrderListAdapter extends BaseCommonAdapter<MyOrderResponse.OrderBean> implements NetInterface {
    // 订单状态>1|未支付,2|已支付,3|支付中,4|已付定金,21|历史订单,100|取消订单,101|支付超时,102|支付订金,105|退款中,106|部分已退,107|全部已退,',
    private Context mContext;
    private CountDownTask mCountDownTask;
    private int mCloseItemPosition;
    private long mCountDownTime = 7 * 24 * 60 * 60 * 1000l;

    public MyOrderListAdapter(Context context) {
        super(context, R.layout.adapter_my_order);
        this.mContext = context;
    }

    public void setCountDownTask(CountDownTask countDownTask) {
        if (!Objects.equals(mCountDownTask, countDownTask)) {
            mCountDownTask = countDownTask;
            notifyDataSetChanged();
        }
    }

    @Override
    public void convertView(final ViewHolder holder, final MyOrderResponse.OrderBean orderInfoBean) {
        //TODO 图片闪烁
        ImageLoaderUtil.getInstance().loadImage(orderInfoBean.mHouseThumb, (ImageView) holder.getChildView(R.id.iv_order_item_pic), R.drawable.ic_ylt_productdefault);
        holder.setTextView(R.id.tv_address_name, orderInfoBean.mHouseName);

        holder.setTextView(R.id.tv_house_type, orderInfoBean.mProjectName);
        holder.setTextView(R.id.tv_order_price, orderInfoBean.mOrderTotal);
        Button mButton = holder.getChildView(R.id.btn_pay_money);
        //将改变过显示状态的view设置回原来状态
        holder.getChildView(R.id.tv_time_count_down).setVisibility(View.GONE);
        holder.getChildView(R.id.ll_order_status_desc).setVisibility(View.GONE);
        holder.getChildView(R.id.ll_contract).setVisibility(View.GONE);
        holder.getChildView(R.id.btn_again_buy).setVisibility(View.GONE);
        holder.getChildView(R.id.ll_order_status_desc).setVisibility(View.GONE);
        holder.getChildView(R.id.btn_more_concessions).setVisibility(View.GONE);
        mButton.setVisibility(View.GONE);
        holder.getChildView(R.id.tv_order_status_price_desc).setVisibility(View.VISIBLE);

        switch (orderInfoBean.mOrderTatus) {
            case "1": //待付余款 没有预售证
                mButton.setVisibility(View.VISIBLE);
                mButton.setTextColor(mContext.getResources().getColor(R.color.defuat_text_grey_color));
                holder.setTextView(R.id.tv_order_status, R.string.the_balance_to_be_paid);

                holder.getChildView(R.id.ll_order_status_desc).setVisibility(View.VISIBLE);
                holder.getChildView(R.id.tv_order_status_price_desc).setVisibility(View.GONE);
                holder.setTextView(R.id.tv_order_status_desc, R.string.after_opening_pay_balance);
                mButton.setClickable(false);
                // mButton.setBackground(mContext.getResources().getDrawable(R.drawable.common_btn_entity_grey));
                mButton.setBackgroundResource(R.drawable.common_btn_entity_grey);
                mButton.setTextColor(mContext.getResources().getColor(R.color.defuat_text_grey_color));

                break;
            case "2"://已付清，签署电子合同
                holder.getChildView(R.id.ll_contract).setVisibility(View.VISIBLE);
                holder.setTextView(R.id.tv_order_status, orderInfoBean.mOrderTatus);
                holder.setTextView(R.id.tv_order_status, R.string.outright);
                holder.getChildView(R.id.btn_contract_progress).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, TimeLineActivity.class);
//                        context.startActivity(intent);
                        Toast.makeText(mContext, holder.getPosition() + "合同进度", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.getChildView(R.id.btn_view_contract).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ViewContractActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(mContext, holder.getPosition() + "查看合同", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case "4"://待付余款，已经付过定金了
                mButton.setVisibility(View.VISIBLE);
//                mButton.setBackground(mContext.getResources().getDrawable(R.drawable.common_btn_empty_orange));
                mButton.setBackgroundResource(R.drawable.common_btn_empty_orange);
                mButton.setTextColor(mContext.getResources().getColor(R.color.orange_light));
                holder.getChildView(R.id.ll_order_status_desc).setVisibility(View.VISIBLE);

                holder.setTextView(R.id.tv_order_status_desc, R.string.pay);
                holder.setTextView(R.id.tv_order_status_price_desc, mContext.getResources().getString(R.string.money) + (Double.parseDouble(orderInfoBean.mOrderTotal) - Double.parseDouble(orderInfoBean.mAllPayPrice)));
                holder.setTextView(R.id.tv_order_status, R.string.wait_pay_the_balance);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 分笔支付页面
                        Intent intent = new Intent(mContext, OrderPayActivity.class);
                        Bundle bundle = new Bundle();
                        OrderBean orderBean = new OrderBean();
                        orderBean.orderID = orderInfoBean.mOrderId;
                        orderBean.orderNum = orderInfoBean.mOrderDisplayId;
                        orderBean.orderAllpayPrice = orderInfoBean.mAllPayPrice;
                        orderBean.orderTotal = orderInfoBean.mOrderTotal;
                        orderBean.orderSurplus = Double.parseDouble(orderInfoBean.mOrderTotal) - Double.parseDouble(orderInfoBean.mAllPayPrice) + "";
                        orderBean.projectName = orderInfoBean.mProjectName;
                        orderBean.projectDescription = "";
                        orderBean.orderDeposit = "";
                        orderBean.orderStatus = "1";
                        bundle.putSerializable("order", orderBean);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
                break;

            case "21"://已完成
                holder.getChildView(R.id.ll_contract).setVisibility(View.VISIBLE);
                holder.setTextView(R.id.tv_order_status, orderInfoBean.mOrderTatus);
                holder.getChildView(R.id.btn_contract_progress).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ViewContractActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(mContext, holder.getPosition() + "合同进度", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.getChildView(R.id.btn_view_contract).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ViewContractActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(mContext, holder.getPosition() + "查看合同", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.setTextView(R.id.tv_order_status, R.string.completed);

                break;

            case "102"://未支付
                holder.getChildView(R.id.tv_time_count_down).setVisibility(View.VISIBLE);
//                mButton.setBackground(mContext.getResources().getDrawable(R.drawable.common_btn_empty_orange));
                mButton.setBackgroundResource(R.drawable.common_btn_empty_orange);
                mButton.setTextColor(mContext.getResources().getColor(R.color.orange_light));
                mButton.setVisibility(View.VISIBLE);
                holder.setTextView(R.id.tv_order_status, R.string.be_paid_a_deposit);
                holder.getChildView(R.id.tv_time_count_down).setTag(orderInfoBean.mOrderId);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 定金支付页
                        Intent intent = new Intent(mContext, OrderPayActivity.class);
                        Bundle bundle = new Bundle();
                        OrderBean orderBean = new OrderBean();
                        orderBean.orderID = orderInfoBean.mOrderId;
                        orderBean.orderNum = orderInfoBean.mOrderDisplayId;
                        orderBean.orderAllpayPrice = orderInfoBean.mAllPayPrice;
                        orderBean.orderTotal = orderInfoBean.mOrderTotal;
                        orderBean.orderDeposit = orderInfoBean.mFinalPrice;
                        orderBean.orderStatus = "0";
                        orderBean.orderSurplus = "";
                        orderBean.projectName = orderInfoBean.mProjectName;
                        orderBean.projectDescription = "";
                        bundle.putSerializable("order", orderBean);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
//                holder.getChildView(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //TODO 关闭订单 从列表移除
//                        Toast.makeText(mContext, holder.getPosition() + "删除当前关闭的条目", Toast.LENGTH_SHORT).show();
//                        mCloseItemPosition = holder.getPosition();
//                        closeOrder(orderInfoBean);
//
//                    }
//                });
                if (Long.parseLong(orderInfoBean.mDifferenceData) < mCountDownTime) {
                    startCountDown(holder.getPosition(), orderInfoBean, holder.getConvertView());
                } else {
                    cancelCountDown(holder.getPosition(), orderInfoBean, holder.getConvertView());
                    holder.getChildView(R.id.tv_close).setVisibility(View.VISIBLE);
                }
                break;

            case "101"://已关闭 不知道到是不是支付超时的？
                holder.getChildView(R.id.btn_again_buy).setVisibility(View.VISIBLE);
                holder.setTextView(R.id.tv_order_status, orderInfoBean.mOrderTatus);
                holder.setTextView(R.id.tv_order_status, R.string.closed);
                holder.getChildView(R.id.tv_close).setVisibility(View.VISIBLE);
                holder.getChildView(R.id.btn_again_buy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, ScenicFragmentActivity.class));

                    }
                });
                holder.getChildView(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 关闭订单 从列表移除
                        Toast.makeText(mContext, holder.getPosition() + "删除当前关闭的条目", Toast.LENGTH_SHORT).show();
                        mCloseItemPosition = holder.getPosition();
                        showCallDialog(mContext, orderInfoBean);

                    }
                });
                break;

            case "100": //已取消订单
//                mButton.setTextColor(mContext.getResources().getColor(R.color.defuat_text_grey_color));
                holder.getChildView(R.id.btn_more_concessions).setVisibility(View.VISIBLE);
                holder.setTextView(R.id.tv_order_status, orderInfoBean.mOrderTatus);
                holder.setTextView(R.id.tv_order_status, R.string.cancelled);
                holder.getChildView(R.id.tv_close).setVisibility(View.VISIBLE);
                holder.getChildView(R.id.tv_order_status_price_desc).setVisibility(View.GONE);
                holder.setTextView(R.id.tv_order_status_desc, orderInfoBean.mCusomterName);
                holder.getChildView(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 关闭订单 从列表移除
                        Toast.makeText(mContext, holder.getPosition() + "删除当前关闭的条目", Toast.LENGTH_SHORT).show();
                        mCloseItemPosition = holder.getPosition();
                        showCallDialog(mContext, orderInfoBean);

                    }
                });
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 进入新奇景区页面
                        mContext.startActivity(new Intent(mContext, ScenicFragmentActivity.class));
                    }
                });
                break;
            default:
                break;
        }
    }


    private void startCountDown(final int position, final MyOrderResponse.OrderBean orderBean, View convertView) {
        if (mCountDownTask != null) {
            mCountDownTask.until(convertView, mCountDownTime - Long.parseLong(orderBean.mDifferenceData),
                    1000, new CountDownTimers.OnCountDownListener() {
                        @Override
                        public void onTick(View view, long millisUntilFinished) {
                            doOnTick(position, view, millisUntilFinished, 1000);
                        }

                        @Override
                        public void onFinish(View view) {
                            doOnFinish(position, view);
                        }
                    });
        }
    }

    private void doOnTick(int position, View view, long millisUntilFinished, long countDownInterval) {
        TextView view1 = (TextView) view.findViewById(R.id.tv_time_count_down);
        view1.setText(TimeFormatUtils.dealTime(mContext, millisUntilFinished / 1000) + "");
    }

    private void doOnFinish(int position, View view) {
        //TODO显示关闭按钮 改变订单状态
        view.findViewById(R.id.tv_time_count_down).setVisibility(View.GONE);
        view.findViewById(R.id.tv_close).setVisibility(View.VISIBLE);
        TextView view1 = (TextView) view.findViewById(R.id.tv_time_count_down);
        view1.setText("");

    }

    private void cancelCountDown(int position, MyOrderResponse.OrderBean orderBean, View view) {
        if (mCountDownTask != null) {
            mCountDownTask.cancel(view);

        }
    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {
        OrderCloselResponse orderCloselResponse = (OrderCloselResponse) result;
        if (orderCloselResponse.mResult.equals("01")) {
            Toast.makeText(mContext, "订单删除成功", Toast.LENGTH_SHORT).show();
            //TODO  删除后，刷新界面
            refreshData();
        } else if (orderCloselResponse.mResult.equals("03")) {
            Toast.makeText(mContext, orderCloselResponse.mResult, Toast.LENGTH_SHORT).show();
        }
    }

    //删除数据后 刷新界面
    private void refreshData() {
        getList().remove(mCloseItemPosition);
        notifyDataSetChanged();
    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }

    public void closeOrder(MyOrderResponse.OrderBean orderBean) {
        OrderCloseController orderCloseController = new OrderCloseController(mContext);
        orderCloseController.handleCloseOrderByNet(this, orderBean.mOrderId);
    }

    /**
     * @param mContext
     */
    public void showCallDialog(final Context mContext, final MyOrderResponse.OrderBean orderInfoBean) {

        final NormalDialog normalDialog = new NormalDialog(mContext);
        normalDialog.isTitleShow(false)
                .bgColor(Color.parseColor("#FFFFFF"))
                .cornerRadius(5)
                .contentGravity(Gravity.CENTER)
                .contentTextColor(Color.parseColor("#2A2A2A"))
                .content("确认删除订单？")
                .btnText("取消", "确认")
                .btnTextColor(Color.parseColor("#2A2A2A"), Color.parseColor("#2A2A2A"))
                .setCancelable(false);
        normalDialog.show();
        normalDialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                normalDialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                normalDialog.dismiss();
                closeOrder(orderInfoBean);
//                    refreshData();
            }
        });
    }

}
