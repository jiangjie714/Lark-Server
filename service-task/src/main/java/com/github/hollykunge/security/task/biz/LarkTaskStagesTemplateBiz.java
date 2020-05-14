package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.task.entity.LarkTaskStagesTemplate;
import com.github.hollykunge.security.task.mapper.LarkTaskStagesTemplateMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * @author fansq
 * @since 20-4-23
 * @deprecation 项目模板任务列表
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LarkTaskStagesTemplateBiz extends BaseBiz<LarkTaskStagesTemplateMapper, LarkTaskStagesTemplate> {
    @Override
    protected String getPageName() {
        return null;
    }

    /**
     * 模板任务列分页
     * @param query
     * @return
     */
    public TableResultResponse<LarkTaskStagesTemplate> list(Query query) {
        Class<LarkTaskStagesTemplate> clazz = (Class<LarkTaskStagesTemplate>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        Condition condition = new Condition(clazz);
        if(query.entrySet().size()>0) {
            Example.Criteria criteria = condition.createCriteria();
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                criteria.andEqualTo(entry.getKey(), entry.getValue().toString());
            }
        }
        condition.orderBy("sort").desc();
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<LarkTaskStagesTemplate> list = mapper.selectByExample(example);
        return new TableResultResponse<>(result.getPageSize(), result.getPageNum() ,result.getPages(), result.getTotal(), list);
    }
}
