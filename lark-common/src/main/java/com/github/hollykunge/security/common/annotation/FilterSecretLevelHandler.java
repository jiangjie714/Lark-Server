package com.github.hollykunge.security.common.annotation;


import com.github.hollykunge.security.common.dictionary.SecretFileterConditionEnum;
import com.github.hollykunge.security.common.parser.IDynamicSecretGenerator;
import com.github.hollykunge.security.common.parser.impl.DefaultDynamicKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zhhongyu
 * @description: 密级资源处理器注解
 * @since: Create in 10:40 2019/12/2
 */
@Retention(RetentionPolicy.RUNTIME)
// 在运行时可以获取
@Target(value = ElementType.METHOD)
// 作用到接口，方法等
public @interface FilterSecretLevelHandler {
    /**
     * 配置条件，为在密级中还是在密级外
     * @return
     */
    public SecretFileterConditionEnum condition() default SecretFileterConditionEnum.in;

    /**
     * 可配置的密级集
     * @return
     */
    public String secrets() default "";

    /**
     * 可配置的单个密级
     * @return
     */
    public String secret() default "";

    /**
     * 动态生成密级
     * @return
     */
    public Class<? extends IDynamicSecretGenerator> dynamicSecret() default DefaultDynamicKey.class;
}
