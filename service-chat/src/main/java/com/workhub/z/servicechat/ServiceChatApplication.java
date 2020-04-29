package com.workhub.z.servicechat;


import com.workhub.z.servicechat.config.CacheConst;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.redis.RedisUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
@EnableFeignClients({"com.github.hollykunge.security","com.workhub.z.servicechat.feign"})
@EnableDiscoveryClient
@MapperScan("com.workhub.z.servicechat.dao")
@EnableTransactionManagement
@EnableCaching
@ComponentScan({"com.workhub.z.servicechat.*","com.github.hollykunge.security.admin.api.*"})
public class ServiceChatApplication {
    static Logger logger = LoggerFactory.getLogger(ServiceChatApplication.class);
    //是否清理缓存
    private static boolean restartServiceClearCache;

    @Value("${restartServiceClearCache}")
    private void setRestartServiceClearCache(boolean restartServiceClearCache){
        ServiceChatApplication.restartServiceClearCache = restartServiceClearCache;
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ServiceChatApplication.class, args);
        //校验消息枚举重复
        boolean msgEnumCheck = Common.checkMsgEnumDuplicate();
        if(restartServiceClearCache){
            //清理缓存
            try {
                //清理用户群组缓存
                RedisUtil.removeKeys(CacheConst.userGroupIds+":");
                //清理用户会议缓存
                RedisUtil.removeKeys(CacheConst.userMeetIds+":");
                //6清理涉密词汇表
                RedisUtil.removeKeys(CacheConst.SECRET_WORDSCACHE);
            } catch (Exception e) {
                logger.error("初始化清除缓存操作异常");
                logger.error(Common.getExceptionMessage(e));
            }
        }
    }

}

