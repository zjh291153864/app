package com.zhonghong.xqshijie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {
	public static Map<String, Fragment> fragments = new HashMap<String, Fragment>();
	private static MainActivity mContext;

    public static Fragment getInstanceByIndex(int index, String title, final MainActivity activity) {
        Fragment fragment = null;
		mContext = activity;
        String tag = transTag(index);
        String className = "";
        fragment = fragments.get(tag);

        if (fragment  ==null) {
			fragment = newInstance(index,title);
        }
        
        return fragment;
    }


	private static Fragment newInstance(int index, String title) {
		Fragment newFragment = null;
		Bundle args = new Bundle();
		switch (index) {
			case R.id.menu_home_page_v:
				newFragment = new HomePageFragment();
				break;
			case R.id.menu_scenic_v:
				newFragment = new ScenicFragment();
				break;
			case R.id.menu_ylt_v:
				newFragment = new YltFragment();
				break;
			case R.id.menu_member_center_v:
				newFragment = new MemberCenterFragment();
				break;
			case R.id.menu_newcomer_v:
				newFragment = new WebFragment();
//				Intent intent = new Intent(mContext, WebViewActivity.class);
//				intent.putExtra("url", "https://m.xqshijie.com/");
//				intent.putExtra("title","");
//				mContext.startActivity(intent);
				args.putString("url", "https://m.xqshijie.com/");
				break;
			case R.id.menu_conn_v:
				newFragment = new ConnectionFragment();
				break;
		}
		if (newFragment!=null) {
			args.putString("title", title);
			newFragment.setArguments(args);
			fragments.put(title, newFragment);
		}
		return newFragment;
	}
    
    public static String transTag(int i) {
    	String tag = "";
    	switch (i) {
			case R.id.menu_home_page_v:
				tag = "home_page";
				break;
			case R.id.menu_scenic_v:
				tag = "scenic";
				break;
			case R.id.menu_ylt_v:
				tag = "ylt";
				break;
			case R.id.menu_member_center_v:
				tag = "menber_center";
				break;
			case R.id.menu_newcomer_v:
				tag = "newcomer";
				break;
			case R.id.menu_conn_v:
				tag = "conn";
				break;
		}
    	
    	return tag;
    }
}
