package com.xzy.weather.util;

import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Author:xzy
 * Date:2020/8/20 19:50
 **/
public class TimeUtil {

    private static final String TAG = "TimeUtil";

    public static int[] MONTH_DAY_COUNT = {
            0,31,28,31,30,31,30,31,31,30,31,30,31
    };

    /**
     * 获取和风天气返回时间中的小时
     * @param fxTime 预报时间 yyyy-MM-ddThh:mm+hh:mm
     * @return 时间 hh:mm
     */
    public static String getHeFxTimeHour(String fxTime){
        if(fxTime == null){
            return null;
        }
        String[] s1 = fxTime.split("T");
        String[] s2 = s1.length > 1 ? s1[1].split("\\+") : s1[0].split("\\+");

        return s2 == null ? null : s2[0];
    }

    /**
     * 获取和风天气返回时间中的日期
     * @param fxTime 预报时间 yyyy-MM-ddThh:mm+hh:mm
     * @return 时间 yyyy-MM-dd
     */
    public static String getHeFxTimeDate(String fxTime){
        if(fxTime == null){
            return null;
        }
        String[] s1 = fxTime.split("T");
        if(s1 == null || s1[0].indexOf("-") == -1) return null;
        return s1[0];
    }

    /**
     * 计算两个日期的天数间隔
     * @param date1 日期1 yyyy-MM-dd
     * @param date2 日期2
     * @return 天数间隔
     */
    public static int getIntervalDay(String date1, String date2){
        if(date1 == null || date2 == null){
            return 0;
        }
        return getIntervalDay(date1) - getIntervalDay(date2);
    }

    /**
     * 计算两个时间的小时间隔
     * @param hour1 hh:mm
     * @param date1 yyyy-MM-dd
     * @return 单位h
     */
    public static float getIntervalHour(String hour1, String hour2, String date1, String date2){
        //Log.d(TAG, "getIntervalHour: " + hour1 + " " + hour2 + " " + date1 + " " + date2);
        String[] t1 = hour1.split(":");
        String[] t2 = hour2.split(":");
        int h1 = Integer.parseInt(t1[0]);
        float m1 = Integer.parseInt(t1[1]);
        int h2 = Integer.parseInt(t2[0]);
        float m2 = Integer.parseInt(t2[1]);

        float res1 = h1 + m1/60;
        float res2 = h2 + m2/60;

        return Math.abs(getIntervalDay(date2) - getIntervalDay(date1) + (res2 - res1 + 24)%24);
    }

    public static float getIntervalHour(String hour1, String hour2){
        String[] t1 = hour1.split(":");
        String[] t2 = hour2.split(":");
        int h1 = Integer.parseInt(t1[0]);
        float m1 = Integer.parseInt(t1[1]);
        int h2 = Integer.parseInt(t2[0]);
        float m2 = Integer.parseInt(t2[1]);

        float res1 = h1 + m1/60;
        float res2 = h2 + m2/60;

        return Math.abs(res2 - res1);
    }

    public static String getHourNow(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public static String getDateNow(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return calendar.get(Calendar.YEAR) + "-" + (1+calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DATE);
    }

    /**
     * 计算指定日期到0000-01-01的天数
     * @param date 指定日期 yyyy-MM-dd
     * @return 天数间隔
     */
    public static int getIntervalDay(String date){
        if(date == null || "".equals(date)) {
            return 0;
        }
        int y1, m1, d1;
        int days = 0;
        String[] d = date.split("-");

        if(d.length < 3){
            return 0;
        }

        y1 = Integer.parseInt(d[0]) - 1;
        m1 = Integer.parseInt(d[1]);
        d1 = Integer.parseInt(d[2]);

        days += y1 * 365 + (y1 / 4) + (y1 / 400) - (y1 / 100);
        y1++;
        for(int i = 1; i < m1; i++){
            days += MONTH_DAY_COUNT[i];
            if(i == 2 && (((y1 % 4 == 0) && (y1 % 100 != 0)) || y1 % 400 == 0 )){
                days++;
            }
        }
        days += d1;
        return days;
    }

    /**
     * 日期字符串转换为Date
     * @param strDate 日期 yyyy-MM-dd
     * @return Date对象
     */
    public static Date strToDate(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(strDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据日期获得星期几
     * @param date 日期
     * @return 星期几
     */
    public static String getWeek(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        return sdf.format(date);
    }
}
