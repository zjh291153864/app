package com.zhonghong.xqshijie.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by xiezl on 16/6/14.
 */
public class BitmapByGlideUtil {


    /**
     * 通过 glide 加载图片
     * @param mContext
     * @param picRemoteUrl 如果不为空, 加载远程
     * @param picLocalUrl 如果不为空且本地存在, 加载本地
     * @param defaultImageID 加载本地默认图
     * @param picView
     */
    public static void loadPicByGlide(Context mContext, String picRemoteUrl, String picLocalUrl, int defaultImageID, ImageView picView) {
        try {
            if (!StringUtils.isNull(picLocalUrl)) {
                File localPic = new File(picLocalUrl);
                if (localPic.exists()) {
                    Glide.with(mContext).load(picLocalUrl).centerCrop().placeholder(defaultImageID).crossFade().into(picView);
                    return;
                }
            }
            if (!StringUtils.isNull(picRemoteUrl)) {
                Glide.with(mContext).load(picRemoteUrl).centerCrop().placeholder(defaultImageID).crossFade().into(picView);
            } else if (defaultImageID != 0) {
                Glide.with(mContext).load(defaultImageID).centerCrop().placeholder(defaultImageID).crossFade().into(picView);
            }  else {
                return;
            }

        } catch (Exception e) {
        }

    }
}
