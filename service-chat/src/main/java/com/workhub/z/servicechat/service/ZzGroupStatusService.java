package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.GroupStatusVo;
import com.workhub.z.servicechat.entity.ZzGroupStatus;

import java.util.Map;

/**
 * @author:zhuqz
 * description: 群（会议）流水日志
 * date:2019/8/26 16:50
 **/
public interface ZzGroupStatusService {
    /**
     * 新增
     * @param zzGroupStatus
     * @return
     */
    int add(ZzGroupStatus zzGroupStatus);

    /**
     * 查询
     * @param param
     * @return
     * @throws Exception
     */
    TableResultResponse<GroupStatusVo> query(Map param) throws Exception;
}
