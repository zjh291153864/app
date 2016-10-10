package com.zhonghong.xqshijie.adapter;

import android.content.Context;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseCommonAdapter;
import com.zhonghong.xqshijie.base.ViewHolder;
import com.zhonghong.xqshijie.data.response.ProjectDetailResponse;


public class YLTProjectListAdapter extends BaseCommonAdapter<ProjectDetailResponse.ProjectYLTS> {
    private Context mContext;

    public YLTProjectListAdapter(Context context) {
        super(context, R.layout.adapter_ylt_project_list);
        this.mContext = context;
    }

    @Override
    public void convertView(ViewHolder holder, ProjectDetailResponse.ProjectYLTS data) {
      holder.setTextView(R.id.tv_house_type,data.mHouseName);
      holder.setTextView(R.id.tv_house_area,data.mHouseArea.concat(mContext.getResources().getString(R.string.square)));
      holder.setTextView(R.id.tv_house_price,mContext.getResources().getString(R.string.money).concat(data.mHousePrice));
    }
}
