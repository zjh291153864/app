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

public class ImageAndTextVerticalView extends LinearLayout {
    protected TextView tvVerticalimagebgView;
    protected TextView tvVerticatitleView;//文字

    protected int mImageSrc = 0;
    protected String mTextValue = "";
    private Context context;


    public ImageAndTextVerticalView(Context context) {
        super(context);
        init(context);
    }

    public ImageAndTextVerticalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.ImageAndTextVerticalViewAttrs);
        mImageSrc = attrsArray.getResourceId(R.styleable.ImageAndTextVerticalViewAttrs_imageSrc, 0);
        mTextValue = attrsArray.getString(R.styleable.ImageAndTextVerticalViewAttrs_textValue);
        init(context);
    }

    public ImageAndTextVerticalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.ImageAndTextVerticalViewAttrs);
        mImageSrc = attrsArray.getResourceId(R.styleable.ImageAndTextVerticalViewAttrs_imageSrc, 0);
        mTextValue = attrsArray.getString(R.styleable.ImageAndTextVerticalViewAttrs_textValue);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.widget_ivtvvertical_view, this);
        tvVerticalimagebgView = (TextView) findViewById(R.id.tv_verticalimagebg_view);
        tvVerticatitleView = (TextView) findViewById(R.id.tv_verticatitle_view);
        if (!StringUtils.isNull(mTextValue) ) {
            if (!StringUtils.isNull(mTextValue)) {
                tvVerticatitleView.setText(mTextValue);
            }
            tvVerticatitleView.setVisibility(View.VISIBLE);
        } else {
            tvVerticatitleView.setVisibility(View.INVISIBLE);
        }


        if ( mImageSrc != 0) {
            if (mImageSrc != 0) {
                tvVerticalimagebgView.setBackgroundResource(mImageSrc);
            }
            tvVerticalimagebgView.setVisibility(View.VISIBLE);
        } else {
            tvVerticalimagebgView.setVisibility(View.INVISIBLE);
        }
    }

}