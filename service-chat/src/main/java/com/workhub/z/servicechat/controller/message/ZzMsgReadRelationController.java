package com.workhub.z.servicechat.controller.message;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.workhub.z.servicechat.VO.NoReadVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.entity.message.ZzMsgReadRelation;
import com.workhub.z.servicechat.service.ZzMsgReadRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息阅读状态关系表(ZzMsgReadRelation)表控制层
 *
 * @author makejava
 * @since 2019-05-23 13:27:22
 */
@RestController
@RequestMapping("/MsgReadRelation/")
public class ZzMsgReadRelationController {
    private static Logger log = LoggerFactory.getLogger(ZzMsgReadRelationController.class);
    /**
     * 服务对象
     */
    @Resource
    private ZzMsgReadRelationService zzMsgReadRelationService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public ObjectRestResponse selectOne(String id) {
        ZzMsgReadRelation data=this.zzMsgReadRelationService.queryById(id);
        ObjectRestResponse res=new ObjectRestResponse();
        res.msg("200");
        res.rel(true);
        res.data(data);
        return res;
    }

    @PostMapping("/create")
    public ObjectRestResponse insert(@RequestBody ZzMsgReadRelation zzMsgReadRelation){
        zzMsgReadRelation.setId(RandomId.getUUID());
        try{
            Common.putEntityNullToEmptyString(zzMsgReadRelation);
        }catch (Exception e){
            e.getStackTrace();
            log.error(Common.getExceptionMessage(e));
        }
        this.zzMsgReadRelationService.insert(zzMsgReadRelation);
//        Integer insert = this.zzMsgReadRelationService.insert(zzMsgReadRelation);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
//        if (insert == null){
//            objectRestResponse.data("失败");
//            return objectRestResponse;
//        }
        objectRestResponse.data("成功");
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        return objectRestResponse;
    }

    @DeleteMapping("/delete")
    public ObjectRestResponse delete(@RequestParam("sender")String sender,
                                     @RequestParam("consumer")String consumer
                                     ){
        boolean flag = this.zzMsgReadRelationService.deleteByConsumerAndSender(sender,consumer);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.data("成功");
        objectRestResponse.msg("200");
        objectRestResponse.rel(flag);
        return objectRestResponse;
    }

    @PostMapping("/querynoreadcount")
    public ObjectRestResponse queryNoReadCount(@RequestParam("consumer")String consumer){
        Long count = this.zzMsgReadRelationService.queryNoReadCount(consumer);
        ObjectRestResponse res = new ObjectRestResponse();
        res.msg("200");
        res.rel(true);
        res.data(count);
        return res;
    }

    @PostMapping("/querynoreadcountlist")
    public ListRestResponse queryNoReadCountList(@RequestParam("consumer")String consumer){
        List<NoReadVo> list = this.zzMsgReadRelationService.queryNoReadCountList(consumer);
        return new ListRestResponse("200",list==null?0:list.size(),list);
    }
    /**
    *@Description: 清除未读标识
    *@Param:
    *@return:
    *@Author: 忠
    *@date: 2019/6/12
    */
    @PostMapping("/deleteBySender")
    public ObjectRestResponse deleteBySender(@RequestParam("sender")String sender,@RequestParam("receiver") String receiver){
        this.zzMsgReadRelationService.deleteByConsumerAndSender(sender,receiver);
        ObjectRestResponse restResponse=new ObjectRestResponse();
        restResponse.rel(true);
        restResponse.data("成功");
        restResponse.msg("200");
        return  restResponse;
    }
}