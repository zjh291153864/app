package com.zhonghong.xqshijie.net;

import org.xutils.common.Callback;

/**
 * Created by xiezl on 16/6/21.
 */


public class NetCallBack<ResultType> implements Callback.CommonCallback<ResultType> {
    @Override
    public void onSuccess(ResultType result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

//    @Override
//    public void onSuccess(ResultType result) {
//        //可以根据需求进行统一的请求成功的逻辑处理
//    }
//
//    @Override
//    public void onError(Throwable ex, boolean isOnCallback) {
//        //可以根据需求进行统一的请求网络失败的逻辑处理
//    }
//
//    @Override
//    public void onCancelled(CancelledException cex) {
//
//    }
//
//    @Override
//    public void onFinished() {
//
//    }


}
