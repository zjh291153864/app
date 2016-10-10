package com.zhonghong.xqshijie.activity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.AddressController;
import com.zhonghong.xqshijie.data.response.AddressJsonResponse;
import com.zhonghong.xqshijie.data.response.AddressJsonResponse.RegionsBean.ProvincesBean;
import com.zhonghong.xqshijie.data.response.AddressJsonResponse.RegionsBean.ProvincesBean.CitysBean;
import com.zhonghong.xqshijie.data.response.AddressJsonResponse.RegionsBean.ProvincesBean.CitysBean.DistrictsBean;
import com.zhonghong.xqshijie.data.response.AddressResponse.AddressListBean;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.AssetsUtils;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.MailingAddressPopupWindow;
import com.zhonghong.xqshijie.widget.TitleView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jh on 2016/7/3.
 */
public class AddaNewAddressActivity extends BaseActivity {
    private TitleView mTitle;
    private AddressListBean mAddressBean;
//    联系人
    private EditText mAddaNewAddressAddressee;
//    联系方式
    private EditText mEtAddaNewAddressContactInformation;
//    收货地址栏
    private LinearLayout mLLAddaNewAddressMailingAddress;
//    收货地址
    private TextView mTvAddaNewAddressMailingAddress;
//    详细地址
    private EditText mEtAddaNewAddressAddress;
//    保存
    private Button mBtnAddaNewAddressSave;
//    选择地址PopupWindow
    private MailingAddressPopupWindow mPopupWindow;

    private String mProvinceName="";
    private String mProvinceId="0";
    private String mCityName="";
    private String mCityId="0";
    private String mDistrictName ="";
    private String mDistrictId ="0";
    private String mAddressee;//收件人
    private String mContactInformation;//联系方式
    private String mMailingAddress;//邮寄地址
    private String mAddress;//详细地址
    private String mMemberId;//用户编码
    private String mIsDefault;
    private AddressController mAddressController;


    @Override
    public View initContentView() {
        View contentView= LayoutInflater.from(this).inflate(R.layout.activity_addanewaddress,null);
        mAddaNewAddressAddressee = (EditText)contentView.findViewById(R.id.et_addanewaddress_addressee);
        mEtAddaNewAddressContactInformation = (EditText)contentView.findViewById(R.id.et_addanewaddress_contact_information);
        mLLAddaNewAddressMailingAddress = (LinearLayout)contentView.findViewById(R.id.ll_addanewaddress_mailing_address);
        mTvAddaNewAddressMailingAddress = (TextView)contentView.findViewById(R.id.tv_addanewaddress_mailing_address);
        mEtAddaNewAddressAddress = (EditText)contentView.findViewById(R.id.et_addanewaddress_address);
        mBtnAddaNewAddressSave = (Button)contentView.findViewById(R.id.btn_addanewaddress_save);
        mTitle = (TitleView)contentView.findViewById(R.id.title);

        mLLAddaNewAddressMailingAddress.setOnClickListener(this);
        mBtnAddaNewAddressSave.setOnClickListener(this);
        mTitle.setLeftImageOnClickListener(this);
        return contentView;
    }

    @Override
    public void handleCreate() {
        mMemberId= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_ID);
        mAddressBean =(AddressListBean)getIntent().getSerializableExtra("address");
        if(mAddressBean !=null){
            mTitle.setTitle("修改地址");
            mAddaNewAddressAddressee.setText(mAddressBean.mConsigneeName);
            mEtAddaNewAddressContactInformation.setText(mAddressBean.mConsigneeMobile);
            mProvinceName = mAddressBean.mProvinceName;
            mProvinceId = mAddressBean.mProvinceId;
            mCityName=mAddressBean.mCityName;
            mCityId=mAddressBean.mCityId;
            mDistrictName =mAddressBean.mCountyName;
            mDistrictId =mAddressBean.mCountyId;
            if(mAddressBean.mIsDefault){
                mIsDefault="1";
            }else{
                mIsDefault="0";
            }
            mTvAddaNewAddressMailingAddress.setText(mAddressBean.mProvinceName+" "+mAddressBean.mCityName+" "+mAddressBean.mCountyName);
            mEtAddaNewAddressAddress.setText(mAddressBean.mAddress);
        }
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
        		case R.id.common_title_TV_left:
        			finish();
        			break;
        		case R.id.ll_addanewaddress_mailing_address:
//                  实例化SelectPicPopupWindow
                    mPopupWindow = new MailingAddressPopupWindow(this, new MailingAddressPopupWindow.popupCallback(){
                        @Override
                        public void dosomething(ProvincesBean province, CitysBean city, DistrictsBean district) {
                            if(province!=null){
                                mProvinceName=province.mName;
                                mProvinceId=province.mId;
                                if(city!=null){
                                    mCityName=city.mName;
                                    mCityId=city.mId;
                                    if(district!=null){
                                        mDistrictName =district.mName;
                                        mDistrictId =district.mId;
                                    }else{
                                        mDistrictName ="";
                                        mDistrictId ="0";
                                    }
                                }else{
                                    mCityName="";
                                    mCityId="0";
                                    mDistrictName ="";
                                    mDistrictId ="0";
                                }
                            }else{
                                    mProvinceName = mAddressBean.mProvinceName;
                                    mProvinceId = mAddressBean.mProvinceId;
                                    mCityName=mAddressBean.mCityName;
                                    mCityId=mAddressBean.mCityId;
                                    mDistrictName =mAddressBean.mCountyName;
                                    mDistrictId =mAddressBean.mCountyId;
                            }
                            mTvAddaNewAddressMailingAddress.setText(mProvinceName+" "+mCityName+" "+ mDistrictName);
                        }
                    });
//                    设置PopupWindow在layout中显示的位置
                    mPopupWindow.alertPopupwindow(this.findViewById(R.id.ll_addanewaddress_layout));
        			break;
            case R.id.btn_addanewaddress_save:
                mAddressee=mAddaNewAddressAddressee.getText().toString();
                mContactInformation=mEtAddaNewAddressContactInformation.getText().toString();
                mMailingAddress=mTvAddaNewAddressMailingAddress.getText().toString();
                mAddress =mEtAddaNewAddressAddress.getText().toString();
                analyzingData();
                break;

        		default:
        			break;
        		}
    }

    //    资料判断
    private void analyzingData(){
        if(StringUtils.isNull(mAddressee)){
            InfoToast.makeText(this, this.getString(R.string.please_enter_the_recipient), Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtils.isNull(mContactInformation)){
            InfoToast.makeText(this, this.getString(R.string.the_phone_number_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if(!PublicUtils.isTelNum(mContactInformation)){
            InfoToast.makeText(this, this.getString(R.string.the_phone_number_is_not_legitimate), Toast.LENGTH_SHORT).show();
            return;
        }if(StringUtils.isNull(mMailingAddress)){
            InfoToast.makeText(this, this.getString(R.string.please_select_mailing_address), Toast.LENGTH_SHORT).show();
            return;
        }if(StringUtils.isNull(mAddress)){
            InfoToast.makeText(this, this.getString(R.string.please_enter_the_full_address), Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(mAddressBean==null){
                addAddressMethod();
            }else{
                updateAddressMethod();
            }
        }
    }

//    添加地址
    private void addAddressMethod() {
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(mAddressController==null){
            mAddressController =new AddressController(this);
        }
        mAddressController.handleAddressByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                AddaNewAddressActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse= (LoginResponse) result;
                if(loginResponse.mResult.equals("01")){//添加成功
                    InfoToast.makeText(AddaNewAddressActivity.this,getResources().getString(R.string.added_successfully),Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    InfoToast.makeText(AddaNewAddressActivity.this,loginResponse.mResult,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                AddaNewAddressActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(AddaNewAddressActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {

            }
        },mMemberId,mProvinceId,mCityId, mDistrictId,mAddressee,mContactInformation,mAddress,"0");
    }

//    修改地址
    private void updateAddressMethod() {
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(mAddressController==null){
            mAddressController =new AddressController(this);
        }
        mAddressController.handleUpdateAddressByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                AddaNewAddressActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse= (LoginResponse) result;
                if(loginResponse.mResult.equals("01")){
                    InfoToast.makeText(AddaNewAddressActivity.this,getResources().getString(R.string.successfully_modified),Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                AddaNewAddressActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(AddaNewAddressActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {

            }
        },mAddressBean.mAddressId,mProvinceId,mCityId, mDistrictId,mAddressee,mContactInformation,mAddress,mIsDefault);
    }
}
