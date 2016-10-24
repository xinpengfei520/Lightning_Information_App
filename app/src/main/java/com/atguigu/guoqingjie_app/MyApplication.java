package com.atguigu.guoqingjie_app;

import android.app.Application;

import org.xutils.x;

/**
 * Created by xinpengfei on 2016/10/22.
 * 微信:18091383534
 * Function :
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
