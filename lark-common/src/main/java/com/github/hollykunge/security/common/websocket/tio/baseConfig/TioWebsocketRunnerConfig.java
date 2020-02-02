package com.github.hollykunge.security.common.websocket.tio.baseConfig;

import com.github.hollykunge.security.common.websocket.tio.baseConfig.CurrentWsServerStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * <p>websocket-tio启动类</p>
 * @author: zhhongyu
 * @since: 2019/5/16
 */
@Slf4j
public class TioWebsocketRunnerConfig {
    @Bean
    public CurrentWsServerStarter currentWsServerStarter(CurrentWsServerStarter wsServerStarter) throws Exception {
        try{
            log.info("tio-websocket启动starting...");
            wsServerStarter.start();
            log.info("tio-websocket启动ending...");
        }catch (RuntimeException e){
            log.info("tio-websocket error...",e.getMessage());
        }
        return wsServerStarter;
    }
}
