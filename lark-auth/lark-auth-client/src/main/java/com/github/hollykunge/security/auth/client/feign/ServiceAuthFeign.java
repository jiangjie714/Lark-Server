package com.github.hollykunge.security.auth.client.feign;

import com.github.hollykunge.security.auth.common.dto.SysAuthDto;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 * @author 协同设计小组
 * @date 2017/9/15
 */
@FeignClient(value = "${auth.serviceId}",configuration = {})
public interface ServiceAuthFeign {
    /**
     * @param serviceId
     * @param secret
     * @return
     */
    @RequestMapping(value = "/client/myClient",method = RequestMethod.POST)
    ObjectRestResponse<List<String>> getAllowedClient(@RequestParam("serviceId") String serviceId, @RequestParam("secret") String secret);

    /**
     * @param clientId
     * @param secret
     * @return
     */
    @RequestMapping(value = "/client/token",method = RequestMethod.POST)
    ObjectRestResponse getAccessToken(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);

    /**
     * @param clientId
     * @param secret
     * @return
     */
    @RequestMapping(value = "/client/servicePubKey",method = RequestMethod.POST)
    ObjectRestResponse<byte[]> getServicePublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);

    /**
     * @param clientId
     * @param secret
     * @return
     */
    @RequestMapping(value = "/client/userPubKey",method = RequestMethod.POST)
    ObjectRestResponse<byte[]> getUserPublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret);

    /**
     * @return
     */
    @RequestMapping(value = "/client/sysUser",method = RequestMethod.GET)
    ObjectRestResponse<SysAuthDto> getSysUser();
}
