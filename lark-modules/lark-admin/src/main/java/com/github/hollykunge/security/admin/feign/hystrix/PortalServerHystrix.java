package com.github.hollykunge.security.admin.feign.hystrix;

import com.github.hollykunge.security.admin.feign.PortalServerFeign;
import com.github.hollykunge.security.common.feign.BaseHystrixFactory;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.portal.dto.CommonToolsDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 14:51 2020/5/18
 */
@Component
public class PortalServerHystrix extends BaseHystrixFactory<PortalServerHystrix> implements PortalServerFeign {
    @Override
    public TableResultResponse<CommonToolsDto> page(Map<String, Object> params) {
        return getHystrixTableReponse();
    }

    @Override
    public ObjectRestResponse<CommonToolsDto> update(Map<String, Object> params) {
        return getHystrixObjectReponse();
    }

    @Override
    public ObjectRestResponse<CommonToolsDto> remove(String id) {
        return getHystrixObjectReponse();
    }

    @Override
    public ObjectRestResponse<CommonToolsDto> add(HashMap<String, Object> hashMap) {
        return getHystrixObjectReponse();
    }
}
