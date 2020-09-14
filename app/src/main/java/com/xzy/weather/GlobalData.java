package com.xzy.weather;

import com.xzy.weather.bean.SettingBean;

/**
 * Author:xzy
 * Date:2020/9/11 13:34
 **/
public class GlobalData {

    private SettingBean setting;

    private static class InnerGlobalData {
        private static GlobalData instance = new GlobalData();
    }

    private GlobalData() {
    }

    public static GlobalData getInstance() {
        return InnerGlobalData.instance;
    }

//    public static GlobalData getInstance() {
//
//        if(data == null) {
//            data = new GlobalData();
//        }
//        return data;
//    }

    public SettingBean getSetting() {
        return setting;
    }

    public void setSetting(SettingBean setting) {
        this.setting = setting;
    }
}
