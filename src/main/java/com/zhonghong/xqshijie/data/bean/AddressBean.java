package com.zhonghong.xqshijie.data.bean;

import java.io.Serializable;

/**
 * Created by jh on 2016/7/3.
 */
public class AddressBean implements Serializable {
    private String mName;
    private String mAddress;
    private String mPhone;

    public AddressBean(String mName, String mAddress, String mPhone) {
        this.mName = mName;
        this.mAddress = mAddress;
        this.mPhone = mPhone;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "mName='" + mName + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mPhone='" + mPhone + '\'' +
                '}';
    }
}
