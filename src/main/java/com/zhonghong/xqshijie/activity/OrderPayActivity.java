package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.alipay.PayResult;
import com.zhonghong.xqshijie.alipay.payUtil;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.PayChannelListController;
import com.zhonghong.xqshijie.data.bean.OrderBean;
import com.zhonghong.xqshijie.data.response.OrderAliPayResponse;
import com.zhonghong.xqshijie.data.response.PayChannelListResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.TitleView;
import com.zhonghong.xqshijie.widget.popupwindow.BottomPopupWindow;

/**
 * 支付全款页面
 * Created by xiezl on 16/7/15.
 */
public class OrderPayActivity extends BaseActivity implements NetInterface{

    private TitleView mTitle;//标题
    private LinearLayout mTopLayout;
    private TextView mTvOrderPayOrderNumber;//定单编号
    private TextView mTvOrderPayPayPaid;//已付金额
    private TextView mTvOrderPayWaitpay;//待付金额
    private EditText mEtOrderPayPayMoney;//支付金额

    private Button mBtnOrderPayPay;//去支付
    private BottomPopupWindow bottomPopupWindow;
    private PayChannelListController payChannelListController;
    private OrderBean orderBean;


    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_order_pay, null);
        initView(contentView);
        return contentView;
    }

    private void initView(View view) {

        mTopLayout = (LinearLayout) view.findViewById(R.id.topLayout);
        mTitle = (TitleView) view.findViewById(R.id.title);
        mTvOrderPayOrderNumber = (TextView) view.findViewById(R.id.tv_order_pay_order_number);
        mTvOrderPayPayPaid = (TextView) view.findViewById(R.id.tv_order_pay_pay_paid);
        mTvOrderPayWaitpay = (TextView) view.findViewById(R.id.tv_order_pay_wait_pay);
        mEtOrderPayPayMoney = (EditText) view.findViewById(R.id.ev_order_pay_pay_money);
        mBtnOrderPayPay = (Button) view.findViewById(R.id.btn_order_pay_pay);

        mTitle.setLeftImageOnClickListener(this);
        mBtnOrderPayPay.setOnClickListener(this);

    }

    @Override
    public void handleCreate() {
        orderBean= (OrderBean) getIntent().getSerializableExtra("order");//传递过来的支付信息
        Log.i("aaa",orderBean.toString());
        if(payChannelListController==null){
            payChannelListController=new PayChannelListController(this);
        }
        if (orderBean!=null) {
            mTvOrderPayOrderNumber.setText(StringUtils.repNull(orderBean.orderNum));
            mTvOrderPayPayPaid.setText(StringUtils.repNull(orderBean.orderAllpayPrice));
            mTvOrderPayWaitpay.setText("0");
        }

        //获取支付渠道列表
        payChannelListController.handlePayChannelListByNet(this);
        bottomPopupWindow = new BottomPopupWindow(this);
        bottomPopupWindow.addButton(R.string.pay_type_aipay, new View.OnClickListener() {//支付宝
            @Override
            public void onClick(View v) {
                bottomPopupWindow.dismiss();
                if (orderBean!=null) {
                    showProgressDialog(R.string.loading1,false);//显示进度框
                    handleOrderAlipayByNet(String.valueOf(orderBean.orderID), "0.01", "逸乐通", "逸乐通");
                }
            }
        }, false);
    }

    /**
     * 支付记录插入（支付单号取得）
     * @param order_id 订单ID
     * @param pay_amount 实付金额
     * @param subject 商品名称
     * @param body 商品描述（目前和商品名称一个值）
     */
    private void handleOrderAlipayByNet(String order_id,String pay_amount,String subject,String body){
        payChannelListController.handleOrderAlipayByNet(this,order_id,pay_amount,subject,body);
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_TV_left:
                finish();
                break;
            case R.id.btn_order_pay_pay:
                bottomPopupWindow.alertPopupwindow(mTopLayout);
                break;
        }

    }

    @Override
    protected void processMessage(Message msg) {
        super.processMessage(msg);
        hideProgressDialog();//隐藏进度框
        switch (msg.what) {
            case payUtil.SDK_PAY_FLAG:
                PayResult payResult = new PayResult((String) msg.obj);
                /**
                 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                 * docType=1) 建议商户依赖异步通知
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    Toast.makeText(OrderPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OrderPayActivity.this,PaySuccessActivity.class);
                    startActivity(intent);
                } else {
                    // 判断resultStatus 为非"9000"则代表可能支付失败
                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(OrderPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        Toast.makeText(OrderPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderPayActivity.this,PayFailActivity.class);
                        startActivity(intent);

                    }
                }
                break;
        }
    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {
        if(interfaceAction.equals(NetTag.PAYCHANNELLIST)){//获取支付渠道
            PayChannelListResponse payChannelListResponse= (PayChannelListResponse) result;
            Log.i("aaa",payChannelListResponse.toString());
        }else if(interfaceAction.equals(NetTag.ORDERALIPAY)){//支付记录插入（支付单号取得）
            OrderAliPayResponse orderAliPayResponse= (OrderAliPayResponse) result;
            Log.i("aaa",orderAliPayResponse.toString());

            payUtil payUtil = new payUtil();
            payUtil.pay(OrderPayActivity.this,basicHandler,"逸乐通","逸乐通","0.01",orderAliPayResponse.mNumber);
        }
    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }
}
