package com.zhonghong.xqshijie.util;


import android.app.Activity;
import android.content.Context;
import android.net.ParseException;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.data.response.LoginResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jh on 2016/6/27.
 */
public class PublicUtils {
    /**
     * 验证手机号是否正确
     *
     * @param telNum
     * @return
     */
    public static boolean isTelNum(String telNum) {
        String matchRule = "^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";

        Pattern p = Pattern.compile(matchRule);

        Matcher matcher = p.matcher(telNum);
        return matcher.matches();
    }


    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     */
    public static boolean iDCardValidate(String IDStr) throws ParseException {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return false;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return false;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return false;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return false;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return false;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return false;
            }
        } else {
            return true;
        }
        // =====================(end)=====================
        return true;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    private static boolean isDataFormat(String str) {
        boolean flag = false;
        // String
        // regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }


    /**
     * 相册转场回调接口
     */
    public interface photoCallBack {
        public void switchPageOne();

        public void switchPageTwo();

        public void switchToFinish();
    }

    /**
     * 相册联动回调接口
     */
    public interface photoTabCallBack {
        public void switchPage(int whichPage);
    }

    /**
     * 相册外层activity回调
     */
    public interface photoOutsideTabCallBack {
        public void switchActivityPage(int whichPage);
    }

    //    1|效果图,2|实景图,3|户型图,4|区位图,5|沙盘图,6|样板间,7|配套图
    //这块图的命名为拼音
    public static final int XIAOGUO_PHOTOTYPE = 1;
    public static final int SHIJING_PHOTOTYPE = 2;
    public static final int HUXING_PHOTOTYPE = 3;
    public static final int QUWEI_PHOTOTYPE = 4;
    public static final int SHAPAN_PHOTOTYPE = 5;
    public static final int YANGBANJIAN_PHOTOTYPE = 6;
    public static final int PEITAO_PHOTOTYPE = 7;

    /**
     * 根据index获取tab名称
     */
    public static String getPhotoNameByIndex(int photoType) {
        String pNmae = null;
        switch (photoType) {
            case XIAOGUO_PHOTOTYPE:
                pNmae = "效果图";
                break;
            case SHIJING_PHOTOTYPE:
                pNmae = "实景图";
                break;
            case HUXING_PHOTOTYPE:
                pNmae = "户型图";
                break;
            case QUWEI_PHOTOTYPE:
                pNmae = "区位图";
                break;
            case SHAPAN_PHOTOTYPE:
                pNmae = "沙盘图";
                break;
            case YANGBANJIAN_PHOTOTYPE:
                pNmae = "样板间";
                break;
            case PEITAO_PHOTOTYPE:
                pNmae = "配套图";
                break;
        }
        return pNmae;
    }


    /**
     * 平铺ListView
     */

    public static int getLvTotalHeight(ListView pull) {
        int totalHeight = 0;
        ListAdapter adapter = pull.getAdapter();
        for (int i = 0, len = adapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, pull);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }
        return totalHeight + (pull.getDividerHeight() * (pull.getCount() - 1));
    }


    //测试数据 后期会删掉 先留着
    public static final String testurl_1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1467794693220&di=d33d35b41418647d2c4dcbc0b88e7d1e&imgtype=jpg&src=http%3A%2F%2Fpic25.nipic.com%2F20121112%2F5955207_224247025000_2.jpg";
    public static final String testurl_2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1467794693220&di=3a6a08e9937f4ca8ed9ffead7393d5a2&imgtype=jpg&src=http%3A%2F%2Fwww.929898.org.cn%2Fimgall%2Fnfwwomjvfyzwy2lbnyxgg33n%2F2015%2Ff2%2F116%2Fd%2F3.jpg";
    public static final String testurl_3 = "https://ss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=cd65e7fa13d5ad6eb5f963eab1cb39a3/377adab44aed2e7394aa5a128f01a18b87d6fa49.jpg\n";
    public static final String testurl_4 = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1467794736171&di=8dac017278415a66840263cee35539ed&imgtype=jpg&src=http%3A%2F%2Fpic24.nipic.com%2F20121003%2F10754047_140022530392_2.jpg";
    public static final String testurl_5 = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1467794736171&di=97c46e408424293469bc208b6260de65&imgtype=jpg&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F140326%2F235113-1403260I33562.jpg";
    public static final String testurl_6 = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1467794736170&di=baf3af4765e05ef41ce6c5a69943f5cd&imgtype=jpg&src=http%3A%2F%2Fpic24.nipic.com%2F20121009%2F4744012_103328385000_2.jpg";
    public static final String testurl_7 = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1467794736169&di=5a1cc3899ecaf6b0b869b6a9aaf8f276&imgtype=jpg&src=http%3A%2F%2Fpic3.nipic.com%2F20090605%2F2166702_095614055_2.jpg";
    public static final String testurl_8 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3603943369,1952417318&fm=21&gp=0.jpg";
    public static final String testurl_9 = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1467794736166&di=7ec22efe81c4082eeb57a50f2178d392&imgtype=jpg&src=http%3A%2F%2Fpic1.nipic.com%2F2008-11-13%2F2008111384358912_2.jpg";
    public static final String testurl_10 = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1467794736166&di=2303d08b013473c2c6a8536cd527c114&imgtype=jpg&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F140720%2F240467-140H00K62786.jpg";
    public static final String testurl_11 = "https://ss1.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/image/h%3D360/sign=a813da3172094b36c4921deb93ce7c00/810a19d8bc3eb135aa449355a21ea8d3fc1f4458.jpg";
    public static final String testurl_12 = "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=3265fc1289d4b31cef3c93bbb7d7276f/0d338744ebf81a4c29870b98d52a6059252da62e.jpg";
    public static final String testurl_13 = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1467968450237&di=8b62ae90eef60cbb1f236c49030ab566&imgtype=jpg&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F35a85edf8db1cb138964d417df54564e92584ba1.jpg";
    public static final String testurl_14 = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1467968478949&di=c80d799ac923e8bf5434f9f01607eebf&imgtype=jpg&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F4e4a20a4462309f77a31c81b700e0cf3d7cad659.jpg";

    public static void setTranslucentStatus(Activity activity, int resclorid) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(resclorid);
            activity.getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }

    //保存用户信息
    public static void saveUserInformation(Context context, LoginResponse loginResponse) {
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_ID, loginResponse.mMember.mMemberId);//用户ID
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_FULLNAME, loginResponse.mMember.mMemberFullname);//用户全名
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_MOBILE, loginResponse.mMember.mMemberMobile);//用户手机号
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_ID_NUMBER, loginResponse.mMember.mMemberIdNumber);//用户身份证号
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_GENDER, loginResponse.mMember.mMemberGender);//性别(1|男,2|女) 传1或2
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_AVATAR, loginResponse.mMember.mMemberAvatar);//用户头像
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_NICKNAME, loginResponse.mMember.mMemberNickname);//用户昵称
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_PASSWORD, loginResponse.mMember.mMemberPassword);//用户密码
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.LAST_LOGIN, loginResponse.mMember.mLastLogin);//最后登录时间
        SharedPreferencesUtil.getInstance(context).putBooleanValue(SharedPreferencesTag.LOGIN, true);//保存登陆状态
    }

    public static void removeUserInformation(Context context) {
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_ID, "");//用户ID
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_FULLNAME, "");//用户全名
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_MOBILE, "");//用户手机号
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_ID_NUMBER, "");//用户身份证号
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_GENDER, "");//性别(1|男,2|女) 传1或2
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_AVATAR, "");//用户头像
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_NICKNAME, "");//用户昵称
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.MEMBER_PASSWORD, "");//用户密码
        SharedPreferencesUtil.getInstance(context).putStringValue(SharedPreferencesTag.LAST_LOGIN, "");//最后登录时间
        SharedPreferencesUtil.getInstance(context).putBooleanValue(SharedPreferencesTag.LOGIN, false);//保存登陆状态
    }

    /**
     * 获取控件宽高
     */
    public static int getWH(View v, String gettype) {
        int wS = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int hS = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        v.measure(wS, hS);
        int height = v.getMeasuredHeight();
        int width = v.getMeasuredWidth();
        if (gettype.equals("w")) {
            return width;
        } else if (gettype.equals("h")) {
            return height;
        } else {
            return -1;
        }
    }

    /**
     * 防呆
     */
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}