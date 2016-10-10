package com.zhonghong.xqshijie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.AboutUsActivity;
import com.zhonghong.xqshijie.activity.IncomeActivity;
import com.zhonghong.xqshijie.activity.InformationActivity;
import com.zhonghong.xqshijie.activity.MainActivity;
import com.zhonghong.xqshijie.activity.MyOrderActivity;
import com.zhonghong.xqshijie.activity.PhoneActivity;
import com.zhonghong.xqshijie.activity.SettingActivity;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseFragment;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;

/**
 * Created by xiezl on 16/6/14.
 */
public class MemberCenterFragment extends BaseFragment {

    private ImageView mMenberCenterReturn;//返回按钮
    private ImageView mMenberCenterSetUp; //设置按钮
    private Button mBtnMemberCenterLog;//点击登陆按钮
    private RelativeLayout mRlMemberCenterIncome;//收益项
    private RelativeLayout mRlMemberCenterMyOrder;//我的定单项
    private RelativeLayout mRlMemberCenterAccountSettings;//账户设置
    private RelativeLayout mRlMemberCenterAboutUs;//关于我们
    private RelativeLayout mRlMemberConterInformation;//个人信息显示栏
    private ImageView mIvMemberCenterHeadPortrait;//头像
    private TextView mTvMemberCenterNickname;//昵称
    private TextView mTvMemberCenterPhone;//电话
    private Intent mIntent;
    private boolean mLogin;//登陆状态
    private String mMemberAvatar;//头像地址
    private String mMemberNickname;//昵称
    private String mMemberMobile;//电话

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_member_center, null);
        mMenberCenterReturn = (ImageView)contentView.findViewById(R.id.iv_member_center_return);
        mMenberCenterSetUp = (ImageView)contentView.findViewById(R.id.iv_member_center_set_up);
        mBtnMemberCenterLog = (Button)contentView.findViewById(R.id.btn_member_center_log);
        mRlMemberCenterIncome = (RelativeLayout)contentView.findViewById(R.id.rl_member_center_income);
        mRlMemberCenterMyOrder = (RelativeLayout)contentView.findViewById(R.id.rl_member_center_my_order);
        mRlMemberCenterAccountSettings = (RelativeLayout)contentView.findViewById(R.id.rl_member_center_account_settings);
        mRlMemberCenterAboutUs = (RelativeLayout)contentView.findViewById(R.id.rl_member_center_about_us);
        mRlMemberConterInformation = (RelativeLayout)contentView.findViewById(R.id.rl_member_conter_information);
        mIvMemberCenterHeadPortrait = (ImageView)contentView.findViewById(R.id.iv_member_center_head_portrait);
        mTvMemberCenterNickname = (TextView)contentView.findViewById(R.id.tv_member_center_nickname);
        mTvMemberCenterPhone = (TextView)contentView.findViewById(R.id.tv_member_center_phone);

        //初始化view的各控件完成
        isPrepared = true;
        lazyLoad();
        return contentView;
    }

    @Override
    protected void handleCreate() {
        mMenberCenterReturn.setOnClickListener(this);
        mMenberCenterSetUp.setOnClickListener(this);
        mBtnMemberCenterLog.setOnClickListener(this);
        mRlMemberConterInformation.setOnClickListener(this);
        mRlMemberCenterIncome.setOnClickListener(this);
        mRlMemberCenterAccountSettings.setOnClickListener(this);
        mRlMemberCenterMyOrder.setOnClickListener(this);
        mRlMemberCenterAboutUs.setOnClickListener(this);
        //判断是否登录
        mLogin =SharedPreferencesUtil.getInstance(getActivity()).getBooleanValue(SharedPreferencesTag.LOGIN);
        if(mLogin){
            mMemberAvatar=SharedPreferencesUtil.getInstance(getActivity()).getStringValue(SharedPreferencesTag.MEMBER_AVATAR);
            mMemberNickname=SharedPreferencesUtil.getInstance(getActivity()).getStringValue(SharedPreferencesTag.MEMBER_NICKNAME);
            mMemberMobile=SharedPreferencesUtil.getInstance(getActivity()).getStringValue(SharedPreferencesTag.MEMBER_MOBILE);
            mBtnMemberCenterLog.setVisibility(View.GONE);
            mRlMemberConterInformation.setVisibility(View.VISIBLE);
            if(!StringUtils.isNull(mMemberAvatar)){
                ImageLoaderUtil.getInstance().loadImageByHeadPortrait(mMemberAvatar,mIvMemberCenterHeadPortrait,R.drawable.ic_member_centre_head_portrait);
            }
            if(!StringUtils.isNull(mMemberNickname)){
                mTvMemberCenterNickname.setText(mMemberNickname);
            }
            if(!StringUtils.isNull(mMemberMobile)){
                mTvMemberCenterPhone.setText(mMemberMobile);
            }
        }else{
            mBtnMemberCenterLog.setVisibility(View.VISIBLE);
            mRlMemberConterInformation.setVisibility(View.GONE);
        }
    }
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handleCreate();
    }

    @Override
    protected void processMessage(Message msg) {

    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_member_center_return://标题栏返回按钮监听
                MainActivity mainActivity= (MainActivity) getActivity();
                mainActivity.mMenu.toggle();
                break;
            case R.id.iv_member_center_set_up://标题栏设置按钮监听
                mIntent =new Intent(getActivity(), SettingActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_member_center_log:
                mIntent =new Intent(getActivity(), PhoneActivity.class);
                startActivity(mIntent);
                break;
            case R.id.rl_member_conter_information:
                mIntent =new Intent(getActivity(), InformationActivity.class);
                startActivity(mIntent);
                break;
            case R.id.rl_member_center_income:
                if(mLogin){
                    mIntent =new Intent(getActivity(), IncomeActivity.class);
                }else{
                    mIntent =new Intent(getActivity(), PhoneActivity.class);
                }
                startActivity(mIntent);
                break;
            case R.id.rl_member_center_account_settings:
                mIntent =new Intent(getActivity(), SettingActivity.class);
                startActivity(mIntent);
                break;
            case R.id.rl_member_center_my_order:
                if(mLogin){
                    mIntent =new Intent(getActivity(), MyOrderActivity.class);
                }else{
                    mIntent =new Intent(getActivity(), PhoneActivity.class);
                }
                startActivity(mIntent);
                break;
            case R.id.rl_member_center_about_us:
                mIntent =new Intent(getActivity(), AboutUsActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {

    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }
}
