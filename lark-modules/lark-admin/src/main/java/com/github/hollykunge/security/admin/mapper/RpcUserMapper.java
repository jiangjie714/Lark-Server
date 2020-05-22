package com.github.hollykunge.security.admin.mapper;


import com.github.hollykunge.security.admin.dto.biz.AdminUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author LARK
 */
public interface RpcUserMapper{
    /**
     * 根据人员id和身份证号，获取人员信息
     * 可能存在人员多岗位的问题故而使用list接收
     * @param pid
     * @param userid
     * @return
     */
    List<AdminUser> getUserInfo(@Param("pid") String pid, @Param("userid") String userid);

}
