package com.zhonghong.xqshijie.appupdate;

import java.io.Serializable;

/**
 * Created by jh on 2016/7/16.
 */
public class MessageBean implements Serializable{
    public String mMsg;
    public boolean mConnect;
    public MessageBean(String mMsg) {
        this.mMsg = mMsg;
    }
    public MessageBean(boolean mConnect){
        this.mConnect=mConnect;
    }
}
