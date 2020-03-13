package com.workhub.z.servicechat.dao.group;

import com.workhub.z.servicechat.entity.group.ZzGroupApprove;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * author:zhuqz
 * description:
 * date:2019/9/11 15:58
 **/
public interface ZzGroupApproveDao  extends Mapper<ZzGroupApprove> {
    int add(@Param("params")ZzGroupApprove zzGroupApprove);
    int approve(@Param("params")Map params);
    List<Map> getApproveList(@Param("params")Map params);

    /**
     * 是否已经审批0未审批，其他已经审批
     * @param params
     * @return
     */
    String ifApprove(@Param("params")Map params);
    Map<String,Object> getSingleInf(@Param("id")String id);
    Map<String,Object> getApproveGroupDetail(@Param("dataid")String id);
    Map<String,Object> getSingleInfByGroupId(@Param("groupId")String groupId);
    List<Map<String,Object>> getApplyGroupList(@Param("params")Map params);
}
