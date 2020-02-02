package com.github.hollykunge.security.common.websocket.tio.baseConfig;

import org.tio.core.intf.TioUuid;
import org.tio.utils.thread.pool.SynThreadPoolExecutor;
import org.tio.websocket.server.WsServerConfig;
import org.tio.websocket.server.WsServerStarter;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>当前WsServerStarter服务，可进行扩展</p>
 */
public class CurrentWsServerStarter extends WsServerStarter {
    public CurrentWsServerStarter(int port, IWsMsgHandler wsMsgHandler) throws IOException {
        super(port, wsMsgHandler);
    }

    public CurrentWsServerStarter(int port, IWsMsgHandler wsMsgHandler, SynThreadPoolExecutor tioExecutor, ThreadPoolExecutor groupExecutor) throws IOException {
        super(port, wsMsgHandler, tioExecutor, groupExecutor);
    }

    public CurrentWsServerStarter(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler) throws IOException {
        super(wsServerConfig, wsMsgHandler);
    }

    public CurrentWsServerStarter(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler, SynThreadPoolExecutor tioExecutor, ThreadPoolExecutor groupExecutor) throws IOException {
        super(wsServerConfig, wsMsgHandler, tioExecutor, groupExecutor);
    }

    public CurrentWsServerStarter(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler, TioUuid tioUuid, SynThreadPoolExecutor tioExecutor, ThreadPoolExecutor groupExecutor) throws IOException {
        super(wsServerConfig, wsMsgHandler, tioUuid, tioExecutor, groupExecutor);
    }
}
