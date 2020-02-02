package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.entity.Todo;
import com.github.hollykunge.security.mapper.TodoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: 工作台
 * @author: dd
 * @since: 2019-06-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TodoService extends BaseBiz<TodoMapper, Todo> {
    @Override
    protected String getPageName() {
        return null;
    }
}
