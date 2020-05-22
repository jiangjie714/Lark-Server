package com.github.hollykunge.security.auth.mapper;

import com.github.hollykunge.security.auth.entity.Client;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author LARK
 */
public interface ClientMapper extends Mapper<Client> {

    List<String> selectAllowedClient(String serviceId);

    List<Client> selectAuthorityServiceInfo(int clientId);
}