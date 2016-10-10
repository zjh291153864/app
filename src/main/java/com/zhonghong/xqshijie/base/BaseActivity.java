package com.zhonghong.xqshijie.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.MainActivity;
import com.zhonghong.xqshijie.app.ActivityStack;
import com.zhonghong.xqshijie.app.Constants;
import com.zhonghong.xqshijie.dialog.InfoProgressDialog;
import com.zhonghong.xqshijie.dialog.InfoToast;

import org.xutils.x;

import java.lang.reflect.Field;

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    public Context mContext;
    /**
     * 涉及到Activity常量都放到BaseActivity里面
     */
    public final static int EXIT_OK = 0x801;
    /**
     * 等待页面
     */
    protected InfoProgressDialog progressDialog;
    /**
     * 屏幕宽高
     */
    public int screenWidth = 0;
    protected int screenHeight = 0;

    protected Handler basicHandler = new BasicHandle();

    class BasicHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean isDispatch = false;
            switch (msg.what) {
                /*处理公共 handle*/
                case Constants.NOT_REWRITE_HANDLER:
                    isDispatch = true;
                    break;
            }
            /*子类处理 handle*/
            if (!isDispatch) {
                processMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        setStatusBar();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**
         * 引入xutil
         */
        x.view().inject(this);
        View view = initContentView();
        if (view != null) {
            setContentView(view);
        }
        progressDialog = new InfoProgressDialog(this);
        if (screenWidth == 0 || screenHeight == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
        }
        handleCreate();
        ActivityStack.getInstance().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getInstance().popActivity(this);
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final ViewGroup linear_bar = (ViewGroup) findViewById(R.id.rl_title);
            final int statusHeight = getStatusBarHeight();
            linear_bar.post(new Runnable() {
                @Override
                public void run() {
                    int titleHeight = linear_bar.getHeight();
                    android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) linear_bar.getLayoutParams();
                    params.height = statusHeight + titleHeight;
                    linear_bar.setLayoutParams(params);
                }
            });
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @return
     */
    protected int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 初始化一些View操作
     *
     * @return
     */
    public abstract View initContentView();

    /**
     * 逻辑操作，如：请求数据，加载界面...
     */
    public abstract void handleCreate();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_common_title_TV_left:
                if (this instanceof MainActivity){

                }else {
                    finish();
                }
                break;
            case R.id.common_title_BigIV_left:
                if (this instanceof MainActivity){

                }else {
                    finish();
                }
                break;
            default:
                break;
        }
        customOnClick(v);
    }

    /**
     * 页面点击事件处理方法
     *
     * @param v
     */
    protected abstract void customOnClick(View v);

    /**
     * Handler消息处理
     *
     * @param msg
     */
    protected void processMessage(Message msg) {

    }


    public void sendMessage(int what) {
        sendMessage(what, 0, 0, null);
    }

    public void sendMessage(int what, Object data) {
        sendMessage(what, 0, 0, data);
    }

    /**
     * {@link Handler#obtainMessage(int, int, int)}
     *
     * @param what
     * @param arg1
     * @param arg2
     * @param data
     */
    public void sendMessage(int what, int arg1, int arg2, Object data) {
        if (basicHandler != null) {
            Message message = basicHandler.obtainMessage(what, arg1, arg2);
            message.obj = data;
            basicHandler.sendMessage(message);
        }
    }

    /**
     * toast
     *
     * @param resId
     */
    public void toastToMessage(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InfoToast.makeText(BaseActivity.this, getString(resId), Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * toast
     *
     * @param s
     */
    public void toastToMessage(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InfoToast.makeText(BaseActivity.this, s, Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showProgressDialog(int msgResId, boolean cancelable) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setCancelable(cancelable);
            progressDialog.setMessage(msgResId);
            progressDialog.show();
            progressDialog.setOnKeyListener(onKeyListener);
        }
    }

    /**
     * 设置点击返回键可以关闭
     */
    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                hideProgressDialog();
            }
            return false;
        }
    };

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * 得到屏幕的高度
     * @param context
     * @return
     */
    public int getScreenHeight(Context context){
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
