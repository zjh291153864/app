package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.util.StringUtils;

public class MenuItemView extends LinearLayout {
    protected TextView mTvMenuImage;
    protected TextView mTvmenuTextview;//文字

    protected int mImageSrc = 0;
    protected String mTextValue = "";
    private Context mContext;


    public MenuItemView(Context context) {
        super(context);
        init(context);
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.MenuItemViewAttrs);
        mImageSrc = attrsArray.getResourceId(R.styleable.MenuItemViewAttrs_imageLeftSrc, 0);
        mTextValue = attrsArray.getString(R.styleable.MenuItemViewAttrs_textRightValue);
        init(context);
    }

    public MenuItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.MenuItemViewAttrs);
        mImageSrc = attrsArray.getResourceId(R.styleable.MenuItemViewAttrs_imageLeftSrc, 0);
        mTextValue = attrsArray.getString(R.styleable.MenuItemViewAttrs_textRightValue);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(getContext()).inflate(R.layout.widget_menuitem_view, this);
        mTvMenuImage = (TextView) findViewById(R.id.tv_menu_image);
        mTvmenuTextview = (TextView) findViewById(R.id.tv_menu_textview);
        if (!StringUtils.isNull(mTextValue) ) {
            if (!StringUtils.isNull(mTextValue)) {
                mTvmenuTextview.setText(mTextValue);
            }
            mTvmenuTextview.setVisibility(View.VISIBLE);
        } else {
            mTvmenuTextview.setVisibility(View.INVISIBLE);
        }


        if ( mImageSrc != 0) {
            if (mImageSrc != 0) {
                mTvMenuImage.setBackgroundResource(mImageSrc);
            }
            mTvMenuImage.setVisibility(View.VISIBLE);
        } else {
            mTvMenuImage.setVisibility(View.INVISIBLE);
        }
    }
}