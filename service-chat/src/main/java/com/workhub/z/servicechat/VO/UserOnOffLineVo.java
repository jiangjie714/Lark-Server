package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @author zhuqz
 * 用户在线离线 推送前端
 */
@Data
public class UserOnOffLineVo {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 在线离线状态 700 在线 701不不在线
     */
    private String lineCode;
}
