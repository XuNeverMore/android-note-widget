package com.candymobi.notewidget;

import android.app.Application;

/**
 * @author xuchuanting
 * Create on 2020/9/27 10:46
 */
public class MyApp extends Application {

    private static MyApp sMyApp;

    public static MyApp getMyApp() {
        return sMyApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sMyApp = this;
    }
}
