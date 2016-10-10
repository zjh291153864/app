package com.zhonghong.xqshijie.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.ProjectDetailActivity;
import com.zhonghong.xqshijie.adapter.PropertyAttrAdapter;
import com.zhonghong.xqshijie.base.BaseFragment;
import com.zhonghong.xqshijie.data.response.ProjectDetailResponse;

import java.util.ArrayList;
import java.util.List;

public class PropertyAttrFragment extends BaseFragment {

	private ListView mListView;
	private List<ProjectDetailResponse.ProjectBuild> list =new ArrayList<>();
	private PropertyAttrAdapter mPropertyAttrAdapter;
	private ProjectDetailResponse mProjectDetailResponse;
	// 标志位，标志已经初始化完成。
	private boolean isPrepared;
	@Override
	public View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mProjectDetailResponse = (ProjectDetailResponse) getArguments().getSerializable(ProjectDetailActivity.PROJECT_INFO);
		//显示title在fragment上
		View contentView = inflater.inflate(R.layout.fragment_property_attr,null);
		initView(contentView);
		//初始化view的各控件完成
		isPrepared = true;
		lazyLoad();
		return contentView;
	}



	private void initView(View contentView) {
		mListView = (ListView) contentView.findViewById(R.id.lv_project_desc);
	}

	@Override
	protected void handleCreate() {
		mPropertyAttrAdapter = new PropertyAttrAdapter(getContext());
		mListView.setAdapter(mPropertyAttrAdapter);
		if(mProjectDetailResponse!=null){
			mPropertyAttrAdapter.setNewList(mProjectDetailResponse.mProjectBuild);
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
