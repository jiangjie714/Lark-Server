package com.github.hollykunge.security.admin.feign.hystrix;

import com.github.hollykunge.security.admin.feign.CardServerFeign;
import com.github.hollykunge.security.common.feign.BaseHystrixFactory;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.portal.dto.CardDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 服务降级
 * @since: Create in 14:44 2020/5/18
 */
@Component
public class CardServerHystrix extends BaseHystrixFactory<CardServerHystrix> implements CardServerFeign {
    @Override
    public ObjectRestResponse<CardDto> add(HashMap<String, Object> hashMap) {
        return getHystrixObjectReponse();
    }

    @Override
    public TableResultResponse<CardDto> page(Map<String, Object> params) {
        return getHystrixTableReponse();
    }

    @Override
    public ObjectRestResponse<CardDto> update(HashMap<String, Object> hashMap) {
        return getHystrixObjectReponse();
    }

    @Override
    public ObjectRestResponse<CardDto> remove(String id) {
        return getHystrixObjectReponse();
    }
}
