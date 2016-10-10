package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.zhonghong.xqshijie.util.PublicUtils;

public class ScrollViewCustom extends HorizontalScrollView implements View.OnTouchListener {

    private PublicUtils.photoTabCallBack callback = null;

    public void setCallback(PublicUtils.photoTabCallBack callback) {
        this.callback = callback;
    }

    public ScrollViewCustom(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public ScrollViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public ScrollViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                int scrollX = this.getScrollX();
                int width = this.getWidth();
                int scrollViewMeasuredWidth = this.getChildAt(0).getMeasuredWidth();

                if (scrollX == 0) {
                    this.callback.switchPage(-1);
                    System.out.println("滑动到了左端 view.getScrollY()=" + scrollX);
                }

                if ((scrollX+width) == scrollViewMeasuredWidth) {
                    System.out.println("滑动到了右端 scrollX=" + scrollX);
                    this.callback.switchPage(1);
                }

                break;
            default:
                break;
        }
        return false;
    }
}
