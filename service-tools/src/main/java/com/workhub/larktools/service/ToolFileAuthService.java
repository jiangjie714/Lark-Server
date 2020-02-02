package com.workhub.larktools.service;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.workhub.larktools.entity.ToolFileAuth;

import java.util.Map;

/**
 * author:zhuqz
 * description:
 * date:2019/8/30 14:39
 **/
public interface ToolFileAuthService {
    int add(ToolFileAuth toolFileAuth);
    int delete(Map params);
    String ifAuthExists(String fileId,String orgCode);
    ListRestResponse queryByFileId(String fileId);
}
