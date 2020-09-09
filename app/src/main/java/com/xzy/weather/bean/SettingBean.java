package com.xzy.weather.bean;

/**
 * Author:xzy
 * Date:2020/9/9 17:26
 **/
public class SettingBean {

    boolean weatherNotify;
    String tempUnit;
    int autoUpdateTime;

    public SettingBean(){
        weatherNotify = true;
        autoUpdateTime = 1;
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

    public int getAutoUpdateTime() {
        return autoUpdateTime;
    }

    public void setAutoUpdateTime(int autoUpdateTime) {
        this.autoUpdateTime = autoUpdateTime;
    }
}
