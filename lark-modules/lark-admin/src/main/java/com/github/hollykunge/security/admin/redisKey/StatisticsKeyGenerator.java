package com.github.hollykunge.security.admin.redisKey;

import com.ace.cache.constants.CacheScope;
import com.ace.cache.parser.IKeyGenerator;
import com.ace.cache.parser.IUserKeyGenerator;

/**
 * @author fansq
 * @since 20-4-1
 * @deprecation 统计key生成
 */
public class StatisticsKeyGenerator extends IKeyGenerator {
    @Override
    public IUserKeyGenerator getUserKeyGenerator() {
        return null;
    }

    @Override
    public String buildKey(String key, CacheScope scope, Class<?>[] parameterTypes, Object[] arguments) {
        return "StatisticsKey_"+arguments[0]+arguments[1]+key;
    }
}
