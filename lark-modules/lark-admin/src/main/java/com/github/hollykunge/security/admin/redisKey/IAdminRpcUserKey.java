package com.github.hollykunge.security.admin.redisKey;

import com.ace.cache.constants.CacheScope;
import com.ace.cache.parser.IKeyGenerator;
import com.ace.cache.parser.IUserKeyGenerator;
import com.github.hollykunge.security.common.exception.BaseException;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author: zhhongyu
 * @description: 缓存rpc用户key定义规则
 * @since: Create in 16:08 2019/9/19
 */
public class IAdminRpcUserKey extends IKeyGenerator {
    final String CONNECT = ":";

    @Override
    public IUserKeyGenerator getUserKeyGenerator() {
        return null;
    }

    @Override
    public String buildKey(String key, CacheScope scope, Class<?>[] parameterTypes, Object[] arguments) {
        String pre = "";
        String pid = (String) arguments[0];
        String userid = (String) arguments[1];
        if (!StringUtils.isEmpty(pid) && !Objects.equals("", pid)) {
            pre = pid;
        }
        if (StringUtils.isEmpty(pre) && !StringUtils.isEmpty(userid) && !Objects.equals("", userid)) {
            pre = userid;
        }
        if(StringUtils.isEmpty(pre)){
            throw new BaseException("pid或者userid不能都为null或空串...");
        }
        return key+CONNECT+pre;
    }

}