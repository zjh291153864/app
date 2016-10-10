package com.zhonghong.xqshijie.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.data.response.ProjectDetailResponse;
import com.zhonghong.xqshijie.fragment.ProjectOverviewFragment;
import com.zhonghong.xqshijie.fragment.PropertyAttrFragment;
import com.zhonghong.xqshijie.widget.TitleView;
import com.zhonghong.xqshijie.widget.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProjectDescriptionActivity extends BaseActivity {
    public static final String DESC_DATA = "desc_data";
    //标题栏
    private TitleView mTitle;
    private ViewPager mViewpager;
    private ViewPagerIndicator mViewPagerIndicator;
    private List<String> mTitles = Arrays.asList("楼盘属性", "项目描述");
    private List<Fragment> mContents = new ArrayList<Fragment>();// 装载ViewPager数据的List

    private FragmentPagerAdapter mAdapter;// ViewPager适配器
    public String mProjectId;
    ProjectDetailResponse mProjectDetailResponse;

    @Override
    public View initContentView() {
        Bundle projectInfoBundle = getIntent().getExtras();
        mProjectDetailResponse = (ProjectDetailResponse) projectInfoBundle.get(ProjectDetailActivity.PROJECT_INFO);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_project_disctiption, null);
        initViews(contentView);
        //动态设置tab
        mViewPagerIndicator.setVisibleTabCount(2);
        mViewPagerIndicator.setTabItemTitles(mTitles);

        mViewpager.setAdapter(mAdapter);
        mViewPagerIndicator.setViewPager(mViewpager, 0);
        return contentView;
    }

    @Override
    public void handleCreate() {

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

    /**
     * 初始化布局
     *
     * @param contentView
     */
    private void initViews(View contentView) {
        mViewpager = (ViewPager) contentView.findViewById(R.id.viewpager);
        mViewPagerIndicator = (ViewPagerIndicator) contentView.findViewById(R.id.indicator);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mTitle.setLeftImageOnClickListener(this);
        // 根据title初始化fragment

        PropertyAttrFragment propertyAttrFragment = new PropertyAttrFragment();
        ProjectOverviewFragment projectOverviewFragment = new ProjectOverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ProjectDetailActivity.PROJECT_INFO, mProjectDetailResponse);
        propertyAttrFragment.setArguments(bundle);
        projectOverviewFragment.setArguments(bundle);
        mContents.add(propertyAttrFragment);
        mContents.add(projectOverviewFragment);


        // getFragmentManager();
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mContents.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }
        };
    }
}
