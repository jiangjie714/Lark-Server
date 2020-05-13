package com.github.hollykunge.security.common.annotation;


import com.github.hollykunge.security.common.config.FeignConfigration;
import com.github.hollykunge.security.common.dictionary.SecretFileterConditionEnum;
import com.github.hollykunge.security.common.parser.IDynamicSecretGenerator;
import com.github.hollykunge.security.common.parser.impl.DefaultDynamicKey;
import com.github.hollykunge.security.common.websocket.tio.baseConfig.TioWebsocketBaseConfig;
import com.github.hollykunge.security.common.websocket.tio.baseConfig.TioWebsocketRunnerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author: zhhongyu
 * @description: 密级资源处理器注解
 * @since: Create in 10:40 2019/12/2
 */
@Retention(RetentionPolicy.RUNTIME)//在运行时可以获取
@Target({ElementType.TYPE})
@Documented
@Inherited
@Import(FeignConfigration.class)
public @interface EnableLarkFeignAspect {

}
