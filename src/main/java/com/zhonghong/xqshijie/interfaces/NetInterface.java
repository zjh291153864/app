package com.zhonghong.xqshijie.interfaces;

/**
 * Created by xiezl on 16/6/20.
 */
public interface NetInterface {
    /**
     * 接口有正确响应
     * @param result
     */
    public void onNetResult(String interfaceAction,Object result);

    /**
     * 接口调用异常
     * @param ex
     * @param isOnCallback
     */
    public void onNetError(String interfaceAction,Throwable ex, boolean isOnCallback);

    public void onNetFinished(String interfaceAction);

}
