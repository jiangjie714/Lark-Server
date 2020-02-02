package com.github.hollykunge.security.common.vo.mq;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作热力图mq消息
 * @author zhhongyu
 * @since 2019-06-21
 */
@Data
@NoArgsConstructor
public class HotMapVO implements Serializable {
    private static final long serialVersionUID = -7128203829971899888L;

    private String userId;
    private Date mapDate;

}
