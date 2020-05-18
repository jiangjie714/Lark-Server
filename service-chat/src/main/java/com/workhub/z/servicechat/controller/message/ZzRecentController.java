package com.workhub.z.servicechat.controller.message;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.VO.RecentVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.GateRequestHeaderParamConfig;
import com.workhub.z.servicechat.entity.message.ZzRecent;
import com.workhub.z.servicechat.service.ZzRecentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @auther: zhuqz
 * @date: 2020/4/30 16:35
 * @description: 最近联系人
 */
@RestController
@RequestMapping("/zzRecent")
public class ZzRecentController {
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
    @Autowired
    ZzRecentService zzRecentService;
    @Autowired
    HttpServletRequest request;

    /**
     * 新增最近联系人
     * @param zzRecent
     * @return
     *//*
    @PostMapping("/saveRecent")
    public ObjectRestResponse add(@RequestBody ZzRecent zzRecent){
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest));
        String ip = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        zzRecent.setCrtUser(userId);
        zzRecent.setCrtHost(ip);
        zzRecent.setCrtName(userName);
        int i = this.zzRecentService.saveRecent(zzRecent);

        if(i==0){
            return new ObjectRestResponse().rel(false).msg("500");
        }
        return new ObjectRestResponse().rel(true).msg("200");
    }

    *//**
     * 修改
     * @param zzRecent
     * @return
     *//*
    @PutMapping("/updateRecent")
    public ObjectRestResponse update(@RequestBody ZzRecent zzRecent){
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest));
        String ip = Common.nulToEmptyString(request.getHeader(clientIpInHeaderRequest));
        zzRecent.setCrtUser(userId);
        zzRecent.setCrtHost(ip);
        zzRecent.setCrtName(userName);
        int i = this.zzRecentService.updateRecent(zzRecent);

        if(i==0){
            return new ObjectRestResponse().rel(false).msg("500");
        }
        return new ObjectRestResponse().rel(true).msg("200");
    }

    *//**
     * 删除
     * @param userId 用户
     * @param contactId 联系人
     * @return
     *//*
    @DeleteMapping("/removeRecent")
    public ObjectRestResponse delete(@RequestParam("userId") String userId,@RequestParam("contactId") String contactId){

        int i = this.zzRecentService.removeRecent(userId,contactId);

        if(i==0){
            return new ObjectRestResponse().rel(false).msg("500");
        }
        return new ObjectRestResponse().rel(true).msg("200");
    }
*/
    /**
     * 获取最近联系人列表
     * @return
     */
    @GetMapping("/listRecents")
    public ListRestResponse getList(@RequestParam String userId){
//        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        List<RecentVo> dataList = this.zzRecentService.listRecents(userId);
        return new ListRestResponse("200",dataList.size(),dataList);
    }
}