package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jh on 2016/7/12.
 */

public class PhotoRecyclerView extends RecyclerView {
    public PhotoRecyclerView(Context context) {
        super(context);
    }

    public PhotoRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if(e.getAction() ==  MotionEvent.ACTION_MOVE){
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        if(e.getAction() ==  MotionEvent.ACTION_MOVE){
            return false;
//        }
//        return true;
    }
}
