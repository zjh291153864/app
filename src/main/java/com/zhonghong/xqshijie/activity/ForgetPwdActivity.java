package com.zhonghong.xqshijie.activity;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.LoginRegController;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.MyCountTimer;
import com.zhonghong.xqshijie.util.PasswordUtil;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.TitleView;

/**
 * 找回密码页面
 * Created by jh on 2016/6/27.
 */
public class ForgetPwdActivity extends BaseActivity{
    private TitleView mTitle;
    //    手机号输入框
    private EditText mEtForgetPwdPhone;
    //    验证码输入框
    private EditText mEtForgetPwdSms;
    //    发送验证码按钮
    private Button mBtnForgetPwdSms;
    //    密码输入框
    private EditText mEtForgetPwdPwd;
    //    密码明文暗文按钮
    private ImageView mIvForgetPwdPwd;
    //    下一步
    private Button mBtnForgetpwdNext;
    //    密码是否隐藏
    private boolean mIsVisible = true;
    private String mSms="";
    private String mPhone="";
    private String mPassword="";
    private String mCode="";
    private LoginRegController mLoginRegController;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_forgetpwd, null);
        mTitle = (TitleView)contentView.findViewById(R.id.title);
        mEtForgetPwdPhone = (EditText) contentView.findViewById(R.id.et_forgetpwd_phone);
        mEtForgetPwdSms = (EditText) contentView.findViewById(R.id.et_forgetpwd_sms);
        mBtnForgetPwdSms = (Button) contentView.findViewById(R.id.btn_forgetpwd_sms);
        mEtForgetPwdPwd = (EditText) contentView.findViewById(R.id.et_forgetpwd_pwd);
        mIvForgetPwdPwd = (ImageView) contentView.findViewById(R.id.iv_forgetpwd_pwd);
        mBtnForgetpwdNext = (Button) contentView.findViewById(R.id.btn_forgetpwd_next);

        mTitle.setLeftImageOnClickListener(this);
        mBtnForgetPwdSms.setOnClickListener(this);
        mIvForgetPwdPwd.setOnClickListener(this);
        mBtnForgetpwdNext.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void handleCreate() {
        //        获取传递过来的手机号
        mPhone=getIntent().getStringExtra("mPhone");
        mEtForgetPwdPhone.setText(mPhone);
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_TV_left:
                finish();
                break;
            case R.id.btn_forgetpwd_sms:
                mPhone= mEtForgetPwdPhone.getText().toString();
                if(verificationPhone()){
//                    发送验证码的方法
                    sendSMSVerificationCode();
//                   倒计时且不能点击
                    MyCountTimer timeCount = new MyCountTimer(mBtnForgetPwdSms, 0xfff30008, 0xff969696);
                    timeCount.start();
                }
                break;
            case R.id.iv_forgetpwd_pwd:
                String content = mEtForgetPwdPwd.getText().toString();
//               密码明文暗文
                if (mIsVisible) {
                    mEtForgetPwdPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEtForgetPwdPwd.setSelection(content.length());
                    mIvForgetPwdPwd.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyes));
                    mIsVisible = false;
                } else {
                    mEtForgetPwdPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtForgetPwdPwd.setSelection(content.length());
                    mIvForgetPwdPwd.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyes_closed));
                    mIsVisible = true;
                }
                break;
            case R.id.btn_forgetpwd_next:
                mPhone= mEtForgetPwdPhone.getText().toString();
                mSms= mEtForgetPwdSms.getText().toString();
                mPassword= mEtForgetPwdPwd.getText().toString();
                verificationPswAndPhone();
                break;
            default:
                break;
        }
    }

    //    输入数据判断
    private void verificationPswAndPhone() {
        if (!verificationPhone()) {
            return;
        }
        if (StringUtils.isNull(mSms)) {
            InfoToast.makeText(this, this.getString(R.string.please_enter_verification_code), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNull(mPassword)) {
            InfoToast.makeText(this, this.getString(R.string.please_enter_your_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isRigthPW(mPassword)) {
            InfoToast.makeText(this, this.getString(R.string.the_password_is_6_to_18_digits_and_letters), Toast.LENGTH_SHORT).show();
            mEtForgetPwdPwd.setFocusable(true);
            mEtForgetPwdPwd.setFocusableInTouchMode(true);
            mEtForgetPwdPwd.requestFocus();
            return;
        }
        if (!mSms.equals(mCode)) {
            InfoToast.makeText(this, this.getString(R.string.code_is_incorrect), Toast.LENGTH_SHORT).show();
            mEtForgetPwdSms.setFocusable(true);
            mEtForgetPwdSms.setFocusableInTouchMode(true);
            mEtForgetPwdSms.requestFocus();
            return;
        }else {
            mPassword= PasswordUtil.encryptPassword(mPassword,ForgetPwdActivity.this);
            requestFrogetpwd();
        }
    }

    //    手机号判断
    public boolean verificationPhone(){
        if(StringUtils.isNull(mPhone)){
            InfoToast.makeText(this, this.getString(R.string.the_phone_number_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!PublicUtils.isTelNum(mPhone)){
            InfoToast.makeText(this, this.getString(R.string.the_phone_number_is_not_legitimate), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    //    发送验证码
    private void sendSMSVerificationCode(){
        if(mLoginRegController ==null){
            mLoginRegController =new LoginRegController(this);
        }
        mLoginRegController.handleForgitPwdCodesByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                LoginResponse loginResponse=(LoginResponse) result;
                if(loginResponse.mResult.equals("01")){
                    mCode=loginResponse.mVaildateCode;
                    InfoToast.makeText(ForgetPwdActivity.this,getResources().getString(R.string.sent_verification_code)+mPhone,Toast.LENGTH_SHORT).show();
                }else{
                    InfoToast.makeText(ForgetPwdActivity.this,loginResponse.mMsg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                InfoToast.makeText(ForgetPwdActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mPhone);
    }

    //    密码找回
    private void requestFrogetpwd() {
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(mLoginRegController ==null){
            mLoginRegController =new LoginRegController(this);
        }
        mLoginRegController.handleForgitPwdByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                ForgetPwdActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse=(LoginResponse) result;
                if(loginResponse.mResult.equals("01")){//密码修改成功
                    String newPwdCode=SharedPreferencesUtil.getInstance(ForgetPwdActivity.this).getStringValue(SharedPreferencesTag.NEWPWDCODE);
                    SharedPreferencesUtil.getInstance(ForgetPwdActivity.this).putStringValue(SharedPreferencesTag.PWDCODE,newPwdCode);
                    InfoToast.makeText(ForgetPwdActivity.this,getResources().getString(R.string.password_reset_complete),Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    InfoToast.makeText(ForgetPwdActivity.this,loginResponse.mMsg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                ForgetPwdActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(ForgetPwdActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mPhone,mPassword,mSms);
    }
}
