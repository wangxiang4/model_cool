package com.cool.core.base.util;

import cn.hutool.core.annotation.AnnotationUtil;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Author: 菜鸟小王子
 * 从Spring容器获取实例
 */
@Component
@NoArgsConstructor
public class SpringUtils implements ApplicationContextAware{

    //获取SpringBoot上下文
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext;
        }
    }

    //提供自定义上下文方法
    public static void setAppCtx(ApplicationContext webAppCtx) {
        if (webAppCtx != null) {
            applicationContext = webAppCtx;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    //通过Class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    //通过name获取Bean.
    public static final Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

    //通过name,以及Class名字返回指定的Bean
    public static final Object getBean(String beanName, String className) throws ClassNotFoundException {
        Class clz = Class.forName(className);
        return getApplicationContext().getBean(beanName, clz.getClass());
    }

    //容器是否有这个Bean
    public static boolean containsBean(String name) {
        return getApplicationContext().containsBean(name);
    }

    //容器中这个Bean是否是单例
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return getApplicationContext().isSingleton(name);
    }

    //获取注册对象的类型
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getApplicationContext().getType(name);
    }

    //返回给定bean名称的别名
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return getApplicationContext().getAliases(name);
    }


    /** 
    * @Param: [clazz]
    * @return: T 
    * @Author: 菜鸟小王子
    * @Date: 2020/9/29 14:12 
    * @description: 拿当前(接口下多个实现类Order最大的那个实现类的Bean|比如Order(1),内部使用排序)
    */ 
    public static <T> T getFirstOrderBean(Class<T> clazz) {
        Map<String, T> list = getApplicationContext().getBeansOfType(clazz);
        if (null == list || list.isEmpty()) {
            return null;
        } else {
            String clazzName = list.entrySet().stream().map((entry) -> {
                Order order = AnnotationUtil.getAnnotation(entry.getValue().getClass(), Order.class);
                return new AbstractMap.SimpleEntry<Integer, String>(null == order ? Integer.MAX_VALUE : order.value(), entry.getKey());
            }).sorted(Comparator.comparing(Map.Entry::getKey)).collect(Collectors.toList()).get(0).getValue();
            return (T) getBean(clazzName);
        }

    }


}
