package com.demo.router.api;

import com.demo.router.annotation.RouteMeta;
import com.demo.router.api.template.IProvider;
import com.demo.router.api.template.IRouteList;

import java.util.HashMap;
import java.util.Map;

public class Warehouse {
    // root 映射表 保存分组信息
    static Map<String, Class<? extends IRouteList>> routeListClassMap = new HashMap<>();

    // group 映射表 保存组中的所有数据
    static Map<String, RouteMeta> routeListMap = new HashMap<>();

//    static Map<String, Map<String, RouteMeta>> routeListGroupMap = new HashMap<>();

    // group 映射表 保存组中的所有数据
    static Map<String, IProvider> serviceMap = new HashMap<>();
}
