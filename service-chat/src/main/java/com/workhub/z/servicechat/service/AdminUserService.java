package com.workhub.z.servicechat.service;

import com.workhub.z.servicechat.VO.ChatAdminUserVo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
/**
 * @auther: zhuqz
 * @date: 2020/3/24 08:59
 * @description: admin user服务feign适配器
 */
public interface AdminUserService {
    List<ChatAdminUserVo> userList(@PathVariable("ids") String userIds);
    ChatAdminUserVo getUserInfo(@PathVariable("id") String userId);
}
