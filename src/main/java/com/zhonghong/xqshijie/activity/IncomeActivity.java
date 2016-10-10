package com.zhonghong.xqshijie.activity;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jingchen.pulltorefresh.DefaultRefreshLister;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.RefreshUtils;
import com.jingchen.pulltorefresh.parent.ParentFrameLayout;
import com.jingchen.pulltorefresh.pullableview.PullableScrollView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.MyWalletController;
import com.zhonghong.xqshijie.data.bean.IncomeTransRecordBean;
import com.zhonghong.xqshijie.data.response.MyWalletResponse;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.ListViewMeasureUtil;
import com.zhonghong.xqshijie.util.NetUtils;
import com.zhonghong.xqshijie.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zg on 2016/6/30.
 * 收益界面
 */
public class IncomeActivity extends BaseActivity implements NetInterface {
    private static final int MY_WALLET = 0;
    //标题栏
    private TitleView mTitle;
    private ListView mListView;
    private TextView mTvXQB;//新奇币
    private TextView mTvCashBonuses;//现金奖励
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private ScrollView mScrollView;
    private QuickAdapter<MyWalletResponse.Cashs> mQuickAdapter;
    private PullToRefreshLayout mPtrMyOrderRefreshlayout = null;
    private MyWalletController mMyWalletController;
    private PullableScrollView mPullableScrollView;
    private DefaultRefreshLister mDefaultRefreshLister = null;
    private ParentFrameLayout mParentInterface;
    //交易列表信息
    private List<IncomeTransRecordBean> mIncomeTransRecordBeanList = new ArrayList<>();
    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_income, null);
        mParentInterface = (ParentFrameLayout) contentView.findViewById(R.id.parent_my_wallet_content);
        mParentInterface.setReLoadOnclickListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_SHOWLOADING, null);
            }
        }, RefreshUtils.NetState.NET_NULL);
        initView(contentView);
        mTitle.setLeftImageOnClickListener(this);
        //下拉刷新及上拉加载的回调函数
        mPtrMyOrderRefreshlayout.setOnRefreshListener(mDefaultRefreshLister = new DefaultRefreshLister(IncomeActivity.this) {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                super.onRefresh(pullToRefreshLayout);
                if (havaNetWork) {
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
                mDefaultRefreshLister.notifynonetwork(pullToRefreshLayout);
            }
        });
//        mPullableScrollView.smoothScrollTo(0, 0);
        return contentView;
    }

    private void initView(View contentView) {
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mListView = (ListView) contentView.findViewById(R.id.lv_transaction_record);
        mTvXQB = (TextView) contentView.findViewById(R.id.tv_income_xqb_count);
        mTvCashBonuses = (TextView) contentView.findViewById(R.id.tv_cash_bonuses);
        mPtrMyOrderRefreshlayout = ((PullToRefreshLayout) contentView.findViewById(R.id.ptr_my_order_refreshlayout));
        mPullableScrollView = (PullableScrollView) contentView.findViewById(R.id.scv_ylt_refresh);

    }

    /**
     * 网络请求获取数据（产品列表数据，5条一次）
     */
    private void initData() {
        if (mMyWalletController == null) {
            mMyWalletController = new MyWalletController(this);
        }
        mMyWalletController.handleMyOrderByNet(this, 987+"");
    }

    @Override
    public void handleCreate() {
        initData();
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

    @Override
    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what){
            case MY_WALLET:
                MyWalletResponse myWalletResponse = (MyWalletResponse) msg.obj;
                if(myWalletResponse==null){

                    return;
                }
                mTvXQB.setText(myWalletResponse.mCount);
                mListView.setAdapter(mQuickAdapter = new QuickAdapter<MyWalletResponse.Cashs>(this, R.layout.adapter_income_transaction_record, myWalletResponse.mCashs) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, MyWalletResponse.Cashs item) {
                        helper.setText(R.id.tv_income_count, getResources().getString(R.string.money)+item.mMoney)
                                .setText(R.id.tv_income_time, item.mCreateTime);
                    }
                });
                ListViewMeasureUtil.setListViewHeightBasedOnChildren(mListView);
                break;
        }
        RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);//异常处理 加载完毕时

    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {
        if (mDefaultRefreshLister != null) {
            mDefaultRefreshLister.notifyhandledata(true, mPtrMyOrderRefreshlayout);//这句是核心代码  true代表成功 , false代表失败，若有其他状态我可以覆盖一个带字符串的参数来分别识别不同状态的回调
        }
        basicHandler.obtainMessage(MY_WALLET, result).sendToTarget();
    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
//        mListView.setVisibility(View.GONE);
        if (mParentInterface != null) {
            if (NetUtils.isNetConnected()) {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
            } else {
                RefreshUtils.AlertWhichParentlayout(mParentInterface, RefreshUtils.NetState.NET_NONET, basicHandler);
            }
        }
    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }
}
