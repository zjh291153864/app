package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.appupdate.MessageBean;
import com.zhonghong.xqshijie.appupdate.UpdateAppController;
import com.zhonghong.xqshijie.appupdate.UpdateConstants;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.util.AppUtils;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.TitleView;
import com.zhonghong.xqshijie.widget.dialog.NormalDialog;
import com.zhonghong.xqshijie.widget.dialog.OnBtnClickL;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by zg on 2016/6/27.
 * 设置页面
 */
public class SettingActivity extends BaseActivity {

    private TitleView mTitle;//标题栏
    private Button mButtonExitLogin;//退出登录
    private TextView mTextViewFeedBack;//意见反馈
    private TextView mTextViewAddressList;//地址列表
    //当前版本
    private TextView mTextViewCurrentVersion;
    private RelativeLayout mRlCurrentVersion;
    private String mVerName;
    private TextView mTextViewChangePassword;//修改密码

    private TextView mTextViewForOur;//给我们评价
    private boolean mLogin;//登陆状态


    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_setting, null);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        mButtonExitLogin = (Button) contentView.findViewById(R.id.btn_exit_login);
        mTextViewAddressList = (TextView) contentView.findViewById(R.id.tv_address_list);
        mTextViewChangePassword = (TextView) contentView.findViewById(R.id.tv_change_password);
        mRlCurrentVersion = (RelativeLayout) contentView.findViewById(R.id.rl_current_version);
        mTextViewCurrentVersion = (TextView) contentView.findViewById(R.id.tv_current_version);
        mTextViewFeedBack = (TextView) contentView.findViewById(R.id.tv_feedback);
        mTextViewForOur = (TextView) contentView.findViewById(R.id.tv_for_our_evaluation);
        mTitle = (TitleView) contentView.findViewById(R.id.title);

        mTitle.setLeftImageOnClickListener(this);
        mTextViewAddressList.setOnClickListener(this);
        mTextViewChangePassword.setOnClickListener(this);
        mRlCurrentVersion.setOnClickListener(this);
        mTextViewFeedBack.setOnClickListener(this);
        mTextViewForOur.setOnClickListener(this);
        mButtonExitLogin.setOnClickListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void handleCreate() {
        mLogin= SharedPreferencesUtil.getInstance(this).getBooleanValue(SharedPreferencesTag.LOGIN);
        mVerName= AppUtils.getVerName();
        if(!StringUtils.isNull(mVerName)){
            mTextViewCurrentVersion.setText(mVerName);
        }
        if(mLogin){
            mButtonExitLogin.setVisibility(View.VISIBLE);
            mTextViewAddressList.setVisibility(View.VISIBLE);
            mTextViewChangePassword.setVisibility(View.VISIBLE);
        }else{
            mButtonExitLogin.setVisibility(View.GONE);
            mTextViewAddressList.setVisibility(View.GONE);
            mTextViewChangePassword.setVisibility(View.GONE);
        }
    }

    @Override
    protected void customOnClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_address_list:
                intent = new Intent(this, AddressListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_change_password:
                intent = new Intent(this, ChangePwdActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_for_our_evaluation:
                intent = new Intent(this, ChangePwdActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_feedback:
                intent = new Intent(this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_current_version:
                this.showProgressDialog(R.string.loading,false);//显示进度框
                UpdateAppController.startUpdateService(this);
                break;
            case R.id.common_title_TV_left:
                finish();
                break;
            case R.id.btn_exit_login:
                final NormalDialog normalDialog=new NormalDialog(this);
                normalDialog.isTitleShow(false)
                        .bgColor(Color.parseColor("#FFFFFF"))
                        .cornerRadius(5)
                        .contentGravity(Gravity.CENTER)
                        .contentTextColor(Color.parseColor("#2A2A2A"))
                        .content("确定退出登陆？")
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
                        //清空存储用户信息
                        PublicUtils.removeUserInformation(SettingActivity.this);
                        handleCreate();
                        normalDialog.dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(MessageBean event) {
        this.hideProgressDialog();//隐藏进度框
        if(UpdateConstants.CANCEL_NETERROR.equals(event.mMsg)){//网络连接失败
            InfoToast.makeText(this,getResources().getString(R.string.network_requests_fail), Toast.LENGTH_SHORT).show();
        }else if(UpdateConstants.CANCEL_NETERROR.equals(event.mMsg)){//当前是最新版本
            final NormalDialog normalDialog=new NormalDialog(this);
            normalDialog.isTitleShow(false).bgColor(Color.parseColor("#FFFFFF"))
                    .cornerRadius(5)
                    .contentGravity(Gravity.CENTER)
                    .contentTextColor(Color.parseColor("#2A2A2A"))
                    .content("当前APP已是最新版本")
                    .btnNum(1)
                    .btnText(getResources().getString(R.string.determine))
                    .btnTextColor(Color.parseColor("#2A2A2A"))
                    .show();
            normalDialog.setOnBtnClickL(new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    normalDialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
