package com.xzy.weather.bean;

import com.google.gson.Gson;
import com.xzy.weather.util.TimeUtil;

/**
 * Author:xzy
 * Date:2020/8/20 13:56
 **/
public class MyWeatherBean {

    String date;
    String time;
    String temp;
    String tempMax;
    String tempMin;
    String text;
    String windScale;
    String air;

    public String toJson(){
        return new Gson().toJson(this);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getAir() {
        return air;
    }

    public void setAir(String air) {
        this.air = air;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWindScale() {
        return windScale;
    }

    public void setWindScale(String windScale) {
        this.windScale = windScale;
    }
}
