package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.common.vo.RpcUserInfo;
import com.github.hollykunge.security.task.entity.LarkProject;
import com.github.hollykunge.security.task.dto.LarkProjectDto;
import com.github.hollykunge.security.task.entity.LarkProjectMember;
import com.github.hollykunge.security.task.entity.LarkTaskStages;
import com.github.hollykunge.security.task.feign.LarkProjectFeign;
import com.github.hollykunge.security.task.mapper.LarkProjectMapper;
import com.github.hollykunge.security.task.mapper.LarkProjectMemberMapper;
import com.github.hollykunge.security.task.mapper.LarkTaskStagesMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private LarkProjectMemberMapper larkProjectMemberMapper;

    @Autowired
    private LarkProjectFeign larkProjectFeign;

    @Autowired
    private LarkTaskStagesMapper larkTaskStagesMapper;


    @Override
    protected String getPageName() {
        return null;
    }

    public ObjectRestResponse<LarkProject> createProject(LarkProject project, HttpServletRequest request){

        String projectId = UUIDUtils.generateShortUuid();
        project.setCode(projectId);
        project.setId(projectId);
        larkProjectMapper.insertSelective(project);

        //待处理
        LarkTaskStages larkTaskStages = new LarkTaskStages();
        larkTaskStages.setName("待处理");
        larkTaskStages.setProjectCode(projectId);
        larkTaskStages.setDeleted(0);
        larkTaskStages.setSort(1);
        larkTaskStages.setDescrption("默认任务列表");
        larkTaskStages.setId(UUIDUtils.generateShortUuid());
        larkTaskStagesMapper.insertSelective(larkTaskStages);
        //进行中
        LarkTaskStages larkTask = new LarkTaskStages();
        larkTask.setName("进行中");
        larkTask.setProjectCode(projectId);
        larkTask.setDeleted(0);
        larkTask.setSort(1);
        larkTask.setDescrption("默认任务列表");
        larkTask.setId(UUIDUtils.generateShortUuid());
        larkTaskStagesMapper.insertSelective(larkTask);
        //已完成
        LarkTaskStages TaskStages = new LarkTaskStages();
        TaskStages.setName("已完成");
        TaskStages.setProjectCode(projectId);
        TaskStages.setDeleted(0);
        TaskStages.setSort(1);
        TaskStages.setDescrption("默认任务列表");
        TaskStages.setId(UUIDUtils.generateShortUuid());
        larkTaskStagesMapper.insertSelective(larkTask);

        LarkProjectMember larkProjectMember = new LarkProjectMember();
        larkProjectMember.setId(UUIDUtils.generateShortUuid());
        larkProjectMember.setProjectCode(projectId);
        String userId  = request.getHeader("userId");
        larkProjectMember.setMemberCode("68yHX85Z");
        larkProjectMember.setJoinTime(new Date());
        larkProjectMember.setIsOwner(0);
        larkProjectMember.setAuthorize("");
        larkProjectMemberMapper.insertSelective(larkProjectMember);

        return new ObjectRestResponse<>().data(project).msg("项目新建成功！").rel(true);
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
     * 根据用户id 获取项目列表
     * @param query
     * @return
     */
    public TableResultResponse<LarkProjectDto> selectByQueryUserInfo(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        Object userId = query.get("userId");
        if(StringUtils.isEmpty(userId)){
            throw new BaseException("用户id不可为空");
        }
        //List<LarkProject> larkProjectList = larkProjectMapper.getProjectPageListByUserId(userId.toString());
        List<LarkProjectDto> larkProjectDtos = larkProjectMapper.getProjectOwner(userId.toString());
        return new TableResultResponse<>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), larkProjectDtos);
    }

    public ObjectRestResponse<LarkProjectDto> getProjectUser(String projectCode){
        LarkProjectDto larkProjectDto = new LarkProjectDto();
        LarkProject larkProject = larkProjectMapper.selectByPrimaryKey(projectCode);
        BeanUtils.copyProperties(larkProject,larkProjectDto);
        Example example = new Example(LarkProjectMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectCode",projectCode);
        criteria.andEqualTo("isOwner",0);
        LarkProjectMember larkProjectMember = larkProjectMemberMapper.selectByExample(example).get(0);
        List<String> strings = new ArrayList<>();
        strings.add(larkProjectMember.getMemberCode());
        ObjectRestResponse<List<RpcUserInfo>> objectRestResponse = larkProjectFeign.getUserInfo(strings);
        RpcUserInfo rpcUserInfo = objectRestResponse.getResult().get(0);
        larkProjectDto.setProjectUserId(rpcUserInfo.getId());
        larkProjectDto.setProjectUserName(rpcUserInfo.getName());
        larkProjectDto.setProjectUserOrgCode(rpcUserInfo.getOrgCode());
        larkProjectDto.setProjectUserPid(rpcUserInfo.getPId());
        larkProjectDto.setProjectUserOrgCodeName(rpcUserInfo.getOrgName());
        larkProjectDto.setOEmail(rpcUserInfo.getOEmail());
        return new ObjectRestResponse<>().data(larkProjectDto).rel(true);
    }
}
