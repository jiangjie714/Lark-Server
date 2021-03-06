package com.workhub.z.servicechat.VO;

import java.util.Date;
import java.util.List;

public class SingleMessageVO {
        //    消息的id
        private String id;
        //    发送者姓名
        private String username;
        //    发送者头像地址
        private String avatar;
        //    发送者id
        private String fromId;
        //    接受者id
        private String toId;
        //    被@成员的id
        private List<String> atId;
        //    消息密级，需要匹配当前聊天的密级（60 - 非密，70 - 秘密，80 - 机密，90 - 绝密）
        private Integer secretLevel;
        //    消息类型，根据类型判断消息显示方式（1 - 文本消息，2 - 文件消息，3 - 图片消息）
        private Integer type;
        //   消息的内容（根据消息类型，如果为文本消息，该值为文本内容；如果为图片或者文件，该值为文件或图片的名称和文件类型，文件id）
        private ContentVO content;
        //   消息发送时间
        private Date time;
        //   消息发送时间（当天显示时间，昨天以前显示日志）
        private String sendTimeShort;
        //   消息发送时间（yyyy-MM-dd HH:mm:ss）
        private String sendTimeFull;
        //   是否群组
        private boolean isGroup;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public List<String> getAtId() {
            return atId;
        }

        public void setAtId(List<String> atId) {
            this.atId = atId;
        }

        public Integer getSecretLevel() {
            return secretLevel;
        }

        public void setSecretLevel(Integer secretLevel) {
            this.secretLevel = secretLevel;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public ContentVO getContent() {
            return content;
        }

        public void setContent(ContentVO content) {
            this.content = content;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public boolean isGroup() {
            return isGroup;
        }

        public void setGroup(boolean group) {
            isGroup = group;
        }

    public String getSendTimeShort() {
        return sendTimeShort;
    }

    public void setSendTimeShort(String sendTimeShort) {
        this.sendTimeShort = sendTimeShort;
    }

    public String getSendTimeFull() {
        return sendTimeFull;
    }

    public void setSendTimeFull(String sendTimeFull) {
        this.sendTimeFull = sendTimeFull;
    }
}
