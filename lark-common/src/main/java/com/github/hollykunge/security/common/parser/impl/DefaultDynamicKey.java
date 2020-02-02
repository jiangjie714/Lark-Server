package com.github.hollykunge.security.common.parser.impl;

import com.github.hollykunge.security.common.config.AutoConfiguration;
import com.github.hollykunge.security.common.parser.IDynamicSecretGenerator;
import com.github.hollykunge.security.common.parser.IUserSecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

/**
 * @author: zhhongyu
 * @description: 默认生成key(通过)
 * @since: Create in 9:18 2019/12/3
 */
public class DefaultDynamicKey extends IDynamicSecretGenerator {
    private final static String SPLIT = ",";
    private final static String INDEX = "{";
    private final static String LAST = "}";

    @Autowired
    private IUserSecretGenerator iUserSecretGenerator;

    @Override
    public IUserSecretGenerator getUserSecretGenerator() {
        return iUserSecretGenerator;
    }

    @Override
    public String[] buildSecret(String secret, Class<?>[] parameterTypes, Object[] arguments) {
        //如果secret为{}则为配置的批量密级
        if (secret.indexOf(INDEX) == 0 && secret.lastIndexOf(LAST) == (secret.length()-1)) {
            String secretTemp = secret.substring(1, secret.length() - 1);
            if(secretTemp.contains(SPLIT)){
                return secretTemp.split(SPLIT);
            }
        }
        return new String[]{secret};
    }
}
