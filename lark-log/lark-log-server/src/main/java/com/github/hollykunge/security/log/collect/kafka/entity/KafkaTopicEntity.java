package com.github.hollykunge.security.log.collect.kafka.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author: zhhongyu
 * @description: kafuka创建topic的主题记录
 * @since: Create in 14:17 2020/2/28
 */
@Data
@Table(name = "KAFKA_TOPIC_INFO")
public class KafkaTopicEntity extends BaseEntity {
    /**
     * 主题名称
     */
    @Column(name = "TOPIC_NAME")
    private String topicName;
    /**
     * 分区
     */
    @Column(name = "PARTITION")
    private String partition;
    /**
     * 索引
     */
    @Column(name = "OFFSET")
    private String offSet;
}
