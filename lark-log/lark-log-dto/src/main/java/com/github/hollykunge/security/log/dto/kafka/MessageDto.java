package com.github.hollykunge.security.log.dto.kafka;

import lombok.Data;

/**
 * @author: zhhongyu
 * @description: 消息dto
 * @since: Create in 10:24 2020/3/4
 */
@Data
public class MessageDto {
    /**
     * fastjson类型的string
     */
    private String message;


}
