package com.demo.router.core;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.demo.router.annotation.RouteMeta;
import com.demo.router.core.callback.NavigationCallback;
import com.demo.router.core.template.IProvider;

import java.util.ArrayList;

/**
 * 跳卡
 */
public class Postcard extends RouteMeta {
    public Bundle mBundle;
    /**
     * Intent的Flag
     */
    public int flags = -1;
    /**
     * 动画
     */
    public Bundle optionsCompat;
    //老版
    public int enterAnim;
    public int exitAnim;

    // 服务
    public IProvider service;

    public Postcard(String path, String group) {
        this(path, group, null);
    }

    public Postcard(String path, String group, Bundle bundle) {
        super(path, group);
        this.mBundle = (null == bundle ? new Bundle() : bundle);
    }

    public Bundle getExtras() {
        return mBundle;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }

    public IProvider getService() {
        return service;
    }

    public void setService(IProvider service) {
        this.service = service;
    }

    /**
     * Intent.FLAG_ACTIVITY**
     *
     * @param flag
     * @return
     */
    public Postcard withFlags(int flag) {
        this.flags = flag;
        return this;
    }


    public int getFlags() {
        return flags;
    }

    /**
     * 跳转动画
     *
     * @param enterAnim
     * @param exitAnim
     * @return
     */
    public Postcard withTransition(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    /**
     * 转场动画
     *
     * @param compat
     * @return
     */
    public Postcard withOptionsCompat(ActivityOptions compat) {
        if (null != compat) {
            this.optionsCompat = compat.toBundle();
        }
        return this;
    }

    public Postcard withString(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }


    public Postcard withBoolean(String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }


    public Postcard withShort(String key, short value) {
        mBundle.putShort(key, value);
        return this;
    }


    public Postcard withInt(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }


    public Postcard withLong(String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }


    public Postcard withDouble(String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }


    public Postcard withByte(String key, byte value) {
        mBundle.putByte(key, value);
        return this;
    }


    public Postcard withChar(String key, char value) {
        mBundle.putChar(key, value);
        return this;
    }


    public Postcard withFloat(String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }


    public Postcard withParcelable(String key, Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }


    public Postcard withStringArray(String key, String[] value) {
        mBundle.putStringArray(key, value);
        return this;
    }


    public Postcard withBooleanArray(String key, boolean[] value) {
        mBundle.putBooleanArray(key, value);
        return this;
    }


    public Postcard withShortArray(String key, short[] value) {
        mBundle.putShortArray(key, value);
        return this;
    }


    public Postcard withIntArray(String key, int[] value) {
        mBundle.putIntArray(key, value);
        return this;
    }


    public Postcard withLongArray(String key, long[] value) {
        mBundle.putLongArray(key, value);
        return this;
    }


    public Postcard withDoubleArray(String key, double[] value) {
        mBundle.putDoubleArray(key, value);
        return this;
    }


    public Postcard withByteArray(String key, byte[] value) {
        mBundle.putByteArray(key, value);
        return this;
    }


    public Postcard withCharArray(String key, char[] value) {
        mBundle.putCharArray(key, value);
        return this;
    }


    public Postcard withFloatArray(String key, float[] value) {
        mBundle.putFloatArray(key, value);
        return this;
    }


    public Postcard withParcelableArray(String key, Parcelable[] value) {
        mBundle.putParcelableArray(key, value);
        return this;
    }

    public Postcard withParcelableArrayList(String key, ArrayList<? extends Parcelable> value) {
        mBundle.putParcelableArrayList(key, value);
        return this;
    }

    public Postcard withIntegerArrayList(String key, ArrayList<Integer> value) {
        mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public Postcard withStringArrayList(String key, ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return this;
    }

    public Bundle getOptionsBundle() {
        return optionsCompat;
    }


    public Object navigation() {
        return navigation(null, null);
    }

    public Object navigation(Context context) {
        return navigation(context, null);
    }


    public Object navigation(Context context, NavigationCallback callback) {
        return Router.getInstance().navigation(context, this, -1, callback);
    }

    public Object navigation(Activity activity, int requestCode) {
        return navigation(activity, requestCode, null);
    }

    public Object navigation(Activity activity, int requestCode, NavigationCallback callback) {
        return Router.getInstance().navigation(activity, this, requestCode, callback);
    }


}