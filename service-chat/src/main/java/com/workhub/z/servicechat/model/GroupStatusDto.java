package com.workhub.z.servicechat.model;

import lombok.Data;

import java.sql.Clob;

/**
 * @author:zhuqz
 * description: 群（会议） 流水日志
 * date:2019/10/24 9:27
 **/
@Data
public class GroupStatusDto {
    private String id;
    private String groupId;
    private String groupName;
    private String operator;
    private String operatorName;
    /**操作类型MessageType.FLOW...
     * 300新建群，301新增成员，302删除成员，303群解散，304上传群文件,305 下载群文件，306 群审批
     * 后续跟进需要补充*/
    private String operateType;
    private String operateTime;
    private Clob describe;
}
