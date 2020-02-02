package com.github.hollykunge.security.common.config;

import com.github.hollykunge.security.common.aop.FilterSecretLevelAspect;
import com.github.hollykunge.security.common.parser.IDynamicSecretGenerator;
import com.github.hollykunge.security.common.parser.IUserSecretGenerator;
import com.github.hollykunge.security.common.parser.impl.DefaultDynamicKey;
import com.github.hollykunge.security.common.parser.impl.UserSecretImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author: zhhongyu
 * @description:中间定义类
 * @since: Create in 15:11 2019/12/3
 */
@EnableAspectJAutoProxy
public class AutoConfiguration {
    @Bean
    public IUserSecretGenerator getIUserSecretGenerator(){
        return new UserSecretImpl();
    }
    @Bean
    public IDynamicSecretGenerator getIDynamicSecretGenerator(){
        return new DefaultDynamicKey();
    }
    @Bean
    public FilterSecretLevelAspect getFilterSecretLevelAspect(){
        return new FilterSecretLevelAspect();
    }

}
