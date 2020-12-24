package com.dudu.common.boot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 根据注解拦截所标识的方法
 */
@Aspect
@Component
public class UrlAspect {

    @Pointcut("@annotation(com.dudu.common.boot.aop.UrlAction)")
    public void annotationUrl() { }

    @Pointcut("execution(* com.dudu.common.boot.aop.TestController.method(..))")
    public void annotationMethod() { }

    @After("annotationUrl()")
    public void after(JoinPoint joinPoint) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Assert.notNull(attributes, "attributes must not be null");
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        System.out.println("url   :   " + url);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UrlAction urlAction = method.getAnnotation(UrlAction.class);

        System.out.println("拦截器拦截   :" + urlAction.name());
    }

    @Before("annotationMethod()")
    public void before(JoinPoint joinPoint) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Assert.notNull(attributes, "attributes must not be null");
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        System.out.println("url   :   " + url);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        System.out.println("类方法拦截   :" + method.getName());
    }
}
