package com.github.hollykunge.security.admin.api.dto;

import lombok.Data;

/**
 * @author: zhhongyu
 * @description: 用户登录实体（用于用户修改密码传输实体用）
 * @since: Create in 12:30 2020/4/18
 */
@Data
public class ChangeUserPwdDto {
    /**
     * 用户名
     */
    private String username;
    /**
     * 原始密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;
}
