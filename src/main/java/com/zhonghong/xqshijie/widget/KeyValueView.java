package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.util.StringUtils;

public class KeyValueView extends LinearLayout {
    protected TextView mTvKeyView;
    protected TextView mTvValueView;

    protected String mTextKey = "";
    protected String mTextValue = "";
    private Context mContext;


    public KeyValueView(Context context) {
        super(context);
        init(context);
    }

    public KeyValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.KeyValueAttrs);
        mTextKey = attrsArray.getString(R.styleable.KeyValueAttrs_mTextKey);
        mTextValue = attrsArray.getString(R.styleable.KeyValueAttrs_mTextValue);
        init(context);
    }

    public KeyValueView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.KeyValueAttrs);
        mTextKey = attrsArray.getString(R.styleable.KeyValueAttrs_mTextKey);
        mTextValue = attrsArray.getString(R.styleable.KeyValueAttrs_mTextValue);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(getContext()).inflate(R.layout.widget_keyvalue_view, this);
        mTvKeyView = (TextView) findViewById(R.id.tv_key_view);
        mTvValueView = (TextView) findViewById(R.id.tv_value_view);

        if (!StringUtils.isNull(mTextKey)) {
            mTvKeyView.setText(mTextKey);
        }
        if (!StringUtils.isNull(mTextValue)) {
            mTvValueView.setText(mTextValue);
        }

    }


    /**
     * KEY 赋值
     * @param keyDesc
     */
    public void setTextKeyDesc(String keyDesc) {
        mTextKey = keyDesc;
        if (mTextKey != null) {
            mUIHandler.obtainMessage(MESSAGE_REFRESH_KEY, 0, 0, mTextKey).sendToTarget();
        }
    }

    /**
     * value 赋值
     * @param valueDesc
     */
    public void setTextValueDesc(String valueDesc) {
        mTextValue = valueDesc;
        if (mTextValue != null) {
            mUIHandler.obtainMessage(MESSAGE_REFRESH_DESC, 0, 0, mTextValue).sendToTarget();
        }
    }

    private UIHandler mUIHandler = new UIHandler();

    private static final int MESSAGE_REFRESH_DESC = 1;
    private static final int MESSAGE_REFRESH_KEY = 2;

    private class UIHandler extends Handler {
        @SuppressWarnings({"unchecked"})
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_REFRESH_DESC:
                    mTvValueView.setText(msg.obj == null ? "" : (String) msg.obj);
                    break;
                case MESSAGE_REFRESH_KEY:
                    mTvKeyView.setText(msg.obj == null ? "" : (String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

}