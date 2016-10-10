package com.zhonghong.xqshijie.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zhonghong.xqshijie.app.ActivityStack;

import java.io.File;

/**
 * Created by xiezl on 16/7/11.
 */
public class VersionUpdateUtils {
    /**
     * 调用系统的安装提示 Tools.Instanll()<BR>
     *
     * @param file
     * @param context
     */
    public static void Instanll(File file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 退出程序 注意：该方法只能够运行在主线程里面<BR>
     */
    public static void exitApplication() {
        ActivityStack.getInstance().popAllActivityExcept();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
