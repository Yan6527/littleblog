package com.atyan.utils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    /**
     * 将一个对象的属性复制到另一个对象
     *
     * @param source      源对象
     * @param targetClass 目标对象类
     * @param <T>         源对象类型
     * @param <V>         目标对象类型
     * @return 复制后的目标对象
     */
    public static <T, V> V copyProperties(T source, Class<V> targetClass) {
        if (source == null) {
            return null;
        }
        V target = null;
        try {
            target = targetClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, target);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 将一个对象列表的属性复制给另一个对象
     *
     * @param sourceList  源对象列表
     * @param targetClass 目标对象类
     * @param <T>         源对象类型
     * @param <V>         目标对象类型
     * @return 复制后的目标对象列表
     */
    public static <T, V> List<V> copyBeanList(List<T> sourceList, Class<V> targetClass) {
        if (sourceList == null || targetClass == null) {
            return null;
        }

        return sourceList.stream()
                .map(source -> copyProperties(source, targetClass))
                .collect(Collectors.toList());
    }


}
