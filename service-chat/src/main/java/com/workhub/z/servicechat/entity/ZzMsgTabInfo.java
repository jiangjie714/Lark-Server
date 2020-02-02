package com.workhub.z.servicechat.entity;

import java.io.Serializable;

/**
 * 消息标记信息表(ZzMsgTabInfo)实体类
 *
 * @author makejava
 * @since 2019-05-23 16:46:13
 */
public class ZzMsgTabInfo implements Serializable {
    private static final long serialVersionUID = 779377105808837722L;
    //标记id
    private String id;
    //群组id
    private String groupId;
    //标记人id
    private String userId;
    //消息id
    private String msgId;
    //标记类型
    private String tabType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

}