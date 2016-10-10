package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.LoginRegController;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.TitleView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 手机号页面
 * Created by jh on 2016/6/27.
 */
public class PhoneActivity extends BaseActivity{

    //    标题栏
    private TitleView mTitle;
    //    输入框
    private EditText mEtPhonePhone;
    //    显示textView
    private TextView mTvPhoneShow;
    //    下一步按钮
    private Button mBtnPhoneNext;
    private String mPhone;
    private LoginRegController mLoginRegController;
    private Intent intent;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_phone, null);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mEtPhonePhone = (EditText) contentView.findViewById(R.id.et_phone_phone);
        mTvPhoneShow = (TextView) contentView.findViewById(R.id.tv_phone_show);
        mBtnPhoneNext = (Button) contentView.findViewById(R.id.btn_phone_next);

        mTitle.setLeftImageOnClickListener(this);
        mEtPhonePhone.addTextChangedListener(mTextWatchar);
        mBtnPhoneNext.setOnClickListener(this);

        return contentView;
    }

    @Override
    public void handleCreate() {

    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()){
            case R.id.common_title_TV_left:
                finish();
                break;
            case R.id.btn_phone_next:
                mPhone= mEtPhonePhone.getText().toString();
                verificationPhone();
                break;
        }
    }

    //    输入框监听
    TextWatcher mTextWatchar=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mTvPhoneShow.setText(s);
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    //    输入数据判断
    public void verificationPhone(){
        if(StringUtils.isNull(mPhone)){
            InfoToast.makeText(this, this.getString(R.string.the_phone_number_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if(!PublicUtils.isTelNum(mPhone)){
            InfoToast.makeText(this, this.getString(R.string.the_phone_number_is_not_legitimate), Toast.LENGTH_SHORT).show();
            return;
        }else{
            //先请求数据判断是否注册后执行跳转
            verificationRegistered();
        }
    }

    public void verificationRegistered(){
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(mLoginRegController ==null){
            mLoginRegController =new LoginRegController(this);
        }
        mLoginRegController.handlePhoneByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                PhoneActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse=(LoginResponse)result;
                if(loginResponse.mResult.equals("01")){//已注册
                    intent=new Intent(PhoneActivity.this,LoginActivity.class);
                    intent.putExtra("mPhone",mPhone);
                    SharedPreferencesUtil.getInstance(PhoneActivity.this).putStringValue(SharedPreferencesTag.PWDCODE,loginResponse.mPwd);
                    startActivityForResult(intent,0);
                }else if(loginResponse.mResult.equals("02")){//未注册
                    intent=new Intent(PhoneActivity.this,RegisteredActivity.class);
                    intent.putExtra("mPhone",mPhone);
                    startActivity(intent);
                }else{//其他状态
                    InfoToast.makeText(PhoneActivity.this,loginResponse.mMsg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                PhoneActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(PhoneActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        }, mPhone);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        		case 1:
        			finish();
        			break;
        		default:
        			break;
        		}
    }
}
