package com.xzy.weather.util;

/**
 * Author:xzy
 * Date:2020/8/20 19:50
 **/
public class TimeUtil {

    /**
     * 获取和风天气返回时间中的小时
     * @param fxTime 预报事件
     * @return
     */
    public static String getHeFxTimeHour(String fxTime){
        if(fxTime == null){
            return null;
        }
        String[] s1 = fxTime.split("T");
        String[] s2 = s1.length > 1 ? s1[1].split("\\+") : null;

        return s2 == null ? null : s2[0];
    }
}
