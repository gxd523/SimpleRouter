package com.demo.router.api.template;

import com.demo.router.annotation.RouteMeta;

import java.util.Map;

public interface IRouteList {
    void addRoute(Map<String, RouteMeta> routeMap);
}
