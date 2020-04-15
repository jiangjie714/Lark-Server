package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.task.entity.LarkProject;
import com.github.hollykunge.security.task.entity.LarkProjectDto;
import com.github.hollykunge.security.task.mapper.LarkProjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-14
 * @deprecation project service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LarkProjectBiz extends BaseBiz<LarkProjectMapper, LarkProject> {

    @Autowired
    private LarkProjectMapper larkProjectMapper;
    @Override
    protected String getPageName() {
        return null;
    }

    /**
     * 删除项目
     * @param projectCode
     */
    public ObjectRestResponse<LarkProject> deleteProject(String projectCode){
        LarkProject larkProject = larkProjectMapper.selectByPrimaryKey(projectCode);
        larkProjectMapper.deleteByPrimaryKey(projectCode);
        return new ObjectRestResponse<>().data(larkProject).rel(true).msg("删除成功");
    }

    /**
     * 根据用户id 获取项目
     * @param query
     * @return
     */
    @Override
    public TableResultResponse<LarkProject> selectByQuery(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        Object userId = query.get("userId");
        if(StringUtils.isEmpty(userId)){
            throw new BaseException("用户id不可为空");
        }
        List<LarkProject> larkProjectList = larkProjectMapper.getProjectPageListByUserId(userId.toString());
        return new TableResultResponse<>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), larkProjectList);
    }

    public ObjectRestResponse<LarkProjectDto> getProjectUser(String projectCode){
        LarkProject larkProject = larkProjectMapper.selectByPrimaryKey(projectCode);
        //todo 未完成
        return null;
    }
}
