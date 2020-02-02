package com.github.hollykunge.security.common.websocket.tio.baseConfig;

import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.websocket.tio.baseServiceConfig.WebsocketServerAioListener;
import com.github.hollykunge.security.common.websocket.tio.baseServiceConfig.WebsocketWsMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.tio.server.ServerGroupContext;

/**
 * <p>websocket-tio服务配置类</p>
 *
 * @author: zhhongyu
 * @since: 2019/5/16
 */
@Slf4j
public class TioWebsocketBaseConfig {

    @Value("${websocket.tio.server.port}")
    private int port;
    @Value("${websocket.tio.server.name}")
    private String protocolName;
    @Value("${websocket.tio.server.heartbeat.timeout}")
    private int heartbeatTimeout;

    @Autowired
    private TioIpStatListener tioIpStatListener;

    @Autowired
    private WebsocketServerAioListener websocketServerAioListener;

    @Bean
    public CurrentWsServerStarter wsServerStarter(WebsocketWsMsgHandler websocketWsMsgHandler) throws Exception {
        CurrentWsServerStarter wsServerStarter = new CurrentWsServerStarter(port, websocketWsMsgHandler);
        try {
            log.info("Tio-Websocet装载配置starting...");
            ServerGroupContext serverGroupContext = wsServerStarter.getServerGroupContext();
            serverGroupContext.setName(protocolName);
            serverGroupContext.setServerAioListener(websocketServerAioListener);

            //设置ip监控
            serverGroupContext.setIpStatListener(tioIpStatListener);
            //设置ip统计时间段
            serverGroupContext.ipStats.addDurations(CommonConstants.IPSTAT_DURATIONS);

            //设置心跳超时时间
            serverGroupContext.setHeartbeatTimeout(heartbeatTimeout);
            log.info("Tio-Websocet装载配置endding...");
        }catch (RuntimeException e){
            log.info("Tio-Websocet装载配置失败...",e.getMessage());
        }
        return wsServerStarter;
    }
}
