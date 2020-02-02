package com.github.hollykunge.security.admin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zhhongyu
 * @description: 人员处理注解
 * @since: Create in 10:39 2019/12/2
 */
@Retention(RetentionPolicy.RUNTIME)
// 在运行时可以获取
@Target(value = ElementType.METHOD)
// 作用到，方法，接口上等
public @interface FilterByDeletedAndOrderHandler {
}
