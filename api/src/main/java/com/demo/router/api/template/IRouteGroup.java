package com.demo.router.api.template;

import java.util.Map;

public interface IRouteGroup {
    void addRouteListClass(Map<String, Class<? extends IRouteList>> routes);
}
