package com.zhonghong.xqshijie.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseFragment;
import com.zhonghong.xqshijie.widget.TitleView;

/**
 * Created by xiezl on 16/6/14.
 */
public class NewComerFragment extends BaseFragment {

    private TitleView mTitle;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_newcomer, null);
        mTitle = (TitleView)contentView.findViewById(R.id.title);
        //初始化view的各控件完成
        isPrepared = true;
        lazyLoad();
        return contentView;
    }

    @Override
    protected void handleCreate() {
        if (getArguments()!=null&&getArguments().get("title")!=null){
            mTitle.setTitle((String) getArguments().get("title"));
        }

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }

    @Override
    protected void processMessage(Message msg) {

    }

    @Override
    protected void customOnClick(View v) {

    }

    @Override
    public void onNetResult(String interfaceAction, Object result) {

    }

    @Override
    public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onNetFinished(String interfaceAction) {

    }
}
