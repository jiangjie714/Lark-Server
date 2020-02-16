package com.workhub.z.servicechat.entity.group;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:zhuqz
 * description:群审批实体映射表
 * date:2019/9/11 15:59
 **/
@Data
public class ZzGroupApprove implements Serializable {
    private static final long serialVersionUID = 8529358970999128038L;
    private String id;
    private String msg;//消息内容
    private String approveFlg;//审批标记0未审批1通过2不通过
    private Date createTime;//创建时间
    //群（或者其他资源）id
    private String groupId;
    private String approve;//审批人id
    private String approveName;//审批人命名
    private String approveNo;//审批人身份证
    private String approveList;//审批人列表
    private Date approveTime;//审批时间
    private String creator;//申请人id
    private String creatorName;//申请人姓名
    private String groupName;//群名称
    //类型0会议1会议
    private String type;
}
