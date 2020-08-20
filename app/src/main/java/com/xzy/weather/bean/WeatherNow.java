package com.xzy.weather.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author:xzy
 * Date:2020/8/20 14:15
 **/
public class WeatherNow {

    @SerializedName("temp")
    public String temperature;

    @SerializedName("text")
    public String weatherText;

    @SerializedName("vis")
    public String vis;

    @SerializedName("windDir")
    public String windDir;

    @SerializedName("windScale")
    public String windScale;
}
