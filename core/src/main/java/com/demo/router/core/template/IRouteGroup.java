package com.demo.router.core.template;

import java.util.Map;

public interface IRouteGroup {
    void addRouteListClass(Map<String, Class<? extends IRouteList>> routes);
}
