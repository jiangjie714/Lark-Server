package com.github.hollykunge.serviceunitproject.service;

import com.github.hollykunge.serviceunitproject.common.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "ace-admin")
@Repository
public interface IUserService {
    /**
    *@Description: 根据user身份证查询用户信息
    *@Param:
    */

  /*  @RequestMapping(value = "/api/user/validate", method = RequestMethod.POST)
    public UserInfo validate(@RequestParam("username") String username, @RequestParam("password") String password);
//    @RequestMapping(value = "/api/user/test", method = RequestMethod.POST)
//    public void test();

    @RequestMapping(value = "/api/user/list", method = RequestMethod.GET)
    public List<UserInfo> userList(@RequestParam("userIds") String userIdSet);

    @RequestMapping(value = "/api/user/all", method = RequestMethod.POST)
    public List<UserInfo> all();*/
    //最新的带缓存的获取用户信息
    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public UserInfo getUserInfo(@RequestParam Map<String, String> map);
    /**
     * 根据岗位id和密级获取人员列表
     * @param id 岗位id
     * @param secretLevel 密级
     * @return
     */
   /* @RequestMapping(value = "api/rpc/positionList", method = RequestMethod.GET)
    public List<UserInfo>  getUserListBySecret(@RequestParam("id") String id,@RequestParam("secretLevel")String secretLevel);*/
}
