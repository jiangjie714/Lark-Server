package com.workhub.z.servicechat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:zhuqz
 * description:群/会议状态变更记录
 * date:2019/8/26 15:48
 **/
@Data
public class ZzGroupStatus implements Serializable {
    private static final long serialVersionUID = 6513085026622637229L;
    private String id;
    private String groupId;
    private String operator;
    private String operatorName;
    /**操作类型300新建群，301新增成员，302删除成员，303群解散，304上传群文件 后续跟进需要补充*/
    private String operateType;
    private Date operateTime;
    private String describe;
    //0群，1会议
    private String type;
}
