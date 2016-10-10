package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.util.StringUtils;

public class TitleView extends RelativeLayout {
    private LinearLayout ll_common_title_TV_left;
    private LinearLayout ll_common_title_TV_right;
    protected ImageView tvLeft;
    protected ImageView tvRight;
    protected TextView tvCenter;
    protected LinearLayout tvCenterLinear;

    protected int rightImageSrc = 0;
    protected int bgSrc = 0;
    protected int leftImageSrc = 0;
    protected String centerValue = "";
    protected String rightValue = "";
    protected String leftValue = "";
    private Context context;
    private View mBacklayout;
    private View mTitleLine;


    public void setTitleLineColor(int colorResId) {
        mTitleLine.setBackgroundColor(colorResId);
    }

    public void setRightImageOnClickListener(OnClickListener mOnClickListener) {
        ll_common_title_TV_right.setOnClickListener(mOnClickListener);
    }

    public void setLeftImageOnClickListener(OnClickListener mOnClickListener) {
        ll_common_title_TV_left.setOnClickListener(mOnClickListener);
    }

    public TitleView(Context context) {
        super(context);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.TitleViewAttrs);
        rightImageSrc = attrsArray.getResourceId(R.styleable.TitleViewAttrs_rightImageSrc, 0);
        leftImageSrc = attrsArray.getResourceId(R.styleable.TitleViewAttrs_leftImageSrc, 0);
        centerValue = attrsArray.getString(R.styleable.TitleViewAttrs_centerValue);
        rightValue = attrsArray.getString(R.styleable.TitleViewAttrs_rightValue);
        leftValue = attrsArray.getString(R.styleable.TitleViewAttrs_leftValue);
        bgSrc = attrsArray.getColor(R.styleable.TitleViewAttrs_bg, ContextCompat.getColor(context, R.color.white));
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.TitleViewAttrs);
        rightImageSrc = attrsArray.getResourceId(R.styleable.TitleViewAttrs_rightImageSrc, 0);
        leftImageSrc = attrsArray.getResourceId(R.styleable.TitleViewAttrs_leftImageSrc, 0);
        centerValue = attrsArray.getString(R.styleable.TitleViewAttrs_centerValue);
        rightValue = attrsArray.getString(R.styleable.TitleViewAttrs_rightValue);
        leftValue = attrsArray.getString(R.styleable.TitleViewAttrs_leftValue);
        bgSrc = attrsArray.getColor(R.styleable.TitleViewAttrs_bg, ContextCompat.getColor(context, R.color.white));
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.widget_common_title, this);
        tvLeft = (ImageView) findViewById(R.id.common_title_TV_left);
        tvRight = (ImageView) findViewById(R.id.common_title_TV_right);
        tvCenter = (TextView) findViewById(R.id.common_title_TV_center);
        mTitleLine = findViewById(R.id.title_line);
        mBacklayout = findViewById(R.id.common_title_RL);
        tvCenterLinear = (LinearLayout) findViewById(R.id.common_title_TV_center_linear);
        ll_common_title_TV_right = (LinearLayout) findViewById(R.id.ll_common_title_TV_right);
        ll_common_title_TV_left = (LinearLayout) findViewById(R.id.ll_common_title_TV_left);

        if (!StringUtils.isNull(centerValue)) {
            tvCenter.setText(centerValue);
        }


        if (rightImageSrc != 0) {
            tvRight.setImageResource(rightImageSrc);
        }


        if (leftImageSrc != 0) {
            tvLeft.setImageResource(leftImageSrc);
        }

        if (bgSrc != 0) {
            mBacklayout.setBackgroundColor(bgSrc);
        }

    }

    public void setTitle(String title) {
        centerValue = title;
        if (tvCenter != null) {
            mUIHandler.obtainMessage(MESSAGE_REFRESH_TITEL, 0, 0, title).sendToTarget();
        }
    }

    public void setTvCenterTextColor(int resId) {
        tvCenter.setTextColor(resId);
    }

    private static final int MESSAGE_REFRESH_TITEL = 1;

    private UIHandler mUIHandler = new UIHandler();

    private class UIHandler extends Handler {
        @SuppressWarnings({"unchecked"})
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_REFRESH_TITEL:
                    tvCenter.setText(StringUtils.repNull((String) msg.obj));
                    break;
                default:
                    break;
            }
        }
    }


}