package com.workhub.z.servicechat.controller;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.workhub.z.servicechat.service.ZzUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author:zhuqz
 * description: 用户在线离线
 * date:2019/12/10 15:21
 **/
@RestController
@RequestMapping("/zzUserOnline")
public class ZzUserOnlineController {
    @Autowired
    ZzUserOnlineService zzUserOnlineService;

    /**
     * 获取当前在线全部人员
     * @return
     */
    @GetMapping("/getAllOnlineUserList")
    public ListRestResponse getAllOnlineUserList(){
        List<String> userOnlineList = this.zzUserOnlineService.getAllOnlineUserList();
        return new ListRestResponse("200",userOnlineList.size(),userOnlineList);
    }
}
