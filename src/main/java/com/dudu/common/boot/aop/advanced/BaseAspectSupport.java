package com.dudu.common.boot.aop.advanced;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author mengli
 */
public abstract class BaseAspectSupport {

    /**
     * 解析方法
     *
     * @param point 目标类连接点对象
     * @return 当前方法对象
     */
    Method resolveMethod(ProceedingJoinPoint point) {

        // 目标签名
        Signature signature = point.getSignature();

        if (isMethodSignatureInstance(signature)) {
            // 方法签名
            MethodSignature methodSignature = (MethodSignature) signature;
            // 方法名称
            String methodName = methodSignature.getName();
            // 参数类型
            Class<?>[] parameterTypes = methodSignature.getMethod().getParameterTypes();
            // 代理对象类型
            Class<?> targetClass = point.getTarget().getClass();
            // 返回当前方法对象
            return getDeclaredMethod(targetClass, methodName, parameterTypes);
        }

        throw new IllegalStateException("无法解析当前目标: " + signature.getDeclaringTypeName());
    }

    /**
     * 判断目标签名是否为方法签名实例（只处理方法）
     *
     * @param signature 目标签名
     * @return boolean
     */
    private boolean isMethodSignatureInstance(Signature signature) {
        return signature instanceof MethodSignature;
    }

    /**
     * 获取目标所有方法，包含protected、private方法
     *
     * @param clazz          目标类型
     * @param methodName     方法名称
     * @param parameterTypes 参数类型
     * @return 方法对象 or null
     */
    private Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethod(superClass, methodName, parameterTypes);
            }
        }
        return null;
    }
}
