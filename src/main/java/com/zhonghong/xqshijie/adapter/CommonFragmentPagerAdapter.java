package com.zhonghong.xqshijie.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.zhonghong.xqshijie.base.BaseFragment;

import java.util.List;

/**
 * Created by jh on 2016/7/5.
 */
public class CommonFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    List<Fragment> mFragmentList = null;

    public CommonFragmentPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (null != mFragmentList) {
            return mFragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
