package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.widget.TitleView;

/**
 * 定金支付成功页面
 * Created by jh on 2016/7/21.
 */
public class PaySuccessEarnestActivity extends BaseActivity {
    private TitleView mTitle;//标题
    private TextView mTvEarnestMoney;//金额
    private TextView mTvEarnestOrderNumber;//订单编号
    private TextView mTvEarnestProjectName;//项目名称
    private TextView mTvEarnestPayPaid;//付款金额
    private Button mBtnEarnestPay;//继续支付

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_pay_success_earnest, null);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mTvEarnestMoney = (TextView) contentView.findViewById(R.id.tv_earnest_money);
        mTvEarnestOrderNumber = (TextView) contentView.findViewById(R.id.tv_earnest_order_number);
        mTvEarnestProjectName = (TextView) contentView.findViewById(R.id.tv_earnest_project_name);
        mTvEarnestPayPaid = (TextView) contentView.findViewById(R.id.tv_earnest_pay_paid);
        mBtnEarnestPay = (Button) contentView.findViewById(R.id.btn_earnest_pay);

        mTitle.setLeftImageOnClickListener(this);
        mBtnEarnestPay.setOnClickListener(this);
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
        		case R.id.btn_earnest_pay:
                    Intent intent=new Intent(this,OrderPayActivity.class);
                    startActivity(intent);
        			break;

        		default:
        			break;
        		}
    }
}
