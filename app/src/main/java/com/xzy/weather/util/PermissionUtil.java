package com.xzy.weather.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xzy
 * Date:2020/8/19 15:10
 **/
public class PermissionUtil {

    private static List<String> requestList = new ArrayList<>();

    /**
     * 添加权限到申请列表
     * @param context 上下文
     * @param permission 权限名
     */
    public static void addPermission(Context context, String permission){
        if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
            requestList.add(permission);
        }
    }

    public static List<String> getRequestList(){
        return requestList;
    }
}
