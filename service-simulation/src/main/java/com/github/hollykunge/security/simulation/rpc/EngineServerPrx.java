package com.github.hollykunge.security.simulation.rpc;

public interface EngineServerPrx extends com.zeroc.Ice.ObjectPrx {
    default void printString(String s) {
        printString(s, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default void printString(String s, java.util.Map<String, String> context) {
        _iceI_printStringAsync(s, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<Void> printStringAsync(String s) {
        return _iceI_printStringAsync(s, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<Void> printStringAsync(String s, java.util.Map<String, String> context) {
        return _iceI_printStringAsync(s, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<Void> _iceI_printStringAsync(String iceP_s, java.util.Map<String, String> context, boolean sync) {
        com.zeroc.IceInternal.OutgoingAsync<Void> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "printString", null, sync, null);
        f.invoke(false, context, null, ostr -> {
            ostr.writeString(iceP_s);
        }, null);
        return f;
    }

    default String dllSystemPrepare(String systemName, double startTime, double step, double stopTime, String users) {
        return dllSystemPrepare(systemName, startTime, step, stopTime, users, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default String dllSystemPrepare(String systemName, double startTime, double step, double stopTime, String users, java.util.Map<String, String> context) {
        return _iceI_dllSystemPrepareAsync(systemName, startTime, step, stopTime, users, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> dllSystemPrepareAsync(String systemName, double startTime, double step, double stopTime, String users) {
        return _iceI_dllSystemPrepareAsync(systemName, startTime, step, stopTime, users, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> dllSystemPrepareAsync(String systemName, double startTime, double step, double stopTime, String users, java.util.Map<String, String> context) {
        return _iceI_dllSystemPrepareAsync(systemName, startTime, step, stopTime, users, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<java.lang.String> _iceI_dllSystemPrepareAsync(String iceP_systemName, double iceP_startTime, double iceP_step, double iceP_stopTime, String iceP_users, java.util.Map<String, String> context, boolean sync) {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.String> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "dllSystemPrepare", null, sync, null);
        f.invoke(true, context, null, ostr -> {
            ostr.writeString(iceP_systemName);
            ostr.writeDouble(iceP_startTime);
            ostr.writeDouble(iceP_step);
            ostr.writeDouble(iceP_stopTime);
            ostr.writeString(iceP_users);
        }, istr -> {
            String ret;
            ret = istr.readString();
            return ret;
        });
        return f;
    }

    default String dllSystemStart(String systemName) {
        return dllSystemStart(systemName, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default String dllSystemStart(String systemName, java.util.Map<String, String> context) {
        return _iceI_dllSystemStartAsync(systemName, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> dllSystemStartAsync(String systemName) {
        return _iceI_dllSystemStartAsync(systemName, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> dllSystemStartAsync(String systemName, java.util.Map<String, String> context) {
        return _iceI_dllSystemStartAsync(systemName, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<java.lang.String> _iceI_dllSystemStartAsync(String iceP_systemName, java.util.Map<String, String> context, boolean sync) {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.String> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "dllSystemStart", null, sync, null);
        f.invoke(true, context, null, ostr -> {
            ostr.writeString(iceP_systemName);
        }, istr -> {
            String ret;
            ret = istr.readString();
            return ret;
        });
        return f;
    }

    default String dllSystemPause(String systemName) {
        return dllSystemPause(systemName, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default String dllSystemPause(String systemName, java.util.Map<String, String> context) {
        return _iceI_dllSystemPauseAsync(systemName, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> dllSystemPauseAsync(String systemName) {
        return _iceI_dllSystemPauseAsync(systemName, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> dllSystemPauseAsync(String systemName, java.util.Map<String, String> context) {
        return _iceI_dllSystemPauseAsync(systemName, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<java.lang.String> _iceI_dllSystemPauseAsync(String iceP_systemName, java.util.Map<String, String> context, boolean sync) {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.String> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "dllSystemPause", null, sync, null);
        f.invoke(true, context, null, ostr -> {
            ostr.writeString(iceP_systemName);
        }, istr -> {
            String ret;
            ret = istr.readString();
            return ret;
        });
        return f;
    }

    default String dllSystemStop(String systemName) {
        return dllSystemStop(systemName, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default String dllSystemStop(String systemName, java.util.Map<String, String> context) {
        return _iceI_dllSystemStopAsync(systemName, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> dllSystemStopAsync(String systemName) {
        return _iceI_dllSystemStopAsync(systemName, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> dllSystemStopAsync(String systemName, java.util.Map<String, String> context) {
        return _iceI_dllSystemStopAsync(systemName, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<java.lang.String> _iceI_dllSystemStopAsync(String iceP_systemName, java.util.Map<String, String> context, boolean sync) {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.String> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "dllSystemStop", null, sync, null);
        f.invoke(true, context, null, ostr -> {
            ostr.writeString(iceP_systemName);
        }, istr -> {
            String ret;
            ret = istr.readString();
            return ret;
        });
        return f;
    }

    default String dllGetOnlineNodes(String systemName) {
        return dllGetOnlineNodes(systemName, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default String dllGetOnlineNodes(String systemName, java.util.Map<String, String> context) {
        return _iceI_dllGetOnlineNodesAsync(systemName, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> dllGetOnlineNodesAsync(String systemName) {
        return _iceI_dllGetOnlineNodesAsync(systemName, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.String> dllGetOnlineNodesAsync(String systemName, java.util.Map<String, String> context) {
        return _iceI_dllGetOnlineNodesAsync(systemName, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<java.lang.String> _iceI_dllGetOnlineNodesAsync(String iceP_systemName, java.util.Map<String, String> context, boolean sync) {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.String> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "dllGetOnlineNodes", null, sync, null);
        f.invoke(true, context, null, ostr -> {
            ostr.writeString(iceP_systemName);
        }, istr -> {
            String ret;
            ret = istr.readString();
            return ret;
        });
        return f;
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     *
     * @param obj The untyped proxy.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static EngineServerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj) {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, ice_staticId(), EngineServerPrx.class, _EngineServerPrxI.class);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     *
     * @param obj     The untyped proxy.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static EngineServerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, java.util.Map<String, String> context) {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, context, ice_staticId(), EngineServerPrx.class, _EngineServerPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     *
     * @param obj   The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static EngineServerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet) {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, ice_staticId(), EngineServerPrx.class, _EngineServerPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     *
     * @param obj     The untyped proxy.
     * @param facet   The name of the desired facet.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static EngineServerPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet, java.util.Map<String, String> context) {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, context, ice_staticId(), EngineServerPrx.class, _EngineServerPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     *
     * @param obj The untyped proxy.
     * @return A proxy for this type.
     **/
    static EngineServerPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj) {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, EngineServerPrx.class, _EngineServerPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     *
     * @param obj   The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type.
     **/
    static EngineServerPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj, String facet) {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, facet, EngineServerPrx.class, _EngineServerPrxI.class);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the per-proxy context.
     *
     * @param newContext The context for the new proxy.
     * @return A proxy with the specified per-proxy context.
     **/
    @Override
    default EngineServerPrx ice_context(java.util.Map<String, String> newContext) {
        return (EngineServerPrx) _ice_context(newContext);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the adapter ID.
     *
     * @param newAdapterId The adapter ID for the new proxy.
     * @return A proxy with the specified adapter ID.
     **/
    @Override
    default EngineServerPrx ice_adapterId(String newAdapterId) {
        return (EngineServerPrx) _ice_adapterId(newAdapterId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoints.
     *
     * @param newEndpoints The endpoints for the new proxy.
     * @return A proxy with the specified endpoints.
     **/
    @Override
    default EngineServerPrx ice_endpoints(com.zeroc.Ice.Endpoint[] newEndpoints) {
        return (EngineServerPrx) _ice_endpoints(newEndpoints);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator cache timeout.
     *
     * @param newTimeout The new locator cache timeout (in seconds).
     * @return A proxy with the specified locator cache timeout.
     **/
    @Override
    default EngineServerPrx ice_locatorCacheTimeout(int newTimeout) {
        return (EngineServerPrx) _ice_locatorCacheTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the invocation timeout.
     *
     * @param newTimeout The new invocation timeout (in seconds).
     * @return A proxy with the specified invocation timeout.
     **/
    @Override
    default EngineServerPrx ice_invocationTimeout(int newTimeout) {
        return (EngineServerPrx) _ice_invocationTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for connection caching.
     *
     * @param newCache <code>true</code> if the new proxy should cache connections; <code>false</code> otherwise.
     * @return A proxy with the specified caching policy.
     **/
    @Override
    default EngineServerPrx ice_connectionCached(boolean newCache) {
        return (EngineServerPrx) _ice_connectionCached(newCache);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoint selection policy.
     *
     * @param newType The new endpoint selection policy.
     * @return A proxy with the specified endpoint selection policy.
     **/
    @Override
    default EngineServerPrx ice_endpointSelection(com.zeroc.Ice.EndpointSelectionType newType) {
        return (EngineServerPrx) _ice_endpointSelection(newType);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for how it selects endpoints.
     *
     * @param b If <code>b</code> is <code>true</code>, only endpoints that use a secure transport are
     *          used by the new proxy. If <code>b</code> is false, the returned proxy uses both secure and
     *          insecure endpoints.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default EngineServerPrx ice_secure(boolean b) {
        return (EngineServerPrx) _ice_secure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the encoding used to marshal parameters.
     *
     * @param e The encoding version to use to marshal request parameters.
     * @return A proxy with the specified encoding version.
     **/
    @Override
    default EngineServerPrx ice_encodingVersion(com.zeroc.Ice.EncodingVersion e) {
        return (EngineServerPrx) _ice_encodingVersion(e);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its endpoint selection policy.
     *
     * @param b If <code>b</code> is <code>true</code>, the new proxy will use secure endpoints for invocations
     *          and only use insecure endpoints if an invocation cannot be made via secure endpoints. If <code>b</code> is
     *          <code>false</code>, the proxy prefers insecure endpoints to secure ones.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default EngineServerPrx ice_preferSecure(boolean b) {
        return (EngineServerPrx) _ice_preferSecure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the router.
     *
     * @param router The router for the new proxy.
     * @return A proxy with the specified router.
     **/
    @Override
    default EngineServerPrx ice_router(com.zeroc.Ice.RouterPrx router) {
        return (EngineServerPrx) _ice_router(router);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator.
     *
     * @param locator The locator for the new proxy.
     * @return A proxy with the specified locator.
     **/
    @Override
    default EngineServerPrx ice_locator(com.zeroc.Ice.LocatorPrx locator) {
        return (EngineServerPrx) _ice_locator(locator);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for collocation optimization.
     *
     * @param b <code>true</code> if the new proxy enables collocation optimization; <code>false</code> otherwise.
     * @return A proxy with the specified collocation optimization.
     **/
    @Override
    default EngineServerPrx ice_collocationOptimized(boolean b) {
        return (EngineServerPrx) _ice_collocationOptimized(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses twoway invocations.
     *
     * @return A proxy that uses twoway invocations.
     **/
    @Override
    default EngineServerPrx ice_twoway() {
        return (EngineServerPrx) _ice_twoway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses oneway invocations.
     *
     * @return A proxy that uses oneway invocations.
     **/
    @Override
    default EngineServerPrx ice_oneway() {
        return (EngineServerPrx) _ice_oneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch oneway invocations.
     *
     * @return A proxy that uses batch oneway invocations.
     **/
    @Override
    default EngineServerPrx ice_batchOneway() {
        return (EngineServerPrx) _ice_batchOneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses datagram invocations.
     *
     * @return A proxy that uses datagram invocations.
     **/
    @Override
    default EngineServerPrx ice_datagram() {
        return (EngineServerPrx) _ice_datagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch datagram invocations.
     *
     * @return A proxy that uses batch datagram invocations.
     **/
    @Override
    default EngineServerPrx ice_batchDatagram() {
        return (EngineServerPrx) _ice_batchDatagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, except for compression.
     *
     * @param co <code>true</code> enables compression for the new proxy; <code>false</code> disables compression.
     * @return A proxy with the specified compression setting.
     **/
    @Override
    default EngineServerPrx ice_compress(boolean co) {
        return (EngineServerPrx) _ice_compress(co);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection timeout setting.
     *
     * @param t The connection timeout for the proxy in milliseconds.
     * @return A proxy with the specified timeout.
     **/
    @Override
    default EngineServerPrx ice_timeout(int t) {
        return (EngineServerPrx) _ice_timeout(t);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection ID.
     *
     * @param connectionId The connection ID for the new proxy. An empty string removes the connection ID.
     * @return A proxy with the specified connection ID.
     **/
    @Override
    default EngineServerPrx ice_connectionId(String connectionId) {
        return (EngineServerPrx) _ice_connectionId(connectionId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except it's a fixed proxy bound
     * the given connection.@param connection The fixed proxy connection.
     *
     * @return A fixed proxy bound to the given connection.
     **/
    @Override
    default EngineServerPrx ice_fixed(com.zeroc.Ice.Connection connection) {
        return (EngineServerPrx) _ice_fixed(connection);
    }

    static String ice_staticId() {
        return "::EngineDemo::EngineServer";
    }
}
