package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.util.StringUtils;

public class CategoryTitleView extends LinearLayout {
    protected TextView mCategoryView;

    protected String mCategoryValue = "";
    private Context context;


    public CategoryTitleView(Context context) {
        super(context);
        init(context);
    }

    public CategoryTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.CategoryTitleViewAttrs);
        mCategoryValue = attrsArray.getString(R.styleable.CategoryTitleViewAttrs_text);
        init(context);
    }

    public CategoryTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.CategoryTitleViewAttrs);
        mCategoryValue = attrsArray.getString(R.styleable.CategoryTitleViewAttrs_text);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.widget_category_view, this);
        mCategoryView = (TextView) findViewById(R.id.tv_category_title);
        if (!StringUtils.isNull(mCategoryValue)) {
            if (!StringUtils.isNull(mCategoryValue)) {
                mCategoryView.setText(mCategoryValue);
                mCategoryView.setVisibility(View.VISIBLE);
            } else {
                mCategoryView.setVisibility(View.INVISIBLE);
            }
        }
    }


    public void setLeftValue(String mCategoryValue) {
        this.mCategoryValue = mCategoryValue;
        if (!StringUtils.isNull(mCategoryValue)) {
            mUIHandler.obtainMessage(MESSAGE_REFRESH_TITLE, 0, 0, mCategoryValue).sendToTarget();
            mCategoryView.setVisibility(View.VISIBLE);
        } else {
            mCategoryView.setVisibility(View.INVISIBLE);
        }
    }


    private static final int MESSAGE_REFRESH_TITLE = 0;

    private UIHandler mUIHandler = new UIHandler();

    private class UIHandler extends Handler {
        @SuppressWarnings({"unchecked"})
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_REFRESH_TITLE:
                    mCategoryView.setText(StringUtils.repNull((String) msg.obj));
                    break;
                default:
                    break;
            }
        }
    }


}