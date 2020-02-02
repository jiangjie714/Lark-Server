package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @author:zhuqz
 * description:
 * date:2019/9/16 16:47
 **/
@Data
public class GroupApplyVo {
    private String id;//数据id
    private String apply;//申请人id
    private String applyName;//申请人名称
    private String applyTime;//申请时间
    private String groupName;//群名称
    private String groupLevel;//群密级
    private String groupMembers;//群成员个数
    private String groupId;//群id
    private String groupDescribe;//群描述
    private String groupScope;//群范围
    private String groupPro;//群项目
    private String approveStatus;//审批状态0待审批1通过2不通过
    private String approve;//审批人id，多个逗号分割
    private String approveName;//审批人姓名，多个逗号分割
    private String approveTime;//审批时间
    //类型：0群1会议
    private String type;
}
