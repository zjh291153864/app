package com.zhonghong.xqshijie.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;

public class ImageTextTab extends LinearLayout {

    private View view;
    private ImageView tabimg;
    private TextView tabtv;
    private int focusid;
    private int unfocusid;
    private Context context;
    int focusColor;
    int unfoucsColor;
    int textsize;
    String text;

    public ImageTextTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        view = View.inflate(context, R.layout.imagetextstab, this);
        tabimg = (ImageView) view.findViewById(R.id.picstabimg);
        tabtv = (TextView) view.findViewById(R.id.picstabtexts);
    }

    public void setImageResId(int focusid, int unfocusid) {
        this.focusid = focusid;
        this.unfocusid = unfocusid;
        setImageRes(focusid);
    }

    public void setTextColorId(int focusColor, int unfoucsColor, String text,int textsize) {
        this.focusColor = focusColor;
        this.unfoucsColor = unfoucsColor;
        this.text = text;
        this.textsize = textsize;
    }

    public ImageTextTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextTab(Context context) {
        this(context, null);
    }

    public void setTexts(String text, int textsizesp, int colorid) {
        tabtv.setTextSize(textsizesp);
        tabtv.setText(text);
        tabtv.setTextColor(getResources().getColor(colorid));
    }

    private void setImageRes(int resid) {
        tabimg.setVisibility(View.VISIBLE);
        tabimg.setImageDrawable(context.getResources().getDrawable(resid));
    }

    public void setFoucs() {
        setTexts(this.text, this.textsize, this.focusColor);
        setImageRes(this.focusid);
    }

    public void setUnFoucs() {
        setTexts(this.text, this.textsize, this.unfoucsColor);
        setImageRes(this.unfocusid);
    }

}
