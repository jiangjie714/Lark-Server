package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.service.ClientParameterInvalid;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.task.constant.TaskCommon;
import com.github.hollykunge.security.task.dto.LarkTaskDto;
import com.github.hollykunge.security.task.dto.TaskNum;
import com.github.hollykunge.security.task.entity.LarkProject;
import com.github.hollykunge.security.task.entity.LarkTask;
import com.github.hollykunge.security.task.entity.LarkTaskMember;
import com.github.hollykunge.security.task.mapper.LarkProjectMapper;
import com.github.hollykunge.security.task.mapper.LarkTaskMapper;
import com.github.hollykunge.security.task.mapper.LarkTaskMemberMapper;
import com.github.hollykunge.security.task.vo.LarkTaskVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fansq
 * @since 20-4-17
 * @deprecation 任务操作 service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LarkTaskBiz extends BaseBiz<LarkTaskMapper, LarkTask> {

    @Autowired
    private LarkTaskMapper larkTaskMapper;

    @Autowired
    private LarkTaskMemberMapper larkTaskMemberMapper;

    @Autowired
    private LarkProjectMapper larkProjectMapper;
    @Override
    protected String getPageName() {
        return null;
    }

    public ObjectRestResponse<LarkTask> add(LarkTask larkTask,
                                            String memberCode, HttpServletRequest request) {
        LarkProject larkProject = larkProjectMapper.selectByPrimaryKey(larkTask.getProjectCode());
        if(ObjectUtils.isEmpty(larkProject)){
            throw new ClientParameterInvalid("项目为空！");
        }
        String userId  = request.getHeader("userId");
        if(ObjectUtils.isEmpty(larkTask)){
            throw new ClientParameterInvalid("任务内容不可为空！");
        }
        if(StringUtils.isEmpty(larkTask.getPcode())){
            larkTask.setPath(larkTask.getName());
        }else{
            larkTask.setPath(larkTask.getPath()+"/"+larkTask.getName());
        }
        String taskId = UUIDUtils.generateShortUuid();
        larkTask.setCode(taskId);
        larkTask.setId(taskId);
        larkTask.setCrtUser("userId");
        larkTask.setPath("/"+larkTask.getName());
        larkTask.setDone(TaskCommon.NUMBER_ONE);
        larkTask.setPri(TaskCommon.NUMBER_ONE);
        larkTask.setStatus("1");
        larkTask.setTaskPrivate(larkProject.getOpenTaskPrivated());
        larkTaskMapper.insertSelective(larkTask);
        if(StringUtils.pathEquals(memberCode,userId)){
            LarkTaskMember larkTaskMember = new LarkTaskMember();
            larkTaskMember.setId(UUIDUtils.generateShortUuid());
            larkTaskMember.setTaskCode(taskId);
            larkTaskMember.setMemeberCode(memberCode);
            larkTaskMember.setIsOwner(0);
            larkTaskMember.setIsExecutor(0);
            larkTaskMember.setAttr1("0");
            larkTaskMember.setJoinTime(new Date());
            larkTaskMemberMapper.insertSelective(larkTaskMember);
        }else{
            LarkTaskMember larkTaskMember = new LarkTaskMember();
            larkTaskMember.setId(UUIDUtils.generateShortUuid());
            larkTaskMember.setTaskCode(taskId);
            larkTaskMember.setMemeberCode(memberCode);
            larkTaskMember.setIsOwner(0);
            larkTaskMember.setIsExecutor(1);
            larkTaskMember.setAttr1("0");
            larkTaskMember.setJoinTime(new Date());
            larkTaskMemberMapper.insertSelective(larkTaskMember);
            LarkTaskMember larkTaskMember1 = new LarkTaskMember();
            larkTaskMember1.setId(UUIDUtils.generateShortUuid());
            larkTaskMember1.setTaskCode(taskId);
            larkTaskMember1.setMemeberCode(userId);
            larkTaskMember1.setIsOwner(1);
            larkTaskMember1.setIsExecutor(0);
            larkTaskMember1.setAttr1("0");
            larkTaskMember1.setJoinTime(new Date());
            larkTaskMemberMapper.insertSelective(larkTaskMember1);
        }
        return  new ObjectRestResponse<>().data(larkTask).rel(true);
    }
    /**
     * 获取 我参与的 1 我创建的 2 我执行的 3 是否完成 接口 数据获取
     * @param map
     * @return
     */
    public TableResultResponse<LarkTaskDto> getTaskByUserIdAndDone(Map<String, Object> map) {
        Query query = new Query(map);
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        String userId = map.get("userId").toString();
        String done = map.get("done").toString();
        String num = map.get("num").toString();
        List<LarkTaskDto> larkTaskDtos = larkTaskMapper.getTaskByUserIdAndDone(userId,done,num);
       return new TableResultResponse<>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), larkTaskDtos);
    }

    /**
     * 拆分请求第二步 getTasksByProjectId
     * 根据具体任务列id  获取具体任务集合 以及任务的具体信息
     * @param larkTaskVO
     * @return
     */
    public ObjectRestResponse<List<LarkTaskDto>> getLarkTaskList(LarkTaskVO larkTaskVO){
        List<LarkTaskDto> larkTaskDtos = larkTaskMapper.getLarkTaskList(larkTaskVO);
        return new ObjectRestResponse<>().data(larkTaskDtos).rel(true);
    }

    /**
     * @deprecation 标签页面 数据显示接口拆分 第二步
     * @param map 参数列表
     * @return 结果集分页
     */
    public TableResultResponse<LarkTaskDto> getTaskAndTag(Map<String, Object> map) {
        Query query = new Query(map);
        if(MapUtils.isEmpty(map)){
            throw new ClientParameterInvalid("参数不可为空！");
        }
        Object projectCode = map.get("projectCode");
        if(ObjectUtils.isEmpty(projectCode)){
            throw new ClientParameterInvalid("项目id不可为空！");
        }
        Object taskCode = map.get("taskCode");
        if(ObjectUtils.isEmpty(map.get("taskCode"))){
            throw new ClientParameterInvalid("任务id不可为空！");
        }
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<LarkTaskDto> larkTaskDtos = larkTaskMapper.getTaskAndTag(projectCode.toString(),taskCode.toString());
        return new TableResultResponse<>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), larkTaskDtos);
    }

    /**
     * 任务文件导入 最多100条
     * @param file
     * @param larkTaskbiz
     * @return
     */
    public ObjectRestResponse importExcel(MultipartFile file, LarkTaskBiz larkTaskbiz) {
        //EasyExcelUtil.importExcel(file.getInputStream(),userBiz,roleUserMapMapper,positionUserMapMapper,userMapper,orgMapper);
        return new ObjectRestResponse().rel(true).msg("导入成功！");
    }

    /**
     * 复制任务
     * @param taskId
     * @return
     * todo 没想好怎么写
     */
    public ObjectRestResponse<LarkTask> copyTaskInfo(String taskId) {
        return null;
    }

    /**
     * 更新任务完成状态  完成状态和执行状态不是一回事  因为要计算项目完成进度
     * @param larkTask
     * @return
     */
    public ObjectRestResponse<LarkTask> updateTaskStatus(LarkTask larkTask) {
        LarkProject larkProject = larkProjectMapper.selectByPrimaryKey(larkTask.getProjectCode());
        Integer autoUpdateSchedule = larkProject.getAutoUpdateSchedule();
        larkTask.setStatus(TaskCommon.NUMBER_ONE_STRING);
        larkTask.setDone(TaskCommon.NUMBER_ZERO);
        EntityUtils.setCreatAndUpdatInfo(larkTask);
        larkTaskMapper.updateByPrimaryKey(larkTask);
        //if 计算任务进度并更新状态
        if(autoUpdateSchedule.intValue()== TaskCommon.NUMBER_ZERO.intValue()){
            //计算完成百分比
            TaskNum taskNum = larkTaskMapper.getPercentComplete(larkTask.getProjectCode());
            larkProject.setSchedule(new BigDecimal(taskNum.getNumPercent()));
            EntityUtils.setCreatAndUpdatInfo(larkProject);
            larkProjectMapper.updateByPrimaryKeySelective(larkProject);
        }
        return new ObjectRestResponse<>().data(larkTask).rel(true);
    }
}
