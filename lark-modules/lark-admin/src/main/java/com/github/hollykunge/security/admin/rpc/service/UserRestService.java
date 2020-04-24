package com.github.hollykunge.security.admin.rpc.service;

import com.ace.cache.annotation.Cache;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.admin.redisKey.IAdminRpcUserKey;
import com.github.hollykunge.security.common.exception.service.ClientParameterInvalid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: zhhongyu
 * @description: rpc相关人员业务
 * @since: Create in 14:20 2019/9/19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRestService {
    @Resource
    private UserMapper userMapper;

    /**
     * 根据userId或者根据pid来获取用户，影响性能
     *
     * @param pid
     * @param userId
     */
    @Cache(generator = IAdminRpcUserKey.class,key = AdminCommonConstant.CACHE_KEY_RPC_USER,expire = 525600)
    public String getUserInfo(String pid, String userId) {
        if (StringUtils.isEmpty(pid) && StringUtils.isEmpty(userId)) {
            throw new ClientParameterInvalid("pid或者userId为空。");
        }
        List<AdminUser> userInfo = userMapper.findByUserIdOrPid(userId, pid);
        return JSONObject.toJSONString(userInfo);
    }
}
