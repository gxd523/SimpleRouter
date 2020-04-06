package com.demo.router;

import android.app.Application;

import com.demo.router.api.Router;

/**
 * Created by guoxiaodong on 2020/4/5 18:45
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Router.init(this);
    }
}
