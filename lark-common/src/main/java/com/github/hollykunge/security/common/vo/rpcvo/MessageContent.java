package com.github.hollykunge.security.common.vo.rpcvo;

/**
 *@Description: 消息详细内容Dto,消息私有属性
 *@Author: 忠
 *@date: 2019/6/19
 */
public class MessageContent {
    //    文件ID，文本：0
    private String id;
    //    文件地址，文本：0
    private String url;
    //    消息类型，根据类型判断消息显示方式（1 - 文本消息，2 - 图片消息，3 - 文件消息）
    private Integer type;
    //    文件后缀，文本：0
    private String extension;
    //    文件名称，文本：文本内容
    private String title;
    //    文件密级，文本：文本密级
    private Integer secretLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(Integer secretLevel) {
        this.secretLevel = secretLevel;
    }


}

