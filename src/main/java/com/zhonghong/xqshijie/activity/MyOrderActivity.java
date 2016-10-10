package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jingchen.pulltorefresh.DefaultRefreshLister;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.RefreshUtils;
import com.jingchen.pulltorefresh.parent.ParentFrameLayout;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.adapter.MyOrderListAdapter;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.MyOrderListController;
import com.zhonghong.xqshijie.data.response.MyOrderResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.ListViewMeasureUtil;
import com.zhonghong.xqshijie.util.NetUtils;
import com.zhonghong.xqshijie.widget.TitleView;
import com.zhonghong.xqshijie.widget.countdown.CountDownTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zg on 2016/7/1.
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements AdapterView.OnItemClickListener, NetInterface {
    //标题栏
    public static final String ORDER_BEAN = "orderbean";
    private static final int MY_ORDER_INFO = 0;
    private int mPageSize = 2;
    private int mPage = 1;
    private ListView mListView;
    private TitleView mTitle;
    private MyOrderListAdapter mMyOrderListAdapter;
    private List<MyOrderResponse.OrderBean> mOrderInfoList = new ArrayList<>();
    private PullToRefreshLayout mPtrMyOrderRefreshlayout = null;
    private MyOrderListController mMyOrderListController;
    private DefaultRefreshLister mDefaultRefreshLister = null;
    private CountDownTask mCountDownTask;
    private ParentFrameLayout mParentInterface;
    private boolean mIsTheLastPage = false;//判断接口分页返回的数据是否到尾页

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_my_order, null);
        mParentInterface = (ParentFrameLayout) contentView.findViewById(R.id.parent_my_order_content);
        mParentInterface.setReLoadOnclickListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_SHOWLOADING, null);
            }
        }, RefreshUtils.NetState.NET_NULL);
        initView(contentView);
        initListener();
        initData();
        return contentView;
    }

    private void initListener() {
        mTitle.setLeftImageOnClickListener(this);
        //下拉刷新及上拉加载的回调函数
        mPtrMyOrderRefreshlayout.setOnRefreshListener(mDefaultRefreshLister = new DefaultRefreshLister(MyOrderActivity.this) {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                super.onRefresh(pullToRefreshLayout);
                if (havaNetWork) {
                    mPage = 1;
                    initData();
                } else {
                    if (mDefaultRefreshLister != null) {
                        mDefaultRefreshLister.notifynonetwork(pullToRefreshLayout);
                    }
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                super.onLoadMore(pullToRefreshLayout);
                if (havaNetWork) {
                    if (!mIsTheLastPage) {
                        mPage++;
                        initData();
                    } else {
                        mDefaultRefreshLister.notifyhandledata(true, mPtrMyOrderRefreshlayout);//这句是核心代码  true代表成功 , false代表失败，若有其他状态我可以覆盖一个带字符串的参数来分别识别不同状态的回调
                    }
                } else {
                    if (mDefaultRefreshLister != null) {
                        mDefaultRefreshLister.notifynonetwork(pullToRefreshLayout);
                    }
                    RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_NONET, basicHandler);
                }
            }
        });
        mListView.setOnItemClickListener(this);
        mMyOrderListAdapter = new MyOrderListAdapter(this);
        mListView.setAdapter(mMyOrderListAdapter);
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                // TODO Auto-generated method stub
//                isInit = true;
//                switch (scrollState) {
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 滑动停止
//                        for (; start_index < end_index; start_index++) {
//                            startCountDown();
//                        }
//                        startCountDown();
//                        break;
//                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                        cancelCountDown();
//                        break;
//                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                        cancelCountDown();
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//                // TODO Auto-generated method stub
//                // 设置当前屏幕显示的起始index和结束index
//                start_index = firstVisibleItem;
//                end_index = firstVisibleItem + visibleItemCount;
//
//            }
//        });

    }
//    private int start_index, end_index;

    // 判断是否是初始化
//    private boolean isInit = false;
    private void initView(View contentView) {
        mListView = (ListView) contentView.findViewById(R.id.lv_my_order);
//        netStateByNetNoNetLayout = (NetStateByNetNoNetLayout) contentView.findViewById(R.id.netstate_nonet_layout);
        mPtrMyOrderRefreshlayout = ((PullToRefreshLayout) contentView.findViewById(R.id.ptr_my_order_refreshlayout));
        mTitle = (TitleView) contentView.findViewById(R.id.title);
    }

    /**
     * 网络请求获取数据（产品列表数据，5条一次）
     */
    private void initData() {
        if (mMyOrderListController == null) {
            mMyOrderListController = new MyOrderListController(this);
        }
//        this.showProgressDialog(R.string.loading, false);//显示进度框
        mMyOrderListController.handleMyOrderByNet(this, mPageSize + "", mPage + "");
    }

    @Override
    protected void processMessage(Message msg) {
        super.processMessage(msg);
//        MyOrderActivity.this.hideProgressDialog();//隐藏进度框
        MyOrderResponse myOrderResponse = (MyOrderResponse) msg.obj;
        mOrderInfoList = myOrderResponse.mOrderBean;
        mOrderInfoList.addAll(mOrderInfoList);
        if (null != mOrderInfoList && mOrderInfoList.size() > 0) {
//            for (int i = 0; i < mOrderInfoList.size(); i++) {
//                if(i==0){
//                    mOrderInfoList.get(i).mOrderTatus="102";
//                    mOrderInfoList.get(i).mCreateTime = "1468981387000";
//                }
//                if(i==1){
//                    mOrderInfoList.get(i).mOrderTatus="21";
//                    mOrderInfoList.get(i).mCreateTime = "1468981387000";
//                }
//            }

            if (mOrderInfoList.size() == mPageSize) {
                mIsTheLastPage = false;
            } else {
                mIsTheLastPage = true;
            }
            if (mPage == 1) {
                mMyOrderListAdapter.setNewList(mOrderInfoList);
            } else {
                mMyOrderListAdapter.addAll(mOrderInfoList);
            }
            ListViewMeasureUtil.setListViewHeightBasedOnChildren(mListView);
            RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);
        } else {
            if (mOrderInfoList.size() == 0) {//如果返回数据的长度为0  说明page多加了1
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


    }

    @Override
    public void handleCreate() {
        RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_SHOWLOADING, null);
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

    private void refreshOnlineStatus() {
        Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MyOrderDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER_BEAN, mMyOrderListAdapter.getItem(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {
        if (mDefaultRefreshLister != null) {
            mDefaultRefreshLister.notifyhandledata(true, mPtrMyOrderRefreshlayout);//这句是核心代码  true代表成功 , false代表失败，若有其他状态我可以覆盖一个带字符串的参数来分别识别不同状态的回调
        }
        basicHandler.obtainMessage(MY_ORDER_INFO, result).sendToTarget();
    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
//        MyOrderActivity.this.hideProgressDialog();//隐藏进度框
//        mListView.setVisibility(View.GONE);

        if (mParentInterface != null) {
            if (NetUtils.isNetConnected()) {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
            } else {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_NONET, basicHandler);
            }


        }


        if (mDefaultRefreshLister != null) {
            mDefaultRefreshLister.notifyhandledata(false, mPtrMyOrderRefreshlayout);
            //这句是核心代码  true代表成功 , false代表失败，若有其他状态我可以覆盖一个带字符串的参数来分别识别不同状态的回调
        }
    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        startCountDown();
    }

    private void startCountDown() {
        mCountDownTask = CountDownTask.create();
        mMyOrderListAdapter.setCountDownTask(mCountDownTask);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelCountDown();
    }

    private void cancelCountDown() {
        mMyOrderListAdapter.setCountDownTask(null);
        mCountDownTask.cancel();
    }


}
