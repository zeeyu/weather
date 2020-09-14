package com.xzy.weather.bean;

/**
 * Author:xzy
 * Date:2020/9/9 17:26
 **/
public class SettingBean {

    boolean weatherNotify;
    String tempUnit;
    String autoUpdateTime;

    public SettingBean(){
        weatherNotify = true;
        autoUpdateTime = "1小时";
        tempUnit = "°C";
    }

    public boolean isWeatherNotify() {
        return weatherNotify;
    }

    public void setWeatherNotify(boolean weatherNotify) {
        this.weatherNotify = weatherNotify;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }

    public String getAutoUpdateTime() {
        return autoUpdateTime;
    }

    public void setAutoUpdateTime(String autoUpdateTime) {
        this.autoUpdateTime = autoUpdateTime;
    }
}
