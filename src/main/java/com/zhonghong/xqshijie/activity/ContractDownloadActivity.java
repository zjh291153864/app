package com.zhonghong.xqshijie.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.adapter.DownloadContentAdapter;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.data.bean.DownloadContractBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zg on 2016/7/4.
 * 合同下载界面
 */
public class ContractDownloadActivity extends BaseActivity {

    private ListView mLvDownloadContent;
    private TextView mTvTakeCare;
    List<DownloadContractBean> mDownloadContractList = new ArrayList<>();
    private DownloadContentAdapter mDownloadContentAdapter;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_download_cantract, null);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        mLvDownloadContent = (ListView) contentView.findViewById(R.id.lv_download_contract_content);
        mTvTakeCare = (TextView) contentView.findViewById(R.id.tv_take_care);
        String[] downloadContractContent = getResources().getStringArray(R.array.download_contract);
        for (int i = 0; i < downloadContractContent.length; i++) {
            DownloadContractBean downloadContractBean = new DownloadContractBean();
            downloadContractBean.mNumber = i;
            downloadContractBean.mContractContent = downloadContractContent[i];
            mDownloadContractList.add(downloadContractBean);
        }
        mDownloadContentAdapter = new DownloadContentAdapter(this);
        mLvDownloadContent.setAdapter(mDownloadContentAdapter);
        mDownloadContentAdapter.setNewList(mDownloadContractList);

    }

    @Override
    public void handleCreate() {

    }

    @Override
    protected void customOnClick(View v) {

    }
}
