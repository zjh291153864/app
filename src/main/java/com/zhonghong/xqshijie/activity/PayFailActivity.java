package com.zhonghong.xqshijie.activity;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;

/**
 * Created by xiezl on 16/7/15.
 */
public class PayFailActivity extends BaseActivity {

    private RelativeLayout mTopLayout;
    private TextView mOrderNumberTv;
    private TextView mOrderPayPaidTv;
    private TextView mOrderPayWaitpayTv;
    private EditText mOrderPayMoneyEv;
    private Button mBtnPay;



    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_pay_fail, null);
        initView(contentView);
        return contentView;
    }

    private void initView(View view) {

        mTopLayout = (RelativeLayout) view.findViewById(R.id.topLayout);
        mOrderNumberTv = (TextView) view.findViewById(R.id.tv_order_pay_order_number);
        mOrderPayPaidTv = (TextView) view.findViewById(R.id.tv_order_pay_pay_paid);
        mOrderPayWaitpayTv = (TextView) view.findViewById(R.id.tv_order_pay_wait_pay);
        mOrderPayMoneyEv = (EditText) view.findViewById(R.id.ev_order_pay_pay_money);
        mBtnPay = (Button) view.findViewById(R.id.btn_order_pay_pay);
//        mBtnPay.setOnClickListener(this);

    }

    @Override
    public void handleCreate() {


    }

    @Override
    protected void customOnClick(View v) {

    }

    @Override
    protected void processMessage(Message msg) {
        super.processMessage(msg);
    }
}
