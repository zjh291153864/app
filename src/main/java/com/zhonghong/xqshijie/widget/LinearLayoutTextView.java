package com.zhonghong.xqshijie.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;

public class LinearLayoutTextView extends LinearLayout {

    protected String mDescName = "";
    protected String mDescContent = "";
    private Context context;

    private TextView mTvDescName;
    private TextView mTvDescContent;
    public LinearLayoutTextView(Context context) {
        this(context,null);
    }

    public LinearLayoutTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LinearLayoutTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.LinearLayoutTextView);
        mDescName = attrsArray.getString(R.styleable.LinearLayoutTextView_tv_desc_name);
        mDescContent = attrsArray.getString(R.styleable.LinearLayoutTextView_tv_desc_content);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.widget_linearlayout_textview, this);
        mTvDescName = (TextView) findViewById(R.id.tv_desc_name);
        mTvDescContent = (TextView) findViewById(R.id.tv_desc_content);


    }

    public void setTextNameValue(String name) {


        mTvDescName.setText(name);
    }
    public void setTextContentValue(String content) {
        mTvDescContent.setText(content);
        //mUIHandler.obtainMessage(MESSAGE_REFRESH_TITEL, 0, 0, title).sendToTarget();

    }

    private static final int MESSAGE_REFRESH_TITEL = 1;
    private static final int MESSAGE_REFRESH_IMAGR = 2;

//    private UIHandler mUIHandler = new UIHandler();
//
//    private class UIHandler extends Handler {
//        @SuppressWarnings({"unchecked"})
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case MESSAGE_REFRESH_TITEL:
//                    tvVerticatitleView.setText(StringUtils.repNull((String) msg.obj));
//                    break;
//                case MESSAGE_REFRESH_IMAGR:
//                    String imageUrl = (String) msg.obj;
//                    ImageLoaderUtil.getInstance().loadImage(imageUrl, ivVerticalimagebgView,R.drawable.ylt_pic_top);
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

}