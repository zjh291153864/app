package com.zhonghong.xqshijie.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.FeedBackController;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.TitleView;

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity {
    //标题栏
    private TitleView mTitle;
    private EditText mEtFeedbackContent;
    private EditText mEtPhoneNumber;
    private Button mBtnSubmit;
    private static final int MAXLINES = 12;//最大输入行数。
    private String mContentStr;//反馈文字
    private String mPhoneNumber;//手机号
    private FeedBackController feedBackController;
    private String mMemberId;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_feedback, null);
        mEtFeedbackContent = (EditText) contentView.findViewById(R.id.et_feedback_content);
        mEtPhoneNumber = (EditText) contentView.findViewById(R.id.et_phone_number);
        mBtnSubmit = (Button) contentView.findViewById(R.id.btn_submit);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mTitle.setLeftImageOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        // 显示输入行数
        mEtFeedbackContent.addTextChangedListener(textWatcher);
        return contentView;
    }

    @Override
    public void handleCreate() {
        String memberId = SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_ID);
        if(StringUtils.isNull(memberId)){
            mMemberId="0";
        }else{
            mMemberId=memberId;
        }
    }

    //输入限制
    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,int count) {
            // TODO Auto-generated method stub
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            // TODO Auto-generated method stub
        }
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            int lines = mEtFeedbackContent.getLineCount();
            // 限制最大输入行数
            if (lines > MAXLINES) {
                String str = s.toString();
                int cursorStart = mEtFeedbackContent.getSelectionStart();
                int cursorEnd = mEtFeedbackContent.getSelectionEnd();
                if (cursorStart == cursorEnd && cursorStart < str.length()&& cursorStart >= 1) {
                    str = str.substring(0, cursorStart - 1)+ str.substring(cursorStart);
                } else {
                    str = str.substring(0, s.length() - 1);
                }
                // setText会触发afterTextChanged的递归
                mEtFeedbackContent.setText(str);
                // setSelection用的索引不能使用str.length()否则会越界
                mEtFeedbackContent.setSelection(mEtFeedbackContent.getText().length());
            }
        }
    };

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                mContentStr = mEtFeedbackContent.getText().toString();
                mPhoneNumber = mEtPhoneNumber.getText().toString();
                verificationContent();
                break;
            case R.id.common_title_TV_left:
                finish();
                break;
        }
    }

    //    输入数据判断
    public void verificationContent(){
        if(StringUtils.isNull(mContentStr)){
            InfoToast.makeText(this, this.getString(R.string.feedback_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtils.isNull(mPhoneNumber)){
            InfoToast.makeText(this, this.getString(R.string.the_phone_number_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if(!PublicUtils.isTelNum(mPhoneNumber)){
            InfoToast.makeText(this, this.getString(R.string.the_phone_number_is_not_legitimate), Toast.LENGTH_SHORT).show();
            return;
        }else{
            //发送反馈
          sendFeedBack();
        }
    }

    private void sendFeedBack() {
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(feedBackController==null){
            feedBackController =new FeedBackController(this);
        }
        feedBackController.handleFeedBackByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                FeedBackActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse=(LoginResponse) result;
                if(loginResponse.mResult.equals("01")){//反馈发送成功
                    InfoToast.makeText(FeedBackActivity.this,getResources().getString(R.string.feedback_success),Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    InfoToast.makeText(FeedBackActivity.this,loginResponse.mMsg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                FeedBackActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(FeedBackActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        }, mContentStr, mPhoneNumber, mMemberId);
    }
}
