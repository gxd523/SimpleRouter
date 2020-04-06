package com.demo.router.core;

import com.demo.router.annotation.RouteMeta;
import com.demo.router.core.template.IRouteList;
import com.demo.router.core.template.IProvider;

import java.util.HashMap;
import java.util.Map;

public class Warehouse {
    // root 映射表 保存分组信息
    static Map<String, Class<? extends IRouteList>> routeListClassMap = new HashMap<>();

    // group 映射表 保存组中的所有数据
    static Map<String, RouteMeta> routeListMap = new HashMap<>();

    static Map<String, Map<String, RouteMeta>> routeListGroupMap = new HashMap<>();

    // group 映射表 保存组中的所有数据
    static Map<Class, IProvider> serviceMap = new HashMap<>();
}
