package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.task.entity.LarkTaskWorkTime;
import com.github.hollykunge.security.task.mapper.LarkTaskWorkTimeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fansq
 * @since 20-4-24
 * @deprecatio 任务工时service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LarkTaskWorkTimeBiz extends BaseBiz<LarkTaskWorkTimeMapper, LarkTaskWorkTime> {
    @Override
    protected String getPageName() {
        return null;
    }
}
