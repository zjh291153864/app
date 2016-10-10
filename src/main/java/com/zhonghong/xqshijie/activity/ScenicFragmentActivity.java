package com.zhonghong.xqshijie.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.fragment.HomePageFragment;
import com.zhonghong.xqshijie.fragment.ScenicFragment;

/**
 * Created by xiezl on 16/7/5.
 */
public class ScenicFragmentActivity extends BaseActivity {
    private String mProvincId;
    private String mCityId;

    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_scenic_fragment, null);
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
        mProvincId = getIntent().getStringExtra(HomePageFragment.PROVINC_ID);
        mCityId = getIntent().getStringExtra(HomePageFragment.CITY_ID);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new ScenicFragment();
        Bundle data = new Bundle();
        data.putString("title", getResources().getString(R.string.menu_scenic));
        data.putString(HomePageFragment.PROVINC_ID, mProvincId);
        data.putString(HomePageFragment.CITY_ID, mCityId);
        fragment.setArguments(data);//通过Bundle向Activity中传递值
        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }
}
