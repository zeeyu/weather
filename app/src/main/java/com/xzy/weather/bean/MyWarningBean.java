package com.xzy.weather.bean;

import com.google.gson.Gson;

/**
 * 灾害预警信息
 * Author:xzy
 * Date:2020/8/20 15:27
 **/

public class MyWarningBean {

    String pubTime; //发布时间，格式yyyy-MM-dd HH:mm
    String title;
    String level;
    String type;
    String text;    //详细信息
    String sender;

    public String toJson(){
        return new Gson().toJson(this);
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
