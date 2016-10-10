package com.zhonghong.xqshijie.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by jh on 2016/7/15.
 */
public class PermissionsChecker {

    // 判断权限集合
    public static boolean lacksPermissions(Context context, String... permissions) {

        for (String permission : permissions) {
            if (lacksPermission(permission,context)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private static boolean lacksPermission(String permission, Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }


}