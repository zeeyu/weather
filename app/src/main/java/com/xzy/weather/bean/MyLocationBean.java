package com.xzy.weather.bean;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Author:xzy
 * Date:2020/8/19 10:35
 **/
public class MyLocationBean {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("country")
    private String country;

    @SerializedName("adm2")
    private String city;

    @SerializedName("adm1")
    private String province;

    private boolean isLocal;  //是否为当地

    private boolean isCommon; //是否为常用位置

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

    public MyLocationBean(String id, String name, String country, String province, String city, String type){
        this.id = id;
        this.name = name;
        this.country = country;
        this.province = province;
        this.city = city;
        this.isLocal = false;
        this.isCommon = false;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    /**
     * 获取完整地址字符串
     * @return 格式 [区-市-省，国家]
     */
    public String getFullAddress(){
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if(name != null){
            sb.append(name);
            first = false;
        }
        if(city != null && !city.equals(name)){
            if(!first){
                sb.append("-");
            }
            first = false;
            sb.append(city);
        }
        if(province != null && !province.equals(city)){
            if(!first){
                sb.append("-");
            }
            first = false;
            sb.append(province);
        }
        if(country != null){
            if(!first){
                sb.append(",");
            }
            sb.append(country);
        }
        return sb.toString();
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
