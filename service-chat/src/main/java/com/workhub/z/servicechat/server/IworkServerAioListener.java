package com.workhub.z.servicechat.server;

import com.ace.cache.annotation.Cache;
import com.workhub.z.servicechat.processor.ProcessLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.server.WsServerAioListener;

import javax.annotation.PostConstruct;

import static com.workhub.z.servicechat.config.MessageType.*;


/**
 * @author 忠
 * @program ace-security
 * @Description
 * @date 2019-01-11 13:58
 */
//@Component
public class IworkServerAioListener extends WsServerAioListener {

    private static Logger log = LoggerFactory.getLogger(IworkServerAioListener.class);

//    @Autowired
//    protected ProcessLogin processLogin;
//    private static IworkServerAioListener  serverHandler ;

//    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
//    public void init() {
//        serverHandler = this;
//        serverHandler.processLogin = this.processLogin;
//        // 初使化时将已静态化的Service实例化
//    }

    public static final IworkServerAioListener me = new IworkServerAioListener();

    private IworkServerAioListener() {
//        init();
    }
    //连接后
    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        super.onAfterConnected(channelContext, isConnected, isReconnect);
//        if (log.isInfoEnabled()) {
//            log.info("onAfterConnected\r\n{}", channelContext);
//        }
    }
    //第二次
    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
        super.onAfterSent(channelContext, packet, isSentSuccess);
//        if (log.isInfoEnabled()) {
//            log.info("onAfterSent\r\n{}\r\n{}", packet.logstr(), channelContext);
//        }
    }
    //关闭前
    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
        super.onBeforeClose(channelContext, throwable, remark, isRemove);
//        System.out.println("close"+channelContext);
//        if (log.isInfoEnabled()) {
//            log.info("onBeforeClose\r\n{}", channelContext);
//        }
    }
//    解码后
    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
        super.onAfterDecoded(channelContext, packet, packetSize);
//        if (log.isInfoEnabled()) {
//            log.info("onAfterDecoded\r\n{}\r\n{}", packet.logstr(), channelContext);
//        }
    }
//    收到字节消息后
    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
        super.onAfterReceivedBytes(channelContext, receivedBytes);
//        if (log.isInfoEnabled()) {
//            log.info("onAfterReceivedBytes\r\n{}", channelContext);
//        }
    }
//    handled处理后
    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
        super.onAfterHandled(channelContext, packet, cost);
//        if (log.isInfoEnabled()) {
//            log.info("onAfterHandled\r\n{}\r\n{}", packet.logstr(), channelContext);
//        }
    }
}
