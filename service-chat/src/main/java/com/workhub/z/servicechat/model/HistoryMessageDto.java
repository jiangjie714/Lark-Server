package com.workhub.z.servicechat.model;

import java.util.Date;

public class HistoryMessageDto {
    //    消息的id
    private String id;
    //    发送者id
    private String fromId;
    //    接受者id
    private String toId;
    //    消息密级，需要匹配当前聊天的密级（60 - 非密，70 - 秘密，80 - 机密，90 - 绝密）
    private String secretLevel;
    //    消息类型，根据类型判断消息显示方式（1 - 文本消息，2 - 文件消息，3 - 图片消息）
    private String type;
    //   消息的内容（根据消息类型，如果为文本消息，该值为文本内容；如果为图片或者文件，该值为文件或图片的名称和文件类型，文件id）
    private String content;
    //   消息发送时间
    private Date time;
    //   是否群组
    private String isGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(String secretLevel) {
        this.secretLevel = secretLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }
}
