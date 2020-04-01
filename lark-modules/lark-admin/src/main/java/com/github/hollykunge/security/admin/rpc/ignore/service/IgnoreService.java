package com.github.hollykunge.security.admin.rpc.ignore.service;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author fansq
 * @since 20-3-27
 */
@FeignClient(value = "service-chat")
public interface IgnoreService {

    /**
     * 群统计
     * @param orgCode
     * @param dateType
     * @return
     */
    @RequestMapping(value = "/statistics/groupInfStatistics", method = RequestMethod.GET)
    ObjectRestResponse groupStatistics(@RequestParam("orgCode")String orgCode, @RequestParam(name = "date",required = false)String dateType);


    /**
     * 消息统计
     * @param orgCode
     * @param dateType
     * @return
     */
    @RequestMapping(value = "/statistics/msgStatistics", method = RequestMethod.GET)
    ObjectRestResponse msgStatistics(@RequestParam("orgCode")String orgCode, @RequestParam(name = "date",required = false)String dateType);


    /**
     * 附件统计
     * @param orgCode
     * @param dateType
     * @return
     */
    @RequestMapping(value = "/statistics/fileStatistics", method = RequestMethod.GET)
    ObjectRestResponse fileStatistics(@RequestParam("orgCode")String orgCode, @RequestParam(name = "date",required = false)String dateType);

    /**
     * 群人员统计获取
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/statistics/groupOrgStatistics", method = RequestMethod.GET)
    ObjectRestResponse groupUserStatistics(@RequestParam(name = "groupId",required = false)String groupId);
}
