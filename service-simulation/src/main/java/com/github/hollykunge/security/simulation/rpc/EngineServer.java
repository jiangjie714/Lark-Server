package com.github.hollykunge.security.simulation.rpc;

public interface EngineServer extends com.zeroc.Ice.Object {
    void printString(String s, com.zeroc.Ice.Current current);

    String dllSystemPrepare(String systemName, double startTime, double step, double stopTime, String users, com.zeroc.Ice.Current current);

    String dllSystemStart(String systemName, com.zeroc.Ice.Current current);

    String dllSystemPause(String systemName, com.zeroc.Ice.Current current);

    String dllSystemStop(String systemName, com.zeroc.Ice.Current current);

    String dllGetOnlineNodes(String systemName, com.zeroc.Ice.Current current);

    static final String[] _iceIds =
            {
                    "::EngineDemo::EngineServer",
                    "::Ice::Object"
            };

    @Override
    default String[] ice_ids(com.zeroc.Ice.Current current) {
        return _iceIds;
    }

    @Override
    default String ice_id(com.zeroc.Ice.Current current) {
        return ice_staticId();
    }

    static String ice_staticId() {
        return "::EngineDemo::EngineServer";
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_printString(EngineServer obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current) {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_s;
        iceP_s = istr.readString();
        inS.endReadParams();
        obj.printString(iceP_s, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_dllSystemPrepare(EngineServer obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current) {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_systemName;
        double iceP_startTime;
        double iceP_step;
        double iceP_stopTime;
        String iceP_users;
        iceP_systemName = istr.readString();
        iceP_startTime = istr.readDouble();
        iceP_step = istr.readDouble();
        iceP_stopTime = istr.readDouble();
        iceP_users = istr.readString();
        inS.endReadParams();
        String ret = obj.dllSystemPrepare(iceP_systemName, iceP_startTime, iceP_step, iceP_stopTime, iceP_users, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_dllSystemStart(EngineServer obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current) {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_systemName;
        iceP_systemName = istr.readString();
        inS.endReadParams();
        String ret = obj.dllSystemStart(iceP_systemName, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_dllSystemPause(EngineServer obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current) {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_systemName;
        iceP_systemName = istr.readString();
        inS.endReadParams();
        String ret = obj.dllSystemPause(iceP_systemName, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_dllSystemStop(EngineServer obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current) {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_systemName;
        iceP_systemName = istr.readString();
        inS.endReadParams();
        String ret = obj.dllSystemStop(iceP_systemName, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_dllGetOnlineNodes(EngineServer obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current) {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_systemName;
        iceP_systemName = istr.readString();
        inS.endReadParams();
        String ret = obj.dllGetOnlineNodes(iceP_systemName, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    final static String[] _iceOps =
            {
                    "dllGetOnlineNodes",
                    "dllSystemPause",
                    "dllSystemPrepare",
                    "dllSystemStart",
                    "dllSystemStop",
                    "ice_id",
                    "ice_ids",
                    "ice_isA",
                    "ice_ping",
                    "printString"
            };

    @Override
    default java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceDispatch(com.zeroc.IceInternal.Incoming in, com.zeroc.Ice.Current current)
            throws com.zeroc.Ice.UserException {
        int pos = java.util.Arrays.binarySearch(_iceOps, current.operation);
        if (pos < 0) {
            throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
        }

        switch (pos) {
            case 0: {
                return _iceD_dllGetOnlineNodes(this, in, current);
            }
            case 1: {
                return _iceD_dllSystemPause(this, in, current);
            }
            case 2: {
                return _iceD_dllSystemPrepare(this, in, current);
            }
            case 3: {
                return _iceD_dllSystemStart(this, in, current);
            }
            case 4: {
                return _iceD_dllSystemStop(this, in, current);
            }
            case 5: {
                return com.zeroc.Ice.Object._iceD_ice_id(this, in, current);
            }
            case 6: {
                return com.zeroc.Ice.Object._iceD_ice_ids(this, in, current);
            }
            case 7: {
                return com.zeroc.Ice.Object._iceD_ice_isA(this, in, current);
            }
            case 8: {
                return com.zeroc.Ice.Object._iceD_ice_ping(this, in, current);
            }
            case 9: {
                return _iceD_printString(this, in, current);
            }
        }

        assert (false);
        throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
    }
}
