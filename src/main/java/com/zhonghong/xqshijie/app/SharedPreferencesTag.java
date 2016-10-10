package com.zhonghong.xqshijie.app;

/**
 * SharedPreferencesTag,常量类
 */
public class SharedPreferencesTag {
    public static final String DEMO_KEY="demo_key";

    public static final String DOMAIN_URL="domain_url";//可配置的域名

    /**
     * SharedPreferences存储数据说明
     */
    public static final String PWDCODE="pwdCode";//String     密码三位随机数
    public static final String NEWPWDCODE="newPwdCode";//String     新生成密码三位随机数
    public static final String MEMBER_ID="member_id";//String	    用户id
    public static final String MEMBER_FULLNAME="member_fullname";//String	    用户全名
    public static final String MEMBER_MOBILE="member_mobile";//String	    用户手机号
    public static final String MEMBER_ID_NUMBER="member_id_number";//String	    用户身份证号
    public static final String MEMBER_GENDER="member_gender";//String	    性别(1|男,2|女) 传1或2
    public static final String MEMBER_AVATAR="member_avatar";//String	    用户头像
    public static final String MEMBER_NICKNAME="member_nickname";//String	    用户昵称
    public static final String MEMBER_PASSWORD="member_password";//String	    用户密码
    public static final String LAST_LOGIN="last_login";//String	    最后登录时间
    public static final String LOGIN="login";//boolean	是否登陆
}
