package com.workhub.z.servicechat.feign;

import com.workhub.z.servicechat.entity.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "lark-admin")
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

    @RequestMapping(value = "/api/api/user/{ids}/list", method = RequestMethod.GET)
    List<UserInfo> userList(@PathVariable("ids") String userIds);

    @RequestMapping(value = "/api/user/all", method = RequestMethod.POST)
    public List<UserInfo> all();

    @RequestMapping(value = "/api/user/userId/{id}/info", method = RequestMethod.GET)
    UserInfo getUserInfoByUserId(@PathVariable("id") String userId);
    /**
     * 根据岗位id和密级获取人员列表
     * @param id 岗位id
     * @param secretLevel 密级
     * @return
     */
   /* @RequestMapping(value = "api/rpc/positionList", method = RequestMethod.GET)
    public List<UserInfo>  getUserListBySecret(@RequestParam("id") String id,@RequestParam("secretLevel")String secretLevel);*/
}
