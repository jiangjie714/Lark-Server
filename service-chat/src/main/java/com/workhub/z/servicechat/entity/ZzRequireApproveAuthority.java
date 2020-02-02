package com.workhub.z.servicechat.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:zhuqz
 * description: 新建群组、会议、上传下载附件不需要审批
 * date:2019/11/8 14:34
 **/
@Data
public class ZzRequireApproveAuthority implements Serializable {
    private static final long serialVersionUID = -1759346342986138468L;
    /**
     * 通知的socket群体id，多个逗号分割
     */
    private String socketTeam;
}