package com.xzy.weather.util;

/**
 * Author:xzy
 * Date:2020/8/25 16:35
 **/
public class StringUtil {

    public static String getWeatherName(String weather){
        switch (weather) {
            case "晴":
                return "sunny";
            case "多云":
                return "cloudy";
            case "阴":
                return "overcast";
            case "雨":
                return "rainy";
            case "雪":
                return "snowy";
            case "雾":
                return "foggy";
            case "霾":
                return "hazy";
            default:
                return "unknown";
        }
    }
}
