package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.api.service.AdminUserServiceFeignClient;
import com.workhub.z.servicechat.VO.ChatAdminUserVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.service.AdminUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/3/24 08:59
 * @description: admin user服务feign适配器
 */
@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {
    Logger logger = LoggerFactory.getLogger(AdminUserServiceImpl.class);
    @Autowired
    AdminUserServiceFeignClient adminUserServiceFeignClient;

    @Override
    public List<ChatAdminUserVo> userList(String userIds) {
        List<AdminUser> adminUserList = adminUserServiceFeignClient.getUserListByIds(userIds);
        List<ChatAdminUserVo> chatUserList = new ArrayList<>(16);

        try {
            for(int i=0;i<adminUserList.size();i++){
                ChatAdminUserVo chatAdminUserVo = new ChatAdminUserVo();
                Common.copyObject(adminUserList.get(i),chatAdminUserVo);
                chatUserList.add(chatAdminUserVo);
            }
        } catch (Exception e) {
            logger.error("调用admin用户服务userList的适配器转换信息出错");
            logger.error(Common.getExceptionMessage(e));
            return null;
        }
        return chatUserList;
    }

    @Override
    public ChatAdminUserVo getUserInfo(String userId) {
        AdminUser adminUser = adminUserServiceFeignClient.getUserInfoByUserId(userId);
        ChatAdminUserVo chatAdminUserVo = new ChatAdminUserVo();

        try {
            Common.copyObject(adminUser,chatAdminUserVo);
        } catch (Exception e) {
            logger.error("调用admin用户服务getUserInfo的适配器转换信息出错");
            logger.error(Common.getExceptionMessage(e));
            return null;
        }
        return chatAdminUserVo;
    }

}