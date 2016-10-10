package com.zhonghong.xqshijie.widget.adcircleviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezl on 16/7/8.
 */
public abstract class ImageCycleAdapter<T> extends PagerAdapter {

    /**
     * 图片视图缓存列表
     */
    private ArrayList<View> mImageViewCacheList;

    /**
     * 图片资源列表
     */
    protected final List<T> mAdList;

    /**
     * 广告图片点击监听
     */
    private ImageCycleViewListener mImageCycleViewListener;

    private Context mContext;
    private ViewPager mAdvPager;

    public ImageCycleAdapter(Context context, ArrayList<T> adList, ImageCycleViewListener imageCycleViewListener,ViewPager mAdvPager) {
        this.mContext = context;
        this.mAdvPager = mAdvPager;
        this.mAdList = adList == null ? new ArrayList<T>() : new ArrayList<T>(adList);
        mImageCycleViewListener = imageCycleViewListener;
        mImageViewCacheList = new ArrayList<View>();
    }

    @Override
    public int getCount() {
        return (mAdList != null && mAdList.size() == 1) ? 1 : Integer.MAX_VALUE;
    }

    public T getItem(int position) {
        if (position >= mAdList.size()) return null;
        return mAdList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        String imageUrl = mAdList.get(position % mAdList.size()).mImageUrl;
//        ImageView imageView;
//        if (mImageViewCacheList.isEmpty()) {
//            imageView = new ImageView(mContext);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        } else {
//            imageView = mImageViewCacheList.remove(0);
//        }
//        // 设置图片点击监听
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mImageCycleViewListener.onImageClick(position % mAdList.size(), v);
//            }
//        });
//        imageView.setTag(imageUrl);
//        container.addView(imageView);
//        mImageCycleViewListener.displayImage(imageUrl, imageView);
//        return imageView;
        T item = mAdList.get(position % mAdList.size());
        View mview;
        if (mImageViewCacheList.isEmpty()) {
             mview = initContentView(item, container, position);
        }else{
            mview = mImageViewCacheList.remove(0);
        }
        // 设置图片点击监听
        mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageCycleViewListener.onImageClick(position % mAdList.size(), v);
            }
        });
//        mview.setTag(imageUrl);
        container.addView(mview);
        return mview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        mAdvPager.removeView(view);
        mImageViewCacheList.add(view);
    }


    protected abstract View initContentView(T adList, ViewGroup container,  int position);

}
