package com.github.hollykunge.security.auth.controller;

import com.github.hollykunge.security.auth.common.dto.SysAuthDto;
import com.github.hollykunge.security.auth.configuration.KeyConfiguration;
import com.github.hollykunge.security.auth.configuration.SystemUserConfiguration;
import com.github.hollykunge.security.auth.service.AuthClientService;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author 协同设计小组
 * @date 2017/9/10
 */
@RestController
@RequestMapping("client")
public class ClientController{
    @Autowired
    private AuthClientService authClientService;
    @Autowired
    private KeyConfiguration keyConfiguration;
    @Autowired
    private SystemUserConfiguration systemUserConfiguration;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ObjectRestResponse getAccessToken(String clientId, String secret) throws Exception {
        return new ObjectRestResponse<String>().data(authClientService.apply(clientId, secret));
    }

    @RequestMapping(value = "/myClient",method = RequestMethod.POST)
    public ObjectRestResponse getAllowedClient(String serviceId, String secret) {
        return new ObjectRestResponse<List<String>>().data(authClientService.getAllowedClient(serviceId, secret));
    }

    @RequestMapping(value = "/servicePubKey",method = RequestMethod.POST)
    public ObjectRestResponse<byte[]> getServicePublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret) throws Exception {
        authClientService.validate(clientId, secret);
        return new ObjectRestResponse<byte[]>().data(keyConfiguration.getServicePubKey());
    }

    @RequestMapping(value = "/userPubKey",method = RequestMethod.POST)
    public ObjectRestResponse<byte[]> getUserPublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret) throws Exception {
        authClientService.validate(clientId, secret);
        return new ObjectRestResponse<byte[]>().data(keyConfiguration.getUserPubKey());
    }
    @RequestMapping(value = "/sysUser",method = RequestMethod.GET)
    public ObjectRestResponse<SysAuthDto> getSysUser() throws Exception {
        SysAuthDto sysAuthDto = new SysAuthDto();
        BeanUtils.copyProperties(systemUserConfiguration,sysAuthDto);
        return new ObjectRestResponse<SysAuthDto>().data(sysAuthDto);
    }
}
