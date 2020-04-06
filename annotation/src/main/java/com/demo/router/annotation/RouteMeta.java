package com.demo.router.annotation;

import javax.lang.model.element.Element;

public class RouteMeta {
    private Type type;
    private String path;
    private String group;
    private Class annotatedClass;
    private Element annotatedElement;

    public RouteMeta(String path, String group) {
        this(null, path, group, null);
    }

    public RouteMeta(Type type, String path, String group, Class annotatedClass) {
        this(type, path, group, annotatedClass, null);
    }

    public RouteMeta(Type type, String path, String group, Class annotatedClass, Element annotatedElement) {
        this.type = type;
        this.path = path;
        this.group = group;
        this.annotatedClass = annotatedClass;
        this.annotatedElement = annotatedElement;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Class getAnnotatedClass() {
        return annotatedClass;
    }

    public void setAnnotatedClass(Class annotatedClass) {
        this.annotatedClass = annotatedClass;
    }

    public Element getAnnotatedElement() {
        return annotatedElement;
    }

    public void setAnnotatedElement(Element annotatedElement) {
        this.annotatedElement = annotatedElement;
    }

    public enum Type {
        ACTIVITY,
        PROVIDER
    }
}
