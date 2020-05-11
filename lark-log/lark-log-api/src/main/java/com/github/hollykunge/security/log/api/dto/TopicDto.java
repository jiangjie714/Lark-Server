package com.github.hollykunge.security.log.api.dto;

import lombok.Data;

/**
 * @author: zhhongyu
 * @description: topic数据
 * @since: Create in 15:19 2020/2/28
 */
@Data
public class TopicDto {
    /**
     * 主题id（对应mongodb中的主题id）
     */
    private String topicId;

    /**
     * 主体名称
     */
    private String topicName;
    /**
     * 分区
     */
    private String partition;
    /**
     * 索引
     */
    private String offSet;
    /**
     * 消息
     */
    private MessageDto message;
}
