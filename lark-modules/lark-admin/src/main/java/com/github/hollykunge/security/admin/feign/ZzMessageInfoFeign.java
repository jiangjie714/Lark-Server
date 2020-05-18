package com.github.hollykunge.security.admin.feign;

import com.github.hollykunge.security.admin.feign.hystrix.ChatServerHystrix;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 15:02 2019/8/16
 */
@FeignClient(value = "service-chat",path = "zzMessageInfo",fallbackFactory = ChatServerHystrix.class)
public interface ZzMessageInfoFeign {
    /**
     * 消息监控 私聊
     *
     * @param params  pageSize条数,pageNum页码,timeEnd开始时间,timeBegin结束时间,sender发送人,receiver接收人,levels消息密级
     * @return 列表
     */
    @RequestMapping(value="/queryAllMessagePrivate",method = RequestMethod.POST)
    TableResultResponse queryAllMessagePrivate( @RequestBody Map params);
    /**
     * 消息监控 群聊
     *
     * @param params  pageSize条数,pageNum页码,timeEnd开始时间,timeBegin结束时间,sender发送人,receiver接收人,levels消息密级
     * @return 列表
     */
    @RequestMapping(value="/queryAllMessageGroup",method = RequestMethod.POST)
    TableResultResponse queryAllMessageGroup(@RequestBody Map params);
}
