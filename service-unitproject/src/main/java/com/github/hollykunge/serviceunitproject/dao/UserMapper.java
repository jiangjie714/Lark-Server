package com.github.hollykunge.serviceunitproject.dao;


import com.github.hollykunge.serviceunitproject.model.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {
  /*  *//**
     * 通过角色ID查询用户列表，关联表中查询
     *
     * @param roleId 角色ID
     * @return 用户列表
     *//*
    List<User> selectUsersByRoleId(@Param("roleId") String roleId);*/
}