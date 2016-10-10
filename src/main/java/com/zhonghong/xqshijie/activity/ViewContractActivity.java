package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.adapter.ViewContentAdapter;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.ConfirmOrderController;
import com.zhonghong.xqshijie.data.bean.DownloadContractBean;
import com.zhonghong.xqshijie.data.response.ContractListResponse;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zg on 2016/7/7.
 * 查看合同
 */
public class ViewContractActivity extends BaseActivity implements NetInterface,AdapterView.OnItemClickListener{
    //标题栏
    private TitleView mTitle;
    private ListView mLvViewContract;
    private TextView mTvTakeCare;
    List<DownloadContractBean> mViewContentAdapterList = new ArrayList<>();
    private ViewContentAdapter mViewContentAdapter;
    private ConfirmOrderController confirmOrderController;
    private List<ContractListResponse.ContractsBean.BannersBean> mBanner;
    private ArrayList<String> mContractlist;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_view_contract, null);
        initView(contentView);
        return contentView;
    }


    private void initView(View contentView) {
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mTitle.setLeftImageOnClickListener(this);
        mLvViewContract = (ListView) contentView.findViewById(R.id.lv_view_contract);
        mTvTakeCare = (TextView) contentView.findViewById(R.id.tv_take_care);
        mLvViewContract.setOnItemClickListener(this);
        mViewContentAdapter = new ViewContentAdapter(this);
        mLvViewContract.setAdapter(mViewContentAdapter);



    }

    @Override
    public void handleCreate() {
        if(confirmOrderController==null){
            confirmOrderController=new ConfirmOrderController(this);
        }
        confirmOrderController.handleContractListByNet(this, "3");
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_TV_left:
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onNetResult(String interfaceAction, Object result) {
        ContractListResponse contractListResponse= (ContractListResponse) result;
        if(contractListResponse.mResult.equals("01")){
            mBanner=contractListResponse.contracts.banners;
            mViewContentAdapter.setNewList(mBanner);

        }else{
            InfoToast.makeText(ViewContractActivity.this,getResources().getString(R.string.access_contract_information_failed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
        InfoToast.makeText(ViewContractActivity.this,getResources().getString(R.string.access_contract_information_failed), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       String url =  mViewContentAdapter.getItem(position).mUrl;
        Intent intent  = new Intent(this,WebViewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
}
