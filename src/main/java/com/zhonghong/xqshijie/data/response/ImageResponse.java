package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by jh on 2016/7/14.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ImageResponse {
    /**
     * code : 1
     * msg : []
     * data : {"encrypt":"http://test.static.xqshijie.com/qmjjr/orgi/cert/20160711/MTQ2ODIyMDg5ODA2Ny03MDc5ODMjcW1qanIjMy1lbmNyeXB0.jpg","spread":"http://test.static.xqshijie.com/qmjjr/orgi/cert/20160711/MTQ2ODIyMDg5ODA2Ny03MDc5ODMtb3JnaQ==.jpg","original":"http://test.static.xqshijie.com/qmjjr/orgi/cert/20160711/MTQ2ODIyMDg5ODA2Ny03MDc5ODMtb3JnaQ==.jpg","images":["http://test.static.xqshijie.com/qmjjr/orgi/cert/20160713/MTQ2ODM5MzU5OTI3NC03MDc5ODMtb3JnaQ==-224x168.jpg","http://test.static.xqshijie.com/qmjjr/orgi/cert/20160713/MTQ2ODM5MzU5OTI3NC03MDc5ODMtb3JnaQ==-24x18.jpg","http://test.static.xqshijie.com/qmjjr/orgi/cert/20160713/MTQ2ODM5MzU5OTI3NC03MDc5ODMtb3JnaQ==-424x318.jpg","http://test.static.xqshijie.com/qmjjr/orgi/cert/20160713/MTQ2ODM5MzU5OTI3NC03MDc5ODMtb3JnaQ==-624x468.jpg","http://test.static.xqshijie.com/qmjjr/orgi/cert/20160713/MTQ2ODM5MzU5OTI3NC03MDc5ODMtb3JnaQ==-824x618.jpg"]}
     */
    @SerializedName("code")
    public int mCode;
    /**
     * encrypt : http://test.static.xqshijie.com/qmjjr/orgi/cert/20160711/MTQ2ODIyMDg5ODA2Ny03MDc5ODMjcW1qanIjMy1lbmNyeXB0.jpg
     * spread : http://test.static.xqshijie.com/qmjjr/orgi/cert/20160711/MTQ2ODIyMDg5ODA2Ny03MDc5ODMtb3JnaQ==.jpg
     * original : http://test.static.xqshijie.com/qmjjr/orgi/cert/20160711/MTQ2ODIyMDg5ODA2Ny03MDc5ODMtb3JnaQ==.jpg
     * images : ["http://test.static.xqshijie.com/qmjjr/orgi/cert/20160713/MTQ2ODM5MzU5OTI3NC03MDc5ODMtb3JnaQ==-224x168.jpg","http://test.static.xqshijie.com/qmjjr/orgi/cert/20160713/MTQ2ODM5MzU5OTI3NC03MDc5ODMtb3JnaQ==-24x18.jpg","http://test.static.xqshijie.com/qmjjr/orgi/cert/20160713/MTQ2ODM5MzU5OTI3NC03MDc5ODMtb3JnaQ==-424x318.jpg","http://test.static.xqshijie.com/qmjjr/orgi/cert/20160713/MTQ2ODM5MzU5OTI3NC03MDc5ODMtb3JnaQ==-624x468.jpg","http://test.static.xqshijie.com/qmjjr/orgi/cert/20160713/MTQ2ODM5MzU5OTI3NC03MDc5ODMtb3JnaQ==-824x618.jpg"]
     */
    @SerializedName("data")
    public DataBean mData;
    @SerializedName("msg")
    public List<String> mMsg;

    public static class DataBean {
        @SerializedName("encrypt")
        public String mEncrypt;
        @SerializedName("spread")
        public String mSpread;
        @SerializedName("original")
        public String mOriginal;
        @SerializedName("images")
        public List<String> mImages;

        @Override
        public String toString() {
            return "DataBean{" +
                    "mEncrypt='" + mEncrypt + '\'' +
                    ", mSpread='" + mSpread + '\'' +
                    ", mOriginal='" + mOriginal + '\'' +
                    ", mImages=" + mImages +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ImageResponse{" +
                "mCode=" + mCode +
                ", mData=" + mData +
                ", mMsg='" + mMsg + '\'' +
                '}';
    }
}
