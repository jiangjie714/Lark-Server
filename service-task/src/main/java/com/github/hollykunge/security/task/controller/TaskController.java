package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.task.biz.LarkTaskMemberbiz;
import com.github.hollykunge.security.task.biz.LarkTaskToTagbiz;
import com.github.hollykunge.security.task.biz.LarkTaskbiz;
import com.github.hollykunge.security.task.dto.LarkTaskDto;
import com.github.hollykunge.security.task.entity.LarkTask;
import com.github.hollykunge.security.task.entity.LarkTaskMember;
import com.github.hollykunge.security.task.entity.LarkTaskToTag;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fansq
 * @since 20-4-17
 * @deprecation 任务操作类
 */
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController<LarkTaskbiz, LarkTask> {

    @Autowired
    private LarkTaskbiz larkTaskbiz;

    @Autowired
    private LarkTaskMemberbiz larkTaskMemberbiz;

    @Autowired
    private LarkTaskToTagbiz larkTaskToTagbiz;

    /**
     * 重写add 在新建任务时
     * @param larkTask
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ObjectRestResponse<LarkTask> add(@RequestBody LarkTask larkTask,
                                            @RequestParam("memberCode") String memberCode,
                                            HttpServletRequest request) {
        return larkTaskbiz.add(larkTask,memberCode,request);
    }

    /**
     * 排序
     * @param larkTasks
     * @return
     */
    @RequestMapping(value = "/sort",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse sort(@RequestBody List<LarkTask> larkTasks){
        for(LarkTask larkTask:larkTasks){
            baseBiz.updateSelectiveById(larkTask);
        }
        return new BaseResponse(200,"排序成功！");
    }


    /**
     * 所有任务移至回收站！
     * @param larkTasks
     * @return
     */
    @RequestMapping(value = "/deleted",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse deleted(@RequestBody List<LarkTask> larkTasks){
        for(LarkTask larkTask:larkTasks){
            larkTask.setDeleted(1);
            larkTask.setDeletedTime(new Date());
            baseBiz.updateSelectiveById(larkTask);
        }
        return new BaseResponse(200,"所有任务已移至回收站！");
    }

    /**
     * 获取 我参与的  我创建的  我执行的  是否完成 接口 数据获取
     * @return
     */
    @RequestMapping(value = "/getTaskByUserIdAndDone",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<LarkTaskDto> getTaskByUserIdAndDone(@RequestBody Map<String,Object> map){
        if(MapUtils.isEmpty(map)){
            throw new BaseException("参数不可为空！");
        }
        return larkTaskbiz.getTaskByUserIdAndDone(map);
    }

    /**
     * 拆分请求第二步 getTasksByProjectId
     * 根据具体任务列id  获取具体任务集合 以及任务的具体信息
     * @param stagesCode
     * @return
     */
    @RequestMapping(value = "/getLarkTaskList",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<LarkTaskDto>> getLarkTaskList(@RequestParam("stagesCode") String stagesCode){
        String userId = request.getHeader("userId");
        //todo 暂时使用写死的userId
        return larkTaskbiz.getLarkTaskList(stagesCode,"68yHX85Z");
    }

    /**
     * 给任务设置标签
     * @param tagCode
     * @param taskCode
     * @return
     */
    @RequestMapping(value = "/setTagToTask",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse setTagToTask(@RequestParam("tagCode") String tagCode,@RequestParam("taskCode") String taskCode){
        LarkTaskToTag larkTaskToTag = new LarkTaskToTag();
        larkTaskToTag.setTagCode(tagCode);
        larkTaskToTag.setTaskCode(taskCode);
        larkTaskToTagbiz.insertSelective(larkTaskToTag);
        return new BaseResponse(200,"设置成功");
    }

}

