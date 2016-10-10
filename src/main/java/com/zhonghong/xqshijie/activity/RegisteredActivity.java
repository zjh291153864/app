package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.LoginRegController;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.MyCountTimer;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.TitleView;

/**
 * 注册页面
 * Created by jh on 2016/6/27.
 */
public class RegisteredActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    private TitleView mTitle;
    //    验证码输入框
    private EditText mEtRegisteredSms;
    //    请求验证码按钮
    private Button mBtnRegisteredSms;
    //    密码输入框
    private EditText mEtRegisteredPwd;
    //    密码明文暗文
    private ImageView mIvRegisteredPwd;
    //    注册按钮
    private Button mBtnRegisteredRegistered;
    //    同意条款选取框
    private CheckBox mCbRegisteredAgree;
    //    条款链接
    private TextView mTvRegisteredXqsj;
    private String mPhone="";
    private String mSms="";
    private String mCode="";
    private String mPassword="";
    //    密码是否隐藏
    private boolean mIsVisible = true;
    private LoginRegController mLoginRegController;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_registered, null);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mEtRegisteredSms = (EditText) contentView.findViewById(R.id.et_registered_sms);
        mBtnRegisteredSms = (Button) contentView.findViewById(R.id.btn_registered_sms);
        mEtRegisteredPwd = (EditText) contentView.findViewById(R.id.et_registered_pwd);
        mIvRegisteredPwd = (ImageView) contentView.findViewById(R.id.iv_registered_pwd);
        mBtnRegisteredRegistered = (Button) contentView.findViewById(R.id.btn_registered_registered);
        mCbRegisteredAgree = (CheckBox) contentView.findViewById(R.id.cb_registered_agree);
        mTvRegisteredXqsj = (TextView) contentView.findViewById(R.id.tv_registered_xqsj);

        mTitle.setLeftImageOnClickListener(this);
        mBtnRegisteredSms.setOnClickListener(this);
        mIvRegisteredPwd.setOnClickListener(this);
        mBtnRegisteredRegistered.setOnClickListener(this);
        mTvRegisteredXqsj.setOnClickListener(this);
        mCbRegisteredAgree.setOnCheckedChangeListener(this);
        return contentView;
    }

    @Override
    public void handleCreate() {
//        获取传递过来的手机号
        mPhone= getIntent().getStringExtra("mPhone");
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_TV_left:
                finish();
                break;
            case R.id.btn_registered_sms:
//               发送验证码的方法
                sendSMSVerificationCode();
//               传入了文字颜色值
                MyCountTimer timeCount = new MyCountTimer(mBtnRegisteredSms, 0xfff30008, 0xff969696);
                timeCount.start();
                break;
            case R.id.iv_registered_pwd:
                String content = mEtRegisteredPwd.getText().toString();
//               密码明文暗文
                if (mIsVisible) {
                    mEtRegisteredPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEtRegisteredPwd.setSelection(content.length());
                    mIvRegisteredPwd.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyes));
                    mIsVisible = false;
                } else {
                    mEtRegisteredPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtRegisteredPwd.setSelection(content.length());
                    mIvRegisteredPwd.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyes_closed));
                    mIsVisible = true;
                }
                break;
            case R.id.btn_registered_registered:
                mSms= mEtRegisteredSms.getText().toString();
                mPassword= mEtRegisteredPwd.getText().toString();
                verificationPswAndPhone();
                break;
            case R.id.tv_registered_xqsj:
                Intent intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("url", "http://www.baidu.com");
                intent.putExtra("title","产品合同");
                startActivity(intent);
                break;

            default:
                break;
        }
    }
    //    输入数据判断
    private void verificationPswAndPhone() {
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
            mEtRegisteredPwd.setFocusable(true);
            mEtRegisteredPwd.setFocusableInTouchMode(true);
            mEtRegisteredPwd.requestFocus();
            return;
        }
        if (!mSms.equals(mCode)) {
            InfoToast.makeText(this, this.getString(R.string.code_is_incorrect), Toast.LENGTH_SHORT).show();
            mEtRegisteredSms.setFocusable(true);
            mEtRegisteredSms.setFocusableInTouchMode(true);
            mEtRegisteredSms.requestFocus();
            return;
        }else {
            requestRegistered();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mBtnRegisteredRegistered.setClickable(true);
        } else {
            mBtnRegisteredRegistered.setClickable(false);
        }
    }

    //    发送验证码
    private void sendSMSVerificationCode(){
        if(mLoginRegController ==null){
            mLoginRegController =new LoginRegController(this);
        }
        mLoginRegController.handleRegisteredCodesByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                LoginResponse loginResponse=(LoginResponse) result;
                if(loginResponse.mResult.equals("01")){
                    mCode=loginResponse.mVaildateCode;
                    InfoToast.makeText(RegisteredActivity.this,getResources().getString(R.string.sent_verification_code)+mPhone,Toast.LENGTH_SHORT).show();
                }else{
                    InfoToast.makeText(RegisteredActivity.this,loginResponse.mMsg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                InfoToast.makeText(RegisteredActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mPhone);
    }

    //    注册帐号
    private void requestRegistered() {
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(mLoginRegController ==null){
            mLoginRegController =new LoginRegController(this);
        }
        mLoginRegController.handleRegisterByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                RegisteredActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse=(LoginResponse) result;
                if(loginResponse.mResult.equals("01")){//注册成功
                    InfoToast.makeText(RegisteredActivity.this,getResources().getString(R.string.registration_success),Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    InfoToast.makeText(RegisteredActivity.this,loginResponse.mMsg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                RegisteredActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(RegisteredActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mPhone,mPassword,mSms);
    }
}
