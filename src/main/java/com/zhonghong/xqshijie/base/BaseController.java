package com.zhonghong.xqshijie.base;

import android.content.Context;


public abstract class BaseController {

    protected Context mContext;

    public BaseController(Context mContext) {
        super();
        this.mContext = mContext;
    }

}
