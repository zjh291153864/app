package com.zhonghong.xqshijie.data.response;

import java.io.Serializable;

/**
 * Created by xiezl on 16/7/7.
 */
public class BaseResponse implements Serializable {
   public boolean isCache = false;//是否需要缓存
   public String netPageNo ="-1";
}
