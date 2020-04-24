package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.task.dto.LarkTaskDto;
import com.github.hollykunge.security.task.entity.LarkTask;
import com.github.hollykunge.security.task.entity.LarkTaskMember;
import com.github.hollykunge.security.task.mapper.LarkTaskMapper;
import com.github.hollykunge.security.task.mapper.LarkTaskMemberMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
public class LarkTaskbiz extends BaseBiz<LarkTaskMapper, LarkTask> {

    @Autowired
    private LarkTaskMapper larkTaskMapper;

    @Autowired
    private LarkTaskMemberMapper larkTaskMemberMapper;
    @Override
    protected String getPageName() {
        return null;
    }

    public ObjectRestResponse<LarkTask> add(LarkTask larkTask,
                                            String memberCode, HttpServletRequest request) {
        String userId  = request.getHeader("userId");
        if(ObjectUtils.isEmpty(larkTask)){
            throw new BaseException("任务内容不可为空！");
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
        larkTaskMapper.insertSelective(larkTask);
        if(StringUtils.isEmpty(memberCode)){
            LarkTaskMember larkTaskMember = new LarkTaskMember();
            larkTaskMember.setId(UUIDUtils.generateShortUuid());
            larkTaskMember.setTaskCode(taskId);
            larkTaskMember.setMemeberCode("68yHX85Z");
            larkTaskMember.setIsOwner(0);
            larkTaskMember.setIsExecutor(0);
            larkTaskMember.setJoinTime(new Date());
            larkTaskMemberMapper.insertSelective(larkTaskMember);
        }else{
            LarkTaskMember larkTaskMember = new LarkTaskMember();
            larkTaskMember.setId(UUIDUtils.generateShortUuid());
            larkTaskMember.setTaskCode(taskId);
            larkTaskMember.setMemeberCode(memberCode);
            larkTaskMember.setIsOwner(0);
            larkTaskMember.setIsExecutor(1);
            larkTaskMember.setJoinTime(new Date());
            larkTaskMemberMapper.insertSelective(larkTaskMember);
            LarkTaskMember larkTaskMember1 = new LarkTaskMember();
            larkTaskMember1.setId(UUIDUtils.generateShortUuid());
            larkTaskMember1.setTaskCode(taskId);
            larkTaskMember1.setMemeberCode(userId);
            larkTaskMember1.setIsOwner(1);
            larkTaskMember1.setIsExecutor(0);
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
     * @param stagesCode
     * @return
     */
    public ObjectRestResponse<List<LarkTaskDto>> getLarkTaskList(String stagesCode,String userId){
        List<LarkTaskDto> larkTaskDtos = larkTaskMapper.getLarkTaskList(stagesCode,"0",userId);
        return new ObjectRestResponse<>().data(larkTaskDtos).rel(true);
    }
}
