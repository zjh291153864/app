package com.zhonghong.xqshijie.widget.popupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;


public class BottomPopupWindowBtn extends LinearLayout {

	public TextView btn;
	public BottomPopupWindowBtn(Context context) {
		super(context);
		LayoutInflater.from(getContext()).inflate(R.layout.popwindow_bottom_btn, this);
		btn = (TextView)findViewById(R.id.btn);
	}
	public BottomPopupWindowBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(getContext()).inflate(R.layout.popwindow_bottom_btn, this);
		btn = (TextView)findViewById(R.id.btn);
	}
}
