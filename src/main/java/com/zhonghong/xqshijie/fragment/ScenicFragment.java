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
import com.jingchen.pulltorefresh.pullableview.PullableScrollView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.ProjectDetailActivity;
import com.zhonghong.xqshijie.activity.WebViewActivity;
import com.zhonghong.xqshijie.base.BaseFragment;
import com.zhonghong.xqshijie.controller.ScenicController;
import com.zhonghong.xqshijie.data.response.ScenicBannersResponse;
import com.zhonghong.xqshijie.data.response.ScenicProductResponse;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.util.AppUtils;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;
import com.zhonghong.xqshijie.util.ListViewMeasureUtil;
import com.zhonghong.xqshijie.util.LogUtils;
import com.zhonghong.xqshijie.util.NetUtils;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.TitleBigView;
import com.zhonghong.xqshijie.widget.adcircleviewpager.ImageCycleView;
import com.zhonghong.xqshijie.widget.adcircleviewpager.ImageCycleViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezl on 16/6/14.
 */
public class ScenicFragment extends BaseFragment {

    public static final int HANDLE_REFRESH_PRODUCTS = 0x1432;//刷新产品列表
    public static final int HANDLE_REFRESH_BANNER = 0x1433;//刷新产品列表

    private TitleBigView mTitle;//顶部标题栏
    private com.zhonghong.xqshijie.widget.TitleView mTitle2;
    private ImageCycleView mImageCycleView;

    private ListView mLvScnichomeProductlist = null; //景区产品列表
    private QuickAdapter<ScenicProductResponse.ProjectsBean> mScenicQuickAdapter = null;
    private ScenicController mScenicController = null;
    private int mPage = 1;
    private int mPageSize = 10;
    private com.jingchen.pulltorefresh.pullableview.PullableScrollView mPullToRefreshScrollView;
    private PullToRefreshLayout mScenicHomeLayout;//下拉上拉刷新布局
    private DefaultRefreshLister dftRefListener = null;
    private String mProvincId = null;//省份ID
    private String mCityId = null;
    private ParentFrameLayout mParentScenic;//异常布局
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_scenic, null);
        mTitle = (TitleBigView) contentView.findViewById(R.id.title);
        mTitle2 = (com.zhonghong.xqshijie.widget.TitleView) contentView.findViewById(R.id.title2);
        mImageCycleView = (ImageCycleView) contentView.findViewById(R.id.icv_banner_cycleView);
        mPullToRefreshScrollView = (PullableScrollView) contentView.findViewById(R.id.scv_scenichomepage_refresh);
        mScenicHomeLayout = (PullToRefreshLayout) contentView.findViewById(R.id.ptr_scenichomepage_refresh);
        mParentScenic = (ParentFrameLayout) contentView.findViewById(R.id.exp_scenic_content);
        mLvScnichomeProductlist = (ListView) contentView.findViewById(R.id.lv_scnichome_productlist);
        //初始化view的各控件完成
        isPrepared = true;
        lazyLoad();
        return contentView;
    }


    @Override
    protected void handleCreate() {
        mParentScenic.setReLoadOnclickListeners(new View.OnClickListener() {//重新加载
            @Override
            public void onClick(View v) {
                RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_SHOWLOADING, null);
                if (!StringUtils.isNull(mProvincId)) {//根据目的地查询景区
                    handleScenicForAddressByNet(mPageSize, mPage, mProvincId, mCityId);
                } else {//查询所有景区
                    handleScenicBannerByNet();
                    handleScenicByNet(mPageSize, mPage);
                }
            }
        }, RefreshUtils.NetState.NET_NULL);

        /**
         * 设置网络异常跳转到设置界面
         */
        mTitle.setExceptionNetSetting(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.startToSystemNetSettingActivity(getActivity());
            }
        });

        mScenicHomeLayout.setOnRefreshListener(dftRefListener = new DefaultRefreshLister(getActivity()) {

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                super.onRefresh(pullToRefreshLayout);
                if (havaNetWork) {
                    mImageCycleView.requestFocus();
                    mImageCycleView.setFocusable(true);
                    mImageCycleView.setFocusableInTouchMode(true);
                    handleScenicBannerByNet();
                    mPage = 1;
                    if (!StringUtils.isNull(mProvincId)) {//根据目的地查询景区
                        handleScenicForAddressByNet(mPageSize, mPage, mProvincId, mCityId);
                    } else {
                        handleScenicBannerByNet();
                        handleScenicByNet(mPageSize, mPage);
                    }
                } else {
                    if (dftRefListener != null) {
                        dftRefListener.notifynonetwork(pullToRefreshLayout);
                    }
                    RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_NONET, basicHandler);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                super.onLoadMore(pullToRefreshLayout);
                if (havaNetWork) {
                    mImageCycleView.requestFocus();
                    mImageCycleView.setFocusable(true);
                    mImageCycleView.setFocusableInTouchMode(true);
                    if (!StringUtils.isNull(mProvincId)) {//根据目的地查询景区
                        handleScenicForAddressByNet(mPageSize, mPage++, mProvincId, mCityId);
                    } else {
                        handleScenicByNet(mPageSize, mPage++);
                    }
                } else {
                    if (dftRefListener != null) {
                        dftRefListener.notifynonetwork(pullToRefreshLayout);
                    }
                    RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_NONET, basicHandler);
                }
            }
        });

        if (getArguments() != null) {
            if (getArguments().get("title") != null) {
                mTitle.setTitle((String) getArguments().get("title"));
                mTitle2.setTitle((String) getArguments().get("title"));
            }

            if (getArguments().getString(HomePageFragment.PROVINC_ID) != null) {
                mTitle.setVisibility(View.GONE);
                mTitle2.setVisibility(View.VISIBLE);
                mProvincId = getArguments().getString(HomePageFragment.PROVINC_ID);
                mCityId = getArguments().getString(HomePageFragment.CITY_ID);
                mTitle.setTitleLeftImage(R.drawable.ic_return);
            } else {
                mTitle.setVisibility(View.VISIBLE);
                mTitle2.setVisibility(View.GONE);
            }
        }

        if (mScenicController == null) {
            mScenicController = new ScenicController(getActivity());
        }

        if (!StringUtils.isNull(mProvincId)) {//根据目的地查询景区
            handleScenicForAddressByNet(mPageSize, mPage, mProvincId, mCityId);
        } else {//查询所有景区
            handleScenicBannerByNet();
            handleScenicByNet(mPageSize, mPage);
        }
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }

    /**
     * 头部联网异常的显示,接收到网络改变的广播
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
    protected void processMessage(Message msg) {
        switch (msg.what) {
            case HANDLE_REFRESH_PRODUCTS:
                ScenicProductResponse scenicProductResponse = (ScenicProductResponse) msg.obj;
                refreshListByHandle(scenicProductResponse);
                break;
            case HANDLE_REFRESH_BANNER:
                ScenicBannersResponse scenicBannersResponse = (ScenicBannersResponse) msg.obj;
                refreshBannersByHandle(scenicBannersResponse);
                break;
        }
    }

    @Override
    protected void customOnClick(View v) {

    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {
        if (interfaceAction.equals(NetTag.GETSCENICPRODUCT)) {
            basicHandler.obtainMessage(HANDLE_REFRESH_PRODUCTS, result).sendToTarget();
        } else if (interfaceAction.equals(NetTag.GETSCENICBANNER)) {
            basicHandler.obtainMessage(HANDLE_REFRESH_BANNER, result).sendToTarget();
        } else if (interfaceAction.equals(NetTag.GETPROBYREGION)) {
            basicHandler.obtainMessage(HANDLE_REFRESH_PRODUCTS, result).sendToTarget();
        }

        if (dftRefListener != null) {
            dftRefListener.notifyhandledata(true, mScenicHomeLayout);
        }
    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean hadCache) {

        LogUtils.e("onNetError");

        if (dftRefListener != null) {
            dftRefListener.notifyhandledata(false, mScenicHomeLayout);
        }

        //异常处理
        if (NetUtils.isNetConnected()) {
            if (hadCache) {
                RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);//有网友缓存
            } else {
                RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);//有网无数据
            }
        } else {
            if (hadCache) {
                RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);//无网有缓存
            } else {
                RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_NONET, basicHandler);//无网无数据
            }
        }

    }

    @Override
    public void onNetFinished(String interfaceAction) {
        LogUtils.d("onNetFinished");
    }


    /**
     * 获取景区广告
     */
    private void handleScenicBannerByNet() {
        mScenicController.handleScenicBannerByNet(this, true, NetTag.GETSCENICBANNER_CACHE);
    }

    /**
     * 获取景区列表
     *
     * @param mPageSize
     * @param mPage
     */
    private void handleScenicByNet(int mPageSize, int mPage) {
        mScenicController.handleScenicByNet(this, String.valueOf(mPageSize), String.valueOf(mPage), true, NetTag.GETSCENICPRODUCT_CACHE, mParentScenic);
    }

    /**
     * 根据目的地获取景区列表
     *
     * @param mPageSize
     * @param mPage
     * @param mProvinceId
     * @param mCityId
     */
    private void handleScenicForAddressByNet(int mPageSize, int mPage, String mProvinceId, String mCityId) {
        mScenicController.handleScenicForAddressByNet(this, String.valueOf(mPageSize), String.valueOf(mPage), mProvinceId, mCityId, false, NetTag.GETPROBYREGION_CACHE);
    }

    /**
     * 获取广告后刷新
     *
     * @param scenicBannersResponse
     */
    private void refreshBannersByHandle(final ScenicBannersResponse scenicBannersResponse) {
        if (scenicBannersResponse != null && scenicBannersResponse.mXqjqConfig != null && scenicBannersResponse.mXqjqConfig.mBanners != null) {
            initFlowView(scenicBannersResponse.mXqjqConfig.mBanners);
            RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);
        } else {
            if (NetUtils.isNetConnected()) {
                RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
            } else {
                RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_NONET, basicHandler);
            }
        }
    }

    /**
     * 获取数据后刷新list
     *
     * @param scenicProductResponse
     */
    private void refreshListByHandle(final ScenicProductResponse scenicProductResponse) {
        if (scenicProductResponse != null && scenicProductResponse.projects != null) {
            mLvScnichomeProductlist.setAdapter(mScenicQuickAdapter = new QuickAdapter<ScenicProductResponse.ProjectsBean>(getActivity(), R.layout.adapter_project_item, scenicProductResponse.projects) {
                @Override
                protected void convert(BaseAdapterHelper helper, ScenicProductResponse.ProjectsBean item) {
                    ImageView iv_includebg_view = helper.getView(R.id.iv_includebg_view);
                    TextView tv_projecttext_view = helper.getView(R.id.tv_projecttext_view);
                    TextView tv_projectloc_view = helper.getView(R.id.tv_projectloc_view);
                    ImageLoaderUtil.getInstance().loadImage(item.mProjectCover, iv_includebg_view, R.drawable.image_onloading_homebig);
                    tv_projecttext_view.setText(StringUtils.repNull(item.mProjectName));
                    tv_projectloc_view.setText(StringUtils.repNull(item.mProjectProvince).concat(StringUtils.repNull(item.mProjectCity)));
                }
            });

            mLvScnichomeProductlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    ScenicProductResponse.ProjectsBean item = scenicProductResponse.projects.get(position);
                    if (item != null && !StringUtils.isNull(item.mProjectId)) {
                        Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
                        intent.putExtra(HomePageFragment.PROJECT_ID, item.mProjectId);
                        getActivity().startActivity(intent);
                    }
                }
            });

            ListViewMeasureUtil.setListViewHeightBasedOnChildren(mLvScnichomeProductlist);
            RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);
//            mPullToRefreshScrollView.smoothScrollTo(0, 0);
        } else {
            if (NetUtils.isNetConnected()) {
                RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
            } else {
                RefreshUtils.AlertWhichParentlayout(mParentScenic, RefreshUtils.NetState.NET_NONET, basicHandler);
            }
        }
    }

    /**
     * 初始化轮播图
     */
    private void initFlowView(final List<ScenicBannersResponse.XqjqConfigBean.BannersBean> imageAdList) {
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
                    ScenicBannersResponse.XqjqConfigBean.BannersBean bannersBean = imageAdList.get(position);
                    if (bannersBean != null && "project".equals(bannersBean.mField)) {
                        if (!StringUtils.isNull(bannersBean.mFieldValue)) {
                            Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
                            intent.putExtra(HomePageFragment.PROJECT_ID, bannersBean.mFieldValue);
                            getActivity().startActivity(intent);
                        }
                    } else if (bannersBean != null && "link".equals(bannersBean.mField) && !StringUtils.isNull(bannersBean.mFieldValue)) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url", bannersBean.mFieldValue);
                        getActivity().startActivity(intent);
                    }
                }
            }

            @Override
            public View initContentView(Object item, ViewGroup container, int position) {
                ScenicBannersResponse.XqjqConfigBean.BannersBean itemtemp = (ScenicBannersResponse.XqjqConfigBean.BannersBean) item;
                View mView = View.inflate(getActivity(), R.layout.widget_adcycle_imageitem_view, null);
                ImageView iv_adcycle_view = (ImageView) mView.findViewById(R.id.iv_adcycle_view);
                String imageURL = itemtemp.mImageUrl;
                ImageLoaderUtil.getInstance().loadImage(imageURL, iv_adcycle_view, R.drawable.image_onloading_homebig);
                return mView;
            }
        };

        /**设置数据*/
        mImageCycleView.setImageResources((ArrayList<ScenicBannersResponse.XqjqConfigBean.BannersBean>) imageAdList, mAdCycleViewListener);
        mImageCycleView.startImageCycle();
    }

}
