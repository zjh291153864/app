package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.adapter.AddressListAdapter;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.AddressController;
import com.zhonghong.xqshijie.data.response.AddressResponse;
import com.zhonghong.xqshijie.data.response.AddressResponse.AddressListBean;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zg on 2016/6/30.
 * 地址列表界面
 */
public class AddressListActivity extends BaseActivity{

    private TitleView mTitle;
//    新增地址
    private Button mBtnAddresslistNew;
//    地址列表
    private ListView mPtrlvAddressListList;
    private AddressListAdapter mAddressListAdapter;
    private AddressController mAddressController;

    private List<AddressListBean> mAddressList=new ArrayList<AddressListBean>(){};
    private String mMemberId;

    @Override
    public View initContentView() {
        View contentView= LayoutInflater.from(this).inflate(R.layout.activity_addresslist,null);
        mTitle = (TitleView)contentView.findViewById(R.id.title);
        mBtnAddresslistNew = (Button)contentView.findViewById(R.id.btn_addresslist_new);
        mPtrlvAddressListList = (ListView)contentView.findViewById(R.id.ptrlv_addresslist_list);

        mPtrlvAddressListList.setOnItemClickListener(onItemClickListener);
        mBtnAddresslistNew.setOnClickListener(this);
        mTitle.setLeftImageOnClickListener(this);
        return contentView;
    }

    @Override
    public void handleCreate() {
        mMemberId= SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_ID);
        Log.i("aaa","aaa"+mMemberId);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getAddressList();
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
        		case R.id.common_title_TV_left:
        			finish();
        			break;
        		case R.id.btn_addresslist_new:
                    Intent intent=new Intent(this, AddaNewAddressActivity.class);
                    startActivity(intent);
        			break;

        		default:
        			break;
        		}
    }

//    Item监听
    AdapterView.OnItemClickListener onItemClickListener= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AddressListBean isDefaultAddress = mAddressListAdapter.getAddressIs();
            if(isDefaultAddress!=null){
                updateIsDefault(isDefaultAddress,"0");
            }
            updateIsDefault(mAddressList.get(position),"1");
        }
    };

//    获取地址列表
    private void getAddressList(){
        this.showProgressDialog(R.string.loading,false);//显示进度框
        if(mAddressController==null){
            mAddressController =new AddressController(this);
        }
        mAddressController.handleGetAddressListByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                AddressListActivity.this.hideProgressDialog();//隐藏进度框
                AddressResponse addressResponse=(AddressResponse) result;
                mAddressList.clear();
                mAddressList.addAll(addressResponse.mAddressList);
                if(addressResponse.mResult.equals("01")){//获取收货地址
                    if(mAddressListAdapter==null){
                        if(mAddressList!=null&&mAddressList.size()>0){
                            mAddressListAdapter=new AddressListAdapter(AddressListActivity.this,mAddressList);
                            mPtrlvAddressListList.setAdapter(mAddressListAdapter);
                        }
                    }else{
                        if(mAddressList!=null&&mAddressList.size()>0){
                            mAddressListAdapter.notifyDataSetChanged();
                        }
                    }
                }else if(addressResponse.mResult.equals("02")){//收货地址列表为空
                    InfoToast.makeText(AddressListActivity.this,getResources().getString(R.string.no_delivery_address), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                AddressListActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(AddressListActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mMemberId);
    }

    //    修改默认地址
    private void updateIsDefault(AddressListBean address, final String isDefault) {
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(mAddressController==null){
            mAddressController =new AddressController(this);
        }
        mAddressController.handleUpdateAddressByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                AddressListActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse= (LoginResponse) result;
                if(loginResponse.mResult.equals("01")){//修改成功
                    if(isDefault.equals("1")){
                        InfoToast.makeText(AddressListActivity.this,getResources().getString(R.string.successfully_modified),Toast.LENGTH_SHORT).show();
                        getAddressList();
                    }
                }else{
                    InfoToast.makeText(AddressListActivity.this,loginResponse.mResult,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                AddressListActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(AddressListActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },address.mAddressId,address.mProvinceId,address.mCityId,address.mCountyId,address.mConsigneeName,address.mConsigneeMobile,address.mAddress,isDefault);
    }

//    删除地址
    public void deleteAddress(String addressId){
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(mAddressController==null){
            mAddressController =new AddressController(this);
        }
        mAddressController.handleDelAddressByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                AddressListActivity.this.hideProgressDialog();//隐藏进度框
                LoginResponse loginResponse= (LoginResponse) result;
                if(loginResponse.mResult.equals("01")) {//删除成功
                    InfoToast.makeText(AddressListActivity.this,getResources().getString(R.string.successfully_deleted),Toast.LENGTH_SHORT).show();
                    getAddressList();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                AddressListActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(AddressListActivity.this,getResources().getString(R.string.network_requests_fail),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },addressId);
    }
}