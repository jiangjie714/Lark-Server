package com.workhub.z.servicechat.service;

import com.workhub.z.servicechat.entity.config.ZzRequireApproveAuthority;

/**
 * @author:zhuqz
 * description:创建群、会议下载不用审批
 * date:2019/11/11 11:18
 **/
public interface ZzRequireApproveAuthorityService {
    /**
     * 获取哪些群体不用审批
     * @return
     */
    ZzRequireApproveAuthority queryData();
    /**
     * 更新不用审批权限的群体，逗号分割
     * @param teams
     * @return
     */
    int updateData(String teams);

    /**
     * 是否需要审批权限 1是0否
     * @param orgid
     * @return
     */
    int needApprove(String orgid);
}