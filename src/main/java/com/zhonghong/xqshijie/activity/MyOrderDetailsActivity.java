package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jingchen.pulltorefresh.DefaultRefreshLister;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.RefreshUtils;
import com.jingchen.pulltorefresh.parent.ParentFrameLayout;
import com.jingchen.pulltorefresh.pullableview.PullableScrollView;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.adapter.OrderPayInfoAdapter;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.MyOrderPaymentListController;
import com.zhonghong.xqshijie.controller.OrderCancelController;
import com.zhonghong.xqshijie.data.response.MyOrderPaymentListResponse;
import com.zhonghong.xqshijie.data.response.MyOrderResponse;
import com.zhonghong.xqshijie.data.response.OrderCancelResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;
import com.zhonghong.xqshijie.util.ListViewMeasureUtil;
import com.zhonghong.xqshijie.util.NetUtils;
import com.zhonghong.xqshijie.util.TimeFormatUtils;
import com.zhonghong.xqshijie.widget.dialog.NormalDialog;
import com.zhonghong.xqshijie.widget.dialog.OnBtnClickL;

/**
 * Created by zg on 2016/7/1.
 * 我的订单详情
 */
public class MyOrderDetailsActivity extends BaseActivity implements NetInterface {
    private TextView mTvOrderNumber;
    private TextView mTvOrderTime;
    private TextView mTvOrderStatus;//订单状态 七种
    private TextView mTvOrderMoneyCount;
    private TextView mTvOrderTimeCountDown;
    private TextView mTvOrderStatusDesc;//右边的描述
    private ImageView mIvOrderItemPic;
    private TextView mTvOrderAddressName;
    private TextView mTvOrderHouseType;
    private TextView mTvOrderOrderPrice;
    private Button mBtnCancelOrder;
    private ListView mLvOrderPayInfo;
    private Button mBtnContractProgress;
    private Button mBtnContractDownload;
    private Button mBtnPayMoney;
    private Button mBtnAgainBuy;
    private LinearLayout mLlOrderStatusTip;
    private LinearLayout mLlContract;
    private LinearLayout mLlPaymentsList;
    private RelativeLayout mRlCancelLayout;
    private ImageView mImageView;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private PullToRefreshLayout mPtrMyOrderRefreshlayout = null;
    private PullableScrollView mPullableScrollView;
    private DefaultRefreshLister mDefaultRefreshLister = null;
    private ParentFrameLayout mParentInterface;
    private MyOrderPaymentListController mMyOrderPaymentListController;
    private OrderPayInfoAdapter mOrderPayInfoAdapter;
    MyOrderResponse.OrderBean orderInfoBean;
    MyOrderPaymentListResponse myOrderPaymentListResponse;
    boolean isExpand = false;
    private MyCount mc;

    @Override
    public View initContentView() {

        Bundle bundle = getIntent().getExtras();
        orderInfoBean = (MyOrderResponse.OrderBean) bundle.getSerializable(MyOrderActivity.ORDER_BEAN);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_my_order_details, null);
        mParentInterface = (ParentFrameLayout) contentView.findViewById(R.id.parent_my_order_detail_content);
        mParentInterface.setReLoadOnclickListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_SHOWLOADING, null);
            }
        }, RefreshUtils.NetState.NET_NULL);
        initView(contentView);
        return contentView;
    }

    private void showOrder(MyOrderResponse.OrderBean orderInfoBean) {

        mTvOrderStatus.setText(orderInfoBean.mOrderTatus);
        ImageLoaderUtil.getInstance().loadImage(orderInfoBean.mHouseThumb, mIvOrderItemPic, R.drawable.ic_ylt_productdefault);
        mTvOrderAddressName.setText(orderInfoBean.mHouseName);
        mTvOrderHouseType.setText(orderInfoBean.mProjectName);
        mTvOrderOrderPrice.setText(orderInfoBean.mOrderTotal);

        switch (orderInfoBean.mOrderTatus) {
            case "102"://待付定金
                mBtnPayMoney.setVisibility(View.VISIBLE);
                mTvOrderTimeCountDown.setVisibility(View.VISIBLE);
                mTvOrderMoneyCount.setVisibility(View.VISIBLE);
                mBtnCancelOrder.setVisibility(View.VISIBLE);
                mTvOrderMoneyCount.setText(getResources().getString(R.string.money) + orderInfoBean.mFinalPrice);
                //倒计时
                mTvOrderTimeCountDown.setText(orderInfoBean.mCreateTime);
                mRlCancelLayout.setVisibility(View.VISIBLE);
                mLlPaymentsList.setVisibility(View.GONE);
                mTvOrderStatus.setText(R.string.be_paid_a_deposit);
                if ((System.currentTimeMillis() - Long.parseLong(orderInfoBean.mCreateTime)) < 24 * 60 * 60 * 1000) {
                    mc = new MyCount(Long.parseLong(orderInfoBean.mCreateTime) + 24 * 60 * 60 * 1000 - System.currentTimeMillis(), 1000);
                    mc.start();
                } else {
                    changeOrderStatus();
                }
                break;
            case "4"://待付余款
                mTvOrderMoneyCount.setVisibility(View.VISIBLE);
                mTvOrderMoneyCount.setText(getResources().getString(R.string.money) + (Double.parseDouble(orderInfoBean.mOrderTotal) - Double.parseDouble(orderInfoBean.mAllPayPrice)));
                mLlPaymentsList.setVisibility(View.VISIBLE);
                mBtnPayMoney.setVisibility(View.VISIBLE);
                mTvOrderStatus.setText(R.string.the_balance_to_be_paid);
                break;
            case "2"://已付清
                mLlPaymentsList.setVisibility(View.VISIBLE);
                mLlContract.setVisibility(View.VISIBLE);
                mTvOrderStatus.setText(R.string.outright);

                break;
            case "21"://已完成
                mLlPaymentsList.setVisibility(View.VISIBLE);
                mLlContract.setVisibility(View.VISIBLE);
                mTvOrderStatus.setText(R.string.completed);
                mBtnContractDownload.setText(R.string.contract_progress);
                mBtnContractProgress.setText(R.string.view_contract);
                break;
            case "101"://已关闭
                mTvOrderStatusDesc.setText(R.string.order_pay_overtime);
                mTvOrderStatusDesc.setVisibility(View.VISIBLE);
                mBtnAgainBuy.setVisibility(View.VISIBLE);
                mTvOrderStatus.setText(R.string.closed);
                mLlPaymentsList.setVisibility(View.GONE);
                break;
            case "1"://待付余款 无预售证
                mTvOrderStatusDesc.setVisibility(View.VISIBLE);
                mTvOrderStatusDesc.setText(R.string.after_opening_pay_balance);
                mTvOrderMoneyCount.setVisibility(View.GONE);
                mBtnPayMoney.setClickable(false);
                mBtnPayMoney.setBackgroundResource(R.drawable.common_btn_entity_grey);

                mTvOrderStatusDesc.setVisibility(View.VISIBLE);
                mLlPaymentsList.setVisibility(View.VISIBLE);
                mBtnPayMoney.setVisibility(View.VISIBLE);
                mTvOrderStatus.setText(R.string.the_balance_to_be_paid);
                break;
            case "100"://已取消
                mTvOrderStatus.setText(R.string.cancelled);
                mTvOrderStatus.setVisibility(View.VISIBLE);
                mBtnAgainBuy.setVisibility(View.VISIBLE);
                mBtnAgainBuy.setText(getResources().getString(R.string.more_concessions));
                mLlPaymentsList.setVisibility(View.GONE);
                break;

            default:
                break;

        }

        //交易记录列表
        if (myOrderPaymentListResponse.mPaymentsBean.size() < 4) {
            //隐藏向下箭头
            mImageView.setVisibility(View.GONE);
            mOrderPayInfoAdapter.setNewList(myOrderPaymentListResponse.mPaymentsBean);
        } else {//设置默认显示3行
            mImageView.setVisibility(View.VISIBLE);
            mOrderPayInfoAdapter.setNewList(myOrderPaymentListResponse.mPaymentsBean.subList(0, 3));
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isExpand) {
                        isExpand = true;
                        mImageView.findViewById(R.id.iv_arrow).setBackgroundResource(R.drawable.up_arrow);
                        mOrderPayInfoAdapter.setNewList(myOrderPaymentListResponse.mPaymentsBean);
                        ListViewMeasureUtil.setListViewHeightBasedOnChildren(mLvOrderPayInfo);
                    } else {
                        isExpand = false;
                        mImageView.findViewById(R.id.iv_arrow).setBackgroundResource(R.drawable.down_arrow);
                        mOrderPayInfoAdapter.setNewList(myOrderPaymentListResponse.mPaymentsBean.subList(0, 3));
                        ListViewMeasureUtil.setListViewHeightBasedOnChildren(mLvOrderPayInfo);
                    }
                }
            });
        }
        ListViewMeasureUtil.setListViewHeightBasedOnChildren(mLvOrderPayInfo);

    }

    private void initView(View contentView) {
        mPtrMyOrderRefreshlayout = ((PullToRefreshLayout) contentView.findViewById(R.id.ptr_my_order_detail_refreshlayout));
        mPullableScrollView = (PullableScrollView) contentView.findViewById(R.id.scv_order_detail_refresh);
        mTvOrderNumber = (TextView) contentView.findViewById(R.id.tv_order_number);
        mTvOrderTime = (TextView) contentView.findViewById(R.id.tv_order_time);
        mTvOrderStatus = (TextView) contentView.findViewById(R.id.tv_order_status);
        mTvOrderMoneyCount = (TextView) contentView.findViewById(R.id.tv_pay_money_count);
        mTvOrderTimeCountDown = (TextView) contentView.findViewById(R.id.tv_time_count_down);
        mTvOrderStatusDesc = (TextView) contentView.findViewById(R.id.tv_order_status_desc);
        mIvOrderItemPic = (ImageView) contentView.findViewById(R.id.iv_order_item_pic);
        mTvOrderAddressName = (TextView) contentView.findViewById(R.id.tv_address_name);
        mTvOrderHouseType = (TextView) contentView.findViewById(R.id.tv_house_type);
        mTvOrderOrderPrice = (TextView) contentView.findViewById(R.id.tv_order_price);
        mBtnCancelOrder = (Button) contentView.findViewById(R.id.btn_cancel_order);
        mLvOrderPayInfo = (ListView) contentView.findViewById(R.id.lv_order_pay_info);
        mBtnContractProgress = (Button) contentView.findViewById(R.id.btn_contract_progress);
        mBtnContractDownload = (Button) contentView.findViewById(R.id.btn_contract_download);
        mBtnPayMoney = (Button) contentView.findViewById(R.id.btn_pay_money);
        mBtnAgainBuy = (Button) contentView.findViewById(R.id.btn_again_buy);
        mLlOrderStatusTip = (LinearLayout) contentView.findViewById(R.id.ll_order_status_tip);
        mLlContract = (LinearLayout) contentView.findViewById(R.id.ll_contract);
        mLlPaymentsList = (LinearLayout) contentView.findViewById(R.id.ll_payments_list);
        mRlCancelLayout = (RelativeLayout) contentView.findViewById(R.id.rl_cancel_layout);
        mImageView = (ImageView) contentView.findViewById(R.id.iv_arrow);
        mBtnCancelOrder.setOnClickListener(this);
        mBtnContractProgress.setOnClickListener(this);
        mBtnContractDownload.setOnClickListener(this);
        mBtnPayMoney.setOnClickListener(this);
        mBtnAgainBuy.setOnClickListener(this);
        mOrderPayInfoAdapter = new OrderPayInfoAdapter(this);
        mLvOrderPayInfo.setAdapter(mOrderPayInfoAdapter);
        //下拉刷新及上拉加载的回调函数
        mPtrMyOrderRefreshlayout.setOnRefreshListener(mDefaultRefreshLister = new DefaultRefreshLister(MyOrderDetailsActivity.this) {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                super.onRefresh(pullToRefreshLayout);
                if (havaNetWork) {
                    initData();
                } else {
                    if (mDefaultRefreshLister != null) {
                        mDefaultRefreshLister.notifynonetwork(pullToRefreshLayout);
                    }
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                super.onLoadMore(pullToRefreshLayout);
                mDefaultRefreshLister.notifynonetwork(pullToRefreshLayout);
            }
        });
    }

    @Override
    public void handleCreate() {
        initData();
    }

    private void initData() {
//        this.showProgressDialog(R.string.loading, false);//显示进度框
        if (mMyOrderPaymentListController == null) {
            mMyOrderPaymentListController = new MyOrderPaymentListController(this);
        }
        mMyOrderPaymentListController.handleMyOrderPaymentsListByNet(this, orderInfoBean.mOrderId);
    }

    private void reConfirmCancel() {
        final NormalDialog normalDialog = new NormalDialog(this);
        normalDialog.isTitleShow(false)
                .bgColor(Color.parseColor("#FFFFFF"))
                .cornerRadius(5)
                .contentGravity(Gravity.CENTER)
                .contentTextColor(Color.parseColor("#2A2A2A"))
                .content("确认取消订单？")
                .btnTextColor(Color.parseColor("#2A2A2A"), Color.parseColor("#2A2A2A"))
                .show();
        normalDialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                normalDialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                normalDialog.dismiss();
                OrderCancelController cancelController = new OrderCancelController(MyOrderDetailsActivity.this);
                cancelController.handleCancelOrderByNet(MyOrderDetailsActivity.this, orderInfoBean.mOrderId);
            }
        });

    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_order:
                Toast.makeText(this, "取消订单", Toast.LENGTH_SHORT).show();
                reConfirmCancel();
                break;
            case R.id.btn_again_buy:
                startActivity(new Intent(this, ScenicFragmentActivity.class));
                Toast.makeText(this, "查看更多优惠", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_pay_money:
                Toast.makeText(this, "支付", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, OrderPayActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_contract_progress:
                Toast.makeText(this, "合同进度", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_contract_download:
                Toast.makeText(this, "下载合同", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {
//        this.hideProgressDialog();//隐藏进度框
        if (mDefaultRefreshLister != null) {
            mDefaultRefreshLister.notifyhandledata(true, mPtrMyOrderRefreshlayout);//这句是核心代码  true代表成功 , false代表失败，若有其他状态我可以覆盖一个带字符串的参数来分别识别不同状态的回调
        }

        if (interfaceAction.equals(NetTag.GETMYORDERPAYMENTLIST)) {
            basicHandler.obtainMessage(0, result).sendToTarget();
        } else if (interfaceAction.equals(NetTag.ORDERCANCEL)) {
            basicHandler.obtainMessage(1, result).sendToTarget();
        }
    }

    @Override
    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case 0:
                myOrderPaymentListResponse = (MyOrderPaymentListResponse) msg.obj;
                showOrder(orderInfoBean);
                break;
            case 1:
                OrderCancelResponse orderCancelResponse = (OrderCancelResponse) msg.obj;
                if (orderCancelResponse.mResult.equals("01")) {
                    Toast.makeText(this, "订单取消成功", Toast.LENGTH_SHORT).show();
                    mc.cancel();
                    changeOrderStatus();
                    //
                } else if (orderCancelResponse.mResult.equals("03")) {
                    Toast.makeText(this, orderCancelResponse.mResult, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);//异常处理 加载完毕时

    }

    //将订单改变成取消状态
    private void changeOrderStatus() {
        orderInfoBean.mOrderTatus = "100";
        mTvOrderTimeCountDown.setVisibility(View.GONE);
        mTvOrderMoneyCount.setVisibility(View.GONE);
        mBtnCancelOrder.setVisibility(View.GONE);
        showOrder(orderInfoBean);
    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
//        this.hideProgressDialog();//隐藏进度框
        if (mParentInterface != null) {
            if (NetUtils.isNetConnected()) {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
            } else {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_NONET, basicHandler);
            }
        }
    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }

    /*定义一个倒计时的内部类*/
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            changeOrderStatus();

        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTvOrderTimeCountDown.setText(TimeFormatUtils.dealTime(mContext, millisUntilFinished / 1000) + "");
        }

    }
}
