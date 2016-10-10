package com.zhonghong.xqshijie.adapter;

import android.content.Context;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseCommonAdapter;
import com.zhonghong.xqshijie.base.ViewHolder;
import com.zhonghong.xqshijie.data.bean.ProjectOverviewBean;


public class ProjectOverviewAdapter extends BaseCommonAdapter<ProjectOverviewBean> {
    public ProjectOverviewAdapter(Context context) {
        super(context, R.layout.adapter_project_overview);
    }

    @Override
    public void convertView(ViewHolder holder,ProjectOverviewBean projectOverviewBean) {
        //TODO 改Bean类型
        holder.setTextView(R.id.tv_project_details, projectOverviewBean.mTitle);
        holder.setTextView(R.id.tv_content, projectOverviewBean.mDesc);

    }
}
