package com.zhonghong.xqshijie.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 加载 提示框
 */

public class ProgressUtil {
	private static Timer mTimer;
	private static Dialog mProgressDialog = null;
	private static int mSeconds;
	private static Context mContext;

	public static void cancelTimer() {
		if (mTimer != null) {
			mSeconds = 0;
			mTimer.cancel();
			mTimer = null;
		}
	}

	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				dismissProgressDialog();
				Toast.makeText(mContext,mContext.getString(R.string.net_error),Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	private static void beginTimer(final Context context, final Object threadCallback, final int cancelType) {
		mContext = context;
		cancelTimer();
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (mSeconds >= 30) {
					Message msg = new Message();
					msg.obj = threadCallback;
					msg.what = 0;
					msg.arg1 = cancelType;
					mHandler.sendMessage(msg);
					mSeconds = 0;
				} else {
					mSeconds++;
				}
			}
		}, 0, 1000);
	}

	public static void setCancelable() {
		if (mProgressDialog != null) {
			mProgressDialog.setCancelable(false);
		}
	}

	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失  
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}

	public static void showProgressDialog(final Context context, final Object... message) {
		if(isShow()){
			dismissProgressDialog();
		}
		if (mProgressDialog == null) {
			try {
				mProgressDialog = createLoadingDialog(context, message[0].toString());
				// mProgressDialog.setMessage(message[0].toString());
				mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							if (message.length > 1 && isShow()) {
								cancelTimer();
								mSeconds = 0;
							}
						} else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
							return true;
						}
						return false;
					}

				});
				mProgressDialog.show();
				if (message.length > 1) {
					beginTimer(context, message[1], Integer.parseInt(message[2].toString()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isShow() {
		if (mProgressDialog != null) {
			return mProgressDialog.isShowing();
		}
		return false;
	}

	public static void dismissProgressDialog() {
		try {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
				cancelTimer();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void onDestroy() {
		cancelTimer();
		mProgressDialog = null;
		mContext = null;
	}

}
