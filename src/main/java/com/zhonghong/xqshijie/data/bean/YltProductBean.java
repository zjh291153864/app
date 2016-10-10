package com.zhonghong.xqshijie.data.bean;

/**
 * Created by jh on 2016/6/28.
 */
public class YltProductBean {

    private String imgUrl = null;
    private String proName = null;
    private String startPrice = null;
    private String endPrice = null;

    public YltProductBean(String imgUrl, String proName, String startPrice, String endPrice) {
        this.imgUrl = imgUrl;
        this.proName = proName;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
    }

    @Override
    public String toString() {
        return "YltProductBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", proName='" + proName + '\'' +
                ", startPrice='" + startPrice + '\'' +
                ", endPrice='" + endPrice + '\'' +
                '}';
    }

    public YltProductBean() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(String endPrice) {
        this.endPrice = endPrice;
    }
}
