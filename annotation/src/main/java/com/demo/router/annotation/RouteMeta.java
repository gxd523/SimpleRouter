package com.demo.router.annotation;

public class RouteMeta {
    private Type type;
    private String path;
    private String group;
    private String annotatedClassName;

    public RouteMeta(String path, String group) {
        this(null, path, group, null);
    }

    public RouteMeta(Type type, String path, String group, String annotatedClassName) {
        this.type = type;
        this.path = path;
        this.group = group;
        this.annotatedClassName = annotatedClassName;
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

    public String getAnnotatedClassName() {
        return annotatedClassName;
    }

    public void setAnnotatedClassName(String annotatedClassName) {
        this.annotatedClassName = annotatedClassName;
    }

    public enum Type {
        ACTIVITY,
        PROVIDER
    }
}
