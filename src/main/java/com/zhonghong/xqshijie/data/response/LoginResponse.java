package com.zhonghong.xqshijie.data.response;

import com.google.gson.annotations.SerializedName;
import com.zhonghong.xqshijie.net.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by xiezl on 16/6/29.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class LoginResponse {

    /**
     * result : 01
     * msg : 手机号已注册
     */
    @SerializedName("result")
    public String mResult;//返回响应

    @SerializedName("msg")
    public String mMsg;//返回消息

    @SerializedName("pwd")
    public String mPwd;//密码字符串

    @SerializedName("validate_code")
    public String mVaildateCode;//短信验证码

    @SerializedName("member")
    public memberBean mMember;//用户对象

    public static class memberBean{
        @SerializedName("member_id")
        public String mMemberId;//用户id
        @SerializedName("member_fullname")
        public String mMemberFullname;//用户全名
        @SerializedName("member_mobile")
        public String mMemberMobile;//用户手机号
        @SerializedName("member_id_number")
        public String mMemberIdNumber;//用户身份证号
        @SerializedName("member_gender")
        public String mMemberGender;//性别(1|男,2|女) 传1或2
        @SerializedName("member_avatar")
        public String mMemberAvatar;//用户头像
        @SerializedName("member_nickname")
        public String mMemberNickname;//用户昵称
        @SerializedName("member_password")
        public String mMemberPassword;//用户密码
        @SerializedName("last_login")
        public String mLastLogin;//最后登录时间

        @Override
        public String toString() {
            return "memberBean{" +
                    "mMemberId='" + mMemberId + '\'' +
                    ", mMemberFullname='" + mMemberFullname + '\'' +
                    ", mMemberMobile='" + mMemberMobile + '\'' +
                    ", mMemberIdNumber='" + mMemberIdNumber + '\'' +
                    ", mMemberGender='" + mMemberGender + '\'' +
                    ", mMemberAvatar='" + mMemberAvatar + '\'' +
                    ", mMemberNickname='" + mMemberNickname + '\'' +
                    ", mMemberPassword='" + mMemberPassword + '\'' +
                    ", mLastLogin='" + mLastLogin + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "mResult='" + mResult + '\'' +
                ", mMsg='" + mMsg + '\'' +
                ", mPwd='" + mPwd + '\'' +
                ", mVaildateCode='" + mVaildateCode + '\'' +
                ", mMember=" + mMember +
                '}';
    }
}
