package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jh on 2016/7/10.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class AddressResponse implements Serializable{
    /**
     * addressList : [{"city_id":"1001","id":"16","consignee_name":"2222","address":"11111","province_id":"1","county_id":"0","is_default":false,"consignee_mobile":"33333","member_id":"82"},{"city_id":"0","id":"17","consignee_name":"2222","address":"111","province_id":"0","county_id":"0","is_default":false,"consignee_mobile":"3333","member_id":"82"},{"city_id":"1001","id":"18","consignee_name":"tytyty","address":"ttttt","province_id":"1","county_id":"0","is_default":false,"consignee_mobile":"tyty","member_id":"82"},{"city_id":"0","id":"19","consignee_name":"343434","address":"3434343","province_id":"0","county_id":"0","is_default":false,"consignee_mobile":"34343434","member_id":"82"},{"city_id":"0","id":"20","consignee_name":"23232323","address":"232323","province_id":"0","county_id":"0","is_default":false,"consignee_mobile":"15910832163","member_id":"82"},{"city_id":"1001","id":"21","consignee_name":"454545","address":"3434","province_id":"1","county_id":"0","is_default":false,"consignee_mobile":"13845454545","member_id":"82"},{"city_id":"1001","id":"26","consignee_name":"李蒙","address":"123123123","province_id":"1","county_id":"0","is_default":true,"consignee_mobile":"15910832163","member_id":"82"},{"city_id":"1001","id":"109","consignee_name":"李蒙1","address":"北京1","province_id":"1","county_id":"1001001","is_default":false,"consignee_mobile":"15910832161","member_id":"82"}]
     * result : 01
     */
    @SerializedName("result")
    public String mResult;//状态
    @SerializedName("msg")
    public String mMsg;//消息
    /**
     * city_id : 1001
     * id : 16
     * consignee_name : 2222
     * address : 11111
     * province_id : 1
     * county_id : 0
     * is_default : false
     * consignee_mobile : 33333
     * member_id : 82
     */
    @SerializedName("addressList")
    public List<AddressListBean> mAddressList;//地址集合

    public static class AddressListBean implements Serializable{
        @SerializedName("address_id")
        public String mAddressId;//地址id(修改为address_id)
        @SerializedName("province_id")
        public String mProvinceId;//省id
        @SerializedName("city_id")
        public String mCityId;//市id
        @SerializedName("county_id")
        public String mCountyId;//区id
        @SerializedName("province_name")
        public String mProvinceName;//省
        @SerializedName("city_name")
        public String mCityName;//市
        @SerializedName("county_name")
        public String mCountyName;//区
        @SerializedName("address")
        public String mAddress;//详细地址
        @SerializedName("consignee_name")
        public String mConsigneeName;//联系人名称
        @SerializedName("consignee_mobile")
        public String mConsigneeMobile;//联系人手机号
        @SerializedName("is_default")
        public boolean mIsDefault;//是否默认
        @SerializedName("member_id")
        public String mMemberId;//用户id

        @Override
        public String toString() {
            return "AddressListBean{" +
                    "mAddressId='" + mAddressId + '\'' +
                    ", mProvinceId='" + mProvinceId + '\'' +
                    ", mCityId='" + mCityId + '\'' +
                    ", mCountyId='" + mCountyId + '\'' +
                    ", mAddress='" + mAddress + '\'' +
                    ", mConsigneeName='" + mConsigneeName + '\'' +
                    ", mConsigneeMobile='" + mConsigneeMobile + '\'' +
                    ", mIsDefault=" + mIsDefault +
                    ", mMemberId='" + mMemberId + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AddressResponse{" +
                "mResult='" + mResult + '\'' +
                ", mMsg='" + mMsg + '\'' +
                ", mAddressList=" + mAddressList +
                '}';
    }
}
