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

public class TitleBigView extends RelativeLayout {
    protected ImageView tvLeft;
    protected ImageView tvRight;
    protected TextView tvCenter;
    protected LinearLayout tvCenterLinear;

    protected int rightImageBigSrc = 0;
    protected int bgSrc = 0;
    protected int leftImageBigSrc = 0;
    protected String centerValue = "";
    protected String rightValue = "";
    protected String leftValue = "";
    private Context context;
    private View mBacklayout;
    private View mTitleLine;

    /**
     * 网络异常控件变量
     */
    private ImageView mIvNetExceptionWaring;
    private TextView mTvNetExceptionDesc;
    private ImageView mIvNetExceptionSetting;
    private RelativeLayout netExceptionRelativelayout;
    /**
     * 网络异常的资源ID
     */
    protected int mImgWaring = 0;
    protected String mTextValue = "";
    protected int mImgSetting = 0;

    public void setRightImageOnClickListener(OnClickListener mOnClickListener) {
        tvRight.setOnClickListener(mOnClickListener);
    }

    public void setLeftImageOnClickListener(OnClickListener mOnClickListener) {
        tvLeft.setOnClickListener(mOnClickListener);
    }

    /**
     * 网络设置
     * @param mOnClickListener
     */
    public void setExceptionNetSetting(OnClickListener mOnClickListener){
        netExceptionRelativelayout.setOnClickListener(mOnClickListener);
    }

    public TitleBigView(Context context) {
        super(context);
        init(context);
    }

    public TitleBigView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.TitleViewAttrs);
        rightImageBigSrc = attrsArray.getResourceId(R.styleable.TitleViewAttrs_rightImageBigSrc, 0);
        leftImageBigSrc = attrsArray.getResourceId(R.styleable.TitleViewAttrs_leftImageBigSrc, 0);
        centerValue = attrsArray.getString(R.styleable.TitleViewAttrs_centerValue);
        rightValue = attrsArray.getString(R.styleable.TitleViewAttrs_rightValue);
        leftValue = attrsArray.getString(R.styleable.TitleViewAttrs_leftValue);
        bgSrc = attrsArray.getColor(R.styleable.TitleViewAttrs_bg, ContextCompat.getColor(context, R.color.white));
        //网络异常的属性
        mImgWaring = attrsArray.getResourceId(R.styleable.TitleViewAttrs_netExceptionImgWaring, 0);
        mTextValue = attrsArray.getString(R.styleable.TitleViewAttrs_netExceptionTextValue);
        mImgSetting = attrsArray.getResourceId(R.styleable.TitleViewAttrs_netExceptionImgSetting, 0);
        init(context);
    }

    public TitleBigView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.TitleViewAttrs);
        rightImageBigSrc = attrsArray.getResourceId(R.styleable.TitleViewAttrs_rightImageBigSrc, 0);
        leftImageBigSrc = attrsArray.getResourceId(R.styleable.TitleViewAttrs_leftImageBigSrc, 0);
        centerValue = attrsArray.getString(R.styleable.TitleViewAttrs_centerValue);
        rightValue = attrsArray.getString(R.styleable.TitleViewAttrs_rightValue);
        leftValue = attrsArray.getString(R.styleable.TitleViewAttrs_leftValue);
        bgSrc = attrsArray.getColor(R.styleable.TitleViewAttrs_bg, ContextCompat.getColor(context, R.color.white));
        //网络异常的属性
        mImgWaring = attrsArray.getResourceId(R.styleable.TitleViewAttrs_netExceptionImgWaring, 0);
        mTextValue = attrsArray.getString(R.styleable.TitleViewAttrs_netExceptionTextValue);
        mImgSetting = attrsArray.getResourceId(R.styleable.TitleViewAttrs_netExceptionImgSetting, 0);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.widget_common_bigtitle, this);
        tvLeft = (ImageView) findViewById(R.id.common_title_BigIV_left);
        tvRight = (ImageView) findViewById(R.id.common_title_BigIV_right);
        tvCenter = (TextView) findViewById(R.id.common_title_TV_center);
        mTitleLine = findViewById(R.id.title_line);
        mBacklayout = findViewById(R.id.common_title_RL);
        tvCenterLinear = (LinearLayout) findViewById(R.id.common_title_TV_center_linear);
        //网络异常的view
        mIvNetExceptionWaring = (ImageView) findViewById(R.id.iv_net_exception_waring);
        mTvNetExceptionDesc = (TextView) findViewById(R.id.tv_net_desc_exception);
        mIvNetExceptionSetting = (ImageView) findViewById(R.id.iv_net_exception_setting);
        //网络异常总布局
        netExceptionRelativelayout = (RelativeLayout) findViewById(R.id.net_exception_relativelayout);

        if (!StringUtils.isNull(centerValue)) {
            tvCenter.setText(centerValue);
        }


        if (rightImageBigSrc != 0) {
            tvRight.setImageResource(rightImageBigSrc);
        }


        if (leftImageBigSrc != 0) {
            tvLeft.setImageResource(leftImageBigSrc);
        }

        if (bgSrc != 0) {
            mBacklayout.setBackgroundColor(bgSrc);
        }
        //网络异常
        if (mImgWaring != 0) {
            mIvNetExceptionWaring.setImageResource(mImgWaring);
        }
        if (mImgSetting != 0) {
            mIvNetExceptionSetting.setImageResource(mImgSetting);
        }
        if (!StringUtils.isNull(mTextValue)) {
            mTvNetExceptionDesc.setText(mTextValue);
        }

    }


    /**
     * 没有网络时操作提示
     */
    public void setSettingDrawable(int settingDrawable) {
        mImgSetting = settingDrawable;
        if (mImgSetting != 0) {
            mUIHandler.obtainMessage(MESSAGE_REFRESH_SETTINGDRAWABLE, 0, 0, mImgSetting).sendToTarget();
        }
    }

    /**
     * 加载没有网络警告图片
     */
    public void setWaringDrawable(int waringDrawable) {
        mImgWaring = waringDrawable;
        if (mImgWaring != 0) {
            mUIHandler.obtainMessage(MESSAGE_REFRESH_WARINGDRAWABLE, 0, 0, mImgWaring).sendToTarget();
        }
    }

    /**
     * 没有网络描述
     *
     * @param desc
     */
    public void setTextValueDesc(String desc) {
        mTextValue = desc;
        if (mTextValue != null) {
            mUIHandler.obtainMessage(MESSAGE_REFRESH_DESC, 0, 0, mTextValue).sendToTarget();
        }
    }

    /**
     * 显示异常布局
     */
    public void showException() {
        if (netExceptionRelativelayout != null) {
            if (View.GONE == netExceptionRelativelayout.getVisibility()) {
                netExceptionRelativelayout.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * 不显示异常布局
     */
    public void dismissException() {
        if (netExceptionRelativelayout != null) {
            if (View.VISIBLE == netExceptionRelativelayout.getVisibility()) {
                netExceptionRelativelayout.setVisibility(GONE);
            }
        }
    }

    public void setTitle(String title) {
        centerValue = title;
        if (tvCenter != null) {
            mUIHandler.obtainMessage(MESSAGE_REFRESH_TITEL, 0, 0, title).sendToTarget();
        }
    }

    public void setTitleLeftImage(int resId) {
        leftImageBigSrc = resId;
        if (tvCenter != null) {
            mUIHandler.obtainMessage(MESSAGE_REFRESH_LEFT_IMAGE, 0, 0, leftImageBigSrc).sendToTarget();
        }
    }

    private static final int MESSAGE_REFRESH_TITEL = 1;
    private static final int MESSAGE_REFRESH_LEFT_IMAGE = 2;
    /**
     * 网络异常
     */
    private static final int MESSAGE_REFRESH_WARINGDRAWABLE = 3;
    private static final int MESSAGE_REFRESH_DESC = 4;
    private static final int MESSAGE_REFRESH_SETTINGDRAWABLE = 5;

    private UIHandler mUIHandler = new UIHandler();

    private class UIHandler extends Handler {
        @SuppressWarnings({"unchecked"})
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_REFRESH_TITEL:
                    tvCenter.setText(StringUtils.repNull((String) msg.obj));
                    break;
                case MESSAGE_REFRESH_LEFT_IMAGE:
                    tvLeft.setImageResource(leftImageBigSrc);
                    break;
                default:
                    break;
            }
        }
    }


}