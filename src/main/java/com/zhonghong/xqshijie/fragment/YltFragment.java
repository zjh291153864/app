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
import android.widget.Toast;

import com.jingchen.pulltorefresh.DefaultRefreshLister;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.RefreshUtils;
import com.jingchen.pulltorefresh.parent.ParentFrameLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.ConfirmOrderActivity;
import com.zhonghong.xqshijie.activity.YltFragmentActivity;
import com.zhonghong.xqshijie.base.BaseFragment;
import com.zhonghong.xqshijie.controller.YltController;
import com.zhonghong.xqshijie.data.response.YltHomePageResponse;
import com.zhonghong.xqshijie.data.response.YltProductResponse;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.util.AppUtils;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;
import com.zhonghong.xqshijie.util.ListViewMeasureUtil;
import com.zhonghong.xqshijie.util.NetUtils;
import com.zhonghong.xqshijie.widget.ShareWindow;
import com.zhonghong.xqshijie.widget.TitleBigView;
import com.zhonghong.xqshijie.widget.adcircleviewpager.ImageCycleView;
import com.zhonghong.xqshijie.widget.adcircleviewpager.ImageCycleViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezl on 16/6/14.
 */
public class YltFragment extends BaseFragment {
    public static final int REFRESH_YLT_HOMEPAGE = 0x2001;//刷新ylt首页
    public static final int REFRESH_YLT_PRODUCTS = 0x2002;//刷新ylt产品表

    private PullToRefreshLayout mPtrYlthomeRefreshlayout;
    private TitleBigView mTitle;//顶部标题栏
    private com.zhonghong.xqshijie.widget.TitleView mTitle2;
    private ImageCycleView mImageCycleView;
    private ImageView iv_processes_view;//使用流程
    private ListView mLvYltProductList = null;//底部产品的数据列表
    private QuickAdapter<YltProductResponse.DatasBean> mQuickAdapter = null;//底部产品列表的数据适配器
    private YltController mYltController = null;//网络请求Controller
    private int mPage = 1;
    private int mPageSize = 5;
    private boolean mIsTheLastPage = false;//判断接口分页返回的数据是否到尾页
    private DefaultRefreshLister mDefaultRefreshLister = null;
    private ShareWindow sharewindow;
    private ParentFrameLayout mParentInterface;
    private List<YltProductResponse.DatasBean> mDataList = null;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_ylt, null);
        mParentInterface = (ParentFrameLayout) contentView.findViewById(R.id.parent_ylt_content);

        mTitle = (TitleBigView) contentView.findViewById(R.id.title);
        mTitle2 = (com.zhonghong.xqshijie.widget.TitleView) contentView.findViewById(R.id.title2);
        mImageCycleView = (ImageCycleView) contentView.findViewById(R.id.icv_banner_cycleView);
        iv_processes_view = (ImageView) contentView.findViewById(R.id.iv_processes_view);
        mLvYltProductList = (ListView) contentView.findViewById(R.id.lv_ylthome_productlist);
        mPtrYlthomeRefreshlayout = ((PullToRefreshLayout) contentView.findViewById(R.id.ptr_ylthome_refreshlayout));
        //初始化view的各控件完成
        isPrepared = true;
        lazyLoad();
        return contentView;
    }

    @Override
    protected void handleCreate() {
        mParentInterface.setReLoadOnclickListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_SHOWLOADING, null);
                postHomeProductsDatas(1);
                handleYltHomePageByNet();
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

        //下拉刷新及上拉加载的回调函数
        mPtrYlthomeRefreshlayout.setOnRefreshListener(mDefaultRefreshLister = new DefaultRefreshLister(getActivity()) {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                super.onRefresh(pullToRefreshLayout);
                if (havaNetWork) {
                    handleYltHomePageByNet();
                    mPage = 1;
                    postHomeProductsDatas(mPage);
                } else {
                    if (mDefaultRefreshLister != null) {
                        mDefaultRefreshLister.notifynonetwork(pullToRefreshLayout);
                    }
                    RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_NONET, basicHandler);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                super.onLoadMore(pullToRefreshLayout);
                if (havaNetWork) {
                    postHomeProductsDatas(mPage++);
                } else {
                    if (mDefaultRefreshLister != null) {
                        mDefaultRefreshLister.notifynonetwork(pullToRefreshLayout);
                    }
                    RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_NONET, basicHandler);
                }
            }
        });

        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_SHOWLOADING, null);

        if (getArguments() != null && getArguments().get("title") != null) {
            mTitle.setTitle((String) getArguments().get("title"));
            mTitle2.setTitle((String) getArguments().get("title"));
        }

        if (getArguments().getString(YltFragmentActivity.CHANNEL) != null) {
            mTitle.setVisibility(View.GONE);
            mTitle2.setVisibility(View.VISIBLE);
        } else {
            mTitle.setVisibility(View.VISIBLE);
            mTitle2.setVisibility(View.GONE);
        }

        mTitle.setRightImageOnClickListener(this);
        mTitle2.setRightImageOnClickListener(this);

        if (mYltController == null) {
            mYltController = new YltController(getActivity());
        }

        handleYltHomePageByNet();
        postHomeProductsDatas(mPage);

        mLvYltProductList.setAdapter(mQuickAdapter = new QuickAdapter<YltProductResponse.DatasBean>(getActivity(), R.layout.adapter_ylt_homeitem, null) {
            @Override
            protected void convert(BaseAdapterHelper helper, YltProductResponse.DatasBean item) {
                TextView tvName = helper.getView(R.id.tv_ylthomepage_itemname);
                tvName.setText(item.mProjectName);
                TextView tvPrice = helper.getView(R.id.tv_ylthomepage_itemprivce);
                tvPrice.setText(getResources().getString(R.string.money) + item.mHousePrice);
                ImageView ivYltItemLeftImg = helper.getView(R.id.iv_ylthomepage_leftimg);
                ImageLoaderUtil.getInstance().loadImage(item.mProjectPicture, ivYltItemLeftImg, R.drawable.ic_ylt_productdefault);
            }
        });

        ListViewMeasureUtil.setListViewHeightBasedOnChildren(mLvYltProductList);

        mLvYltProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(getActivity(), position + " -- 条目被点击了", Toast.LENGTH_SHORT).show();
//                getActivity().startActivity(new Intent(getActivity(), ProjectDetailActivity.class));
                getActivity().startActivity(new Intent(getActivity(), ConfirmOrderActivity.class));
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }

    /**
     * 获取逸乐通产品数据
     */
    private void handleYltHomePageByNet() {
        mYltController.handleYltHomePageByNet(this, true, NetTag.GETYLTHOMEPAGE_CACHE, mParentInterface);
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

    /**
     * 获取逸乐通列表接口（产品列表数据，5条一次）
     */
    private void postHomeProductsDatas(int pages) {
        mYltController.handleYltProductListByNet(this, String.valueOf(mPageSize), String.valueOf(pages), true, NetTag.GETYLTPRODUCTS_CACHE, mParentInterface);
    }


    @Override
    protected void processMessage(Message msg) {
        switch (msg.what) {
            case REFRESH_YLT_HOMEPAGE:
                YltHomePageResponse objhomepage = (YltHomePageResponse) msg.obj;
                if (objhomepage != null && objhomepage.mDatas != null) {
                    //顶部数据
                    if (objhomepage.mDatas.mProductions != null) {
                        initFlowView(objhomepage.mDatas.mProductions);
                    }
                    //流程
//                    if (objhomepage.mDatas.mProcesses != null && objhomepage.mYltConfig.mProcesses.size() > 0) {
//                        ImageLoaderUtil.getInstance().loadImage("", iv_processes_view, R.drawable.image_onloading_homebig);
//                    }
                    RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);
                } else {
                    //异常处理代码
                    if (NetUtils.isNetConnected()) {
                        RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
                    } else {
                        RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_NONET, basicHandler);
                    }
                }
                break;
            case REFRESH_YLT_PRODUCTS://逸乐通产品列表
                YltProductResponse objpro = (YltProductResponse) msg.obj;

                if (null != objpro.mDataBeans && objpro.mDataBeans.size() > 0) {
                    if (objpro.mDataBeans.size() == mPageSize) {
                        mIsTheLastPage = false;
                    }

                    if (!mIsTheLastPage) {//未到达页尾才加入列表
                        if (objpro.isCache) {
                            mQuickAdapter.addAll(objpro.mDataBeans);
                        } else {
                            if (mPage == 1) {
                                mDataList.clear();
                            }
                            mDataList.addAll(objpro.mDataBeans);
                            mQuickAdapter.replaceAll(mDataList);
                        }
                    }

                    //异常处理代码
                    RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);
                } else {//如果返回数据的长度为0 或者小于pagesize 则视为到达页尾
                    if (objpro.mDataBeans.size() == 0) {//如果返回数据的长度为0  说明page多加了1
                        mPage--;
                    }
                    mIsTheLastPage = true;
                    //异常处理代码
                    if (NetUtils.isNetConnected()) {
                        RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
                    } else {
                        RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_NONET, basicHandler);
                    }
                }
                mQuickAdapter.notifyDataSetChanged();
                ListViewMeasureUtil.setListViewHeightBasedOnChildren(mLvYltProductList);
                break;
        }
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_BigIV_right:
            case R.id.ll_common_title_TV_right:
                if (sharewindow == null) {
                    sharewindow = new ShareWindow(getActivity());
                }
                sharewindow.alertPopupwindow(getActivity().findViewById(R.id.topLayout));
                break;
        }
    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {
        if (interfaceAction.equals(NetTag.GETYLTHOMEPAGE)) {
            basicHandler.obtainMessage(REFRESH_YLT_HOMEPAGE, result).sendToTarget();
        } else if (interfaceAction.equals(NetTag.GETYLTPRODUCTS)) {
            basicHandler.obtainMessage(REFRESH_YLT_PRODUCTS, result).sendToTarget();
        }
        if (mDefaultRefreshLister != null) {
            mDefaultRefreshLister.notifyhandledata(true, mPtrYlthomeRefreshlayout);
        }
    }


    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean hadCaChed) {
        if (mDefaultRefreshLister != null) {
            mDefaultRefreshLister.notifyhandledata(false, mPtrYlthomeRefreshlayout);
        }

        if (NetUtils.isNetConnected()) {
            if (hadCaChed) {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);//有网友缓存
            } else {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);//有网无数据
            }
        } else {
            if (hadCaChed) {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);//无网有缓存
            } else {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_NONET, basicHandler);//无网无数据
            }
        }
    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }

    /**
     * 初始化轮播图
     */
    private void initFlowView(final List<YltHomePageResponse.DatasBean.ProductionsBean> imageAdList) {
//        if (imageAdList == null) {
//            return;
//        }
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getScreenHeight(getActivity()) * 3 / 10);
        mImageCycleView.setLayoutParams(cParams);
        ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                /**实现点击事件*/
//                if (imageAdList != null && imageAdList.size() > 0) {
//                    YltHomePageResponse.DatasBean.ProductionsBean productionsBean = imageAdList.get(position);
//                    if (productionsBean != null && "project".equals(productionsBean.mField)) {
//                        getActivity().startActivity(new Intent(getActivity(), ProjectDetailActivity.class));
//
//                    } else if (bannersBean != null && "link".equals(bannersBean.mField) && !StringUtils.isNull(bannersBean.mFieldValue)) {
//                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                        intent.putExtra("url", bannersBean.mFieldValue);
//                        getActivity().startActivity(intent);
//                    }
//                }
            }

            @Override
            public View initContentView(Object item, ViewGroup container, int position) {
                View mView = View.inflate(getActivity(), R.layout.widget_adcycle_imageitem_view, null);
                ImageView iv_adcycle_view = (ImageView) mView.findViewById(R.id.iv_adcycle_view);
                TextView tv_adcycle_title = (TextView) mView.findViewById(R.id.tv_adcycle_title);
                TextView tv_adcycle_value = (TextView) mView.findViewById(R.id.tv_adcycle_value);
                if (item != null) {
                    YltHomePageResponse.DatasBean.ProductionsBean itemtemp = (YltHomePageResponse.DatasBean.ProductionsBean) item;
                    String imageURL = itemtemp.mInforurl;
//                    String[] split = itemtemp.mInfotitle.split(",");
//                    if(split.length>=2) {
//                        tv_adcycle_title.setText(split[0]);
//                        tv_adcycle_value.setText(split[1]);
//                    }
                    ImageLoaderUtil.getInstance().loadImage(imageURL, iv_adcycle_view, R.drawable.image_onloading_homebig);
                } else {
                    ImageLoaderUtil.getInstance().loadImage(null, iv_adcycle_view, R.drawable.image_onloading_homebig);
                }
                return mView;
            }
        };
        /**设置数据*/
        mImageCycleView.setImageResources((ArrayList<YltHomePageResponse.DatasBean.ProductionsBean>) imageAdList, mAdCycleViewListener);
        mImageCycleView.startImageCycle();
    }


}
