package com.zhonghong.xqshijie.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jh on 2016/7/6.
 * 相册排序时用到的数据bean，将容器和类别进行分类
 */
public class ProductPhotoMappingBean implements Serializable {
    private String name = null;
    private List<ProductPhotoBean> data = null;
    private String type = null;


    public ProductPhotoMappingBean(String name, List<ProductPhotoBean> data, String type) {
        this.name = name;
        this.data = data;
        this.type = type;
        if(data!=null && data.size()>0){
            int index = 0;
            for (ProductPhotoBean ele : data) {
                index++;
                ele.setmPictureType(type);
                ele.setmDecreption(index + "/" + data.size());
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductPhotoBean> getData() {
        return data;
    }

    public void setData(List<ProductPhotoBean> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
