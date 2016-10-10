package com.zhonghong.xqshijie.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingchen.pulltorefresh.DefaultRefreshLister;
import com.jingchen.pulltorefresh.RefreshUtils;
import com.jingchen.pulltorefresh.overscrollview.OverScrollView;
import com.jingchen.pulltorefresh.parent.ParentFrameLayout;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.adapter.ProjectSummaryAdapter;
import com.zhonghong.xqshijie.adapter.YLTProjectListAdapter;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.ProjectDescriptionrController;
import com.zhonghong.xqshijie.data.bean.ProductPhotoBean;
import com.zhonghong.xqshijie.data.bean.ProductPhotoMappingBean;
import com.zhonghong.xqshijie.data.response.ProjectDetailResponse;
import com.zhonghong.xqshijie.fragment.HomePageFragment;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.net.NetTag;
import com.zhonghong.xqshijie.util.AppUtils;
import com.zhonghong.xqshijie.util.DensityUtil;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;
import com.zhonghong.xqshijie.util.ListViewMeasureUtil;
import com.zhonghong.xqshijie.util.LogUtils;
import com.zhonghong.xqshijie.util.NetUtils;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.Flowlayout;
import com.zhonghong.xqshijie.widget.ListViewForScrollView;
import com.zhonghong.xqshijie.widget.ShareWindow;
import com.zhonghong.xqshijie.widget.SpecificationSelectionPopupWindow;
import com.zhonghong.xqshijie.widget.TitleView;
import com.zhonghong.xqshijie.widget.adcircleviewpager.ImageCycleView;
import com.zhonghong.xqshijie.widget.adcircleviewpager.ImageCycleViewListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ProjectDetailActivity extends BaseActivity implements NetInterface, UniversalVideoView.VideoViewCallback {
    public static final int REFLASH_PROJECTPAGE = 0x1001;//刷新
    public static final String PROJECT_INFO = "project_info";
    public static final String VIDEO_URL = "video_url";
    private Button mBtnProjectDetailBuyNow;
    private Button mBtnReadMore;
    private SpecificationSelectionPopupWindow mPopupWindow;
    private View mContentView;//整体内容布局，包含上下页
    private String mProjectId;
    private TextView mProjectName;//工程名
    private TextView mTvSaleStatus;//在售状态
    private TextView mTvProdeitalTureprice;//价格
    private TextView mTvProdeitalAddress;//项目地址
    private TextView mTvYLTPrice;//项目地址
    private ListView mLvProjectSummmary;//工程简介
    private ListViewForScrollView mLvYLTProjectList;//逸乐通项目列表
    private ProjectDetailResponse mProjectDetailResponse;
    private ProjectSummaryAdapter mProjectSummaryAdapter;
    private YLTProjectListAdapter mYLTProjectListAdapter;
    private OverScrollView mOverScrollView;
    private String mTelNumber;//电话号
    private LinearLayout mLlwrap;//标签包裹
    private TitleView mTitle;
    private LinearLayout mRlCall;
    private RelativeLayout mRlKnowMore;//了解更多
    //    private LinearLayout mLlPRodetailBottomBar;
    private LinearLayout mLlDescHeader;
    private String mCity;
    private String mAddress;
    private ImageView mIvAdvertisement;//图片
    private TextView mTvPicCount;//图片的个数
    private int mPicCount;
    private ImageView mIvPlay;
    private ImageView mIvVideoBg;
    ProjectDescriptionrController mProjectDescriptionrController;
    private ShareWindow sharewindow;
    View mBottomLayout;
    View mVideoLayout;
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;
    String path = "http://v.meituan.net/movie/videos/54b5ef29210142259b85810ceaa8a5a0.mp4";
    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    private int lastY = 0;
    private String mVideoUrl;
    private ImageCycleView mImageCycleView;
    private DefaultRefreshLister defaultRefreshLister = null;
    private ParentFrameLayout parentFrameLayout;
    private TextView mTextViewVideoDivision;
    //逸乐通产品上方的虚线
    private View mViewStatusLine;
    private LinearLayout mLinearLocation;
    private ImageView mIvCallUs;

    @Override
    public View initContentView() {
        mContentView = LayoutInflater.from(this).inflate(R.layout.activity_project_detail, null);
        initView(mContentView);
        return mContentView;
    }

    private void initView(View mContentView) {
        mTitle = (TitleView) mContentView.findViewById(R.id.title);
        mOverScrollView = (OverScrollView) mContentView.findViewById(R.id.scv_prodetail_refresh);
        mImageCycleView = (ImageCycleView) mContentView.findViewById(R.id.icv_banner_cycleView);

        mProjectName = (TextView) mContentView.findViewById(R.id.tv_projectdetail_proname);
        mTvSaleStatus = (TextView) mContentView.findViewById(R.id.tv_sale_status);
        mTvProdeitalTureprice = (TextView) mContentView.findViewById(R.id.tv_prodeital_tureprice);
        mTvProdeitalAddress = (TextView) mContentView.findViewById(R.id.tv_prodetail_address);
        mTvYLTPrice = (TextView) mContentView.findViewById(R.id.tv_ylt_price);

        mLvProjectSummmary = (ListView) mContentView.findViewById(R.id.lv_project_summary);

        mLvYLTProjectList = (ListViewForScrollView) mContentView.findViewById(R.id.lv_ylt_project_list);
        mLlwrap = (LinearLayout) mContentView.findViewById(R.id.ll_wrap);
        mVideoView = (UniversalVideoView) mContentView.findViewById(R.id.uvv_videoView);
        mMediaController = (UniversalMediaController) mContentView.findViewById(R.id.umc_media_controller);
        mVideoLayout = mContentView.findViewById(R.id.fl_video_layout);
        mVideoView.setMediaController(mMediaController);
        mMediaController.setFocusable(false);
        mIvPlay = (ImageView) mContentView.findViewById(R.id.iv_play_detail);
        mIvVideoBg = (ImageView) mContentView.findViewById(R.id.iv_video_bg);
        mTitle.setOnClickListener(this);
        mRlKnowMore = (RelativeLayout) mContentView.findViewById(R.id.rl_know_more);
        mLlDescHeader = (LinearLayout) mContentView.findViewById(R.id.ll_desc_header);
        mTextViewVideoDivision = (TextView) mContentView.findViewById(R.id.tv_video_division);
//        mLlPRodetailBottomBar = (LinearLayout) mContentView.findViewById(R.id.ll_prodetail_bottombar);
        mViewStatusLine = (View) mContentView.findViewById(R.id.view_status_line);
        mLinearLocation = (LinearLayout) mContentView.findViewById(R.id.rl_location);
        mIvCallUs = (ImageView) mContentView.findViewById(R.id.iv_call_us);
        mBtnReadMore = (Button) mContentView.findViewById(R.id.btn_read_more_detail);
        mLinearLocation.setOnClickListener(this);
        mIvCallUs.setOnClickListener(this);
        mBtnReadMore.setOnClickListener(this);
        mTextViewVideoDivision = (TextView) mContentView.findViewById(R.id.tv_video_division);
        //视频播放
        mIvPlay.setOnClickListener(this);
    }

    @Override
    public void handleCreate() {
        //异常处理布局
        parentFrameLayout = (ParentFrameLayout) mContentView.findViewById(R.id.parent_project_detail);
        RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_SHOWLOADING, null);
        parentFrameLayout.setReLoadOnclickListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_SHOWLOADING, null);//异常处理
                getData();
            }
        }, RefreshUtils.NetState.NET_NULL);
        //------------------------

        mRlKnowMore.setOnClickListener(this);
//        mRlCall.setOnClickListener(this);

        mProjectSummaryAdapter = new ProjectSummaryAdapter(this);
        mLvProjectSummmary.setAdapter(mProjectSummaryAdapter);
        //逸乐通产品
//        mYLTProjectListAdapter = new YLTProjectListAdapter(this);
//        mLvYLTProjectList.setAdapter(mYLTProjectListAdapter);
//        mBtnProjectDetailBuyNow = (Button) mContentView.findViewById(R.id.btn_project_detail_buy_now);

//        mBtnProjectDetailBuyNow.setOnClickListener(this);

        mTitle.setRightImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharewindow == null) {
                    sharewindow = new ShareWindow(ProjectDetailActivity.this);
                }
                // 显示窗口
                sharewindow.alertPopupwindow(ProjectDetailActivity.this.findViewById(R.id.activity_project_detail));
            }
        });
        //下拉刷新及上拉加载的回调函数
        mOverScrollView.setOnTouchListener(new View.OnTouchListener() {
            private int touchEventId = -9983761;
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    View scroller = (View) msg.obj;

                    if (msg.what == touchEventId) {
                        if (lastY == scroller.getScrollY()) {
                            //停止了，此处你的操作业务
                            System.out.println("停止了");
                        } else {
                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 1);
                            lastY = scroller.getScrollY();
                            System.out.println("停止了" + lastY);
                        }
                    }
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                int y = (int) event.getRawY();
                switch (eventAction) {
                    case MotionEvent.ACTION_UP:
                        handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mOverScrollView.smoothScrollTo(0, 0);
        getData();
    }

    private void getData() {
        mProjectId = getIntent().getStringExtra(HomePageFragment.PROJECT_ID);
        if (mProjectDescriptionrController == null) {
            mProjectDescriptionrController = new ProjectDescriptionrController(this);
        }
        mProjectDescriptionrController.handleMyOrderByNet(this, false, mProjectId, NetTag.GETPROPERTYATTR_CACHE);
    }


    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            //底部立即购买
//            case R.id.btn_project_detail_buy_now:
//                mPopupWindow = new SpecificationSelectionPopupWindow(this, HomePageFragment.PROJECT_ID);
////                    设置PopupWindow在layout中显示的位置
//                mPopupWindow.alertPopupwindow(this.findViewById(R.id.activity_project_detail));
//                break;
            //阅读更多
            case R.id.iv_call_us:
                AppUtils.showCallDialog(mContext, mTelNumber, mContext.getString(R.string.tel_msg).concat(mTelNumber).concat("?"), mContext.getResources().getString(R.string.btn_ok_sting), mContext.getResources().getString(R.string.btn_cancel_sting));
                break;
            case R.id.btn_read_more_detail:
                Intent intent = new Intent(ProjectDetailActivity.this, ProjectDescriptionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PROJECT_INFO, mProjectDetailResponse);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //底部拨打电话
//            case R.id.rl_prodetail_call:
//                AppUtils.showCallDialog(mContext, mTelNumber, mContext.getString(R.string.tel_msg).concat(mTelNumber).concat("?"), mContext.getResources().getString(R.string.btn_ok_sting), mContext.getResources().getString(R.string.btn_cancel_sting));
//                break;
            //头部返回
            case R.id.title:
                finish();
                break;
            //视频播放，调到子页面进行播放
            case R.id.iv_play_detail:
                intent = new Intent(this, VideoPlayerActivity.class);
                intent.putExtra(VIDEO_URL, mVideoUrl);
                startActivity(intent);
////                在本页面播放视频
//                setVideoAreaSize();
//                if (mSeekPosition > 0) {
//                    mVideoView.seekTo(mSeekPosition);
//                }
//                mIvVideoBg.setVisibility(View.GONE);
//                mVideoView.start();
////                mMediaController.setTitle("Big Buck Bunny");
//                mIvPlay.setVisibility(View.GONE);
//                if(!mVideoView.isPlaying()){
//                    mMediaController.show();
//                }
//                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        LogUtils.d("onCompletion ");
//                    }
//                });
                break;
            //定位
            case R.id.rl_location:
                intent = new Intent(ProjectDetailActivity.this, ProjectLocationActivity.class);
                intent.putExtra("ADDRESS", "朝阳区远洋国际E座");
                intent.putExtra("CITY", "北京");
                startActivity(intent);
                break;
            case R.id.rl_know_more:
                startActivity(new Intent(this, YltFragmentActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void processMessage(Message msg) {
        switch (msg.what) {
            case REFLASH_PROJECTPAGE:
                ProjectDetailResponse projectDetailResponse = (ProjectDetailResponse) msg.obj;
                this.mProjectDetailResponse = projectDetailResponse;
                refreshByHandle(projectDetailResponse);
                break;
        }
    }


    /**
     * 刷新UI
     *
     * @param mProjectDetailResponse
     */
    private void refreshByHandle(ProjectDetailResponse mProjectDetailResponse) {
        if (mProjectDetailResponse == null || mProjectDetailResponse.mProjectBuild == null) {
            mOverScrollView.setVisibility(View.GONE);
//            mLlPRodetailBottomBar.setVisibility(View.GONE);
            RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);//异常处理
            return;
        }
        //视频播放
        String url = mProjectDetailResponse.mProjectVideo.get(0).mProjectVideo;
        //测试以下
//        url = path;
//        mProjectDetailResponse.mProjectVideo.get(0).mProjectVideoPic = "resource/xqsjpc/images/index_2_0/banner1.jpg";
        //测试以上
        if (StringUtils.isNull(url) || url.isEmpty()) {
            //测试
            if (StringUtils.isNull(url) || "" == url) {
                mVideoLayout.setVisibility(View.GONE);
                mTextViewVideoDivision.setVisibility(View.GONE);
            } else {
                mVideoUrl = url;
                mTextViewVideoDivision.setHeight(DensityUtil.dip2px(ProjectDetailActivity.this, 30));
                mTextViewVideoDivision.setTextColor(this.getResources().getColor(R.color.white));
                ImageLoaderUtil.getInstance().loadImage(mProjectDetailResponse.mProjectVideo.get(0).mProjectVideoPic, mIvVideoBg, R.drawable.image_onloading_homebig);
            }
            //逸乐通 更多
            if (mProjectDetailResponse.mProjectYLTS == null || mProjectDetailResponse.mProjectYLTS.size() == 0) {
                mTextViewVideoDivision.setVisibility(View.VISIBLE);
                ImageLoaderUtil.getInstance().loadImage(mProjectDetailResponse.mProjectVideo.get(0).mProjectVideoPic, mIvVideoBg, R.drawable.image_onloading_homebig);
            }

            if (mProjectDetailResponse.mProjectYLTS != null && mProjectDetailResponse.mProjectYLTS.size() == 0) {
                mLlDescHeader.setVisibility(View.GONE);
                mRlKnowMore.setVisibility(View.GONE);
                mLvYLTProjectList.setVisibility(View.GONE);
                mViewStatusLine.setVisibility(View.GONE);
            }
            //获取图片数量
            mPicCount = getPicCount(mProjectDetailResponse.mProjectPhoto);
            if (mTvPicCount != null && mPicCount > 0) {
                mTvPicCount.setVisibility(View.VISIBLE);
                mTvPicCount.setText("1/" + mPicCount);
            } else if (mTvPicCount != null) {
                mTvPicCount.setVisibility(View.VISIBLE);
            }
            ProjectDetailResponse.ProjectInfo projectInfo = mProjectDetailResponse.mProjectInfo;
            mCity = projectInfo.mProjectCity;
            mAddress = projectInfo.mProjectAddress;
            //图片
            initFlowView(projectInfo.mProjectCover);
            mVideoLayout.setVisibility(View.GONE);
            mTextViewVideoDivision.setHeight(DensityUtil.dip2px(ProjectDetailActivity.this, 30));
            mTextViewVideoDivision.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
            mVideoUrl = url;
            ImageLoaderUtil.getInstance().loadImage(mProjectDetailResponse.mProjectVideo.get(0).mProjectVideoPic, mIvVideoBg, R.drawable.image_onloading_homebig);
        }
        //逸乐通 更多
        if (mProjectDetailResponse.mProjectYLTS == null || mProjectDetailResponse.mProjectYLTS.size() == 0) {
            mLlDescHeader.setVisibility(View.GONE);
            mRlKnowMore.setVisibility(View.GONE);
            mLvYLTProjectList.setVisibility(View.GONE);
            mViewStatusLine.setVisibility(View.GONE);
        }
        //获取图片数量
        mPicCount = getPicCount(mProjectDetailResponse.mProjectPhoto);
        if (mTvPicCount != null && mPicCount > 0) {
            mTvPicCount.setVisibility(View.VISIBLE);
            mTvPicCount.setText("1/" + mPicCount);
        } else if (mTvPicCount != null) {
            mTvPicCount.setVisibility(View.VISIBLE);
        }
        ProjectDetailResponse.ProjectInfo projectInfo = mProjectDetailResponse.mProjectInfo;
        mCity = projectInfo.mProjectCity;
        mAddress = projectInfo.mProjectAddress;
        //图片
        initFlowView(projectInfo.mProjectCover);

//        ImageLoaderUtil.getInstance().loadImage(projectInfo.mProjectCover, mIvAdvertisement, R.drawable.ic_ylt_productdefault);
        //工程名
        mProjectName.setText(projectInfo.mProjectName);
        //电话号
        mTelNumber = projectInfo.mProjectTel;
        //标签集合
        String labelString = projectInfo.mProjectLabel;
        addLabel(labelString);
        //在售状态
        if (projectInfo.mProjectStatus.equals("1")) {
            mTvSaleStatus.setText(getResources().getString(R.string.sale));
        } else {
            mTvSaleStatus.setText(getResources().getString(R.string.no_sale));
        }
        //均价
//        String averagePrice = getAveragePrice(mProjectDetailResponse.mProjectYLTS);
//        mTvProdeitalTureprice.setText(averagePrice);
        //逸乐通价格
        String lowestPirce = getLowestPrice(mProjectDetailResponse.mProjectYLTS);
        mTvYLTPrice.setText(lowestPirce);
        //项目地址
        mTvProdeitalAddress.setText(projectInfo.mProjectAddress);
        //项目简介
//        mTvSummary.setText(mProjectDetailResponse.mProjectBuild.get(0).mInfoName);
        List<ProjectDetailResponse.ProjectBuild.InfoData> list = mProjectDetailResponse.mProjectBuild.get(0).mInfoData;
        if (list != null && list.size() >= 4) {
            mProjectSummaryAdapter.setNewList(list.subList(0, 4));
        } else {
            mProjectSummaryAdapter.setNewList(list);
        }
        ListViewMeasureUtil.setListViewHeightBasedOnChildren(mLvProjectSummmary);
        //逸乐通产品
//        if (mProjectDetailResponse.mProjectYLTS != null && mProjectDetailResponse.mProjectYLTS.size() > 0) {
//            mYLTProjectListAdapter.setNewList(mProjectDetailResponse.mProjectYLTS);
//        }
        ViewTreeObserver vto = mOverScrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                // mOverScrollView.scrollTo(0, 0);
            }
        });
        this.hideProgressDialog();//隐藏进度框
        RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);//异常处理 加载完毕时
    }

    private int getPicCount(List<ProjectDetailResponse.ProjectPhoto> mProjectPhoto) {
        int count = 0;
        for (int i = 0; i < mProjectPhoto.size(); i++) {
            count += mProjectPhoto.get(i).mData.size();
        }
        return count;
    }

    /**
     * 添加标签
     *
     * @param labelString 标签的字符串
     */
    private void addLabel(String labelString) {
        List<String> labelList = new ArrayList<>();
        String[] labels = labelString.split(",");
        for (int i = 0; i < labels.length; i++) {
            labelList.add(labels[i]);
        }
        Flowlayout flowlayout = new Flowlayout(this);
//        int layoutUpDownPadding = DensityUtil.dip2px(this, 15);
//        int layoutRightLeftPadding = DensityUtil.dip2px(this, 10);
//        flowlayout.setPadding(layoutRightLeftPadding, layoutUpDownPadding, layoutRightLeftPadding, layoutUpDownPadding);

        for (int i = 0; i < labelList.size(); i++) {
            final TextView tv = new TextView(this);
            tv.setTextColor(getResources().getColor(R.color.text_blue));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tv.setGravity(Gravity.CENTER);
            tv.setText(labelList.get(i));
            int textPaddingV = DensityUtil.dip2px(this, 4);
            int textPaddingH = DensityUtil.dip2px(this, 10);
            tv.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);
            tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.common_btn_entity_orange));
            tv.setBackgroundColor(getResources().getColor(R.color.label_bg));
            flowlayout.addView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        mLlwrap.addView(flowlayout);
    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {
        basicHandler.obtainMessage(REFLASH_PROJECTPAGE, result).sendToTarget();
    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
        this.hideProgressDialog();//隐藏进度框
        if (NetUtils.isNetConnected()) {
            RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
        } else {
            RefreshUtils.AlertWhichParentlayout(parentFrameLayout, RefreshUtils.NetState.NET_NONET, basicHandler);
        }
    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }

    /**
     * 获取均价
     *
     * @param mProjectYLTS
     * @return
     */
    public String getAveragePrice(List<ProjectDetailResponse.ProjectYLTS> mProjectYLTS) {
        List<Integer> priceList = new ArrayList<>();
        Double sumPrice = 0.00;
        if (mProjectYLTS != null && mProjectYLTS.size() > 0) {
            for (int i = 0; i < mProjectYLTS.size(); i++) {
                sumPrice += Double.parseDouble(mProjectYLTS.get(i).mHousePrice);
            }
        }
        return sumPrice / mProjectYLTS.size() + "";
    }

    /**
     * 获取最低价格
     *
     * @param mProjectYLTS
     * @return
     */
    public String getLowestPrice(List<ProjectDetailResponse.ProjectYLTS> mProjectYLTS) {
        String min = "";
        if (mProjectYLTS != null && mProjectYLTS.size() > 0) {
            Collections.sort(mProjectYLTS, new SortByHousePrice());
            ProjectDetailResponse.ProjectYLTS bean = mProjectYLTS.get(0);
            if (bean != null) {
                min = bean.mHousePrice;
            }
        }
        return StringUtils.repNull(min);

    }

    class SortByHousePrice implements Comparator<Object> {
        public int compare(Object o1, Object o2) {
            if (o1 != null && o2 != null) {
                ProjectDetailResponse.ProjectYLTS s1 = (ProjectDetailResponse.ProjectYLTS) o1;
                ProjectDetailResponse.ProjectYLTS s2 = (ProjectDetailResponse.ProjectYLTS) o2;
                return s1.mHousePrice.compareTo(s2.mHousePrice);
            }
            return -1;
        }
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {

        this.isFullscreen = isFullscreen;

        if (isFullscreen) {
//            mViewBg.setVisibility(View.GONE);
            mTitle.setVisibility(View.GONE);
//            mLlPRodetailBottomBar.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);

        } else {
            mTitle.setVisibility(View.VISIBLE);
//            mViewBg.setVisibility(View.VISIBLE);
//            mLlPRodetailBottomBar.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
//            mOverScrollView.smoothScrollTo(0,300);
            ViewTreeObserver vto = mOverScrollView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    mOverScrollView.scrollTo(0, lastY);
                }
            });
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) { // Video pause

    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                //cachedHeight = (int) (width * 3f / 4f);
                //cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(path);
                mVideoView.requestFocus();
            }
        });
    }

    //返回键时
    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("onPause");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            LogUtils.d("onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }

    /**
     * 初始化轮播图
     */
    private void initFlowView(final String imageUrl) {
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getScreenHeight(this) * 3 / 10);
        mImageCycleView.setLayoutParams(cParams);
        ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                Intent i = new Intent(ProjectDetailActivity.this, ProjectPhotoActivity.class);
//                testdatainit(i);
                i.putExtra(HomePageFragment.PROJECT_ID, mProjectId);
                startActivity(i);
            }

            @Override
            public View initContentView(Object item, ViewGroup container, int position) {
                View mView = View.inflate(ProjectDetailActivity.this, R.layout.widget_adcycle_imageitem_view, null);
                ImageView iv_adcycle_view = (ImageView) mView.findViewById(R.id.iv_adcycle_view);
                mTvPicCount = (TextView) mView.findViewById(R.id.tv_pic_count);
                if (item != null) {
                    String imageURL = (String) item;
                    ImageLoaderUtil.getInstance().loadImage(imageURL, iv_adcycle_view, R.drawable.image_onloading_homebig);
                } else {
                    ImageLoaderUtil.getInstance().loadImage(null, iv_adcycle_view, R.drawable.image_onloading_homebig);
                }
                return mView;
            }
        };
        /**设置数据*/
        List<String> list = new ArrayList<>();
        if (!StringUtils.isNull(imageUrl)) {
            list.add(imageUrl);
        }
        mImageCycleView.setImageResources((ArrayList<String>) list, mAdCycleViewListener);
        mImageCycleView.startImageCycle();
    }

    /**
     * 造假数据，这里先注掉，等版本测试通过之后再删掉吧
     */
    private void testdatainit(Intent i) {
        List<ProductPhotoBean> datastype1 = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            datastype1.add(new ProductPhotoBean("天津·四合院" + j, PublicUtils.testurl_1));
            datastype1.add(new ProductPhotoBean("北京·六合院" + j, PublicUtils.testurl_2));
        }
        List<ProductPhotoBean> datastype2 = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            datastype2.add(new ProductPhotoBean("北京·朝阳门" + j, PublicUtils.testurl_5));
            datastype2.add(new ProductPhotoBean("北京·朝阳门" + j, PublicUtils.testurl_10));
        }

        List<ProductPhotoBean> datastype3 = new ArrayList<>();
        for (int j = 0; j < 0; j++) {
            datastype3.add(new ProductPhotoBean("黑龙江·远啊", PublicUtils.testurl_6));
            datastype3.add(new ProductPhotoBean("湖北·黄土高坡", PublicUtils.testurl_7));
            datastype3.add(new ProductPhotoBean("湖南·九合院", PublicUtils.testurl_8));
            datastype3.add(new ProductPhotoBean("湖南·九合院", PublicUtils.testurl_11));
            datastype3.add(new ProductPhotoBean("湖南·九合院", PublicUtils.testurl_12));
            datastype3.add(new ProductPhotoBean("湖南·九合院", PublicUtils.testurl_13));
            datastype3.add(new ProductPhotoBean("湖南·九合院", PublicUtils.testurl_14));
        }
        List<ProductPhotoBean> datastype4 = new ArrayList<>();
        for (int j = 0; j < 0; j++) {
            datastype4.add(new ProductPhotoBean("湖南·九合院", PublicUtils.testurl_11));
            datastype4.add(new ProductPhotoBean("湖南·九合院", PublicUtils.testurl_12));
            datastype4.add(new ProductPhotoBean("湖南·九合院", PublicUtils.testurl_13));
            datastype4.add(new ProductPhotoBean("湖南·九合院", PublicUtils.testurl_14));
        }

        List<ProductPhotoBean> datastype5 = new ArrayList<>();
        for (int j = 0; j < 0; j++) {
            datastype5.add(new ProductPhotoBean("湖南·九合院美女", PublicUtils.testurl_11));
            datastype5.add(new ProductPhotoBean("湖南·九美女合院", PublicUtils.testurl_12));
            datastype5.add(new ProductPhotoBean("湖南·九合美女院", PublicUtils.testurl_13));
            datastype5.add(new ProductPhotoBean("湖南·九合美女院", PublicUtils.testurl_14));
        }
        List<ProductPhotoBean> datastype6 = new ArrayList<>();
        for (int j = 0; j < 0; j++) {
            datastype6.add(new ProductPhotoBean("66湖南·九合院美女", PublicUtils.testurl_11));
            datastype6.add(new ProductPhotoBean("66湖南·九美女合院", PublicUtils.testurl_12));
            datastype6.add(new ProductPhotoBean("湖66南·九合美女院", PublicUtils.testurl_13));
            datastype6.add(new ProductPhotoBean("湖南66·九合美女院", PublicUtils.testurl_14));
        }

        List<ProductPhotoBean> datastype7 = new ArrayList<>();
        for (int j = 0; j < 0; j++) {
            datastype7.add(new ProductPhotoBean("湖南·九7合院美女", PublicUtils.testurl_11));
            datastype7.add(new ProductPhotoBean("湖南·九美女7合院", PublicUtils.testurl_12));
            datastype7.add(new ProductPhotoBean("湖南·九7合美女院", PublicUtils.testurl_13));
            datastype7.add(new ProductPhotoBean("湖南·九合美7女院", PublicUtils.testurl_14));
        }

        ProductPhotoMappingBean one = new ProductPhotoMappingBean(PublicUtils.getPhotoNameByIndex(1), datastype1, "1");
        ProductPhotoMappingBean two = new ProductPhotoMappingBean(PublicUtils.getPhotoNameByIndex(2), datastype2, "2");
        ProductPhotoMappingBean three = new ProductPhotoMappingBean(PublicUtils.getPhotoNameByIndex(3), datastype3, "3");
        ProductPhotoMappingBean four = new ProductPhotoMappingBean(PublicUtils.getPhotoNameByIndex(4), datastype4, "4");
        ProductPhotoMappingBean five = new ProductPhotoMappingBean(PublicUtils.getPhotoNameByIndex(5), datastype5, "5");
        ProductPhotoMappingBean six = new ProductPhotoMappingBean(PublicUtils.getPhotoNameByIndex(6), datastype6, "6");
        ProductPhotoMappingBean seven = new ProductPhotoMappingBean(PublicUtils.getPhotoNameByIndex(7), datastype7, "7");

        ArrayList<ProductPhotoMappingBean> datalists = new ArrayList<>();
        datalists.add(one);
        datalists.add(two);
//        datalists.add(three);
//        datalists.add(four);
//        datalists.add(five);
//        datalists.add(six);


//        datalists.add(seven);

        i.putExtra("datas", (Serializable) datalists);
    }

}
