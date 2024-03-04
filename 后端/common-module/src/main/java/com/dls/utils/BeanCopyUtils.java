package com.dls.utils;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    // 单个对象的拷贝
    public static <T>T copyBean(Object source, Class<T> clazz){
        // 创建目标对象
        T object = null;
        try {
            object = clazz.newInstance();
            // 实现属性拷贝
            BeanUtils.copyProperties(source, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回结果
        return object;
    }

    // 集合的拷贝.使用泛型作为参数
    // 第一个参数是要拷贝的集合，第二个参数是类的字节码对象
    public static <O, T>List<T> copyBeanList(List<O> list, Class<T> clazz){
        return list.stream()
                .map(new Function<O, T>() {
                    @Override
                    public T apply(O o) {
                        return copyBean(o, clazz);
                    }
                })  // 转换成流
                .collect(Collectors.toList());  // 转换成集合返回
    }

}
