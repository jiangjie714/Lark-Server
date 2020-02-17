package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.entity.group.ZzGroupApproveLog;

import java.util.Map;

/**
 * author:zhuqz
 * description:群审批日志
 * date:2019/9/13 14:23
 **/
public interface ZzGroupApproveLogService {
    //新增日志接口
    int add(ZzGroupApproveLog zzGroupApproveLog);
    //查询日志信息
    TableResultResponse getApproveLogInf(Map<String,String> param);
}

