package com.github.hollykunge.security.search.ik.feign;

import com.github.hollykunge.security.search.ik.dto.OrgDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author: zhhongyu
 * @description: 调用admin组织的对外提供接口
 * @since: Create in 13:10 2020/3/18
 */
@FeignClient(value = "lark-admin")
public interface IAdminOrgServiceFeign {
    @RequestMapping(value = "/api/org/all",method = RequestMethod.GET)
    public List<OrgDto> allOrg();
}
