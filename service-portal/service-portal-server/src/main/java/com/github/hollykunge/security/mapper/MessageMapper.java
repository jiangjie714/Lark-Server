package com.github.hollykunge.security.mapper;

import com.github.hollykunge.security.entity.Message;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @description: 工作台
 * @author: dd
 * @since: 2019-06-08
 */
public interface MessageMapper extends Mapper<Message> {
    /**
     * 查询用户最新聊天信息
     * @param message
     * @return
     */
    List<Message> userMessages(Message message);
}
