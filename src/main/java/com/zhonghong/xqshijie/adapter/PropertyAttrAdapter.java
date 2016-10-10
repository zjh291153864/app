package com.zhonghong.xqshijie.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseCommonAdapter;
import com.zhonghong.xqshijie.base.ViewHolder;
import com.zhonghong.xqshijie.data.response.ProjectDetailResponse;
import com.zhonghong.xqshijie.widget.LinearLayoutTextView;

import java.util.List;


public class PropertyAttrAdapter extends BaseCommonAdapter<ProjectDetailResponse.ProjectBuild> {
    private Context mContext;

    public PropertyAttrAdapter(Context context) {
        super(context, R.layout.adapter_property_attr);
        this.mContext = context;
    }

    @Override
    public void convertView(ViewHolder holder, ProjectDetailResponse.ProjectBuild projectBuildInfo) {
        holder.setTextView(R.id.tv_project_details, projectBuildInfo.mInfoName);
        LinearLayout linearLayout = holder.getChildView(R.id.ll_content);
        linearLayout.removeAllViews();
        List<ProjectDetailResponse.ProjectBuild.InfoData> data = projectBuildInfo.mInfoData;
        for (int i = 0; i < data.size(); i++) {
            LinearLayoutTextView linearLayoutTextView = new LinearLayoutTextView(mContext);
            linearLayoutTextView.setTextNameValue(data.get(i).mName);
            linearLayoutTextView.setTextContentValue(data.get(i).mValue);
            linearLayout.addView(linearLayoutTextView);
        }
    }
}
