package com.workhub.z.servicechat.entity.group;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:zhuqz
 * description:群（会议）审批日志实体映射表
 * date:2019/9/13 12:34
 **/
@Data
public class ZzGroupApproveLog implements Serializable {
    private static final long serialVersionUID = 3450692001440182917L;
    private String id;
    private String operator;//操作人id
    private String operatorNo;//操作人身份证
    private String operateType;//动作300新建群，301新增成员，302删除成员，303群解散，304上传群文件,305审批群 后续跟进需要补充
    private Date operateTime;//操作时间
    //群（或者其他资源）id
    private String groupId;
    private String groupName;//群名称
    private String groupLevel;//群密级
    private String groupPro;//群项目
    private String groupScope;//参与范围
    private String groupDes;//群描述
    private String groupType;//群类型0普通1跨科室2跨场所
    private String approve;//审批人id
    private String approveName;//审批人姓名
    private String approveRes;//审批结论1通过2不通过
    private String ip;//操作人ip
    private String status;//操作是否成功
    private String operatorName;//操作人姓名
    /**类型0群日志1会议日志 */
    private String type;
}
