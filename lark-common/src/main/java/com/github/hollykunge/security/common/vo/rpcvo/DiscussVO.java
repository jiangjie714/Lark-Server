package com.github.hollykunge.security.common.vo.rpcvo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 研讨页面展示实体类
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class DiscussVO implements Serializable {

    private String id;

    private String user;

    private String avatar;

    private String content;

    private Integer unread;

    private String time;
}
