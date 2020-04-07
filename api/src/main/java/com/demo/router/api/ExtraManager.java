package com.demo.router.api;

import android.util.LruCache;

import com.demo.router.api.template.IExtra;

public class ExtraManager {
    private LruCache<String, IExtra> classCache;

    private ExtraManager() {
        classCache = new LruCache<>(66);
    }

    public static ExtraManager getInstance() {
        return Holder.instance;
    }

    public void loadExtras(Object target) {
        Class<?> targetClass = target.getClass();
        IExtra iExtra = classCache.get(targetClass.getName());
        if (null == iExtra) {
            String extraClassName = String.format("%s.Extra$$%s", targetClass.getPackage().getName(), targetClass.getSimpleName());
            try {
                iExtra = (IExtra) Class.forName(extraClassName).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (iExtra != null) {
            iExtra.loadExtra(target);
            classCache.put(targetClass.getName(), iExtra);
        }
    }

    private static final class Holder {
        private static final ExtraManager instance = new ExtraManager();
    }
}
