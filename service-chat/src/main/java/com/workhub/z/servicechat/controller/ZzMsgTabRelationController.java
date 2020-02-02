package com.workhub.z.servicechat.controller;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.entity.ZzMsgTabRelation;
import com.workhub.z.servicechat.service.ZzMsgTabRelationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 标记消息关系表(ZzMsgTabRelation)表控制层
 *
 * @author makejava
 * @since 2019-05-23 16:12:40
 */
@RestController
@RequestMapping("zzMsgTabRelation")
public class ZzMsgTabRelationController{
    /**
     * 服务对象
     */
    @Resource
    private ZzMsgTabRelationService zzMsgTabRelationService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public ObjectRestResponse selectOne(String id) {
        ZzMsgTabRelation data=this.zzMsgTabRelationService.queryById(id);
        ObjectRestResponse res=new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        res.data(data);
        return  res;
    }

}