package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.ConfirmOrderController;
import com.zhonghong.xqshijie.data.bean.OrderBean;
import com.zhonghong.xqshijie.data.response.ConfirmOrderResponse;
import com.zhonghong.xqshijie.data.response.ContractListResponse;
import com.zhonghong.xqshijie.data.response.ContractListResponse.ContractsBean.BannersBean;
import com.zhonghong.xqshijie.data.response.MyOrderResponse;
import com.zhonghong.xqshijie.data.response.SpecificationSelectionResponse.HousesBean;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.util.IdNumberUtils;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.CleanableEditText;
import com.zhonghong.xqshijie.widget.ListViewForScrollView;
import com.zhonghong.xqshijie.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 2016/7/6.
 */
public class ConfirmOrderActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,AdapterView.OnItemClickListener {
    private TitleView mTitle;
    private ImageView mIvConfirmOrderCover;//项目图片
    private TextView mTvConfirmOrderProjectName;//项目名称
    private TextView mTvConfirmOrderApartment;//产品户型
    private TextView mtvConfirmOrderHousePrice;//产品价格
    private TextView mtvConfirmOrderHouseRemind;//定金提示
    private CleanableEditText metConfirmOrderNameBuyers;//购房人姓名
    private CleanableEditText metConfirmOrderIdNumber;//身份证号
    private TextView metConfirmOrderCellphoneNumber;//手机号
    private Button mBtnConfirmOrderConfirmOrder;//确认定单
    private CheckBox mCbConfirmOrderAgree;//同意合同
    private ListViewForScrollView mLvConfirmOrderContractlist;//合同列表
    private HousesBean mHouse;
    private String mHasCert="1";//测试
    private String mProjectCover;//项目图片
    private String mProjectName;//项目名称
    private ArrayList<String> mContractlist;
    private ArrayAdapter arrayAdapter;
    private String mPhone="";
    private String mIdNumber="";
    private String mFullName="";
    private String mMemberId="";
    private String mOrderType="";//定单类型
    private ConfirmOrderController confirmOrderController;
    private List<BannersBean> mBanner;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_confirm_order, null);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mIvConfirmOrderCover = (ImageView) contentView.findViewById(R.id.iv_confirm_order_cover);
        mTvConfirmOrderProjectName = (TextView) contentView.findViewById(R.id.tv_confirm_order_project_name);
        mTvConfirmOrderApartment = (TextView) contentView.findViewById(R.id.tv_confirm_order＿apartment);
        mtvConfirmOrderHousePrice = (TextView) contentView.findViewById(R.id.tv_confirm_order_house_price);
        mtvConfirmOrderHouseRemind = (TextView) contentView.findViewById(R.id.tv_confirm_order_house_remind);
        metConfirmOrderNameBuyers = (CleanableEditText) contentView.findViewById(R.id.et_confirm_order_name_buyers);
        metConfirmOrderIdNumber = (CleanableEditText) contentView.findViewById(R.id.et_confirm_order_id_number);
        metConfirmOrderCellphoneNumber = (TextView) contentView.findViewById(R.id.tv_confirm_order_cellphone_number);
        mBtnConfirmOrderConfirmOrder = (Button) contentView.findViewById(R.id.btn_confirm_order_confirm_order);
        mCbConfirmOrderAgree = (CheckBox) contentView.findViewById(R.id.cb_confirm_order_agree);
        mLvConfirmOrderContractlist = (ListViewForScrollView) contentView.findViewById(R.id.lv_confirm_order_contractlist);

        mTitle.setLeftImageOnClickListener(this);
        mCbConfirmOrderAgree.setOnCheckedChangeListener(this);
        mBtnConfirmOrderConfirmOrder.setOnClickListener(this);
        mLvConfirmOrderContractlist.setOnItemClickListener(this);
        return contentView;
    }

    @Override
    public void handleCreate() {
        mHouse = (HousesBean) getIntent().getSerializableExtra("house");
        mProjectCover = getIntent().getStringExtra("projectCover");
        mProjectName = getIntent().getStringExtra("projectName");
        mMemberId=SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_ID);
        mPhone=SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_MOBILE);
        mIdNumber=SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_ID_NUMBER);
        mFullName=SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_FULLNAME);
        if(!StringUtils.isNull(mProjectCover)){
            ImageLoaderUtil.getInstance().loadImage(mProjectCover, mIvConfirmOrderCover, R.drawable.ic_ylt_productdefault);
        }
        if(!StringUtils.isNull(mProjectName)){
            mTvConfirmOrderProjectName.setText(mProjectName);
        }
        if(mHouse!=null){
            mTvConfirmOrderApartment.setText("户型：" + mHouse.mHouseName + "-" + mHouse.mHouseArea);
        }
        if (mHasCert.equals("1")) {//有预售证
            mOrderType="1";
            mtvConfirmOrderHouseRemind.setVisibility(View.GONE);
//            SpannableString spanString = new SpannableString(getResources().getString(R.string.money) + mHouse.mHousePrice);
            SpannableString spanString = new SpannableString(getResources().getString(R.string.money) + "15000");
            AbsoluteSizeSpan span = new AbsoluteSizeSpan(33);
            spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mtvConfirmOrderHousePrice.setText(spanString);
        } else if (mHasCert.equals("0")) {//无预售证只可付定金
            mOrderType="3";
            mtvConfirmOrderHouseRemind.setVisibility(View.VISIBLE);
            SpannableString spanString = new SpannableString(getResources().getString(R.string.earnest) + "5,000");
            AbsoluteSizeSpan span = new AbsoluteSizeSpan(33);
            spanString.setSpan(span, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mtvConfirmOrderHousePrice.setText(spanString);
        }
        if(!StringUtils.isNull(mFullName)){
            metConfirmOrderNameBuyers.setText(mFullName);
            metConfirmOrderNameBuyers.setFocusable(false);
        }
        if(!StringUtils.isNull(mIdNumber)){
            try {
                mIdNumber= IdNumberUtils.decrypt(mIdNumber);
                metConfirmOrderIdNumber.setText(mIdNumber);
                metConfirmOrderIdNumber.setFocusable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!StringUtils.isNull(mPhone)){
            metConfirmOrderCellphoneNumber.setText(mPhone);
            metConfirmOrderCellphoneNumber.setFocusable(false);
        }
        getContractList();
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_TV_left:
                finish();
                break;
            case R.id.btn_confirm_order_confirm_order://确认订单
                confirmOrder();
                break;

            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mBtnConfirmOrderConfirmOrder.setClickable(true);
        } else {
            mBtnConfirmOrderConfirmOrder.setClickable(false);
        }
    }

    //确认订单
    private void confirmOrder(){
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(confirmOrderController==null){
            confirmOrderController=new ConfirmOrderController(this);
        }
        confirmOrderController.handleSaveOrderByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                ConfirmOrderActivity.this.hideProgressDialog();//隐藏进度框
                ConfirmOrderResponse confirmOrderResponse= (ConfirmOrderResponse) result;
                if(confirmOrderResponse.mResult.equals("01")){//成功
                    OrderBean order=new OrderBean();
                    order.orderID=confirmOrderResponse.mData.mOrderId;
                    order.orderNum=confirmOrderResponse.mData.mOrderDisplayId;
                    order.orderAllpayPrice=confirmOrderResponse.mData.mAllPayPrice;
                    order.orderTotal=confirmOrderResponse.mData.mOrderTotal;
                    order.orderDeposit=confirmOrderResponse.mData.mDeposit;
                    order.projectName="测试";
                    order.projectDescription="测试";
                    order.orderStatus=mHasCert;
                    Intent intent = new Intent(ConfirmOrderActivity.this, OrderPayActivity.class);
                    intent.putExtra("order",order);
                    startActivity(intent);
                }
                Log.i("aaa",confirmOrderResponse.toString());
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                ConfirmOrderActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(ConfirmOrderActivity.this,getResources().getString(R.string.network_requests_fail), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mMemberId,mFullName,mPhone,"1",1,"10000","100000");
    }

//    获取合同列表
    private void getContractList(){
        this.showProgressDialog(R.string.loading1,false);//显示进度框
        if(confirmOrderController==null){
            confirmOrderController=new ConfirmOrderController(this);
        }
        confirmOrderController.handleContractListByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                ConfirmOrderActivity.this.hideProgressDialog();//隐藏进度框
                ContractListResponse contractListResponse= (ContractListResponse) result;
                if(contractListResponse.mResult.equals("01")){
                    mBanner=contractListResponse.contracts.banners;
                    mContractlist = new ArrayList<String>();
                    for (int i = 0; i <contractListResponse.contracts.banners.size() ; i++) {
                        mContractlist.add(contractListResponse.contracts.banners.get(i).field+"  "+contractListResponse.contracts.banners.get(i).field_value);
                    }
                    arrayAdapter = new ArrayAdapter(ConfirmOrderActivity.this, R.layout.adapter_confirm_order_contractlist_item, R.id.tv_confirm_order_contract, mContractlist);
                    mLvConfirmOrderContractlist.setAdapter(arrayAdapter);
                }else{
                    InfoToast.makeText(ConfirmOrderActivity.this,getResources().getString(R.string.access_contract_information_failed), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                ConfirmOrderActivity.this.hideProgressDialog();//隐藏进度框
                InfoToast.makeText(ConfirmOrderActivity.this,getResources().getString(R.string.access_contract_information_failed), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mOrderType);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,WebViewActivity.class);
        intent.putExtra("url", NetTag.BASEURL+"/"+mBanner.get(position).mUrl);
        intent.putExtra("title","产品合同");
        startActivity(intent);
    }
}
