package com.github.hollykunge.security.auth.feign.hystrix;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.auth.feign.IUserService;
import com.github.hollykunge.security.common.feign.BaseHystrixFactory;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import org.springframework.stereotype.Component;

/**
 * @author: zhhongyu
 * @description: 接口调用降级
 * @since: Create in 14:43 2020/5/13
 */
@Component
public class IUserHystrix extends BaseHystrixFactory<IUserHystrix> implements IUserService {

    @Override
    public ObjectRestResponse<AdminUser> validate(String pid, String password) {
        return getHystrixObjectReponse();
    }
}