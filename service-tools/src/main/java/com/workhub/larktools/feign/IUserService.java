package com.workhub.larktools.feign;

import com.workhub.larktools.entity.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "ace-admin")
@Repository
public interface IUserService {
    /**
    *@Description: 根据user身份证查询用户信息
    *@Param: sn
    *@return: UserInfo
    *@Author: 忠
    *@date: 2019/3/22
    */

    @RequestMapping(value = "/api/user/validate", method = RequestMethod.POST)
    public UserInfo validate(@RequestParam("username") String username, @RequestParam("password") String password);
//    @RequestMapping(value = "/api/user/test", method = RequestMethod.POST)
//    public void test();

    @RequestMapping(value = "/api/user/list", method = RequestMethod.POST)
    public List<UserInfo> userList(@RequestParam("userIds") String userIdSet);

    @RequestMapping(value = "/api/user/all", method = RequestMethod.POST)
    public List<UserInfo> all();

    @RequestMapping(value = "/api/user/info", method = RequestMethod.POST)
    public UserInfo info(@RequestParam("userId") String userId);

    @RequestMapping(value = "/api/orgUsers", method = RequestMethod.GET)
    public String orgUsers(@RequestParam("parentTreeId") String parentTreeId);

}
