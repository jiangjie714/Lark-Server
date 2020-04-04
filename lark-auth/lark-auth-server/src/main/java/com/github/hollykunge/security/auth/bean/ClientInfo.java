package com.github.hollykunge.security.auth.bean;


import com.github.hollykunge.security.auth.common.util.jwt.IJWTInfo;

/**
 *
 * @author 协同设计小组
 * @date 2017/9/10
 */
public class ClientInfo implements IJWTInfo {

    String clientId;
    String name;
    String id;

    public ClientInfo(String clientId, String name, String id) {
        this.clientId = clientId;
        this.name = name;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUniqueName() {
        return clientId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSecretLevel() {
        return null;
    }

    @Override
    public String getOrgPathCode() {
        return null;
    }
}
