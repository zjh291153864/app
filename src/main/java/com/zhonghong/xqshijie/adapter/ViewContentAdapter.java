package com.zhonghong.xqshijie.adapter;

import android.content.Context;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseCommonAdapter;
import com.zhonghong.xqshijie.base.ViewHolder;
import com.zhonghong.xqshijie.data.response.ContractListResponse;


public class ViewContentAdapter extends BaseCommonAdapter<ContractListResponse.ContractsBean.BannersBean> {

    public ViewContentAdapter(Context context) {
        super(context, R.layout.adapter_view_contract);
    }

    @Override
    public void convertView(ViewHolder holder, ContractListResponse.ContractsBean.BannersBean bean) {
        holder.setTextView(R.id.tv_number_one, bean.field);
        holder.setTextView(R.id.tv_contract_content, bean.field_value);
    }
}
