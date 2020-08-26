package com.xzy.weather.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Author:xzy
 * Date:2020/8/20 14:15
 **/
public class MyWeatherNowBean {

    @SerializedName("temp")
    public String temp;

    @SerializedName("text")
    public String text;

    @SerializedName("vis")
    public String vis;

    @SerializedName("windDir")
    public String windDir;

    @SerializedName("windScale")
    public String windScale;

    @SerializedName("uv")
    public String uv;

    @SerializedName("humidity")
    public String humidity;

    @SerializedName("air")
    public String air;

    @SerializedName("sunrise")
    public String sunrise;

    @SerializedName("sunset")
    public String sunset;

    public String toJson(){
        return new Gson().toJson(this);
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getAir() {
        return air;
    }

    public void setAir(String air) {
        this.air = air;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindScale() {
        return windScale;
    }

    public void setWindScale(String windScale) {
        this.windScale = windScale;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
