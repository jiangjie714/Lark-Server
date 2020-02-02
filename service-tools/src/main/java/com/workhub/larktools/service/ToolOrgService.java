package com.workhub.larktools.service;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.workhub.larktools.vo.ToolTreeVo;

import java.util.Map;

public interface ToolOrgService {
    String add(Map param);
    int update(Map param);
    int updateTreeNodeParent(Map param);
    int delete(Map param);
    ListRestResponse<ToolTreeVo> queryCurrentUserTree(String userId);
    ListRestResponse<ToolTreeVo> query(Map param);
}
