package com.zhonghong.xqshijie.util;

import android.text.TextUtils;
import android.util.Log;

import com.zhonghong.xqshijie.app.Constants;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	// 秒转成分钟
	public static String sTOm(int second){
		if(second<60){
			return second+"\"";
		}else if(second==60){
			return "1\'";
		}else{
			int m = second/60;
			int s = second%60;
			return m+"\'"+s+"\"";
		}
	}
	
	// 判断字符串是否为空
	public static boolean isNull(String s){
		if(s == null || s.equals("") || s.length() == 0){
			return true;
		} else {
			return false;
		}
	}
	
	public static int length(String str){
		int length = 0;
		try {
			if(!StringUtils.isNull(str)){
				length = str.getBytes("UTF-8").length;
			}
		} catch (UnsupportedEncodingException e) {
			Log.d(Constants.LOG_TAG, e.getMessage(), e);
		}
		return length;
	}

	/**
     * 替换Null
     * @param str
     * @return
     */
    public static String repNull(String str) {
        if(str==null)str="";
        return str;
    }
    

    /**
     * 去除字符串中的空格回车
     * @param sub
     * @return
     */
	public static String subString(String sub) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(sub);
		return m.replaceAll("");
	}
	
	public static boolean
	isMobile(String mobile){
		return isNull(mobile) ? false : mobile.length() == 11 || mobile.length() == 15 ? true : false;
	}
	
	/**
	 * 根据数字月份转换为英文缩写
	 * @param monthNum
	 * @return
	 */
	public static String MonthNumToMonthStr(String monthNum) {
		if ("01".equals(monthNum)) {
			return "Jan";
		}else if ("02".equals(monthNum)) {
			return "Feb";
		}else if ("03".equals(monthNum)) {
			return "Mar";
		}else if ("04".equals(monthNum)) {
			return "Apr";
		}else if ("05".equals(monthNum)) {
			return "May";
		}else if ("06".equals(monthNum)) {
			return "Jun";
		}else if ("07".equals(monthNum)) {
			return "Jul";
		}else if ("08".equals(monthNum)) {
			return "Aug";
		}else if ("09".equals(monthNum)) {
			return "Sep";
		}else if ("10".equals(monthNum)) {
			return "Oct";
		}else if ("11".equals(monthNum)) {
			return "Nov";
		}else if ("12".equals(monthNum)) {
			return "Dec";
		}
		return "";
	}
	
	/**
	 * 验证密码是否正确
	 * 
	 * @return 匹配正确返回true，否则返回false
	 */
	public static boolean isRigthPW(String newPasswordStr) {
		return newPasswordStr.matches("^(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9]{6,18}$");
	}
	
	/**
	 * 验证密码是否正确
	 * 
	 *            新密码
	 * @return 匹配正确返回true，否则返回false
	 */
	public static String getChineseAndEnglish(String str) {
		if(TextUtils.isEmpty(str)){
			return null;
		}
		StringBuilder lastName = new StringBuilder();
		Pattern p = null;
		Matcher m = null;
		String value = null;
		p = Pattern.compile("([a-zA-Z\u4e00-\u9fa5\uF900-\uFA2D]+)");
		m = p.matcher(str);
		while (m.find()) {
			value = m.group(0);
			lastName.append(value);
		}
		return lastName.toString();
	}

	/**
	 * 创建指定数量的随机字符串
	 * @param numberFlag 是否是数字
	 * @param length
	 * @return
	 */
	public static String createRandom(boolean numberFlag, int length){
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);

		return retStr;
	}
}
