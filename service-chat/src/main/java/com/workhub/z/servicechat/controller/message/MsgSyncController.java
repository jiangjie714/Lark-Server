package com.workhub.z.servicechat.controller.message;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.service.MsgSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: zhuqz
 * @date: 2020/2/28 10:31
 * @description:消息同步
 */
@RestController
@RequestMapping("msgSync")
public class MsgSyncController {
    @Autowired
    MsgSyncService msgSyncService;
    /**
     * 同步消息
     * @return
     */
    @GetMapping("sync")
    public ObjectRestResponse msgStatistics(){
        int i = msgSyncService.syncMsg();
        if(i==0){
            return new ObjectRestResponse<>().rel(false).msg("500").data("error");
        }
        if(i==2){
            return new ObjectRestResponse<>().rel(true).msg("200").data("已经暂停同步消息");
        }
        return new ObjectRestResponse<>().rel(true).msg("200").data("sucess");
    }
}