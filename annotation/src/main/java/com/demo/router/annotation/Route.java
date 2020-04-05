package com.demo.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.CLASS)
public @interface Route {
    String path();

    /**
     * 将路由节点进行分组，可以实现按组动态加载
     */
    String group() default "default_group";
}
