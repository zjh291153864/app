package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.LoginRegController;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.PasswordUtil;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.TitleView;
import com.zhonghong.xqshijie.widget.dialog.NormalDialog;
import com.zhonghong.xqshijie.widget.dialog.OnBtnClickL;

/**
 * 登陆页面
 * Created by jh on 2016/6/27.
 */
public class LoginActivity extends BaseActivity{

    //    标题栏
    private TitleView mTitle;
    //    密码输入框
    private EditText mEtLoginPwd;
    //    密码明文暗文
    private ImageView mIvLoginPwd;
    //    下一步
    private Button mBtnLoginNext;
    //    找回密码
    private TextView mTvLoginForgetPwd;
    //    密码是否隐藏
    private boolean mIsVisible = true;
    private String mPassword;
    private String mPhone;
    private String mPwdCode;
    private LoginRegController mLoginRegController;
    private int mPwdErrorCount =0;
    private String content;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_login, null);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mEtLoginPwd = (EditText) contentView.findViewById(R.id.et_login_pwd);
        mIvLoginPwd = (ImageView) contentView.findViewById(R.id.iv_login_pwd);
        mBtnLoginNext = (Button) contentView.findViewById(R.id.btn_login_next);
        mTvLoginForgetPwd = (TextView) contentView.findViewById(R.id.tv_login_forgetpwd);
//        监听
        mTitle.setLeftImageOnClickListener(this);
        mIvLoginPwd.setOnClickListener(this);
        mBtnLoginNext.setOnClickListener(this);
        mTvLoginForgetPwd.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void handleCreate() {
//        获取传递过来的手机号
        mPhone=getIntent().getStringExtra("mPhone");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPwdCode = SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.PWDCODE);
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_TV_left:
                finish();
                break;
            case R.id.iv_login_pwd:
                content = mEtLoginPwd.getText().toString();
//               密码明文暗文
                if (mIsVisible) {
                    mEtLoginPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEtLoginPwd.setSelection(content.length());
                    mIvLoginPwd.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyes));
                    mIsVisible = false;
                } else {
                    mEtLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtLoginPwd.setSelection(content.length());
                    mIvLoginPwd.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyes_closed));
                    mIsVisible = true;
                }
                break;
            case R.id.btn_login_next:
                mPassword= mEtLoginPwd.getText().toString();
                verificationPwd();
                break;
            case R.id.tv_login_forgetpwd:
                Intent intent=new Intent(LoginActivity.this,ForgetPwdActivity.class);
                intent.putExtra("mPhone",mPhone);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //    输入数据判断
    private void verificationPwd() {
        if (StringUtils.isNull(mPassword)) {
            InfoToast.makeText(this, this.getString(R.string.please_enter_your_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isRigthPW(mPassword)) {
            InfoToast.makeText(this, this.getString(R.string.the_password_is_6_to_18_digits_and_letters), Toast.LENGTH_SHORT).show();
            return;
        } else {
            mPassword= PasswordUtil.logEncryptPassword(mPassword, mPwdCode);
            requestLogin();
        }
    }

    //    请求登陆
    private void requestLogin() {
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(mLoginRegController ==null){
            mLoginRegController =new LoginRegController(this);
        }
        mLoginRegController.handleLoginByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                LoginActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse=(LoginResponse) result;
                if(loginResponse.mResult.equals("01")){
                    PublicUtils.saveUserInformation(LoginActivity.this,loginResponse);//保存用户信息
                    InfoToast.makeText(LoginActivity.this,getResources().getString(R.string.landed_successfully),Toast.LENGTH_SHORT).show();
                    LoginActivity.this.setResult(1);
                    LoginActivity.this.finish();
                }else if(loginResponse.mResult.equals("02")){
                    ++mPwdErrorCount;
                    final NormalDialog normalDialog=new NormalDialog(LoginActivity.this);
                    if(mPwdErrorCount<3){
                        normalDialog.isTitleShow(false).bgColor(Color.parseColor("#FFFFFF"))
                                .cornerRadius(5)
                                .contentGravity(Gravity.CENTER)
                                .contentTextColor(Color.parseColor("#2A2A2A"))
                                .content("密码错误，请从重新输入")
                                .btnNum(1)
                                .btnText("确定")
                                .btnTextColor(Color.parseColor("#2A2A2A"))
                                .show();
                        normalDialog.setOnBtnClickL(new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                content = mEtLoginPwd.getText().toString();
                                mEtLoginPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                mEtLoginPwd.setSelection(content.length());
                                mIvLoginPwd.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyes));
                                mIsVisible = false;
                                normalDialog.dismiss();
                            }
                        });
                    }else if(mPwdErrorCount==3){
                        mPwdErrorCount=0;
                        normalDialog.isTitleShow(false)
                                .bgColor(Color.parseColor("#FFFFFF"))
                                .cornerRadius(5)
                                .contentGravity(Gravity.CENTER)
                                .contentTextColor(Color.parseColor("#2A2A2A"))
                                .content("密码错误，找回密码？")
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
                                normalDialog.dismiss();
                                Intent intent=new Intent(LoginActivity.this,ForgetPwdActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }else{
                    InfoToast.makeText(LoginActivity.this,loginResponse.mMsg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                LoginActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(LoginActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mPhone,mPassword);
    }
}
