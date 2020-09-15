package com.xzy.weather.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.xzy.weather.GlobalData;
import com.xzy.weather.bean.MyLocationBean;
import com.xzy.weather.bean.MyWarningBean;
import com.xzy.weather.bean.MyWeatherBean;
import com.xzy.weather.bean.MyWeatherNowBean;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.WarningBean;
import interfaces.heweather.com.interfacesmodule.bean.air.AirDailyBean;
import interfaces.heweather.com.interfacesmodule.bean.air.AirNowBean;
import interfaces.heweather.com.interfacesmodule.bean.base.Lang;
import interfaces.heweather.com.interfacesmodule.bean.base.Mode;
import interfaces.heweather.com.interfacesmodule.bean.base.Range;
import interfaces.heweather.com.interfacesmodule.bean.geo.GeoBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

/**
 * Author:xzy
 * Date:2020/9/1 09:30
 **/
public class HeWeatherUtil {

    private static final String TAG = "HeWeatherUtil";
    private static String HE_ID = "HE2008191124411841";
    private static String HE_KEY = "1303d8f41ae24186b5de7055a0b0e2a4";

    private static String unit;

    private static void init(){
        HeConfig.init(HE_ID, HE_KEY);
        HeConfig.switchToDevService();
        if(unit == null){
            unit = GlobalData.getInstance().getSetting().getTempUnit();
        }
    }

    public static void getGeoTopCity(Context context, List<MyLocationBean> cityList, OnResultListener listener){
        init();
        HeWeather.getGeoTopCity(context, 20, Range.CN, Lang.ZH_HANS, new HeWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "GetGeoTopCity onError");
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                for (GeoBean.LocationBean locationBean : locationBeanList) {
                    cityList.add(new MyLocationBean(
                            locationBean.getId(),
                            locationBean.getName(),
                            locationBean.getCountry(),
                            locationBean.getAdm2(),
                            locationBean.getAdm1(),
                            locationBean.getType()));
                }
                listener.onSuccess();
            }
        });
    }

    public static void getGeoCityLookup(Context context, String location, MyLocationBean myLocationBean, OnResultListener listener){
        init();
        HeWeather.getGeoCityLookup(context, location, new HeWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "getGeoCityLookup onError");
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                Log.d(TAG, "onSuccess: " + new Gson().toJson(geoBean));
                GeoBean.LocationBean locationBean;
                if(geoBean.getLocationBean().size() > 0) {
                    locationBean = geoBean.getLocationBean().get(0);
                    myLocationBean.setId(locationBean.getId());
                    myLocationBean.setCity(locationBean.getAdm2());
                    myLocationBean.setName(locationBean.getName());
                    myLocationBean.setProvince(locationBean.getAdm1());
                }
                listener.onSuccess();
            }
        });
    }

    public static void getGeoCityLookup(Context context, String location, List<MyLocationBean> cityList, OnResultListener listener){
        init();
        HeWeather.getGeoCityLookup(context, location, Mode.FUZZY, Range.CN, 20, Lang.ZH_HANS, new HeWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "GetGeoTopCity onError");
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                List<GeoBean.LocationBean> locationBeanList = geoBean.getLocationBean();
                cityList.clear();
                for (GeoBean.LocationBean locationBean : locationBeanList) {
                    cityList.add(new MyLocationBean(
                            locationBean.getId(),
                            locationBean.getName(),
                            locationBean.getCountry(),
                            locationBean.getAdm1(),
                            locationBean.getAdm2(),
                            locationBean.getType()));
                }
                listener.onSuccess();
            }
        });
    }

    public static void getWeatherNow(Context context, String location, MyWeatherNowBean weatherNow, OnResultListener listener){
        init();
        HeWeather.getWeatherNow(context, location, new HeWeather.OnResultWeatherNowListener(){

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Weather now onError:", throwable);
            }

            @Override
            public void onSuccess(WeatherNowBean weatherNowBean) {
                Log.d(TAG, "Weather now onSuccess:" + new Gson().toJson(weatherNowBean));
                WeatherNowBean.NowBaseBean weatherNowBaseBean = weatherNowBean.getNow();

                weatherNow.setTemp(weatherNowBaseBean.getTemp());
                weatherNow.setText(weatherNowBaseBean.getText());
                weatherNow.setHumidity(weatherNowBaseBean.getHumidity());
                weatherNow.setVis(weatherNowBaseBean.getVis());
                weatherNow.setWindDir(weatherNowBaseBean.getWindDir());
                weatherNow.setWindScale(weatherNowBaseBean.getWindScale());

                listener.onSuccess();
            }
        });
    }

    public static void getAirNow(Context context, String location, MyWeatherNowBean weatherNow, OnResultListener listener){
        init();
        HeWeather.getAirNow(context, location, Lang.ZH_HANS, new HeWeather.OnResultAirNowListener(){

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Air now onError:", throwable);
            }

            @Override
            public void onSuccess(AirNowBean airNowBean) {
                Log.d(TAG, "Air now onSuccess:" + new Gson().toJson(airNowBean));
                AirNowBean.NowBean nowBean = airNowBean.getNow();

                weatherNow.setAir(nowBean.getCategory());

                listener.onSuccess();
            }
        });
    }

    public static void getWeather24Hourly(Context context, String location, List<MyWeatherBean> weatherHourlyList, OnResultListener listener){
        init();
        HeWeather.getWeather24Hourly(context, location, new HeWeather.OnResultWeatherHourlyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Weather hourly onError:", throwable);
            }

            @Override
            public void onSuccess(WeatherHourlyBean weatherHourlyBean) {
                Log.d(TAG, "Weather hourly onSuccess:" + new Gson().toJson(weatherHourlyBean));
                List<WeatherHourlyBean.HourlyBean> beanList = weatherHourlyBean.getHourly();
                for(int i = 0; i < beanList.size(); i++){
                    WeatherHourlyBean.HourlyBean bean = beanList.get(i);

                    weatherHourlyList.get(i).setTemp(bean.getTemp());
                    weatherHourlyList.get(i).setTime(TimeUtil.getTimeHour(bean.getFxTime()));
                    weatherHourlyList.get(i).setWindScale(bean.getWindScale());
                    weatherHourlyList.get(i).setText(bean.getText());
                }
                listener.onSuccess();
            }
        });
    }

    public static void getWeather7D(Context context, String location, MyWeatherNowBean weatherNow, List<MyWeatherBean> weatherDailyList, OnResultListener listener){
        init();
        HeWeather.getWeather7D(context, location, new HeWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Weather7d onError:", throwable);
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                Log.d(TAG, "Weather7d onSuccess:" + new Gson().toJson(weatherDailyBean));

                List<WeatherDailyBean.DailyBean> beanList = weatherDailyBean.getDaily();
                for(int i = 0; i < beanList.size(); i++){

                    WeatherDailyBean.DailyBean bean = beanList.get(i);

                    if(i == 0) {
                        weatherNow.setUv(bean.getUvIndex());
                        weatherNow.setSunrise(bean.getSunrise());
                        weatherNow.setSunset(bean.getSunset());
                    }

                    weatherDailyList.get(i).setTempMax(bean.getTempMax());
                    weatherDailyList.get(i).setTempMin(bean.getTempMin());
                    weatherDailyList.get(i).setDate(bean.getFxDate());
                    weatherDailyList.get(i).setText(bean.getTextDay());
                }
                listener.onSuccess();
            }
        });
    }

    public static void getAir5D(Context context, String location, List<MyWeatherBean> weatherDailyList, OnResultListener listener){
        init();
        HeWeather.getAir5D(context, location, Lang.ZH_HANS, new HeWeather.OnResultAirDailyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Air5d onError:", throwable);
            }

            @Override
            public void onSuccess(AirDailyBean airDailyBean) {
                Log.d(TAG, "Air5d onSuccess:" + new Gson().toJson(airDailyBean));
                List<AirDailyBean.DailyBean> beanList = airDailyBean.getAirDaily();
                for(int i = 0; i < beanList.size(); i++){
                    AirDailyBean.DailyBean bean = beanList.get(i);

                    weatherDailyList.get(i).setAir(bean.getCategory());
                }
                listener.onSuccess();
            }
        });
    }

    public static void getWarning(Context context, String location, List<MyWarningBean> warningList, OnResultListener listener){
        init();
        HeWeather.getWarning(context, location, new HeWeather.OnResultWarningListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "Warning onError:", throwable);
            }

            @Override
            public void onSuccess(WarningBean warningBean) {
                Log.d(TAG, "Warning onSuccess:" + new Gson().toJson(warningBean));

                for(WarningBean.WarningBeanBase base : warningBean.getBeanBaseList()){

                    MyWarningBean bean = new MyWarningBean();
                    bean.setPubTime(base.getPubTime());
                    bean.setLevel(base.getLevel());
                    bean.setText(base.getText());
                    bean.setType(base.getTypeName());
                    bean.setTitle(base.getTitle());
                    bean.setSender(base.getSender());

                    warningList.add(bean);
                }
                listener.onSuccess();
            }
        });
    }

    public static String formatTempC(String tempC) {
        float temp = Float.parseFloat(tempC);
        return String.valueOf((int)(temp * 1.8f) + 32);
    }

    public static String formatTempF(String tempF) {
        float temp = Float.parseFloat(tempF);
        return String.valueOf((int)((temp - 32f)/1.8f));
    }

    public interface OnResultListener{
        void onSuccess();
    }
}
