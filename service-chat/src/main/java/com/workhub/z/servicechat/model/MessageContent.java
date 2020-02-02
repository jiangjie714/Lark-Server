package com.workhub.z.servicechat.model;
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
    //    消息类型，根据类型判断消息显示方式（1 - 文本消息，2 - 文件消息，3 - 图片消息）
    private Integer type;
    //    文件后缀，文本：0
    private String extension;
    //    文件名称，文本：文本内容
    private String title;
    //    文件密级，文本：文本密级
    private Integer secretLevel;

}
