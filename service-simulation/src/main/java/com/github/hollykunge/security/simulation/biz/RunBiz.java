package com.github.hollykunge.security.simulation.biz;

import com.github.hollykunge.security.simulation.rpc.EngineServerPrx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import static com.github.hollykunge.security.simulation.config.Constant.CONFIG_PATH;
import static com.github.hollykunge.security.simulation.config.Constant.RPC_SOCKET;

@Service
@Transactional(rollbackFor = Exception.class)
public class RunBiz {

    String[] args = null;

    public String dllSystemPrepare(
            String systemId, double startTime,
            double step, double stopTime, String users) {

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy(RPC_SOCKET);
            EngineServerPrx server = EngineServerPrx.checkedCast(base);
            if (server == null) {
                throw new Error("Invalid proxy on dllSystemPrepare");
            }
            return server.dllSystemPrepare(systemId, startTime, step, stopTime, users);
        }
    }

    public String dllSystemStart(String systemId) {
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy(RPC_SOCKET);
            EngineServerPrx server = EngineServerPrx.checkedCast(base);
            if (server == null) {
                throw new Error("Invalid proxy on dllSystemStart");
            }
            return server.dllSystemStart(systemId);
        }
    }

    public String dllSystemPause(String systemId) {
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy(RPC_SOCKET);
            EngineServerPrx server = EngineServerPrx.checkedCast(base);
            if (server == null) {
                throw new Error("Invalid proxy on dllSystemPause");
            }
            return server.dllSystemPause(systemId);
        }
    }

    public String dllSystemStop(String systemId) {
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy(RPC_SOCKET);
            EngineServerPrx server = EngineServerPrx.checkedCast(base);
            if (server == null) {
                throw new Error("Invalid proxy on dllSystemStop");
            }
            return server.dllSystemStop(systemId);
        }
    }

    public String dllGetOnlineNodes(String systemId) {
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy(RPC_SOCKET);
            EngineServerPrx server = EngineServerPrx.checkedCast(base);
            if (server == null) {
                throw new Error("Invalid proxy on dllGetOnlineNodes");
            }
            return server.dllGetOnlineNodes(systemId);
        }
    }

    public Boolean hasConfig(String systemId) {
        File file = new File(CONFIG_PATH + systemId + ".xml");
        return file.exists();
    }
}
