package com.dudu.common.util;

import org.springframework.cglib.beans.BeanCopier;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dujianwei
 * @create 2020/5/26
 * @since 1.0.0
 * @description 基于BeanCopier的属性拷贝
 *
 * 1.当源类和目标类的属性名称、类型都相同，拷贝结果棒棒哒。
 * 2.当源对象和目标对象的属性名称相同、类型不同,那么名称相同而类型不同的属性不会被拷贝。另外注意，原始类型（int，short，char）和 他们的包装类型，在这里都被当成了不同类型。因此不会被拷贝。
 * 3.源类或目标类的setter比getter少，拷贝没问题，此时setter多余，但是不会报错。
 * 4.源类和目标类有相同的属性（两者的getter都存在），但是目标类的setter不存在， 此时会抛出NullPointerException(这个在高版本bug已经修改测试通过,我使用的49.0)
 */
public class BeanCopyUtils {

    /**
     * 创建过的BeanCopier实例放到缓存中，下次可以直接获取，提升性能
     */
    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    /**
     * 该方法没有自定义Converter,简单进行常规属性拷贝
     *
     * @param source 来源对象
     * @param target 目标对象
     */
    public static void copy(final @NotNull Object source, final @NotNull Object target) {

        Class<?> sourceClazz = source.getClass();
        Class<?> targetClazz = target.getClass();

        String key = generateKey(sourceClazz, targetClazz);

        BeanCopier beanCopier;

        if (BEAN_COPIER_CACHE.containsKey(key)) {
            beanCopier = BEAN_COPIER_CACHE.get(key);
        } else {
            beanCopier = BeanCopier.create(sourceClazz, targetClazz, false);
            BEAN_COPIER_CACHE.put(key, beanCopier);
        }

        beanCopier.copy(source, target, null);
    }

    /**
     * 生成key
     *
     * @param sourceClazz 源文件
     * @param targetClazz 目标文件
     * @return key
     */
    private static String generateKey(Class<?> sourceClazz, Class<?> targetClazz) {
        return sourceClazz.getName() + targetClazz.getName();
    }
}
