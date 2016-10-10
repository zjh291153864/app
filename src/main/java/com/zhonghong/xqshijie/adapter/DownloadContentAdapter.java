package com.zhonghong.xqshijie.adapter;

import android.content.Context;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseCommonAdapter;
import com.zhonghong.xqshijie.base.ViewHolder;
import com.zhonghong.xqshijie.data.bean.DownloadContractBean;


public class DownloadContentAdapter extends BaseCommonAdapter<DownloadContractBean> {

    public DownloadContentAdapter(Context context) {
        super(context, R.layout.adapter_download_contract);
    }

    @Override
    public void convertView(ViewHolder holder, DownloadContractBean bean) {
        holder.setTextView(R.id.tv_number_one, bean.mNumber + "");
        holder.setTextView(R.id.tv_contract_content, bean.mContractContent);

    }
}
