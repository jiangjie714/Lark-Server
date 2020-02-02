package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.VO.GroupApproveLogVo;
import com.workhub.z.servicechat.entity.ZzGroupApproveLog;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * author:zhuqz
 * description:
 * date:2019/9/13 14:19
 **/
public interface ZzGroupApproveLogDao   extends Mapper<ZzGroupApproveLog> {
    int add(@Param("params") ZzGroupApproveLog zzGroupApproveLog);
    List<GroupApproveLogVo> getApproveLogInf(@Param("params") Map<String,String> params);
}
