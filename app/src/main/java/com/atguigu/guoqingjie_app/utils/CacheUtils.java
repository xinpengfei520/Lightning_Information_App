package com.atguigu.guoqingjie_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者：尚硅谷-杨光福 on 2016/10/8 10:37
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：缓存工具类-共享偏好
 */
public class CacheUtils {
    /**
     * 缓存文本数据
     *
     * @param context
     * @param key
     * @param values
     */
    public static void putString(Context context, String key, String values) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putString(key, values).commit();
    }

    /**
     * 得到缓存文本信息
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 保存boolean类型
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 得到保持的boolean类型
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }
}
