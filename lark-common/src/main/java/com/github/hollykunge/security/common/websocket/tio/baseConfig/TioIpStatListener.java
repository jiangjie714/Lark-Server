package com.github.hollykunge.security.common.websocket.tio.baseConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.intf.Packet;
import org.tio.core.stat.IpStat;
import org.tio.utils.json.Json;

/**
 * IP 监控,可进行扩展
 *
 * @author: zhhongyu
 * @since: 2019/5/16
 */
@Slf4j
@Component
public class TioIpStatListener implements org.tio.core.stat.IpStatListener {

    public static TioIpStatListener me = new TioIpStatListener();

    @Override
    public void onExpired(GroupContext groupContext, IpStat ipStat) {
        if (log.isInfoEnabled()) {
            log.debug("可以把统计数据入库\r\n{}", Json.toFormatedJson(ipStat));
        }
    }

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean b, boolean b1, IpStat ipStat) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("onAfterConnected\r\n{}", Json.toFormatedJson(ipStat));
        }
    }

    @Override
    public void onDecodeError(ChannelContext channelContext, IpStat ipStat) {
        if (log.isInfoEnabled()) {
            log.debug("onDecodeError\r\n{}", Json.toFormatedJson(ipStat));
        }
    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean b, IpStat ipStat) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("onAfterSent\r\n{}\r\n{}", packet.logstr(), Json.toFormatedJson(ipStat));
        }
    }

    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int i, IpStat ipStat) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("onAfterDecoded\r\n{}\r\n{}", packet.logstr(), Json.toFormatedJson(ipStat));
        }
    }

    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int i, IpStat ipStat) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("onAfterReceivedBytes\r\n{}", Json.toFormatedJson(ipStat));
        }
    }

    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, IpStat ipStat, long l) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("onAfterHandled\r\n{}\r\n{}", packet.logstr(), Json.toFormatedJson(ipStat));
        }
    }
}
