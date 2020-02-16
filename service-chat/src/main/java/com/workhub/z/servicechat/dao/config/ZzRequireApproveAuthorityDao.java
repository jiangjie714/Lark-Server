package com.workhub.z.servicechat.dao.config;

import com.workhub.z.servicechat.entity.config.ZzRequireApproveAuthority;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author:zhuqz
 * description: 新建群组、会议、上传下载附件不需要审批
 * date:2019/11/8 14:33
 **/
public interface ZzRequireApproveAuthorityDao extends Mapper<ZzRequireApproveAuthority> {
    /**
     * 获取哪些群体不用特殊权限
     * @return
     */
    ZzRequireApproveAuthority queryData();

    /**
     * 更新不用审批权限的群体，逗号分割
     * @param teams
     * @return
     */
    int updateData(@Param("teams") String teams);
}