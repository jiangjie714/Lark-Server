package com.github.hollykunge.security.task.feign;


import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.vo.RpcUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-16
 * @deprecation 用于获取项目参与人员的具体信息 例如姓名 职位等
 */
@FeignClient("lark-admin")
public interface LarkProjectFeign {

    @RequestMapping(value = "/user/getUserInfo",method = RequestMethod.POST)
    ObjectRestResponse<List<RpcUserInfo>>  getUserInfo(@RequestBody List<String> userIdList);
}
