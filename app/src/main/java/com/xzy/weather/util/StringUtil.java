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
            case "阵雨":
                return "rainy_shower";
            case "小雨":
                return "rainy_light";
            case "中雨":
                return "rainy_mid";
            case "大雨":
                return "rainy_heavy";
            case "暴雨":
                return "rainy_rainstorm";
            case "雷阵雨":
                return "rainy_thunder";
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

    public static String getWeatherBackgroundName(String weather){
        switch (weather) {
            case "晴":
                return "sunny";
            case "多云":
                return "cloudy";
            case "阴":
                return "overcast";
            case "雨":
            case "阵雨":
            case "小雨":
            case "中雨":
            case "大雨":
            case "暴雨":
            case "雷阵雨":
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

//    public static String getWarningName(String warning){
//
//    }
}
