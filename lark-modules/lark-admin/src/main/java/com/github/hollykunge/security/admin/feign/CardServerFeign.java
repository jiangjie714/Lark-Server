package com.github.hollykunge.security.admin.feign;

import com.github.hollykunge.security.admin.feign.hystrix.CardServerHystrix;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.portal.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fansq
 * @since 19-12-23
 */
@FeignClient(value = "service-portal",path = "/api/card",fallbackFactory = CardServerHystrix.class)
public interface CardServerFeign {

    /**
     * add
     * @param hashMap
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    ObjectRestResponse<CardDto> add(@RequestBody HashMap<String,Object> hashMap);

    /**
     * 分页
     * @param params
     * @return
     */
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    TableResultResponse<CardDto> page(@RequestBody Map<String, Object> params);

    /**
     * 更新
     * @param hashMap
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    ObjectRestResponse<CardDto> update(@RequestBody HashMap<String,Object> hashMap);

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/remove",method = RequestMethod.GET)
    ObjectRestResponse<CardDto> remove(@RequestParam("id") String id);
}
