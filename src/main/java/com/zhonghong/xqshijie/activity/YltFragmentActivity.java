package com.zhonghong.xqshijie.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.fragment.YltFragment;

/**
 * Created by xiezl on 16/7/5.
 */
public class YltFragmentActivity extends BaseActivity {

    public static final String CHANNEL = "YLT";//来源
    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_ylt_fragment, null);
        return contentView;
    }

    @Override
    public void handleCreate() {
        initFragment();
    }

    @Override
    protected void customOnClick(View v) {

    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new YltFragment();
        Bundle data = new Bundle();
        data.putString("title", getResources().getString(R.string.menu_ylt));
        data.putString(YltFragmentActivity.CHANNEL, YltFragmentActivity.CHANNEL);
        fragment.setArguments(data);//通过Bundle向Activity中传递值
        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }
}
