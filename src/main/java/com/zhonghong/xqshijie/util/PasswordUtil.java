package com.zhonghong.xqshijie.util;

import android.content.Context;
import android.util.Log;

import com.zhonghong.xqshijie.app.SharedPreferencesTag;

import im.yixin.sdk.util.StringUtil;

/**
 * Created by jh on 2016/7/6.
 */
public class PasswordUtil {

//    注册对密码明文加密
    public static String encryptPassword(String password,Context mContext){
        String random="";
        StringBuffer sb= new StringBuffer();
        StringBuffer sb2= new StringBuffer();
        random=StringUtils.createRandom(false,3);//生成三个随机数
        SharedPreferencesUtil.getInstance(mContext).putStringValue(SharedPreferencesTag.NEWPWDCODE,random);
        sb2.append(random);
        sb2.append(MD5.bytes2hex(MD5.md5(password.getBytes())));
        sb.append(MD5.bytes2hex(MD5.md5((sb2.toString()).getBytes())));
        sb.append(":");
        sb.append(random);
        return sb.toString();
    };
//    登陆对密码明文加密
    public static String logEncryptPassword(String password,String str){
        String random=str;
        StringBuffer sb= new StringBuffer();
        StringBuffer sb2= new StringBuffer();
        sb2.append(random);
        sb2.append(MD5.bytes2hex(MD5.md5(password.getBytes())));
        sb.append(MD5.bytes2hex(MD5.md5((sb2.toString()).getBytes())));
        sb.append(":");
        sb.append(random);
        return sb.toString();
    };
}
