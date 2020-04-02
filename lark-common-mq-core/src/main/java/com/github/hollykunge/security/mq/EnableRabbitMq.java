package com.github.hollykunge.security.mq;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MqConfiguration.class)
@Documented
@Inherited
public @interface EnableRabbitMq {
}
