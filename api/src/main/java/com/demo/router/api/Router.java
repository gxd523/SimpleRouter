package com.demo.router.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.demo.router.annotation.RouteConfig;
import com.demo.router.annotation.RouteMeta;
import com.demo.router.api.callback.NavigationCallback;
import com.demo.router.api.exception.NoRouteFoundException;
import com.demo.router.api.template.IProvider;
import com.demo.router.api.template.IRouteGroup;
import com.demo.router.api.template.IRouteList;
import com.demo.router.api.utils.ClassUtils;

import java.util.Set;

public class Router {
    private static final String TAG = "DNRouter";
    private static Application application;

    private Router() {
    }

    public static Router getInstance() {
        return Holder.instance;
    }

    /**
     * 初始化
     */
    public static void init(Application application) {
        Router.application = application;
        try {
            Set<String> generateRouteClassSet = ClassUtils.getGenerateRouteClassName(application, RouteConfig.PACKAGE_NAME);
            for (String className : generateRouteClassSet) {
                if (className.startsWith(RouteConfig.PACKAGE_NAME + ".RouteGroup$$")) {
                    ((IRouteGroup) (Class.forName(className).newInstance())).addRouteListClass(Warehouse.routeListClassMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "初始化失败!", e);
        }
    }

    public Postcard build(String path) {
        return build(path, null);
    }

    public Postcard build(String path, String group) {
        if (!TextUtils.isEmpty(path) && path.startsWith("/")) {
            if (TextUtils.isEmpty(group)) {
                return new Postcard(path, path.substring(1, path.indexOf('/', 1)));
            }
        }
        throw new RuntimeException("路由地址无效!");
    }

    /**
     * 获得组别
     */
    private String extractGroup(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new RuntimeException(path + " : 不能提取group.");
        }
        try {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            if (TextUtils.isEmpty(defaultGroup)) {
                throw new RuntimeException(path + " : 不能提取group.");
            } else {
                return defaultGroup;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据跳卡跳转页面
     */
    Object navigation(Context context, final Postcard postcard, final int requestCode, final NavigationCallback callback) {
        try {
            prepareCard(postcard);
        } catch (NoRouteFoundException e) {
            e.printStackTrace();
            if (null != callback) {
                callback.onLost(postcard);
            }
            return null;
        }
        if (null != callback) {
            callback.onFound(postcard);
        }

        switch (postcard.getType()) {
            case ACTIVITY:
                final Context currentContext = null == context ? application : context;
                Class<?> annotatedClass = null;
                try {
                    annotatedClass = Class.forName(postcard.getAnnotatedClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                final Intent intent = new Intent(currentContext, annotatedClass);
                intent.putExtras(postcard.getExtras());
                int flags = postcard.getFlags();
                if (-1 != flags) {
                    intent.setFlags(flags);
                } else if (!(currentContext instanceof Activity)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //可能需要返回码
                        if (requestCode > 0) {
                            ((Activity) currentContext).startActivityForResult(intent, requestCode, postcard.getOptionsBundle());
                        } else {
                            currentContext.startActivity(intent, postcard.getOptionsBundle());
                        }

                        if ((0 != postcard.getEnterAnim() || 0 != postcard.getExitAnim()) && currentContext instanceof Activity) {
                            //老版本
                            ((Activity) currentContext).overridePendingTransition(postcard.getEnterAnim(), postcard.getExitAnim());
                        }
                        //跳转完成
                        if (null != callback) {
                            callback.onArrival(postcard);
                        }
                    }
                });
                break;
            case PROVIDER:
                return postcard.getProvider();
            default:
                break;
        }
        return null;
    }

    /**
     * 准备卡片
     */
    private void prepareCard(Postcard postcard) {
        RouteMeta routeMeta = Warehouse.routeListMap.get(postcard.getPath());
        if (null == routeMeta) {
            Class<? extends IRouteList> iRouteListClass = Warehouse.routeListClassMap.get(postcard.getGroup());
            if (null == iRouteListClass) {
                throw new NoRouteFoundException("没找到对应路由: " + postcard.getGroup() + " " + postcard.getPath());
            }
            IRouteList iRouteList;
            try {
                iRouteList = iRouteListClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("路由分组映射表记录失败.", e);
            }
            iRouteList.addRoute(Warehouse.routeListMap);
            // 已经准备过了就可以移除了 (不会一直存在内存中)
            Warehouse.routeListClassMap.remove(postcard.getGroup());
            prepareCard(postcard);
        } else {
            String annotatedClassName = routeMeta.getAnnotatedClassName();
            postcard.setAnnotatedClassName(annotatedClassName);
            postcard.setType(routeMeta.getType());
            IProvider provider = Warehouse.serviceMap.get(annotatedClassName);
            if (null == provider) {
                try {
                    provider = (IProvider) Class.forName(annotatedClassName).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Warehouse.serviceMap.put(annotatedClassName, provider);
            }
            postcard.setProvider(provider);
        }
    }

    /**
     * 注入
     */
    public void inject(Object target) {
        ExtraManager.getInstance().loadExtras(target);
    }

    private static final class Holder {
        private static final Router instance = new Router();
    }


}
