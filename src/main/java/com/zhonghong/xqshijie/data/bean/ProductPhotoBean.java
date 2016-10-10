package com.zhonghong.xqshijie.data.bean;

import java.io.Serializable;

/**
 * Created by jh on 2016/7/5.
 * 相册基本数据bean
 */
public class ProductPhotoBean implements Serializable {
    private String mPictruTilte;
    private String mPictruUrl;
    private String mPictureType;
    private String mDecreption;
    private String indexInTotalList;

    public String getIndexInTotalList() {
        return indexInTotalList;
    }

    public void setIndexInTotalList(String indexInTotalList) {
        this.indexInTotalList = indexInTotalList;
    }

    public String getmDecreption() {
        return mDecreption;
    }

    public void setmDecreption(String mDecreption) {
        this.mDecreption = mDecreption;
    }

    public ProductPhotoBean(String picture_title, String picture_url) {
        this.mPictruTilte = picture_title;
//        this.mPictruUrl = "https://www.xqshijie.com"+picture_url;//正式环境
        this.mPictruUrl = "http://testwww.xqshijie.com" + picture_url;//测试环境
    }


    public String getmPictruTilte() {
        return mPictruTilte;
    }

    public void setmPictruTilte(String mPictruTilte) {
        this.mPictruTilte = mPictruTilte;
    }

    public String getmPictruUrl() {
        return mPictruUrl;
    }

    public void setmPictruUrl(String mPictruUrl) {
        this.mPictruUrl = mPictruUrl;
    }

    public String getmPictureType() {
        return mPictureType;
    }

    public void setmPictureType(String mPictureType) {
        this.mPictureType = mPictureType;
    }

}
