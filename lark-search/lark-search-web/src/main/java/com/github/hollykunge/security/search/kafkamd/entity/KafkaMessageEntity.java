package com.github.hollykunge.security.search.kafkamd.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author: zhhongyu
 * @description: 未发送成功的消息记录表（该表只保存未发送成功的消息记录）
 * @since: Create in 14:35 2020/2/28
 */
@Data
@Table(name = "KAFKA_MESSAGE_INFO")
public class KafkaMessageEntity extends BaseEntity {
    /**
     * 关联的主题id
     */
    @Column(name = "TOPIC_ID")
    private String topicId;
    /**
     * 消息内容
     */
    @Column(name = "MESSAGE")
    private String message;
    /**
     * 是否发送成功
     */
    @Column(name = "IS_SUCCESS")
    private String isSuccess;
}
