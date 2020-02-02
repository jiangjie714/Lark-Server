package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.entity.ZzCodeMeetingRole;

import java.util.Map;

/**
 * @author:zhuqz
 * description:会议角色代码表
 * date:2019/9/23 14:46
 **/
public interface ZzCodeMeetingRoleService {
    String add(ZzCodeMeetingRole zzCodeMeetingRole);
    int deleteData(Map param);
    int update(ZzCodeMeetingRole zzCodeMeetingRole);
    TableResultResponse query(Map param);
    boolean ifRoleCodeExist(String roleCode);
}

