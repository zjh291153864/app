/**
 * Copyright 2014 Zhenguo Jin
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhonghong.xqshijie.util;

import android.content.Context;

import com.zhonghong.xqshijie.R;

/**
 * APP工具类
 *
 */
public final class TimeFormatUtils {

//
//    public long getRemainingTime(Context context,long createTime) {
//        return System.currentTimeMillis() - createTime;
//    }

    public static StringBuffer dealTime(Context context,Long time) {
        StringBuffer returnString = new StringBuffer();
        long day = time / (24 * 60 * 60);
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
        String dayStr = String.valueOf(day);
        String hoursStr = timeStrFormat(String.valueOf(hours));
        String minutesStr = timeStrFormat(String.valueOf(minutes));
        String secondStr = timeStrFormat(String.valueOf(second));
        if (day != 0) {
            returnString.append(dayStr).append(context.getResources().getString(R.string.day));
        }
        if (day!=0||hours != 0) {
            returnString.append(hoursStr).append(context.getResources().getString(R.string.time));
        }
        if (day==0&&hours != 0 || minutes != 0) {
            returnString.append(minutesStr).append(context.getResources().getString(R.string.minute));
        }
        if (hours==0) {
            returnString.append(secondStr).append(context.getResources().getString(R.string.second));
        }
        return returnString;

    }

    private static String timeStrFormat(String timeStr) {
        switch (timeStr.length()) {
            case 1:
                timeStr = "0" + timeStr;
                break;
        }
        return timeStr;
    }

    public static StringBuffer dealTime(Context context,Long time,boolean showSecond) {
        StringBuffer returnString = new StringBuffer();
        long day = time / (24 * 60 * 60);
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
        String dayStr = String.valueOf(day);
        String hoursStr = timeStrFormat(String.valueOf(hours));
        String minutesStr = timeStrFormat(String.valueOf(minutes));
        String secondStr = timeStrFormat(String.valueOf(second));
        if (day != 0) {
            returnString.append(dayStr).append(context.getResources().getString(R.string.day));
        }
        if (day!=0||hours != 0) {
            returnString.append(hoursStr).append(context.getResources().getString(R.string.time));
        }
        if (day!=0&&hours != 0 || minutes != 0) {
            returnString.append(minutesStr).append(context.getResources().getString(R.string.minute));
        }
//        if (hours == 0) {
//            returnString.append(secondStr).append(context.getResources().getString(R.string.second));
//        }
        return returnString.append(secondStr).append(context.getResources().getString(R.string.second));

    }
}
