package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;
import com.zhonghong.xqshijie.util.StringUtils;

public class ImageIncludeTextView extends FrameLayout {
    protected ImageView ivVerticalimagebgView;
    protected TextView tvVerticatitleView;//文字

    protected int mImageSrc = 0;
    protected int mTextColor = 0;
    protected String mTextValue = "";
    private Context context;


    public ImageIncludeTextView(Context context) {
        super(context);
        init(context);
    }

    public ImageIncludeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.ImageIncludeTextViewAttrs);
        mImageSrc = attrsArray.getResourceId(R.styleable.ImageIncludeTextViewAttrs_imageIncludeSrc, 0);
        mTextValue = attrsArray.getString(R.styleable.ImageIncludeTextViewAttrs_textIncludeValue);
        mTextColor = attrsArray.getColor(R.styleable.ImageIncludeTextViewAttrs_textIncludeColor, ContextCompat.getColor(context, R.color.gray));
        init(context);
    }

    public ImageIncludeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.ImageIncludeTextViewAttrs);
        mImageSrc = attrsArray.getResourceId(R.styleable.ImageIncludeTextViewAttrs_imageIncludeSrc, 0);
        mTextValue = attrsArray.getString(R.styleable.ImageIncludeTextViewAttrs_textIncludeValue);
        mTextColor = attrsArray.getColor(R.styleable.ImageIncludeTextViewAttrs_textIncludeColor, ContextCompat.getColor(context, R.color.gray));
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.widget_imageincludetext_view, this);
        ivVerticalimagebgView = (ImageView) findViewById(R.id.iv_includebg_view);
        tvVerticatitleView = (TextView) findViewById(R.id.tv_includetext_view);
        if (!StringUtils.isNull(mTextValue)) {
            if (!StringUtils.isNull(mTextValue)) {
                tvVerticatitleView.setText(mTextValue);
            }
            tvVerticatitleView.setVisibility(View.VISIBLE);
        } else {
            tvVerticatitleView.setVisibility(View.INVISIBLE);
        }


        if (mImageSrc != 0) {
            if (mImageSrc != 0) {
                ivVerticalimagebgView.setBackgroundResource(mImageSrc);
            }
            ivVerticalimagebgView.setVisibility(View.VISIBLE);
        } else {
            ivVerticalimagebgView.setVisibility(View.INVISIBLE);
        }

        if (mTextColor!=0){
            tvVerticatitleView.setTextColor(mTextColor);
        }

    }

    public void setTextValue(String title) {
        mTextValue = title;
        if (mTextValue != null) {
            mUIHandler.obtainMessage(MESSAGE_REFRESH_TITEL, 0, 0, title).sendToTarget();
            tvVerticatitleView.setVisibility(View.VISIBLE);
        } else {
            tvVerticatitleView.setVisibility(View.INVISIBLE);
        }
    }


    public void setImageBg(String imgUrl) {
        mUIHandler.obtainMessage(MESSAGE_REFRESH_IMAGR, imgUrl).sendToTarget();
        ivVerticalimagebgView.setVisibility(View.VISIBLE);
    }


    private static final int MESSAGE_REFRESH_TITEL = 1;
    private static final int MESSAGE_REFRESH_IMAGR = 2;

    private UIHandler mUIHandler = new UIHandler();

    private class UIHandler extends Handler {
        @SuppressWarnings({"unchecked"})
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_REFRESH_TITEL:
                    tvVerticatitleView.setText(StringUtils.repNull((String) msg.obj));
                    break;
                case MESSAGE_REFRESH_IMAGR:
                    String imageUrl = (String) msg.obj;
                    ImageLoaderUtil.getInstance().loadImage(imageUrl, ivVerticalimagebgView,R.drawable.ic_ylt_productdefault);
                    break;
                default:
                    break;
            }
        }
    }


}