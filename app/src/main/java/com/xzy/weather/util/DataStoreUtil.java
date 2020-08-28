package com.xzy.weather.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.bean.MyWarningBean;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.bean.MyWeatherNowBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xzy
 * Date:2020/8/28 10:07
 **/
public class DataStoreUtil {

    private static String NAME_LOCATION_LIST = "location_list";
    private static String KEY_LOCATION_LIST = "location";
    private static String KEY_WEATHER_7D = "weather7d";
    private static String KEY_WEATHER_24H = "weather24h";
    private static String KEY_WEATHER_NOW = "weather_now";
    private static String KEY_WARNING = "warning";
    private static String KEY_UPDATE_TIME = "update_time";

    /**
     * 保存城市列表
     * @param context 上下文
     * @param locationList 城市列表
     */
    synchronized public static void setLocationList(@NotNull Context context, List<MyLocationBean> locationList){
        String json = new Gson().toJson(locationList);
        SharedPreferences sp = context.getSharedPreferences(NAME_LOCATION_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_LOCATION_LIST, json);
        editor.apply();
    }

    public static List<MyLocationBean> getLocationList(@NotNull Context context){
        SharedPreferences sp = context.getSharedPreferences(NAME_LOCATION_LIST, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_LOCATION_LIST, "");
        if("".equals(json)){
            return new ArrayList<>();
        }
        return new Gson().fromJson(json, new TypeToken<ArrayList<MyLocationBean>>(){}.getType());
    }

    synchronized public static void setWeather7d(@NotNull Context context, String location, List<MyWeatherBean> weatherList){
        String json = new Gson().toJson(weatherList);
        SharedPreferences sp = context.getSharedPreferences(location, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_WEATHER_7D, json);
        editor.apply();
    }

    public static List<MyWeatherBean> getWeather7d(@NotNull Context context, String location){
        SharedPreferences sp = context.getSharedPreferences(location, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_WEATHER_7D, "");
        if("".equals(json)){
            return new ArrayList<>();
        }
        return new Gson().fromJson(json, new TypeToken<ArrayList<MyWeatherBean>>(){}.getType());
    }

    synchronized public static void setWeather24h(@NotNull Context context, String location, List<MyWeatherBean> weatherList){
        String json = new Gson().toJson(weatherList);
        SharedPreferences sp = context.getSharedPreferences(location, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_WEATHER_24H, json);
        editor.apply();
    }

    public static List<MyWeatherBean> getWeather24h(@NotNull Context context, String location){
        SharedPreferences sp = context.getSharedPreferences(location, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_WEATHER_24H, "");
        if("".equals(json)){
            return new ArrayList<>();
        }
        return new Gson().fromJson(json, new TypeToken<ArrayList<MyWeatherBean>>(){}.getType());
    }

    synchronized public static void setWeatherNow(@NotNull Context context, String location, MyWeatherNowBean weather){
        String json = new Gson().toJson(weather);
        SharedPreferences sp = context.getSharedPreferences(location, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_WEATHER_NOW, json);
        editor.apply();
    }

    public static MyWeatherNowBean getWeatherNow(@NotNull Context context, String location){
        SharedPreferences sp = context.getSharedPreferences(location, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_WEATHER_NOW, "");
        if("".equals(json)){
            return new MyWeatherNowBean();
        }
        return new Gson().fromJson(json, MyWeatherNowBean.class);
    }

    synchronized public static void setWarning(@NotNull Context context, String location, List<MyWarningBean> warningList){
        String json = new Gson().toJson(warningList);
        SharedPreferences sp = context.getSharedPreferences(location, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_WARNING, json);
        editor.apply();
    }

    public static List<MyWarningBean> getWarning(@NotNull Context context, String location){
        SharedPreferences sp = context.getSharedPreferences(location, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_WARNING, "");
        if("".equals(json)){
            return new ArrayList<>();
        }
        return new Gson().fromJson(json, new TypeToken<ArrayList<MyWarningBean>>(){}.getType());
    }

    /**
     * 保存对应城市信息的更新时间
     * @param context
     * @param location
     */
    synchronized public static void setLocationInfoUpdateTime(@NotNull Context context, String location){
        String time = TimeUtil.getTimeNow();
        SharedPreferences sp = context.getSharedPreferences(location, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_UPDATE_TIME, time);
        editor.apply();
    }

    public static String getLocationInfoUpdateTime(@NotNull Context context, String location){
        SharedPreferences sp = context.getSharedPreferences(location, Context.MODE_PRIVATE);
        return sp.getString(KEY_UPDATE_TIME, TimeUtil.DEFAULT_TIME);
    }
}
