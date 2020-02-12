package com.workhub.z.servicechat;


import com.workhub.z.servicechat.config.CacheConst;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.redis.RedisUtil;
import com.workhub.z.servicechat.server.IworkWebsocketStarter;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.workhub.z.servicechat.dao")
@EnableTransactionManagement
@EnableCaching
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
        if(restartServiceClearCache){
            //清理缓存
            try {
                //1清理用户在线离线缓存
                RedisUtil.removeKeys(CacheConst.userOnlineCahce);
                //2清理在线用户列表缓存
                RedisUtil.removeKeys(CacheConst.USER_ONLINE_LIST);
                //3清理用户信息缓存
                //RedisUtil.removeKeys(CacheConst.USER_INF+":");
                //4清理用户群组缓存
                RedisUtil.removeKeys(CacheConst.userGroupIds+":");
                //5清理用户会议缓存
                RedisUtil.removeKeys(CacheConst.userMeetIds+":");
            } catch (Exception e) {
                logger.error("初始化清除缓存操作异常");
                logger.error(common.getExceptionMessage(e));
            }
        }
//        初始化网络
        IworkWebsocketStarter iworkWebsocketStarter = new IworkWebsocketStarter();
        iworkWebsocketStarter.run();
    }

}
