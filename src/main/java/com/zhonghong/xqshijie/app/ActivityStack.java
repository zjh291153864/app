package com.zhonghong.xqshijie.app;

/**
 * 自定义activity管理栈<br>
 * 满足dock栏activity返回定位功能<br>
 */

import android.app.Activity;

import java.util.Iterator;
import java.util.Vector;

public class ActivityStack {
	private Vector<Activity> activityStack;
	private static ActivityStack instance;

	private ActivityStack() {
	}

	public static ActivityStack getInstance() {
		if (instance == null) {
			instance = new ActivityStack();
		}
		return instance;
	}

	public void popActivity(Activity activity) {
		if (activity != null) {
			if (!activity.isFinishing()) {
				activity.finish();
			}			
			activityStack.remove(activity);
			activity = null;
		}
	}

	public void popAllActivityExcept(Class<?>... classes) {
		if (null == activityStack) {
			return;
		}		
		
		Iterator<Activity> it = activityStack.iterator();
		while (it.hasNext()) {
			Activity activity = it.next();
			if ((classes != null) && (classes.length > 0)) {
				boolean hasActiviy = false;
				for (Class<?> cls : classes) {
					if (cls.equals(activity.getClass())) {
						hasActiviy = true;
						break;
					}
				}

				if (hasActiviy) {					
					continue;
				}
			}

			if (!activity.isFinishing()) {
				activity.finish();
			}
			
			it.remove();
			activity = null;
		}
	}

	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Vector<Activity>();
		}
		activityStack.add(activity);
	}
}
