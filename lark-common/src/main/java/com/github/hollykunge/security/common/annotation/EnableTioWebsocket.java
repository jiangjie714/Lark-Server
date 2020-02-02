package com.github.hollykunge.security.common.annotation;

import com.github.hollykunge.security.common.websocket.tio.baseConfig.TioWebsocketBaseConfig;
import com.github.hollykunge.security.common.websocket.tio.baseConfig.TioWebsocketRunnerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)//在运行时可以获取
@Target({ElementType.TYPE})
@Documented
@Inherited
@Import({TioWebsocketBaseConfig.class, TioWebsocketRunnerConfig.class})
public @interface EnableTioWebsocket {
    boolean autoRegister() default true;
}