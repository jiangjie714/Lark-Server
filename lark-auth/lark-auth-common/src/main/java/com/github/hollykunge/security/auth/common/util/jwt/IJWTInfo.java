package com.github.hollykunge.security.auth.common.util.jwt;

/**
 * Created by 协同设计小组 on 2017/9/10.
 */
public interface IJWTInfo {
    /**
     * 获取用户名
     * @return
     */
    String getUniqueName();

    /**
     * 获取用户ID
     * @return
     */
    String getId();

    /**
     * 获取名称
     * @return
     */
    String getName();

    /**
     * 获取人员密级
     * @return
     */
    String getSecretLevel();

    String getOrgPathCode();
}
