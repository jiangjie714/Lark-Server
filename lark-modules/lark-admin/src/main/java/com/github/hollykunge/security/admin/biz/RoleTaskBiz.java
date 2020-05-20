package com.github.hollykunge.security.admin.biz;

import com.github.hollykunge.security.admin.entity.RoleTask;
import com.github.hollykunge.security.admin.mapper.RoleTaskMapper;
import com.github.hollykunge.security.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fansq
 * @since 20-4-28
 * @deprecation 仅限 project task role使用
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleTaskBiz extends BaseBiz<RoleTaskMapper, RoleTask> {
    @Override
    protected String getPageName() {
        return null;
    }
}
