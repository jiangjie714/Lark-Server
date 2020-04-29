package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.PrivateFileVo;

/**
 * 私人消息(ZzPrivateMsg)表服务接口
 *
 * @author 忠
 * @since 2019-05-13 10:57:46
 */
public interface ZzPrivateMsgService {
    //私有聊天文件查询
    public TableResultResponse<PrivateFileVo> getFileList(String userId, String receiverId, String query, int page, int size) throws Exception ;
}