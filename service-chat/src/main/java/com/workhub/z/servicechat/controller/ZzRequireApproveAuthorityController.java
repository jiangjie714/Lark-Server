package com.workhub.z.servicechat.controller;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.service.ZzRequireApproveAuthorityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author:zhuqz
 * description:创建群、会议下载不用审批
 * date:2019/11/11 11:20
 **/
@RestController
@RequestMapping("/zzRequireApproveAuthority")
public class ZzRequireApproveAuthorityController {
    @Resource
    ZzRequireApproveAuthorityService requireApproveAuthorityService;

    /**
     * 获取哪些群体不用审批
     * @return
     */
    public ObjectRestResponse queryData(){
        ObjectRestResponse res = new ObjectRestResponse();
        res.data(this.requireApproveAuthorityService.queryData());
        res.rel(true);
        res.msg("500");
        return  res;
    }

    /**
     * 更新不用权限的群体 是orgcode，多个逗号分割
     * @param teams
     * @return
     */
    public ObjectRestResponse updateData(@RequestParam String teams){
        ObjectRestResponse res = new ObjectRestResponse();
        res.data(this.requireApproveAuthorityService.updateData(teams));
        res.rel(true);
        res.msg("500");
        return  res;
    }
}