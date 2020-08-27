package com.xzy.weather.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author:xzy
 * Date:2020/8/19 10:35
 **/
public class MyLocationBean {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("country")
    String country;

    @SerializedName("adm2")
    String city;

    @SerializedName("adm1")
    String province;

    boolean isLocal;  //是否为当地

    boolean isCommon; //是否为常用位置

    public MyLocationBean(){

    }

    public MyLocationBean(String id, String name, String country, String province, String city, String type, boolean isLocal, boolean isCommon){
        this.id = id;
        this.name = name;
        this.country = country;
        this.province = province;
        this.city = city;
        this.isLocal = isLocal;
        this.isCommon = isCommon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public boolean isCommon() {
        return isCommon;
    }

    public void setCommon(boolean common) {
        isCommon = common;
    }
}
