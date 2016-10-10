package com.zhonghong.xqshijie.activity;

import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.ChagePwdController;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.PasswordUtil;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.CleanableEditText;
import com.zhonghong.xqshijie.widget.TitleView;
import com.zhonghong.xqshijie.widget.dialog.NormalDialog;
import com.zhonghong.xqshijie.widget.dialog.OnBtnClickL;

/**
 * Created by jh on 2016/7/1.
 */
public class ChangePwdActivity extends BaseActivity {

    private TitleView mTitle;
//    原密码
    private CleanableEditText mEtChangePwdOriginalPwd;
//    新密码
    private CleanableEditText mEtChangePwdNewPwd;
//    新密码明文暗文按钮
    private ImageView mIvChangePwdNewPwd;
//    新密码是否隐藏
    private boolean mIsVisible = true;
//    提交
    private Button mBtnChangePwdSubmit;
    private String mOriginalPwd="";
    private String mNewPwd="";
    private String mPwdCode;
    private ChagePwdController mChagePwdController;
    private String mMumberId;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_changepwd,null);
        mTitle = (TitleView)contentView.findViewById(R.id.title);
        mEtChangePwdOriginalPwd = (CleanableEditText)contentView.findViewById(R.id.et_changepwd_original_pwd);
        mEtChangePwdNewPwd = (CleanableEditText)contentView.findViewById(R.id.et_changepwd_new_pwd);
        mIvChangePwdNewPwd = (ImageView)contentView.findViewById(R.id.iv_changepwd_new_pwd);
        mBtnChangePwdSubmit = (Button)contentView.findViewById(R.id.btn_changepwd_submit);

        mTitle.setLeftImageOnClickListener(this);
        mIvChangePwdNewPwd.setOnClickListener(this);
        mBtnChangePwdSubmit.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void handleCreate() {
        mPwdCode= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.PWDCODE);
        mMumberId= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_ID);
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
        		case R.id.common_title_TV_left:
        			finish();
        			break;
        		case R.id.iv_changepwd_new_pwd:
                    String content = mEtChangePwdNewPwd.getText().toString();
//               密码明文暗文
                    if (mIsVisible) {
                        mEtChangePwdNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        mEtChangePwdNewPwd.setSelection(content.length());
                        mIvChangePwdNewPwd.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyes));
                        mIsVisible = false;
                    } else {
                        mEtChangePwdNewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        mEtChangePwdNewPwd.setSelection(content.length());
                        mIvChangePwdNewPwd.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyes_closed));
                        mIsVisible = true;
                    }
        			break;
        		case R.id.btn_changepwd_submit:
                    mOriginalPwd=mEtChangePwdOriginalPwd.getText().toString();
                    mNewPwd=mEtChangePwdNewPwd.getText().toString();
                    verificationPwd();
        			break;
        		default:
        			break;
        		}
    }

    private void verificationPwd() {
        if (StringUtils.isNull(mOriginalPwd)) {
            InfoToast.makeText(this, this.getString(R.string.please_enter_the_original_password_login), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isRigthPW(mOriginalPwd)) {
            InfoToast.makeText(this, this.getString(R.string.the_password_is_6_to_18_digits_and_letters), Toast.LENGTH_SHORT).show();
            mEtChangePwdOriginalPwd.setFocusable(true);
            mEtChangePwdOriginalPwd.setFocusableInTouchMode(true);
            mEtChangePwdOriginalPwd.requestFocus();
            return;
        }
        if (StringUtils.isNull(mNewPwd)) {
            InfoToast.makeText(this, this.getString(R.string.enter_a_new_login_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isRigthPW(mNewPwd)) {
            final NormalDialog normalDialog=new NormalDialog(this);
            normalDialog.isTitleShow(false)
                    .bgColor(Color.parseColor("#FFFFFF"))
                    .cornerRadius(5)
                    .contentGravity(Gravity.CENTER)
                    .contentTextColor(Color.parseColor("#2A2A2A"))
                    .content("新密码是6~18位的数字与字母的组合，请重新输入")
                    .btnNum(1)
                    .btnText(getResources().getString(R.string.determine))
                    .btnTextColor(Color.parseColor("#2A2A2A"))
                    .show();
            normalDialog.setOnBtnClickL(new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    mEtChangePwdNewPwd.setFocusable(true);
                    mEtChangePwdNewPwd.setFocusableInTouchMode(true);
                    mEtChangePwdNewPwd.requestFocus();
                    normalDialog.dismiss();
                }
            });
            return;
        }
        if (mNewPwd.equals(mOriginalPwd)) {
            final NormalDialog normalDialog=new NormalDialog(this);
            normalDialog.isTitleShow(false)
                    .bgColor(Color.parseColor("#FFFFFF"))
                    .cornerRadius(5)
                    .contentGravity(Gravity.CENTER)
                    .contentTextColor(Color.parseColor("#2A2A2A"))
                    .content("新密码与原密码相同，请从重新输入")
                    .btnNum(1)
                    .btnText("确定")
                    .btnTextColor(Color.parseColor("#2A2A2A"))
                    .show();
            normalDialog.setOnBtnClickL(new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    mEtChangePwdNewPwd.setFocusable(true);
                    mEtChangePwdNewPwd.setFocusableInTouchMode(true);
                    mEtChangePwdNewPwd.requestFocus();
                    normalDialog.dismiss();
                }
            });
            return;
        }else{
            mOriginalPwd=PasswordUtil.logEncryptPassword(mOriginalPwd,mPwdCode);
            verificationOriginalPwd();
        }
    }

//    验证旧密码
    private void verificationOriginalPwd() {
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(mChagePwdController ==null){
            mChagePwdController =new ChagePwdController(this);
        }
        mChagePwdController.handleOriginalPwdByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                LoginResponse loginResponse=(LoginResponse)result;
                if(loginResponse.mResult.equals("01")){//  原密码正确
                    mNewPwd=PasswordUtil.encryptPassword(mNewPwd,ChangePwdActivity.this);
                    verificationNewPwd();
                }else if(loginResponse.mResult.equals("02")){//原密码错误
                    ChangePwdActivity.this.hideProgressDialog();//隐藏进度框
                    final NormalDialog normalDialog=new NormalDialog(ChangePwdActivity.this);
                    normalDialog.isTitleShow(false).bgColor(Color.parseColor("#FFFFFF"))
                            .cornerRadius(5)
                            .contentGravity(Gravity.CENTER)
                            .contentTextColor(Color.parseColor("#2A2A2A"))
                            .content("原密码错误，请从重新输入")
                            .btnNum(1)
                            .btnText("确定")
                            .btnTextColor(Color.parseColor("#2A2A2A"))
                            .show();
                    normalDialog.setOnBtnClickL(new OnBtnClickL() {
                        @Override
                        public void onBtnClick() {
                            mEtChangePwdOriginalPwd.setFocusable(true);
                            mEtChangePwdOriginalPwd.setFocusableInTouchMode(true);
                            mEtChangePwdOriginalPwd.requestFocus();
                            normalDialog.dismiss();
                        }
                    });
                }else{
                    ChangePwdActivity.this.hideProgressDialog();
                    InfoToast.makeText(ChangePwdActivity.this,loginResponse.mMsg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                ChangePwdActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(ChangePwdActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mMumberId,mOriginalPwd);
    }
//    修改密码
    private void verificationNewPwd() {
        if(mChagePwdController ==null){
            mChagePwdController =new ChagePwdController(this);
        }
        mChagePwdController.handleNewPwdByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                ChangePwdActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse=(LoginResponse)result;
                if(loginResponse.mResult.equals("01")){//密码修改成功
                    String newPwdCode=SharedPreferencesUtil.getInstance(ChangePwdActivity.this).getStringValue(SharedPreferencesTag.NEWPWDCODE);
                    SharedPreferencesUtil.getInstance(ChangePwdActivity.this).putStringValue(SharedPreferencesTag.PWDCODE,newPwdCode);
                    InfoToast.makeText(ChangePwdActivity.this,getResources().getString(R.string.password_reset_complete),Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    InfoToast.makeText(ChangePwdActivity.this,loginResponse.mMsg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                ChangePwdActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(ChangePwdActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mMumberId,mNewPwd);
    }
}
