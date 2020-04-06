package com.demo.router.core.callback;

import com.demo.router.core.Postcard;

public interface NavigationCallback {
    /**
     * 找到跳转页面
     */
    void onFound(Postcard postcard);

    /**
     * 未找到
     */
    void onLost(Postcard postcard);

    /**
     * 成功跳转
     */
    void onArrival(Postcard postcard);
}
