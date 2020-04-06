package com.demo.router.provider;

import android.content.Context;

import com.demo.router.api.template.IProvider;

/**
 * Created by guoxiaodong on 2020/4/6 15:53
 */
public interface Module1Provider extends IProvider {
    void provideModule1(Context context);
}
