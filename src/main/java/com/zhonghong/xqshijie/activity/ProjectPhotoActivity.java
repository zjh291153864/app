package com.zhonghong.xqshijie.activity;

import android.os.Build;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jingchen.pulltorefresh.RefreshUtils;
import com.jingchen.pulltorefresh.parent.ParentFrameLayout;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.adapter.CommonFragmentPagerAdapter;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.PhotoController;
import com.zhonghong.xqshijie.data.bean.ProductPhotoBean;
import com.zhonghong.xqshijie.data.bean.ProductPhotoMappingBean;
import com.zhonghong.xqshijie.data.response.PhotoDataResponse;
import com.zhonghong.xqshijie.fragment.PhotoDetailFragment;
import com.zhonghong.xqshijie.fragment.PhotoTotalFragment;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.NetUtils;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.SystemStatusManager;
import com.zhonghong.xqshijie.widget.NoSlideViewPager;
import com.zhonghong.xqshijie.widget.ScrollViewCustom;

import java.util.ArrayList;
import java.util.List;

public class ProjectPhotoActivity extends BaseActivity {

    public static final int REFRESH_PHOTO = 0x1374;//刷新首页
    private NoSlideViewPager mVpProjectPhotoOutside = null;//外围viewpager
    private ArrayList<ProductPhotoMappingBean> mDataBeans = null;
    private PhotoDetailFragment mPhotoDetailFragment = null;
    private PhotoTotalFragment mPhotoTotalFragment = null;
    private ArrayList<ProductPhotoBean> totalDatas = null;
    private ScrollViewCustom mSrvBottomBarCenber = null;
    private RadioGroup mBottomBarCenterGroup = null;
    private ArrayList<RadioButton> mRbtnList;//保存radiubutton的集合
    private ArrayList<ProductPhotoBean> mTotalList;//总数据
    private View mIvLeftarr;
    private View mIvRightarr;
    private String mProJectId;
    private PhotoController mPcontroller;
    private ParentFrameLayout mParentPhotoRoot;

    @Override

    public View initContentView() {
        setTranslucentStatus();
        View inflateView = View.inflate(this, R.layout.activity_project_photo, null);
        initView(inflateView);
        return inflateView;
    }

    /**
     * 底部条的处理
     */
    private void initBottomBar() {
        Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);
        final int screenHalf = d.getWidth() / 2;//屏幕宽度的一半
        mBottomBarCenterGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int scrollX = mSrvBottomBarCenber.getScrollX();
                RadioButton currRadioButton = (RadioButton) group.findViewById(checkedId);
                int left = currRadioButton.getLeft();
                int leftScreen = left - scrollX;
                mSrvBottomBarCenber.smoothScrollBy((leftScreen - screenHalf), 0);
            }
        });
    }

    /**
     * 添加底部控制器按钮
     */
    private void addBottomBarButton() {
        boolean firstButtonCheck = true;
        mTotalList = new ArrayList<>();//拼接总数据
        int lastsize = 0;
        if (mDataBeans != null) {
            for (ProductPhotoMappingBean productPhotoMappingBean : mDataBeans) {
                if (productPhotoMappingBean != null && productPhotoMappingBean.getData().size() > 0) {
                    List<ProductPhotoBean> currData = productPhotoMappingBean.getData();
                    int currsize = currData.size();//递增
                    int count = 1;
                    for (ProductPhotoBean data : currData) {
                        data.setIndexInTotalList(String.valueOf(count + lastsize));
                        count++;
                    }
                    mTotalList.addAll(currData);
                    final int visibleIndex = Integer.parseInt(productPhotoMappingBean.getType()) - 1;//跟类型定义一样并转成int类型
                    RadioButton visibleRadioButton = mRbtnList.get(visibleIndex);

                    //头次初始化的radiobutton设置为true
                    if (firstButtonCheck) {
                        visibleRadioButton.setChecked(true);
                        firstButtonCheck = false;
                    }

                    visibleRadioButton.setVisibility(View.VISIBLE);
                    visibleRadioButton.setTag(lastsize);//设置一个标记，方便子fragment进行翻页处理
                    visibleRadioButton.setOnClickListener(new View.OnClickListener() {//设置点击事件
                        @Override
                        public void onClick(View v) {
                            //2页分别处理
                            mPhotoDetailFragment.swichPages((int) v.getTag(), true);
                            PhotoTotalFragment.outsideclickpositon = visibleIndex;
//                        mPhotoTotalFragment.refreshLists();
                            mPhotoTotalFragment.swichPages(visibleIndex);
                        }
                    });

                    lastsize += currsize;
                }
            }
        } else {

        }

    }

    private void initFragment() {
        addBottomBarButton();//添加底部控制器按钮及其点击事件，将需要显示的集合拼接(有可能出现部分类别没有图片的情况)
        mPhotoDetailFragment = new PhotoDetailFragment();
        mPhotoDetailFragment.bindData(mTotalList, mDataBeans, mRbtnList);//为相册fragment绑定数据源
        //这个回调是在PhotoDetailFragment点击右上方小按钮的时候回调
        mPhotoDetailFragment.setmCallBack(new PublicUtils.photoCallBack() {
            @Override
            public void switchPageOne() {
            }

            @Override
            public void switchPageTwo() {
                mVpProjectPhotoOutside.setCurrentItem(1, true);
            }

            @Override
            public void switchToFinish() {
                finish();
            }
        });

        //缩略图页
        mPhotoTotalFragment = new PhotoTotalFragment(mRbtnList, mDataBeans, mTotalList);
        //这个回调是在PhotoTotalFragment点击左上方返回的时候回调
        mPhotoTotalFragment.setmCallBack(new PublicUtils.photoCallBack() {
            @Override
            public void switchPageOne() {
                mVpProjectPhotoOutside.setCurrentItem(0, true);
            }

            @Override
            public void switchPageTwo() {
            }

            @Override
            public void switchToFinish() {
            }
        });
        //这个回调是缩略图直接跳到详细图
        mPhotoTotalFragment.setActivityCallBack(new PublicUtils.photoOutsideTabCallBack() {
            @Override
            public void switchActivityPage(int whichPage) {
                mPhotoDetailFragment.swichPages(whichPage, false);
                mVpProjectPhotoOutside.setCurrentItem(0, true);
            }
        });

        ArrayList<android.support.v4.app.Fragment> frags = new ArrayList<android.support.v4.app.Fragment>();
        frags.add(mPhotoDetailFragment);
        frags.add(mPhotoTotalFragment);
        CommonFragmentPagerAdapter comfrgpager = new CommonFragmentPagerAdapter(getSupportFragmentManager(), frags);
        mVpProjectPhotoOutside.setAdapter(comfrgpager);
    }

    public void visibleLeftArr() {
        mIvLeftarr.setVisibility(View.VISIBLE);
        mIvRightarr.setVisibility(View.GONE);
    }

    public void visibleRightArr() {
        mIvLeftarr.setVisibility(View.GONE);
        mIvRightarr.setVisibility(View.VISIBLE);
    }

    public void goneAll() {
        mIvLeftarr.setVisibility(View.GONE);
        mIvRightarr.setVisibility(View.GONE);
    }

    private void initView(View inflateView) {
        mParentPhotoRoot = (ParentFrameLayout) inflateView.findViewById(R.id.parent_frame_photo);
        mParentPhotoRoot.setReLoadOnclickListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshUtils.AlertWhichParentlayout(mParentPhotoRoot, RefreshUtils.NetState.NET_SHOWLOADING, basicHandler);
                parepareData();
            }
        }, RefreshUtils.NetState.NET_NULL);
        mParentPhotoRoot.setBackgroundColor(this.getResources().getColor(R.color.black));
        mVpProjectPhotoOutside = (NoSlideViewPager) inflateView.findViewById(R.id.vp_projectphoto_outside);
        mSrvBottomBarCenber = (ScrollViewCustom) inflateView.findViewById(R.id.srv_bottombar_cenber);
        mBottomBarCenterGroup = (RadioGroup) inflateView.findViewById(R.id.activity_photo_radgroup);
        mIvLeftarr = inflateView.findViewById(R.id.left_arrow);
        mIvRightarr = inflateView.findViewById(R.id.right_arrow);
        RadioButton mRBtnXiaoGuoTu = (RadioButton) inflateView.findViewById(R.id.rbtn_prodetail_typeone);
        RadioButton mRBtnShiJinTu = (RadioButton) inflateView.findViewById(R.id.rbtn_prodetail_typetwo);
        RadioButton mRBtnHuXingTu = (RadioButton) inflateView.findViewById(R.id.rbtn_prodetail_typethree);
        RadioButton mRBtnQuWeiTu = (RadioButton) inflateView.findViewById(R.id.rbtn_prodetail_typefour);
        RadioButton mRBtnShaPanTu = (RadioButton) inflateView.findViewById(R.id.rbtn_prodetail_typefive);
        RadioButton mRBtnYangBanJian = (RadioButton) inflateView.findViewById(R.id.rbtn_prodetail_typesix);
        RadioButton mRBtnPeiTaoTu = (RadioButton) inflateView.findViewById(R.id.rbtn_prodetail_typeseven);
        mRbtnList = new ArrayList<>();
        mRbtnList.add(mRBtnXiaoGuoTu);
        mRbtnList.add(mRBtnShiJinTu);
        mRbtnList.add(mRBtnHuXingTu);
        mRbtnList.add(mRBtnQuWeiTu);
        mRbtnList.add(mRBtnShaPanTu);
        mRbtnList.add(mRBtnYangBanJian);
        mRbtnList.add(mRBtnPeiTaoTu);
        mSrvBottomBarCenber.setCallback(new PublicUtils.photoTabCallBack() {
            @Override
            public void switchPage(int whichPage) {
                if (whichPage > 0) {//右边
                    visibleLeftArr();
                } else {//左边
                    visibleRightArr();
                }
            }
        });
    }

    @Override
    public void handleCreate() {
        RefreshUtils.AlertWhichParentlayout(mParentPhotoRoot, RefreshUtils.NetState.NET_ALLISWELL, basicHandler);
        parepareData();
    }

    @Override
    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case REFRESH_PHOTO:
                PhotoDataResponse photoBean = (PhotoDataResponse) msg.obj;
                if (photoBean != null) {
                    mDataBeans = new ArrayList<ProductPhotoMappingBean>();
                    List<PhotoDataResponse.ProjectPhotoBean> totalJsonData = photoBean.getProject_photo();
                    if (totalJsonData != null && totalJsonData.size() > 0) {
                        for (PhotoDataResponse.ProjectPhotoBean ele : totalJsonData) {
                            List<PhotoDataResponse.ProjectPhotoBean.DataBean> data = ele.getData();//json串数据
                            List<ProductPhotoBean> subLocalDatas = new ArrayList<ProductPhotoBean>();
                            if (data != null && data.size() > 0) {//遍历json的data，导入本地data
                                for (PhotoDataResponse.ProjectPhotoBean.DataBean subEle : data) {
                                    ProductPhotoBean localSubData = new ProductPhotoBean(subEle.getPicture_title(), subEle.getPicture_url());
                                    subLocalDatas.add(localSubData);
                                }
                            }
                            //本地数据转换
                            ProductPhotoMappingBean mLocalBean = new ProductPhotoMappingBean(ele.getName(), subLocalDatas, ele.getType());
                            if (mLocalBean.getData() != null && mLocalBean.getData().size() > 0) {
                                mDataBeans.add(mLocalBean);
                            }
                        }
                    }
//                    if (mDataBeans.size() <= 0) {//测试数据
//                    mDataBeans = (ArrayList<ProductPhotoMappingBean>) getIntent().getSerializableExtra("datas");//测试数据
//                    }

                    if (mDataBeans != null && mDataBeans.size() > 0) {
                        initFragment();
                        initBottomBar();
                    } else {
                        if (NetUtils.isNetConnected()) {
                            RefreshUtils.AlertWhichParentlayout(mParentPhotoRoot, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
                        } else {
                            RefreshUtils.AlertWhichParentlayout(mParentPhotoRoot, RefreshUtils.NetState.NET_NONET, basicHandler);
                        }
                    }
                    break;
                } else {
                    if (NetUtils.isNetConnected()) {
                        RefreshUtils.AlertWhichParentlayout(mParentPhotoRoot, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
                    } else {
                        RefreshUtils.AlertWhichParentlayout(mParentPhotoRoot, RefreshUtils.NetState.NET_NONET, basicHandler);
                    }
                }

        }
    }

    /**
     * 数据准备
     */
    private void parepareData() {
        //获取服务器返回的数据
        mProJectId = getIntent().getStringExtra("project_id");
        //测试
        mProJectId = "48";//这儿到时候要改为传进来的id
        if (mPcontroller == null) {
            mPcontroller = new PhotoController(ProjectPhotoActivity.this);
        }

        mPcontroller.handlePhotoByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                basicHandler.obtainMessage(REFRESH_PHOTO, result).sendToTarget();
            }

            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
                if (NetUtils.isNetConnected()) {
                    RefreshUtils.AlertWhichParentlayout(mParentPhotoRoot, RefreshUtils.NetState.NET_HAVENETNODATA, basicHandler);
                } else {
                    RefreshUtils.AlertWhichParentlayout(mParentPhotoRoot, RefreshUtils.NetState.NET_NONET, basicHandler);
                }
            }

            @Override
            public void onNetFinished(String interfaceAction) {

            }

        }, mProJectId, false, "");
    }

    @Override
    protected void customOnClick(View v) {

    }

    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.black);
            getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }
}
