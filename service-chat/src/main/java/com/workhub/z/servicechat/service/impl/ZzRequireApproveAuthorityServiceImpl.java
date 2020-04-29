package com.workhub.z.servicechat.service.impl;

import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.dao.ZzRequireApproveAuthorityDao;
import com.workhub.z.servicechat.entity.config.ZzRequireApproveAuthority;
import com.workhub.z.servicechat.service.ZzRequireApproveAuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static com.workhub.z.servicechat.config.MessageType.GROUP_SYS;

/**
 * @author:zhuqz
 * description: 创建群、会议下载不用审批
 * date:2019/11/11 11:19
 **/
@Service("zzRequireApproveAuthorityService")
public class ZzRequireApproveAuthorityServiceImpl implements ZzRequireApproveAuthorityService {
    @Resource
    ZzRequireApproveAuthorityDao zzRequireApproveAuthorityDao;
    /**
     * 获取哪些群体不用审批
     * @return
     */
    @Override
    public ZzRequireApproveAuthority queryData() {
        return zzRequireApproveAuthorityDao.queryData();
    }
    /**
     * 更新不用审批权限的群体，逗号分割
     * @param teams
     * @return
     */
    @Override
    public int updateData(String teams) {
        return this.zzRequireApproveAuthorityDao.updateData(teams);
    }

    /**
     * 是否需要审批权限 1是0否
     * @param orgid 人员org的code
     * @return
     */
    @Override
    public int needApprove(String orgid) {
        ZzRequireApproveAuthority zzRequireApproveAuthority = this.zzRequireApproveAuthorityDao.queryData();
        if(zzRequireApproveAuthority!=null && zzRequireApproveAuthority.getSocketTeam()!=null && !"".equals(zzRequireApproveAuthority.getSocketTeam())){
            String[] orgArr = zzRequireApproveAuthority.getSocketTeam().split(",");
            List<String> orgList = Arrays.asList(orgArr);
            //如果有全体不需要审批权限，直接返回不需要
            if(orgList.contains(GROUP_SYS)){
                return MessageType.NO_REQUIRE_APPROVE_AUTHORITY;
            }
            //如果列表里含有当期人所在的org，返回不需要的权限
            if(orgList.contains(orgid)){
                return MessageType.NO_REQUIRE_APPROVE_AUTHORITY;
            }
        }
        return MessageType.REQUIRE_APPROVE_AUTHORITY;
    }
}