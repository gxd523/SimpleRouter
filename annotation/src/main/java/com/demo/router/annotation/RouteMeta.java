package com.demo.router.annotation;

import javax.lang.model.element.Element;

public class RouteMeta {
    public Type type;
    /**
     * 节点 (Activity)
     */
    public Element element;
    /**
     * 注解使用的类对象
     */
    public Class<?> destination;
    /**
     * 路由地址
     */
    public String path;
    /**
     * 路由组
     */
    public String group;

    public RouteMeta() {
    }

    public RouteMeta(Type type, Class<?> destination, String path, String group) {
        this.type = type;
        this.destination = destination;
        this.path = path;
        this.group = group;
    }

    public RouteMeta(Type type, Element element, Class<?> destination, String path, String group) {
        this.type = type;
        this.element = element;
        this.destination = destination;
        this.path = path;
        this.group = group;
    }

    public enum Type {
        ACTIVITY,
        ISERVICE
    }
}
