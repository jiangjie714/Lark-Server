package com.github.hollykunge.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: 工作台-研讨
 * @author: dd
 * @since: 2019-06-07
 */
@Data
@Table(name = "PORTAL_MESSAGE")
public class Message {
    @Column(name = "ID")
    @Id
    private String id;
    @Column(name = "MSG")
    private String msg;
    @Column(name = "MSG_SENDER")
    private String msgSender;
    @Column(name = "FROM_USERNAME")
    private String fromUsername;
    @Column(name = "SEND_TIME")
    private Date sendTime;
    @Column(name = "READ")
    private String read;
    @Column(name = "MSG_TYPE")
    private String msgType;
    @Column(name = "LEVELS")
    private String levels;
    @Column(name = "AVATAR")
    private String avatar;
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "SEND_USER_ID")
    private String sendUserId;
}
