package com.github.hollykunge.security.common.parser;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 9:17 2019/12/3
 */
public abstract class IDynamicSecretGenerator {
    public String[] getSecret(String secret,
                         Class<?>[] parameterTypes,
                         Object[] arguments) {
        return this.buildSecret(secret,parameterTypes,arguments);
    }

    /**
     * 当前登陆人密级
     *
     */
    public abstract IUserSecretGenerator getUserSecretGenerator();

    /**
     * 生成动态密级
     *
     * @param secret
     * @param parameterTypes
     * @param arguments
     * @return
     */
    protected abstract String[] buildSecret(String secret,
                                    Class<?>[] parameterTypes,
                                    Object[] arguments);
}
