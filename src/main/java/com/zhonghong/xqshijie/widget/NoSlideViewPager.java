package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created on 2015/8/18.
 */
public class NoSlideViewPager extends ViewPager {

    private boolean isSlidable = false;

    public void setSlidable(boolean isSlidable) {
        this.isSlidable = isSlidable;
    }

    public NoSlideViewPager(Context context) {
        this(context, null);
    }

    public NoSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (true == isSlidable) {
                return super.onInterceptTouchEvent(ev);
            } else {
                return false;
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}