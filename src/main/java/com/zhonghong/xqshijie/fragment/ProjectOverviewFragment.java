package com.zhonghong.xqshijie.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.ProjectDetailActivity;
import com.zhonghong.xqshijie.adapter.ProjectOverviewAdapter;
import com.zhonghong.xqshijie.base.BaseFragment;
import com.zhonghong.xqshijie.data.bean.ProjectOverviewBean;
import com.zhonghong.xqshijie.data.response.ProjectDetailResponse;

import java.util.ArrayList;
import java.util.List;

public class ProjectOverviewFragment extends BaseFragment {
    private ListView mListView;
    private List<ProjectOverviewBean> mProjectOverviewList =new ArrayList<>();
    private ProjectDetailResponse mProjectDetailResponse;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mProjectDetailResponse = (ProjectDetailResponse) getArguments().getSerializable(ProjectDetailActivity.PROJECT_INFO);
        View contentView = inflater.inflate(R.layout.fragment_project_overview, null);
        initView(contentView);
        //初始化view的各控件完成
        isPrepared = true;
        lazyLoad();
        return contentView;
    }

    private void initView(View contentView) {
        mListView = (ListView) contentView.findViewById(R.id.lv_house_details);
    }
    @Override
    protected void handleCreate() {
        ProjectOverviewAdapter mProjectOverviewAdapter = new ProjectOverviewAdapter(getContext());
        mListView.setAdapter(mProjectOverviewAdapter);
        if(mProjectDetailResponse!=null){
            if(mProjectDetailResponse.mProjectBoard==null||mProjectDetailResponse.mProjectBoard.size()==0){
                return;
            }
            ProjectDetailResponse.ProjectBoard projectBoard = mProjectDetailResponse.mProjectBoard.get(0);
            ProjectOverviewBean projectOverviewBean2 = new ProjectOverviewBean();
            projectOverviewBean2.mTitle = projectBoard.mBoard2Title;
            projectOverviewBean2.mDesc = projectBoard.mBoard2Desc;
            mProjectOverviewList.add(projectOverviewBean2);
            ProjectOverviewBean projectOverviewBean5 = new ProjectOverviewBean();
            projectOverviewBean5.mTitle = projectBoard.mBoard5Title;
            projectOverviewBean5.mDesc = projectBoard.mBoard5Desc;
            mProjectOverviewList.add(projectOverviewBean5);
            ProjectOverviewBean projectOverviewBean6 = new ProjectOverviewBean();
            projectOverviewBean6.mTitle = projectBoard.mBoard6Title;
            projectOverviewBean6.mDesc = projectBoard.mBoard6Desc;
            mProjectOverviewList.add(projectOverviewBean6);
            mProjectOverviewAdapter.setNewList(mProjectOverviewList);
        }
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }

    @Override
    protected void processMessage(Message msg) {

    }

    @Override
    protected void customOnClick(View v) {

    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {

    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }
}
