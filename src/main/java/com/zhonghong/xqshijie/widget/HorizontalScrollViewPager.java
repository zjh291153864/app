package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zhonghong.xqshijie.util.DensityUtil;

/**
 * 横向滑动的ViewPager
 */
public class HorizontalScrollViewPager extends ViewPager {

    private int startX;
    private int startY;

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScrollViewPager(Context context) {
        super(context);
    }

    /**
     * 1. 当触摸点距离左边20dp时，父控件拦截事件
     * 需要父控件拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);// 请求父控件,以及祖宗控件,不要拦截我的事件,
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                // 判断是否是左右划
                if (Math.abs(dx) > Math.abs(dy)) {// 左右滑动
                    if (dx > 0 && startX < DensityUtil.dip2px(getContext(), 20f)) {// 向右滑动
                        getParent().requestDisallowInterceptTouchEvent(false);// 父控件可以拦截事件
                    }

                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);// 父控件可以拦截事件
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

}
