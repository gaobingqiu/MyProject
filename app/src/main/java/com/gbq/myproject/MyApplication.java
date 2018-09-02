package com.gbq.myproject;

import android.app.Application;


public class MyApplication extends Application {
    private static MyApplication mApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    //使用context的地方尽量使用该方法，可极大避免内存异常和空指针异常
    public static MyApplication getApplication() {
        return mApplication;
    }
}
