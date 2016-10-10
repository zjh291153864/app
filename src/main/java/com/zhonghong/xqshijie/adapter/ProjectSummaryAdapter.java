package com.zhonghong.xqshijie.adapter;

import android.content.Context;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseCommonAdapter;
import com.zhonghong.xqshijie.base.ViewHolder;
import com.zhonghong.xqshijie.data.response.ProjectDetailResponse;


public class ProjectSummaryAdapter extends BaseCommonAdapter<ProjectDetailResponse.ProjectBuild.InfoData> {
    private Context mContext;

    public ProjectSummaryAdapter(Context context) {
        super(context, R.layout.adapter_project_summary);
        this.mContext = context;
    }

    @Override
    public void convertView(ViewHolder holder, ProjectDetailResponse.ProjectBuild.InfoData infoData) {
      holder.setTextView(R.id.tv_prodesc_name,infoData.mName);
      holder.setTextView(R.id.tv_prodesc_content,infoData.mValue);
    }
}
