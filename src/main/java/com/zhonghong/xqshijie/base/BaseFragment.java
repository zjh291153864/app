package com.zhonghong.xqshijie.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhonghong.xqshijie.app.Constants;
import com.zhonghong.xqshijie.appupdate.MessageBean;
import com.zhonghong.xqshijie.dialog.InfoProgressDialog;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public abstract class BaseFragment extends Fragment implements View.OnClickListener, NetInterface {


    protected boolean isVisible;

    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * fragment被设置为可见时调用
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 该方法在onVisible里面调用
     */
    protected abstract void lazyLoad();

    /**
     * fragment被设置为不可见时调用
     */
    protected void onInvisible() {
    }


    protected Activity mActivity;
    public InfoProgressDialog progressDialog;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    /**
     * 多个fragment  需要初始化一次的操作放到 onCreate 处理
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new InfoProgressDialog(getActivity());
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = initContentView(inflater, container, savedInstanceState);
        handleCreate();
        //初始化根据当前的网络状态显示异常与否
        aboutException(NetUtils.isNetConnected());
        return view;
    }


    /**
     * 初始化view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public abstract View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 处理业务逻辑
     */
    protected abstract void handleCreate();

    /**
     * Handler消息处理
     *
     * @param msg
     */
    protected abstract void processMessage(Message msg);

    @Override
    public void onClick(View v) {
        customOnClick(v);
    }

    /**
     * 页面点击事件处理方法
     *
     * @param v
     */
    protected abstract void customOnClick(View v);

    /**
     * 得到屏幕的高度
     *
     * @param context
     * @return
     */
    public int getScreenHeight(Context context) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public void toastToMessage(int resId) {
        InfoToast.makeText(getActivity()
                , getString(resId)
                , Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT).show();
    }

    public void toastToMessage(String s) {
        InfoToast.makeText(getActivity()
                , s
                , Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(int msgResId, boolean cancelable) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setCancelable(cancelable);
            progressDialog.setMessage(msgResId);
            progressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void startActvity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(MessageBean event) {
        if (event.mMsg == null) {
            aboutException(event.mConnect);
        }
    }

    /**
     * 头部联网异常的显示
     *
     * @param connect
     */
    protected void aboutException(boolean connect) {
    }

    ;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
