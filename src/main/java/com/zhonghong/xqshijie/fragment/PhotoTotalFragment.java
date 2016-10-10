package com.zhonghong.xqshijie.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.RadioButton;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.adapter.PhotoTotalFragmentListAdapter;
import com.zhonghong.xqshijie.data.bean.ProductPhotoBean;
import com.zhonghong.xqshijie.data.bean.ProductPhotoMappingBean;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.widget.PhotoListView;
import com.zhonghong.xqshijie.widget.TitleView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xiezl on 16/6/14.
 */
public class PhotoTotalFragment extends Fragment {
    public static int outsideclickpositon = -1;//用于解决listview onscroll的问题
    private PublicUtils.photoCallBack mCallBack = null;
    private TitleView mTitleView;
    private PhotoListView mListview;//这个列表是用来装模块的 一个模块一个title+recyclerview+一个tab
    private ArrayList<ProductPhotoMappingBean> mTabDatas = null;
    private List<RadioButton> mRbtnLists = null;
    private PhotoTotalFragmentListAdapter photoTotalFragmentListAdapter = null; //outside listview adapter
    private int mTotalsize = 0;
    private PublicUtils.photoOutsideTabCallBack mActivityCallBack = null;
    private ArrayList<ProductPhotoBean> totallists = null;
    private int mStatusBarHeight;//状态栏
    private int mBottomHeight;//底
    private int mTitleHeight;//顶
    private int mListHeight;//列表高
    private int mScreenHeight;//屏幕高

    public PublicUtils.photoOutsideTabCallBack getActivityCallBack() {
        return mActivityCallBack;
    }

    public void setActivityCallBack(PublicUtils.photoOutsideTabCallBack activityCallBack) {
        this.mActivityCallBack = activityCallBack;
    }

    public PhotoTotalFragment(List<RadioButton> rBtnLists, ArrayList<ProductPhotoMappingBean> mTabDatas, ArrayList<ProductPhotoBean> totallists) {
        this.mRbtnLists = rBtnLists;
        this.mTabDatas = mTabDatas;
        this.totallists = totallists;
        if (this.totallists != null) {
            mTotalsize = this.totallists.size();
        }
    }

    public PublicUtils.photoCallBack getmCallBack() {
        return mCallBack;
    }

    public void setmCallBack(PublicUtils.photoCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    /**
     * 切换到某一页 用于和外部tab联动
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void swichPages(int pageno) {
        if (mListview != null) {
            mListview.setSelectionFromTop(pageno, -15);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photototal, null);
        mTitleView = (TitleView) view.findViewById(R.id.total_title);
        mTitleView.setTitleLineColor(getResources().getColor(R.color.black));
        mTitleView.setTvCenterTextColor(getResources().getColor(R.color.white));
        mTitleView.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.switchPageOne();
            }
        });
        mListview = (PhotoListView) view.findViewById(R.id.lv_photototal_list);
        photoTotalFragmentListAdapter = new PhotoTotalFragmentListAdapter(getActivity(), this.mActivityCallBack);
        photoTotalFragmentListAdapter.setDatas(mTabDatas);
        photoTotalFragmentListAdapter.notifyDataSetChanged();
        mListview.setAdapter(photoTotalFragmentListAdapter);
        mTitleView.setTitle("共" + mTotalsize + "张");

        handleUiheight();

        mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mListHeight >= (2 * (mScreenHeight - (mTitleHeight + mStatusBarHeight + mBottomHeight)))) {//屏幕大于俩屏时再启动判断
                    if (isLastItemVisible()) {
                        mRbtnLists.get(mRbtnLists.size() - 1).setChecked(true);
                    } else {
                        mRbtnLists.get(firstVisibleItem).setChecked(true);
                    }
                }
                Log.e("firstVisibleItem == ", "" + firstVisibleItem);
            }
        });
        return view;
    }

    /**
     * 根据高度来进行底部按钮刷新策略
     * */
    private void handleUiheight() {
        mStatusBarHeight = getStatusHeight(getActivity());
        mTitleHeight = PublicUtils.getWH(mTitleView, "h");
        mListHeight = PublicUtils.getLvTotalHeight(mListview);
        mBottomHeight = mTitleHeight;
        mScreenHeight = getScreenHeight(getActivity());
    }


    //高度计算
    private int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    private int getScreenHeight(Context context) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 判断最后listView中最后一个item是否完全显示出来
     */
    protected boolean isLastItemVisible() {
        final Adapter adapter = mListview.getAdapter();
        if (null == adapter || adapter.isEmpty()) {
            return true;
        }
        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mListview.getLastVisiblePosition();
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - mListview.getFirstVisiblePosition();
            final int childCount = mListview.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mListview.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mListview.getBottom();
            }
        }
        return false;
    }

}
