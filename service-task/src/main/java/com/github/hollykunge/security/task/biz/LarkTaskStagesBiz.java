package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.task.dto.LarkTaskStagesDto;
import com.github.hollykunge.security.task.entity.LarkTaskStages;
import com.github.hollykunge.security.task.mapper.LarkTaskStagesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-17
 * @deprecation 任务列表表 通过stage_code 和lark_task 任务具体信息表关联
 */
@Service
public class LarkTaskStagesBiz extends BaseBiz<LarkTaskStagesMapper, LarkTaskStages> {

    @Autowired
    private LarkTaskStagesMapper larkTaskStagesMapper;

    /**
     * 获取指定项目的任务列表
     * @return
     */
    public List<LarkTaskStagesDto> getTasksByProjectId(String projectCode){
       return larkTaskStagesMapper.getTasksByProjectId(projectCode);
    }

    /**
     * 根据项目id 获取任务列 列表
     * @return 任务列表
     */
    public ObjectRestResponse<List<LarkTaskStages>> getStagesList(String projectCode){
        Condition condition = new Condition(LarkTaskStages.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("projectCode",projectCode);
        criteria.andEqualTo("deleted",0);
        condition.orderBy("crtTime").desc();
        List<LarkTaskStages> larkTaskStages = larkTaskStagesMapper.selectByExample(condition);
        return new ObjectRestResponse<>().data(larkTaskStages).rel(true);
    }

    @Override
    protected String getPageName() {
        return null;
    }
}
