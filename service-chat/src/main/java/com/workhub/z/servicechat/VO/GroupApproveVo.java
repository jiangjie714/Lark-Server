package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @author:zhuqz
 * description:
 * date:2019/9/11 16:04
 **/
@Data
public class GroupApproveVo {
    private String id;
    private String creatorName;//创建人
    private String createTime;//创建时间
    private String groupName;//群名称
    private String groupLevel;//群密级
    private String groupMembers;//群成员个数
    private String groupId;//群id
    private String groupDescribe;//群描述
    private String approveStatus;//审批状态0待审批1通过2不通过
    //类型0群1会议
    private String type;
}
