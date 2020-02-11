package com.workhub.z.servicechat.service.impl;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.api.service.AdminUserServiceFeignClient;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.UserInfo;
import com.workhub.z.servicechat.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("IUserService")
public class UserServiceImpl implements IUserService {


     private AdminUserServiceFeignClient adminUserServiceFeignClient;

    @Override
    public UserInfo validate(String pid, String password) {
        UserInfo userInfo = new UserInfo();
        try {
            common.copyObject(adminUserServiceFeignClient.validate(pid,password),userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    @Override
    public List<UserInfo> userList(String userIdSet)  {
        List<AdminUser> adminUserList = adminUserServiceFeignClient.getUserListByIds(userIdSet);
        UserInfo userInfo = new UserInfo();
        List<UserInfo> userInfoList = null;
        for (AdminUser adminUser:adminUserList
             ) {
            try {
                common.copyObject(adminUser,userInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }

    @Override
    public UserInfo getUserInfo(Map<String, String> map)  {
        UserInfo userInfo = new UserInfo();
        try {
            common.copyObject(adminUserServiceFeignClient.getUserInfoByUserId(map.get("userId")),userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
