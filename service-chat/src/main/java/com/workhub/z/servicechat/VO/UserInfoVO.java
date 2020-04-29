package com.workhub.z.servicechat.VO;

import lombok.Data;

@Data
public class UserInfoVo {
    /**
     * 用户id
     */
    private String id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 密级
     */
    private String secretLevel;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 组织id
     */
    private String orgId;
    /**
     * 用户头像路径
     */
    private String avatar;
    /**
     * 是否在线1在线0离线
     */
    private String online;
    /**
     * org层级全路径
     */
    private String pathName;
}
