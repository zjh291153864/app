package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.widget.TitleView;

/**
 * 支付定金页面
 * Created by jh on 2016/7/21.
 */
public class OrderPayEarnestActivity extends BaseActivity {
    private LinearLayout mTopLayout;
    private TitleView mTitle;
    private TextView mTvOrderEarnestNumber;//订单编号
    private TextView mTvOrderEarnestEarnest;//定金金额
    private Button mBtnOrderEarnestPay;//去支付

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_order_pay_earnest, null);
        initView(contentView);


        return contentView;
    }

    private void initView(View view) {
        mTopLayout = (LinearLayout) view.findViewById(R.id.topLayout);
        mTitle = (TitleView) view.findViewById(R.id.title);
        mTvOrderEarnestNumber = (TextView) view.findViewById(R.id.tv_order_earnest_number);
        mTvOrderEarnestEarnest = (TextView) view.findViewById(R.id.tv_order_earnest_earnest);
        mBtnOrderEarnestPay = (Button) view.findViewById(R.id.btn_order_earnest_pay);

        mTitle.setLeftImageOnClickListener(this);
        mBtnOrderEarnestPay.setOnClickListener(this);
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
            case R.id.btn_order_earnest_pay:
                Intent intent=new Intent(this,PaySuccessEarnestActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
