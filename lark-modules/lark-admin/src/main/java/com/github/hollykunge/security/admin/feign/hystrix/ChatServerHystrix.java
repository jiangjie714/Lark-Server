package com.github.hollykunge.security.admin.feign.hystrix;

import com.github.hollykunge.security.admin.feign.ZzMessageInfoFeign;
import com.github.hollykunge.security.common.feign.BaseHystrixFactory;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 调用研讨服务降级
 * @since: Create in 14:56 2020/5/18
 */
@Component
public class ChatServerHystrix extends BaseHystrixFactory<ChatServerHystrix> implements ZzMessageInfoFeign {
    @Override
    public TableResultResponse queryAllMessagePrivate(Map params) {
        return getHystrixTableReponse();
    }

    @Override
    public TableResultResponse queryAllMessageGroup(Map params) {
        return getHystrixTableReponse();
    }
}
