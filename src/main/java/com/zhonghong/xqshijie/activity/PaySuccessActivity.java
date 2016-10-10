package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.widget.TitleView;

/**
 * 全款支付部分成功页面
 * Created by xiezl on 16/7/15.
 */
public class PaySuccessActivity extends BaseActivity {

    private TitleView mTitle;//标题
    private TextView mTvSuccessMoney;//金额
    private TextView mTvSuccessOrderNumber;//定单编号
    private TextView mTvSuccessProjectName;//项目名称
    private TextView mTvSuccessPayPaid;//付款金额
    private TextView mTvSuccessWaitPay;//待付金额
    private Button mBtnSuccessPay;//继续支付

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_pay_success, null);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mTvSuccessMoney = (TextView) contentView.findViewById(R.id.tv_success_money);
        mTvSuccessOrderNumber = (TextView) contentView.findViewById(R.id.tv_success_order_number);
        mTvSuccessProjectName = (TextView) contentView.findViewById(R.id.tv_success_project_name);
        mTvSuccessPayPaid = (TextView) contentView.findViewById(R.id.tv_success_pay_paid);
        mTvSuccessWaitPay = (TextView) contentView.findViewById(R.id.tv_success_wait_pay);
        mBtnSuccessPay = (Button) contentView.findViewById(R.id.btn_success_pay);

        mTitle.setLeftImageOnClickListener(this);
        mBtnSuccessPay.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void handleCreate() {


    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_TV_left:
                finish();
                break;
            case R.id.btn_success_pay:
                Intent intent=new Intent(this,OrderPayActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void processMessage(Message msg) {
        super.processMessage(msg);
    }
}
