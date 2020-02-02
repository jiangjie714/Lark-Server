package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @author:zhuqz
 * description: 不需要审批的消息体
 * date:2019/11/11 14:16
 **/
@Data
public class RequireApproveAuthorityVo {
    /**
     * 是否需要审批权限 1 是 0 否
     */
    private String requireApproveAuthority;
}