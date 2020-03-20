package com.github.hollykunge.security.search.ik.service;

import com.alibaba.excel.util.StringUtils;
import com.github.hollykunge.security.search.ik.dto.OrgDto;
import com.github.hollykunge.security.search.ik.feign.IAdminOrgServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 13:42 2020/3/18
 */
@Service
public class OrgCodeIkService {
    @Autowired
    private IAdminOrgServiceFeign ifeign;

    public List<String> allOrgs(){
        List<OrgDto> orgDtos = ifeign.allOrg();
        List<String> result = new ArrayList<>();
        orgDtos.forEach(orgDto -> {
            if(!StringUtils.isEmpty(orgDto.getPathCode())){
                result.add(orgDto.getPathName());
            }
        });
        return result;
    }

}
