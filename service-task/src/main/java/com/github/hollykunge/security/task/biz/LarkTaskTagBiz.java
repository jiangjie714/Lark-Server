package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.task.entity.LarkTaskTag;
import com.github.hollykunge.security.task.mapper.LarkTaskTagMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class LarkTaskTagBiz extends BaseBiz<LarkTaskTagMapper, LarkTaskTag> {

    @Autowired
    private LarkTaskTagMapper larkTaskTagMapper;
    @Override
    protected String getPageName() {
        return null;
    }
}
