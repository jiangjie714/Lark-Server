package com.workhub.z.servicechat.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (ZzMegReadLog)实体类
 *
 * @author makejava
 * @since 2019-09-12 17:15:24
 */
@Data
public class ZzMegReadLog implements Serializable {
    private static final long serialVersionUID = 484978818332858903L;
    
    private String id;
    private String sender;
    private String reviser;
    private String reviserName;
    private String senderName;
    private Date readtime;
}