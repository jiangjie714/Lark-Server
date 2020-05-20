package com.github.hollykunge.security.admin.rpc.service;

import com.github.hollykunge.security.admin.entity.ErrorLogEntity;
import com.github.hollykunge.security.admin.mapper.ErrorLogMapper;
import com.github.hollykunge.security.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 13:47 2020/5/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ErrorLogService extends BaseBiz<ErrorLogMapper, ErrorLogEntity> {
    @Override
    protected String getPageName() {
        return "ErrorLogService";
    }
}
