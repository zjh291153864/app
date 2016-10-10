package com.zhonghong.xqshijie.widget.adcircleviewpager;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.widget.HorizontalScrollViewPager;

import java.util.ArrayList;

/**
 * 自定义轮播图
 */
public class ImageCycleView<T> extends LinearLayout {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 图片轮播视图
     */
    private ImageView mDefaultImage;
    private HorizontalScrollViewPager mAdvPager = null;
    /**
     * 滚动图片视图适配
     */
    private ImageCycleAdapter mAdvAdapter;
    /**
     * 图片轮播指示器控件
     */
    private ViewGroup mGroup;

    /**
     * 图片轮播指示个图
     */
    private ImageView mImageView = null;

    /**
     * 滚动图片指示视图列表
     */
    private ImageView[] mImageViews = null;

    /**
     * 图片滚动当前图片下标
     */
    private int mImageIndex = 0;
    /**
     * 手机密度
     */
    private float mScale;
    private boolean isStop;

    /**
     * @param context
     */
    public ImageCycleView(Context context) {
        super(context);
        init(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public ImageCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        LayoutInflater.from(context).inflate(R.layout.widget_adcycle_view, this);
        mDefaultImage = (ImageView) findViewById(R.id.iv_default_image);
        mAdvPager = (HorizontalScrollViewPager) findViewById(R.id.adv_pager);
        mAdvPager.addOnPageChangeListener(new GuidePageChangeListener());
        mAdvPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (mAdvPager.getChildCount() > 1) {
                            // 开始图片滚动
                            startImageTimerTask();
                        }
                        break;
                    default:
                        // 停止图片滚动
                        stopImageTimerTask();
                        break;
                }
                return false;
            }
        });
        // 滚动图片右下指示器视
        mGroup = (ViewGroup) findViewById(R.id.circles);
//        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, context.getApplicationContext().getResources().getDisplayMetrics().heightPixels * 3 / 10);
//        setLayoutParams(cParams);
        mDefaultImage.setVisibility(View.VISIBLE);
    }


    /**
     * 装填图片数据
     *
     * @param :
     * @param imageCycleViewListener
     */
    public void setImageResources(ArrayList<T> imageUrlList, final ImageCycleViewListener imageCycleViewListener) {
        if (imageUrlList != null && imageUrlList.size() > 0) {
            mDefaultImage.setVisibility(View.GONE);
        } else {
            mDefaultImage.setVisibility(View.VISIBLE);
            return;
        }

        // 清除
        mGroup.removeAllViews();
        // 图片广告数量
        final int imageCount = imageUrlList.size();
        mImageViews = new ImageView[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mImageView = new ImageView(mContext);
            int imageParams = (int) (mScale * 8 + 0.5f);// XP与DP转换，适应应不同分辨率
            int imagePadding = (int) (mScale * 5 + 0.5f);
            LayoutParams params = new LayoutParams(imageParams, imageParams);
            params.leftMargin = 18;
            mImageView.setScaleType(ScaleType.FIT_XY);
            mImageView.setLayoutParams(params);
            mImageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);

            mImageViews[i] = mImageView;
            if (i == 0) {
                mImageViews[i].setBackgroundResource(R.drawable.banner_dot_focus);
            } else {
                mImageViews[i].setBackgroundResource(R.drawable.banner_dot_normal);
            }
            if (imageUrlList != null && imageUrlList.size() > 1) {
                mGroup.addView(mImageViews[i]);
            }
        }

        mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList, imageCycleViewListener, mAdvPager) {

            @Override
            protected View initContentView(Object item, ViewGroup container, int position) {
                return imageCycleViewListener.initContentView(item,container,position);
            }

        };
        mAdvPager.setAdapter(mAdvAdapter);
        if (imageUrlList != null && imageUrlList.size() > 1) {
            startImageTimerTask();
        }
    }

    /**
     * 图片轮播(手动控制自动轮播与否，便于资源控件）
     */
    public void startImageCycle() {
        startImageTimerTask();
    }

    /**
     * 暂停轮播—用于节省资源
     */
    public void pushImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片滚动
        mHandler.postDelayed(mImageTimerTask, 3000);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        isStop = true;
        if (mImageTimerTask != null) {
            mHandler.removeCallbacks(mImageTimerTask);
        }
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {
        @Override
        public void run() {
            if (mImageViews != null) {
                mAdvPager.setCurrentItem(mAdvPager.getCurrentItem() + 1);
                if (!isStop) {  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环
                    mHandler.postDelayed(mImageTimerTask, 3000);
                }

            }
        }
    };

    /**
     * 轮播图片监听
     *
     * @author minking
     */
    private final class GuidePageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (mImageViews != null && mImageViews.length > 1) {
                    startImageTimerTask();
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            if (mImageViews != null && mImageViews.length > 1) {
                index = index % mImageViews.length;
                // 设置当前显示的图片
                mImageIndex = index;
                // 设置图片滚动指示器背
                mImageViews[index].setBackgroundResource(R.drawable.banner_dot_focus);
//			imageName.setText(mImageDescList.get(index));
                for (int i = 0; i < mImageViews.length; i++) {
                    if (index != i) {
                        mImageViews[i].setBackgroundResource(R.drawable.banner_dot_normal);
                    }
                }
            }
        }
    }

}
