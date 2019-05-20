package com.dudu.common.boot.annotation;

import java.lang.annotation.*;

/**
 * 使用注解标识需要拦截的url方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UrlAction {

    String name() default "";
}
