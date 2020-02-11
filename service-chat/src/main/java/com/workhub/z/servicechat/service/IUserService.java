package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.workhub.z.servicechat.entity.UserInfo;


import java.util.List;
import java.util.Map;

public interface IUserService {
    /**
    *@Description: 根据user身份证查询用户信息
    *@Param: sn
    *@return: UserInfo
    *@Author: 忠
    *@date: 2019/3/22
    */

    UserInfo validate(String username, String password);

    List<UserInfo> userList(String userIdSet)  ;

    UserInfo getUserInfo(Map<String,String> map)  ;

}
