package com.zhonghong.xqshijie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jingchen.pulltorefresh.DefaultRefreshLister;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.RefreshUtils;
import com.jingchen.pulltorefresh.parent.ParentFrameLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.ProjectDetailActivity;
import com.zhonghong.xqshijie.activity.ScenicFragmentActivity;
import com.zhonghong.xqshijie.activity.WebViewActivity;
import com.zhonghong.xqshijie.activity.YltFragmentActivity;
import com.zhonghong.xqshijie.adapter.HorizontalScrollViewAdapter;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.base.BaseFragment;
import com.zhonghong.xqshijie.controller.HomePageController;
import com.zhonghong.xqshijie.data.response.HomePageResponse;
import com.zhonghong.xqshijie.data.response.YltProductResponse;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.util.AppUtils;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;
import com.zhonghong.xqshijie.util.ListViewMeasureUtil;
import com.zhonghong.xqshijie.util.NetUtils;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.HorizontalScrollCustomView;
import com.zhonghong.xqshijie.widget.ImageIncludeTextView;
import com.zhonghong.xqshijie.widget.TitleBigView;
import com.zhonghong.xqshijie.widget.adcircleviewpager.ImageCycleView;
import com.zhonghong.xqshijie.widget.adcircleviewpager.ImageCycleViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezl on 16/6/14.
 */
public class HomePageFragment extends BaseFragment {
    public static final int REFLASH_HOMEPAGE = 0x1001;//刷新首页
    private TitleBigView mTitle;
    public static final String PROJECT_ID = "project_id";//项目ID
    public static final String PROVINC_ID = "provinc_id";//省目的地ID
    public static final String CITY_ID = "city_id";//市目的地ID
    private com.jingchen.pulltorefresh.pullableview.PullableScrollView mPullToRefreshScrollView;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ImageCycleView mImageCycleView;
    private ListView mHotpojectList;
    private HorizontalScrollCustomView mHScrollList;
    private LinearLayout mHScrollLL;
    private ImageIncludeTextView mThemeparksView1;
    private ImageIncludeTextView mThemeparksView2;
    private ImageIncludeTextView mThemeparksView3;
    private ImageIncludeTextView mThemeparksView4;
    private ImageIncludeTextView mThemeparksView5;
    private LinearLayout mHotInfoLL;
    private ImageView mIvHotImage;
    private TextView mTvHotTitle;
    private TextView mTvHotTime;
    private List<YltProductResponse.DatasBean> mDataList = null;
    private LinearLayout mKnowYlt;
    private LinearLayout mInformationsAll;

    private HomePageController homePageController;
    /**
     * 热销项目组件变量
     */
    private QuickAdapter<HomePageResponse.ObjectBean.ProjectsBean> mHotProjectadapter = null;
    /**
     * 水平滚动组件变量
     */
    private HorizontalScrollViewAdapter mScrollListAdapter = null;

    private DefaultRefreshLister dftRefListener = null;
    private ParentFrameLayout parentFrameLayout;//处理异常情况的回显布局

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_homepage, null);
        initView(contentView);
        //初始化view的各控件完成
        isPrepared = true;
        lazyLoad();
        return contentView;
    }

    private void initView(View view) {
        mTitle = (TitleBigView) view.findViewById(R.id.title);
        mPullToRefreshScrollView = (com.jingchen.pulltorefresh.pullableview.PullableScrollView) view.findViewById(R.id.scv_homepage_refresh);
        mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.ptr_homepage_refresh);
        mImageCycleView = (ImageCycleView) view.findViewById(R.id.icv_banner_cycleView);
        mHotpojectList = (ListView) view.findViewById(R.id.lv_hotpoject_list);
        mHScrollList = (HorizontalScrollCustomView) view.findViewById(R.id.hscrollv_home_list);
        mHScrollLL = (LinearLayout) view.findViewById(R.id.ll_hscrollv_gallery);
        parentFrameLayout = (ParentFrameLayout) view.findViewById(R.id.parent_homepage_outside);
        parentFrameLayout.setReLoadOnclickListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_SHOWLOADING, basicHandler);
                handleHomePageByNet(true, NetTag.GETINDEXCONFIG_CACHE);
            }
        }, RefreshUtils.NetState.NET_NULL);
        mThemeparksView1 = (ImageIncludeTextView) view.findViewById(R.id.imtv_themeparks1);
        mThemeparksView2 = (ImageIncludeTextView) view.findViewById(R.id.imtv_themeparks2);
        mThemeparksView3 = (ImageIncludeTextView) view.findViewById(R.id.imtv_themeparks3);
        mThemeparksView4 = (ImageIncludeTextView) view.findViewById(R.id.imtv_themeparks4);
        mThemeparksView5 = (ImageIncludeTextView) view.findViewById(R.id.imtv_themeparks5);
        mHotInfoLL = (LinearLayout) view.findViewById(R.id.ll_hot_info);
        mIvHotImage = (ImageView) view.findViewById(R.id.iv_hot_image);
        mTvHotTitle = (TextView) view.findViewById(R.id.tv_hot_title);
        mTvHotTime = (TextView) view.findViewById(R.id.tv_hot_time);
        mKnowYlt = (LinearLayout) view.findViewById(R.id.ll_know_ylt);
        mInformationsAll = (LinearLayout) view.findViewById(R.id.ll_informations_all);
        mKnowYlt.setOnClickListener(this);
        mInformationsAll.setOnClickListener(this);
        /**
         * 设置网络异常跳转到设置界面
         */
        mTitle.setExceptionNetSetting(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.startToSystemNetSettingActivity(getActivity());
            }
        });
        mPullToRefreshLayout.setOnRefreshListener(dftRefListener = new DefaultRefreshLister(getActivity()) {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                super.onRefresh(pullToRefreshLayout);
                if (havaNetWork) {
                    mImageCycleView.requestFocus();
                    mImageCycleView.setFocusable(true);
                    mImageCycleView.setFocusableInTouchMode(true);
                    handleHomePageByNet(true, NetTag.GETINDEXCONFIG_CACHE);
                } else {
                    if (dftRefListener != null) {
                        RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_NONET, basicHandler);
                        dftRefListener.notifynonetwork(pullToRefreshLayout);
                    }
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                super.onLoadMore(pullToRefreshLayout);
                if (havaNetWork) {
                    if (dftRefListener != null) {
                        dftRefListener.notifyhandledata(true, mPullToRefreshLayout);
                    }
                    RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);
                } else {
                    if (dftRefListener != null) {
                        dftRefListener.notifynonetwork(pullToRefreshLayout);
                    }
                    RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_NONET, basicHandler);
                }
            }
        });
    }

    @Override
    protected void handleCreate() {
        RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_SHOWLOADING, null);
        handleHomePageByNet(true, NetTag.GETINDEXCONFIG_CACHE);

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }
        /**
         * 头部联网异常的显示，接收到网络改变的广播
         *
         * @param connect 网络是否连接,true 网络连接，false 网络断
         */
    @Override
    protected void aboutException(boolean connect) {
        if (connect) {
            mTitle.dismissException();
        } else {
            mTitle.showException();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void processMessage(Message msg) {
        switch (msg.what) {
            case REFLASH_HOMEPAGE:
                HomePageResponse homePageResponse = (HomePageResponse) msg.obj;
                refreshByHandle(homePageResponse);
                break;
        }
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.ll_know_ylt://了解逸乐通
                getActivity().startActivity(new Intent(getActivity(), YltFragmentActivity.class));
                break;
            case R.id.ll_informations_all:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.baidu.com");
                intent.putExtra("title", "");
                getActivity().startActivity(intent);
                break;
        }
    }


    @Override
    public void onNetResult(String interfaceAction, Object result) {
        basicHandler.obtainMessage(REFLASH_HOMEPAGE, result).sendToTarget();

        if (dftRefListener != null) {
            dftRefListener.notifyhandledata(true, mPullToRefreshLayout);
        }
    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isCaChed) {

        if (dftRefListener != null) {
            dftRefListener.notifyhandledata(false, mPullToRefreshLayout);
        }

        if (NetUtils.isNetConnected()) {
            if (isCaChed) {
                RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);//有网友缓存
            } else {
                RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);//有网无数据
            }
        } else {
            if (isCaChed) {
                RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);//无网有缓存
            } else {
                RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_NONET, basicHandler);//无网无数据
            }
        }
    }

    @Override
    public void onNetFinished(String interfaceAction) {
    }

    private void handleHomePageByNet(boolean isCache, String cacheKey) {
        if (homePageController == null) {
            homePageController = new HomePageController(getActivity());
        }
        homePageController.handleHomePageByNet(this, isCache, cacheKey, parentFrameLayout);
    }

    /**
     * 刷新UI方法
     *
     * @param homePageResponse
     */
    private void refreshByHandle(final HomePageResponse homePageResponse) {
        if (homePageResponse != null && homePageResponse.mObject != null) {

            /**
             * 保存服务器域名
             */
            if (homePageResponse.mObject.mDomainBeanList != null) {
                for (int i = 0; i < homePageResponse.mObject.mDomainBeanList.size(); i++) {
                    HomePageResponse.ObjectBean.DomainBean mDomainName = homePageResponse.mObject.mDomainBeanList.get(i);
                    if (i == 0 && !StringUtils.isNull(mDomainName.mDomainName)) {
                        SharedPreferencesUtil.getInstance(getActivity()).putStringValue(SharedPreferencesTag.DOMAIN_URL, mDomainName.mDomainName);//用户ID
                    }
                }
            }
            /**
             * 默认轮播图
             */
            initFlowView(homePageResponse.mObject.mBannersBeanList);
            /**
             * 热销项目
             */
            if (homePageResponse.mObject.mProjectsBeanList != null) {
                mHotProjectadapter = new QuickAdapter<HomePageResponse.ObjectBean.ProjectsBean>(mActivity, R.layout.adapter_project_item, homePageResponse.mObject.mProjectsBeanList) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, HomePageResponse.ObjectBean.ProjectsBean item) {
                        ImageView iv_includebg_view = helper.getView(R.id.iv_includebg_view);
                        TextView tv_projecttext_view = helper.getView(R.id.tv_projecttext_view);
                        TextView tv_projectloc_view = helper.getView(R.id.tv_projectloc_view);
                        ImageLoaderUtil.getInstance().loadImage(item.mImageUrl, iv_includebg_view, R.drawable.image_onloading_homebig);
                        tv_projecttext_view.setText(StringUtils.repNull(item.mTitle));
                        tv_projectloc_view.setText(StringUtils.repNull(item.mProvinc).concat(StringUtils.repNull(item.mCity)));
                    }
                };
                mHotpojectList.setAdapter(mHotProjectadapter);
                ListViewMeasureUtil.setListViewHeightBasedOnChildren(mHotpojectList);
                mHotpojectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        HomePageResponse.ObjectBean.ProjectsBean menuleftItemBean = (HomePageResponse.ObjectBean.ProjectsBean) mHotpojectList.getAdapter().getItem(position);
                        if (menuleftItemBean != null && !StringUtils.isNull(menuleftItemBean.mId)) {
                            Intent intent = new Intent(new Intent(getActivity(), ProjectDetailActivity.class));
                            intent.putExtra(PROJECT_ID, menuleftItemBean.mId);
                            getActivity().startActivity(intent);
                        }
                    }
                });
            }

            /**
             * 更多目的地
             */
            if (homePageResponse.mObject.mAddresssBeanList != null) {
                mScrollListAdapter = new HorizontalScrollViewAdapter(getActivity(), homePageResponse.mObject.mAddresssBeanList);
                //添加滚动回调
                mHScrollList.setCurrentImageChangeListener(new HorizontalScrollCustomView.CurrentImageChangeListener() {
                    @Override
                    public void onCurrentImgChanged(int position, View viewIndicator) {
//                        mImg.setImageResource(mDatas.get(position));
//                        viewIndicator.setBackgroundColor(Color
//                                .parseColor("#AA024DA4"));
                    }
                });

                //添加点击回调
                mHScrollList.setOnItemClickListener(new HorizontalScrollCustomView.OnItemClickListener() {

                    @Override
                    public void onClick(View view, int position) {
                        HomePageResponse.ObjectBean.AddresssBean addressItemBean = (HomePageResponse.ObjectBean.AddresssBean) mScrollListAdapter.getItem(position);
                        if (addressItemBean != null) {
                            Intent intent = new Intent(getActivity(), ScenicFragmentActivity.class);
                            intent.putExtra(PROVINC_ID, addressItemBean.mProvincId);
                            intent.putExtra(CITY_ID, addressItemBean.mCityId);
                            getActivity().startActivity(intent);
                        }
                    }
                });
                mHScrollList.initDatas(mScrollListAdapter);
            }
            /**
             * 主题乐园
             */
            if (homePageResponse.mObject.mThemeParksBeanList != null) {
                for (int i = 0; i < homePageResponse.mObject.mThemeParksBeanList.size(); i++) {
                    final HomePageResponse.ObjectBean.ThemeParksBean mTemeParksData = homePageResponse.mObject.mThemeParksBeanList.get(i);
                    if (i == 0) {
                        mThemeparksView1.setTextValue(StringUtils.repNull(mTemeParksData.mTitle));
                        mThemeparksView1.setImageBg(StringUtils.repNull(mTemeParksData.mImageUrl));
                        mThemeparksView1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!StringUtils.isNull(mTemeParksData.mProjectId)) {
                                    Intent intent = new Intent(new Intent(getActivity(), ProjectDetailActivity.class));
                                    intent.putExtra(PROJECT_ID, mTemeParksData.mProjectId);
                                    getActivity().startActivity(intent);
                                }
                            }
                        });
                    } else if (i == 1) {
                        mThemeparksView2.setTextValue(StringUtils.repNull(mTemeParksData.mTitle));
                        mThemeparksView2.setImageBg(StringUtils.repNull(mTemeParksData.mImageUrl));
                        mThemeparksView2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!StringUtils.isNull(mTemeParksData.mProjectId)) {
                                    Intent intent = new Intent(new Intent(getActivity(), ProjectDetailActivity.class));
                                    intent.putExtra(PROJECT_ID, mTemeParksData.mProjectId);
                                    getActivity().startActivity(intent);
                                }
                            }
                        });
                    } else if (i == 2) {
                        mThemeparksView3.setTextValue(StringUtils.repNull(mTemeParksData.mTitle));
                        mThemeparksView3.setImageBg(StringUtils.repNull(mTemeParksData.mImageUrl));
                        mThemeparksView3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!StringUtils.isNull(mTemeParksData.mProjectId)) {
                                    Intent intent = new Intent(new Intent(getActivity(), ProjectDetailActivity.class));
                                    intent.putExtra(PROJECT_ID, mTemeParksData.mProjectId);
                                    getActivity().startActivity(intent);
                                }
                            }
                        });
                    } else if (i == 3) {
                        mThemeparksView4.setTextValue(StringUtils.repNull(mTemeParksData.mTitle));
                        mThemeparksView4.setImageBg(StringUtils.repNull(mTemeParksData.mImageUrl));
                        mThemeparksView4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!StringUtils.isNull(mTemeParksData.mProjectId)) {
                                    Intent intent = new Intent(new Intent(getActivity(), ProjectDetailActivity.class));
                                    intent.putExtra(PROJECT_ID, mTemeParksData.mProjectId);
                                    getActivity().startActivity(intent);
                                }
                            }
                        });
                    } else if (i == 4) {
                        mThemeparksView5.setTextValue(StringUtils.repNull(mTemeParksData.mTitle));
                        mThemeparksView5.setImageBg(StringUtils.repNull(mTemeParksData.mImageUrl));
                        mThemeparksView5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!StringUtils.isNull(mTemeParksData.mProjectId)) {
                                    Intent intent = new Intent(new Intent(getActivity(), ProjectDetailActivity.class));
                                    intent.putExtra(PROJECT_ID, mTemeParksData.mProjectId);
                                    getActivity().startActivity(intent);
                                }
                            }
                        });
                    }
                }
            }

            /**
             * 热点资讯
             */
            if (homePageResponse.mObject.mInformationsBeenList != null) {
                for (int i = 0; i < homePageResponse.mObject.mInformationsBeenList.size(); i++) {
                    final HomePageResponse.ObjectBean.InformationsBean mInformationsBean = homePageResponse.mObject.mInformationsBeenList.get(i);
                    if (i == 0) {
                        ImageLoaderUtil.getInstance().loadImage("", mIvHotImage, R.drawable.image_onloading_homeinfo);
                        mTvHotTitle.setText(mInformationsBean.mTitle);
                        mTvHotTime.setText(mInformationsBean.mTime);
                        mHotInfoLL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                                intent.putExtra("url", NetUtils.getDealUrl(mInformationsBean.mLinkUrl, ""));
                                intent.putExtra("title", "");
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                }
            }

            RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);

            mPullToRefreshScrollView.smoothScrollTo(0, 0);
        } else {//异常情况
            if (NetUtils.isNetConnected()) {
                RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
            } else {
                RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_NONET, basicHandler);
            }
        }
    }


    /**
     * 初始化轮播图
     */
    private void initFlowView(final List<HomePageResponse.ObjectBean.BannersBean> imageAdList) {
        if (imageAdList == null) {
            return;
        }
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getScreenHeight(getActivity()) * 3 / 10);
        mImageCycleView.setLayoutParams(cParams);
        ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                /**实现点击事件*/
                if (imageAdList != null) {
                    HomePageResponse.ObjectBean.BannersBean bannersBean = imageAdList.get(position);
                    if (bannersBean != null && "project".equals(bannersBean.mField) && !StringUtils.isNull(bannersBean.mFieldValue)) {
                        Intent intent = new Intent(new Intent(getActivity(), ProjectDetailActivity.class));
                        intent.putExtra(PROJECT_ID, bannersBean.mFieldValue);
                        getActivity().startActivity(intent);

                    } else if (bannersBean != null && "link".equals(bannersBean.mField) && !StringUtils.isNull(bannersBean.mFieldValue)) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url", bannersBean.mFieldValue);
                        intent.putExtra("title", "");
                        getActivity().startActivity(intent);
                    }
                }
            }

            @Override
            public View initContentView(Object item, ViewGroup container, int position) {
                HomePageResponse.ObjectBean.BannersBean itemtemp = (HomePageResponse.ObjectBean.BannersBean) item;
                View mView = View.inflate(getActivity(), R.layout.widget_adcycle_imageitem_view, null);
                ImageView iv_adcycle_view = (ImageView) mView.findViewById(R.id.iv_adcycle_view);
                String imageURL = itemtemp.mImageUrl;
                ImageLoaderUtil.getInstance().loadImage(imageURL, iv_adcycle_view, R.drawable.image_onloading_homebig);
                return mView;
            }
        };
        /**设置数据*/
        mImageCycleView.setImageResources((ArrayList<HomePageResponse.ObjectBean.BannersBean>) imageAdList, mAdCycleViewListener);
        mImageCycleView.startImageCycle();
    }


    /**
     * 默认总数据
     *
     * @return
     */
    private HomePageResponse getDefaultHomePageRes() {
        HomePageResponse homePageResponse = new HomePageResponse();
        HomePageResponse.ObjectBean objectBean = new HomePageResponse.ObjectBean();
        homePageResponse.mObject = objectBean;
        objectBean.mBannersBeanList = getDefaultBannerDatas();
        objectBean.mProjectsBeanList = getDefaultHotProjectDatas();
        objectBean.mAddresssBeanList = getDefaultAddressDatas();
        return homePageResponse;
    }


    /**
     * 获取轮播图默认数据
     */
    private List<HomePageResponse.ObjectBean.BannersBean> getDefaultBannerDatas() {
        ArrayList<HomePageResponse.ObjectBean.BannersBean> mBannerList = new ArrayList<HomePageResponse.ObjectBean.BannersBean>();
        HomePageResponse.ObjectBean.BannersBean mBannerBean1 = new HomePageResponse.ObjectBean.BannersBean();
        mBannerBean1.mField = "";
        mBannerBean1.mFieldValue = "";
        mBannerBean1.mImageUrl = "http://attach.bbs.miui.com/forum/month_1012/101203122706c89249c8f58fcc.jpg";
        mBannerList.add(mBannerBean1);

        HomePageResponse.ObjectBean.BannersBean mBannerBean2 = new HomePageResponse.ObjectBean.BannersBean();
        mBannerBean2.mField = "";
        mBannerBean2.mFieldValue = "";
        mBannerBean2.mImageUrl = "http://bbsdown10.cnmo.com/attachments/201308/06/091441rn5ww131m0gj55r0.jpg";
        mBannerList.add(mBannerBean2);

//        HomePageResponse.ObjectBean.BannersBean mBannerBean3 = new HomePageResponse.ObjectBean.BannersBean();
//        mBannerBean3.mField = "";
//        mBannerBean3.mfieldValue = "";
//        mBannerBean3.mImageUrl = "http://attach.bbs.miui.com/forum/201604/05/001754vp6j0vmcj49f0evc.jpg.thumb.jpg";
//        mBannerList.add(mBannerBean3);
//
//        HomePageResponse.ObjectBean.BannersBean mBannerBean4 = new HomePageResponse.ObjectBean.BannersBean();
//        mBannerBean4.mField = "";
//        mBannerBean4.mfieldValue = "";
//        mBannerBean4.mImageUrl = "http://d.3987.com/taiqiumein_141001/007.jpg";
//        mBannerList.add(mBannerBean4);
        return mBannerList;
    }

    /**
     * 获取默认热点项目数据
     */
    private List<HomePageResponse.ObjectBean.ProjectsBean> getDefaultHotProjectDatas() {
        ArrayList<HomePageResponse.ObjectBean.ProjectsBean> mProjectsList = new ArrayList<HomePageResponse.ObjectBean.ProjectsBean>();
        for (int i = 0; i < 4; i++) {
            HomePageResponse.ObjectBean.ProjectsBean mProjectsBean = new HomePageResponse.ObjectBean.ProjectsBean();
            mProjectsBean.mCity = "北京";
            if (i == 0) {
                mProjectsBean.mImageUrl = "http://bbsdown10.cnmo.com/attachments/201308/06/091441rn5ww131m0gj55r0.jpg";
            } else if (i == 1) {
                mProjectsBean.mImageUrl = "http://d.3987.com/taiqiumein_141001/007.jpg";
            } else if (i == 2) {
                mProjectsBean.mImageUrl = "http://pic28.nipic.com/20130424/11588775_115415688157_2.jpg";
            } else if (i == 3) {
                mProjectsBean.mImageUrl = "http://d.3987.com/taiqiumein_141001/007.jpg";
            }

            mProjectsBean.mTitle = "新奇世界.御马";
            mProjectsList.add(mProjectsBean);
        }
        return mProjectsList;
    }


    /**
     * 获取默认地理位置数据
     */
    private List<HomePageResponse.ObjectBean.AddresssBean> getDefaultAddressDatas() {
        ArrayList<HomePageResponse.ObjectBean.AddresssBean> mAddressList = new ArrayList<HomePageResponse.ObjectBean.AddresssBean>();
        HomePageResponse.ObjectBean.AddresssBean mAddressBean1 = new HomePageResponse.ObjectBean.AddresssBean();
        mAddressBean1.mCityId = "北京";
        mAddressBean1.mImageUrl = "http://attach.bbs.miui.com/forum/month_1012/101203122706c89249c8f58fcc.jpg";
        mAddressList.add(mAddressBean1);

        HomePageResponse.ObjectBean.AddresssBean mAddressBean2 = new HomePageResponse.ObjectBean.AddresssBean();
        mAddressBean2.mProvincId = "山东";
        mAddressBean2.mImageUrl = "http://bbsdown10.cnmo.com/attachments/201308/06/091441rn5ww131m0gj55r0.jpg";
        mAddressList.add(mAddressBean2);

        HomePageResponse.ObjectBean.AddresssBean mAddressBean3 = new HomePageResponse.ObjectBean.AddresssBean();
        mAddressBean3.mProvincId = "海南";
        mAddressBean3.mImageUrl = "http://attach.bbs.miui.com/forum/201604/05/001754vp6j0vmcj49f0evc.jpg.thumb.jpg";
        mAddressList.add(mAddressBean3);

        HomePageResponse.ObjectBean.AddresssBean mAddressBean4 = new HomePageResponse.ObjectBean.AddresssBean();
        mAddressBean4.mProvincId = "黑龙江";
        mAddressBean4.mImageUrl = "http://d.3987.com/taiqiumein_141001/007.jpg";
        mAddressList.add(mAddressBean4);


        HomePageResponse.ObjectBean.AddresssBean mAddressBean5 = new HomePageResponse.ObjectBean.AddresssBean();
        mAddressBean5.mCityId = "北京";
        mAddressBean5.mImageUrl = "http://attach.bbs.miui.com/forum/month_1012/101203122706c89249c8f58fcc.jpg";
        mAddressList.add(mAddressBean5);

        HomePageResponse.ObjectBean.AddresssBean mAddressBean6 = new HomePageResponse.ObjectBean.AddresssBean();
        mAddressBean6.mProvincId = "山东";
        mAddressBean6.mImageUrl = "http://bbsdown10.cnmo.com/attachments/201308/06/091441rn5ww131m0gj55r0.jpg";
        mAddressList.add(mAddressBean6);

        HomePageResponse.ObjectBean.AddresssBean mAddressBean7 = new HomePageResponse.ObjectBean.AddresssBean();
        mAddressBean7.mProvincId = "海南";
        mAddressBean7.mImageUrl = "http://attach.bbs.miui.com/forum/201604/05/001754vp6j0vmcj49f0evc.jpg.thumb.jpg";
        mAddressList.add(mAddressBean7);

        HomePageResponse.ObjectBean.AddresssBean mAddressBean8 = new HomePageResponse.ObjectBean.AddresssBean();
        mAddressBean8.mProvincId = "黑龙江";
        mAddressBean8.mImageUrl = "http://d.3987.com/taiqiumein_141001/007.jpg";
        mAddressList.add(mAddressBean8);

        return mAddressList;
    }


}
