package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jh on 2016/7/4.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class AddressJsonResponse implements Serializable{
    /**
     * result : 01
     * Regions : {"provinces":[{"citys":[{"id":"1001","name":"北京辖区","districts":[{"id":"1001001","name":"东城区"},{"id":"1001002","name":"西城区"},{"id":"1001003","name":"朝阳区"},{"id":"1001004","name":"丰台区"},{"id":"1001005","name":"石景山区"},{"id":"1001006","name":"海淀区"},{"id":"1001007","name":"门头沟区"},{"id":"1001008","name":"房山区"},{"id":"1001009","name":"通州区"},{"id":"1001010","name":"顺义区"},{"id":"1001011","name":"昌平区"},{"id":"1001012","name":"大兴区"},{"id":"1001013","name":"怀柔区"},{"id":"1001014","name":"平谷区"}]},{"id":"1002","name":"北京辖县","districts":[{"id":"1002001","name":"密云县"},{"id":"1002002","name":"延庆县"}]}],"id":"1","name":"北京市"}]}
     */
    @SerializedName("result")
    public String mResult;
    @SerializedName("Regions")
    public RegionsBean mRegions;

    public static class RegionsBean {
        /**
         * citys : [{"id":"1001","name":"北京辖区","districts":[{"id":"1001001","name":"东城区"},{"id":"1001002","name":"西城区"},{"id":"1001003","name":"朝阳区"},{"id":"1001004","name":"丰台区"},{"id":"1001005","name":"石景山区"},{"id":"1001006","name":"海淀区"},{"id":"1001007","name":"门头沟区"},{"id":"1001008","name":"房山区"},{"id":"1001009","name":"通州区"},{"id":"1001010","name":"顺义区"},{"id":"1001011","name":"昌平区"},{"id":"1001012","name":"大兴区"},{"id":"1001013","name":"怀柔区"},{"id":"1001014","name":"平谷区"}]},{"id":"1002","name":"北京辖县","districts":[{"id":"1002001","name":"密云县"},{"id":"1002002","name":"延庆县"}]}]
         * id : 1
         * name : 北京市
         */
        @SerializedName("provinces")
        public List<ProvincesBean> mProvinces;

        public static class ProvincesBean {
            @SerializedName("id")
            public String mId;
            @SerializedName("name")
            public String mName;
            /**
             * id : 1001
             * name : 北京辖区
             * districts : [{"id":"1001001","name":"东城区"},{"id":"1001002","name":"西城区"},{"id":"1001003","name":"朝阳区"},{"id":"1001004","name":"丰台区"},{"id":"1001005","name":"石景山区"},{"id":"1001006","name":"海淀区"},{"id":"1001007","name":"门头沟区"},{"id":"1001008","name":"房山区"},{"id":"1001009","name":"通州区"},{"id":"1001010","name":"顺义区"},{"id":"1001011","name":"昌平区"},{"id":"1001012","name":"大兴区"},{"id":"1001013","name":"怀柔区"},{"id":"1001014","name":"平谷区"}]
             */
            @SerializedName("citys")
            public List<CitysBean> mCitys;

            public static class CitysBean {
                @SerializedName("id")
                public String mId;
                @SerializedName("name")
                public String mName;
                /**
                 * id : 1001001
                 * name : 东城区
                 */
                @SerializedName("districts")
                public List<DistrictsBean> mDistricts;

                public static class DistrictsBean {
                    @SerializedName("id")
                    public String mId;
                    @SerializedName("name")
                    public String mName;

                    public DistrictsBean(String id, String name) {
                        this.mId = id;
                        this.mName = name;
                    }

                    @Override
                    public String toString() {
                        return "DistrictsBean{" +
                                "mId='" + mId + '\'' +
                                ", mName='" + mName + '\'' +
                                '}';
                    }
                }

                @Override
                public String toString() {
                    return "CitysBean{" +
                            "mId='" + mId + '\'' +
                            ", mName='" + mName + '\'' +
                            ", mDistricts=" + mDistricts +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "ProvincesBean{" +
                        "mId='" + mId + '\'' +
                        ", mName='" + mName + '\'' +
                        ", mCitys=" + mCitys +
                        '}';
            }
        }
    }
}
