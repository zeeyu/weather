package com.xzy.weather.util;

import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Author:xzy
 * Date:2020/8/19 13:52
 **/
public class HttpUtil {

    /**
     * 使用okHttp从服务端获取数据
     * @param url 服务端url
     * @param callback
     */
    public static void sendOkHttpRequest(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
