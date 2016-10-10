package com.zhonghong.xqshijie.data.bean;

public class MenuleftItemBean {
    private int resourceId;
    private int img;
    private String title;
    private boolean isUpdate = false;

    public MenuleftItemBean() {
    }

    public MenuleftItemBean(int resourceId, int img, String title, boolean isUpdate) {
        this.resourceId = resourceId;
        this.img = img;
        this.title = title;
        this.isUpdate = isUpdate;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
