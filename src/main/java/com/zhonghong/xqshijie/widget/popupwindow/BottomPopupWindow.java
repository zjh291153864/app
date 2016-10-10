package com.zhonghong.xqshijie.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;


public class BottomPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener{

	private ImageView iv_cancel;
	private View mMenuView;
	private LinearLayout popLL;
	private Activity mContext;
	private TextView titleView;

	public BottomPopupWindow(Activity context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popwindow_bottom, null);
		popLL = (LinearLayout) mMenuView.findViewById(R.id.pop_btn_layout);
		iv_cancel = (ImageView) mMenuView.findViewById(R.id.iv_cancel);
		titleView = (TextView) mMenuView.findViewById(R.id.pop_title);
		// 取消按钮
		iv_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
		setOnDismissListener(this);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		mMenuView.setFocusable(true);
		mMenuView.setFocusableInTouchMode(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00ffffff);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(null);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
		
		mMenuView.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// 手机键盘上的返回键
				switch (keyCode) {
					case KeyEvent.KEYCODE_BACK:
						dismiss();
						break;
				}
				return false;
			}
		});
	}
	
	public void setTitle(int textResId){
		titleView.setText(textResId);
		titleView.setVisibility(View.VISIBLE);
	}

	public void addButton(int textResId, OnClickListener onClickListener, boolean isSpecial) {
		int count = popLL.getChildCount();
		if(count == 0){
			if(titleView.getVisibility() == View.VISIBLE){
				BottomPopupWindowBtn btn = new BottomPopupWindowBtn(mContext);
				btn.btn.setOnClickListener(onClickListener);
				btn.btn.setText(textResId);
				popLL.addView(btn);
			}else{
				BottomPopupWindowBtnNone btn = new BottomPopupWindowBtnNone(mContext);
				btn.btn.setOnClickListener(onClickListener);
				btn.btn.setText(textResId);
				popLL.addView(btn);
			}
		}else{
			BottomPopupWindowBtn btn = new BottomPopupWindowBtn(mContext);
			btn.btn.setOnClickListener(onClickListener);
			btn.btn.setText(textResId);
			popLL.addView(btn);
		}
	}

	/**
	 * 设置添加屏幕的背景透明度
	 *
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		mContext.getWindow().setAttributes(lp);
	}

	public void alertPopupwindow(View outsiderootview) {
		backgroundAlpha(0.5f);
		showAtLocation(outsiderootview,Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

	}

	@Override
	public void onDismiss() {
		backgroundAlpha(1f);
	}
}