package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.GroupApplyVo;
import com.workhub.z.servicechat.VO.GroupApproveVo;
import com.workhub.z.servicechat.entity.group.ZzGroupApprove;

import java.util.Map;

/**
 * author:zhuqz
 * description:群审批
 * date:2019/9/11 16:14
 **/
public interface ZzGroupApproveService {
    /**
     * 新增
     * @param msg
     * @param userId
     * @param userName
     * @param userNo
     * @param userIp
     * @return
     */
    int saveApprove(String msg,String userId,String userName,String userNo,String userIp);

    /**
     * 审批
     * @param params
     * @return
     */
    Map<String,String>  approve(Map params);
    //审批列表查询
    TableResultResponse<GroupApproveVo> getApproveList(Map params) throws Exception;
    //获取审批详细
    String getApproveGroupDetail(String id);
    //单条数据获取
    String getSingleInfByGroupId(String groupId);
    //获取审批列表
    TableResultResponse<GroupApplyVo> getApplyGroupList(Map params) throws Exception;
}
