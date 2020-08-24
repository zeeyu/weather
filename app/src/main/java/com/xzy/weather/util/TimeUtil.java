package com.xzy.weather.util;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author:xzy
 * Date:2020/8/20 19:50
 **/
public class TimeUtil {

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
        String[] s2 = s1.length > 1 ? s1[1].split("\\+") : null;

        return s2 == null ? null : s2[0];
    }

    /**
     * 获取和风天气返回时间中的日期
     * @param fxTime 预报时间 yyyy-MM-ddThh:mm+hh:mm
     * @return 时间 MM-dd
     */
    public static String getHeFxTimeDate(String fxTime){
        if(fxTime == null){
            return null;
        }
        String[] s1 = fxTime.split("T");
        return s1 == null ? null : s1[0].substring(s1[0].indexOf("-"));
    }

    /**
     * 计算两个日期的天数间隔
     * @param date1 日期1 yyyy-MM-dd
     * @param date2 日期2
     * @return 天数间隔
     */
    public static int getIntervalDay(@NotNull String date1, @NotNull String date2){
        return getIntervalDay(date1) - getIntervalDay(date2);
    }

    /**
     * 计算两个时间的小时间隔
     * @param time1 yyyy-MM-ddThh:mm+hh:mm
     * @param time2
     * @return 单位h
     */
    public static float getIntervalHour(String time1, String time2){
        String hour1 = getHeFxTimeHour(time1);
        String hour2 = getHeFxTimeHour(time2);

        int day = getIntervalDay(getHeFxTimeDate(time1), getHeFxTimeDate(time2));
        float hour = 0;

        if(day < 0){
            String temp = time1;
            time1 = time2;
            time2 = temp;
        }

        String[] t1 = hour1.split(":");
        String[] t2 = hour2.split(":");
        int h1 = Integer.valueOf(t1[0]);
        float m1 = Integer.valueOf(t1[1]);
        int h2 = Integer.valueOf(t2[0]);
        float m2 = Integer.valueOf(t2[1]);

        if(m1 < m2){
            hour = (h1 - h2 - 1) + (m1 + 60 - m2) / 60.0f;
        } else {
            hour = (h1 - h2) + (m1 - m2) / 60.0f;
        }
        return hour;
    }

    /**
     * 计算指定日期到0000-01-01的天数
     * @param date 指定日期 YYYY-mm-dd
     * @return 天数间隔
     */
    public static int getIntervalDay(@NotNull String date){
        int y1, m1, d1;
        int days = 0;
        String[] d = date.split("-");

        if(d.length < 3){
            return 0;
        }

        y1 = Integer.valueOf(d[0]) - 1;
        m1 = Integer.valueOf(d[1]);
        d1 = Integer.valueOf(d[2]);

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
     * @param strDate 日期 YYYY-mm-dd
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
