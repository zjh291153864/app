package com.zhonghong.xqshijie.util;

import android.content.Context;

import com.zhonghong.xqshijie.app.MyApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by jh on 2016/7/4.
 */
public final class AssetsUtils {
    /**
     * 从Assets 中读取文件
     *
     * @param mContext
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context mContext,String fileName) {
        StringBuffer Result = new StringBuffer();
        try {
            InputStreamReader inputReader = new InputStreamReader(mContext.getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                Result.append(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.toString();
    }
}
