package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.task.entity.LarkTaskToTag;
import com.github.hollykunge.security.task.mapper.LarkTaskToTagMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LarkTaskToTagbiz extends BaseBiz<LarkTaskToTagMapper, LarkTaskToTag> {
    @Override
    protected String getPageName() {
        return null;
    }
}
