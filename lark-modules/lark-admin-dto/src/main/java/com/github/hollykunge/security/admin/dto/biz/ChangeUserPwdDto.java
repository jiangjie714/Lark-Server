package com.github.hollykunge.security.admin.dto.biz;

import lombok.Data;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 原始密码
     */
    @NotBlank(message = "原始密码不能为空")
    private String oldPassword;
    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
