package com.workhub.z.servicechat.dao.meeting;

import com.workhub.z.servicechat.entity.meeting.ZzCodeMeetingRole;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author:zhuqz
 * description:会议角色代码表
 * date:2019/9/23 14:45
 **/
public interface ZzCodeMeetingRoleDao extends Mapper<ZzCodeMeetingRole> {
    int add(@Param("params") ZzCodeMeetingRole zzCodeMeetingRole);
    int update(@Param("params") ZzCodeMeetingRole zzCodeMeetingRole);
    int deleteData(@Param("params") Map param);
    List<Map> query(@Param("params") Map param);
    String ifCodeNameExists(@Param("params") Map params);
    String getRoleCodeCnt(@Param("roleCode") String roleCode);
}
