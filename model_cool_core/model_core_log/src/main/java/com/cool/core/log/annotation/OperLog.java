package com.cool.core.log.annotation;

import java.lang.annotation.*;

/**
 *<p>
 * 操作日志注解
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/18
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLog {
    /**
     * 描述
     *
     * @return {String}
     */
    String value();
}
