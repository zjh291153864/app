package com.zhonghong.xqshijie.widget.adcircleviewpager;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiezl on 16/7/8.
 */
public interface ImageCycleViewListener<T> {
//    /**
//     * 加载图片资源
//     *
//     * @param imageURL
//     * @param imageView
//     */
//    public void displayImage(String imageURL, ImageView imageView);

    /**
     * 单击图片事件
     *
     * @param position
     * @param imageView
     */
   public void onImageClick(int position, View imageView);


    /**
     * 初始化轮播图
     * @param item
     * @param container
     * @param position
     * @return
     */
    public View initContentView(Object item, ViewGroup container, int position);

}
