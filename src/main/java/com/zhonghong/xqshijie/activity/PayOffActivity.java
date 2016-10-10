package com.zhonghong.xqshijie.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.widget.TitleView;

/**
 * //全款支付完成
 * Created by jh on 2016/7/21.
 */
public class PayOffActivity extends BaseActivity{
    private TitleView mTitle;//标题
    private TextView mTvOffProgect;//项目
    private TextView mTvOffOrderNumber;//定单编号
    private TextView mTvOffProjectName;//项目名称
    private TextView mTvOffPayPaid;//已付金额
    private Button mBtnOffLookOrder;//查看订单
    private Button mBtnOffViewContract;//查看合同

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_pay_off, null);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mTvOffProgect = (TextView) contentView.findViewById(R.id.tv_off_project);
        mTvOffOrderNumber = (TextView) contentView.findViewById(R.id.tv_off_order_number);
        mTvOffProjectName = (TextView) contentView.findViewById(R.id.tv_off_project_name);
        mTvOffPayPaid = (TextView) contentView.findViewById(R.id.tv_off_pay_paid);
        mBtnOffLookOrder = (Button) contentView.findViewById(R.id.btn_off_look_order);
        mBtnOffViewContract = (Button) contentView.findViewById(R.id.btn_off_view_contract);

        mTitle.setLeftImageOnClickListener(this);
        mBtnOffLookOrder.setOnClickListener(this);
        mBtnOffViewContract.setOnClickListener(this);
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
            case R.id.btn_off_look_order:

                break;
            case R.id.btn_off_view_contract:

                break;

            default:
                break;
        }
    }
}
