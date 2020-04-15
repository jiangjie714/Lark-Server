package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.task.config.TaskProduceSend;
import com.github.hollykunge.security.task.entity.LarkProject;
import com.github.hollykunge.security.task.entity.LarkProjectMember;
import com.github.hollykunge.security.task.mapper.LarkProjectMapper;
import com.github.hollykunge.security.task.mapper.LarkProjectMemberMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author fansq
 * @since 20-4-14
 * @deprecation 项目-人员service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LarkProjectMemberbiz extends BaseBiz<LarkProjectMemberMapper, LarkProjectMember> {

    @Autowired
    private LarkProjectMemberMapper larkProjectMemberMapper;

    @Autowired
    private LarkProjectMapper larkProjectMapper;

    @Autowired
    private TaskProduceSend taskProduceSend;
    /**
     * 移除项目成员
     * @return
     */
    public ObjectRestResponse<LarkProjectMember> deleteUserForProject(String memberCode, String projectCode){
        Example example = new Example(LarkProjectMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberCode",memberCode);
        criteria.andEqualTo("projectCode",projectCode);
        LarkProjectMember larkProjectMember = larkProjectMemberMapper.selectByExample(example).get(0);
        larkProjectMemberMapper.deleteByExample(example);
        return new ObjectRestResponse<>().data(larkProjectMember).msg("成员已移除！").rel(true);
    }

    /**
     * 发送项目邀请信息
     */
    public void sendInviteMemberMessage(String projectCode){
        LarkProject larkProject = larkProjectMapper.selectByPrimaryKey(projectCode);
        taskProduceSend.sendCancelPortal(projectCode,larkProject);
    }

    @Override
    public TableResultResponse<LarkProjectMember> selectByQuery(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        Example example = new Example(LarkProjectMember.class);
        Object projectCode = query.get("projectCode");
        if(StringUtils.isEmpty(projectCode)){
            throw  new BaseException("项目id不可为空！");
        }
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectCode",projectCode);
        List<LarkProjectMember> larkProjectMembers = larkProjectMemberMapper.selectByExample(example);
        return new TableResultResponse<>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), larkProjectMembers);
    }

    @Override
    protected String getPageName() {
        return null;
    }
}
