package com.workhub.z.servicechat.server;

import org.springframework.stereotype.Component;
import org.tio.core.GroupContext;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;

import java.io.IOException;

@Component
//@Order(1)
//public class IworkWebsocketStarter implements ApplicationRunner {
public class IworkWebsocketStarter {
    private static WsServerStarter wsServerStarter;
    private ServerGroupContext serverGroupContext;
    private static GroupContext groupContext;
    /**
     *
     * @author tanyaowu
     */
    public IworkWebsocketStarter() throws IOException {
        wsServerStarter = new WsServerStarter(IworkServerConfig.SERVER_PORT, IworkWsMsgHandler.me);

        serverGroupContext = wsServerStarter.getServerGroupContext();
        groupContext  = serverGroupContext;
        serverGroupContext.setName(IworkServerConfig.PROTOCOL_NAME);
        serverGroupContext.setServerAioListener(IworkServerAioListener.me);

        //设置ip监控
        serverGroupContext.setIpStatListener(IworkIpStatListener.me);
        //设置ip统计时间段
        serverGroupContext.ipStats.addDurations(IworkServerConfig.IpStatDuration.IPSTAT_DURATIONS);

        //设置心跳超时时间
        serverGroupContext.setHeartbeatTimeout(IworkServerConfig.HEARTBEAT_TIMEOUT);
    }

    /**
     * @return the serverGroupContext
     */
    public ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public static WsServerStarter getWsServerStarter() {
        return wsServerStarter;
    }

    public static GroupContext getGroupContext() {
        return groupContext;
    }

//    @Override 默认启动 取消
//    public void run(ApplicationArguments applicationArguments) throws Exception {
//        IworkWebsocketStarter appStarter = new IworkWebsocketStarter();
//        appStarter.wsServerStarter.start();
//        System.out.println("网络初始化成功!!");
//    }

    public void run() throws Exception {
        IworkWebsocketStarter appStarter = new IworkWebsocketStarter();
        wsServerStarter.start();
        System.out.println("网络初始化成功!!");
    }
}
