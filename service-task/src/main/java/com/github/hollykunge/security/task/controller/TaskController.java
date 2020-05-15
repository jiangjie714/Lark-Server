package com.github.hollykunge.security.task.controller;

import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.service.ClientParameterInvalid;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.task.biz.LarkTaskMemberBiz;
import com.github.hollykunge.security.task.biz.LarkTaskToTagBiz;
import com.github.hollykunge.security.task.biz.LarkTaskBiz;
import com.github.hollykunge.security.task.dto.LarkTaskDto;
import com.github.hollykunge.security.task.entity.LarkProject;
import com.github.hollykunge.security.task.entity.LarkTask;
import com.github.hollykunge.security.task.entity.LarkTaskToTag;
import com.github.hollykunge.security.task.vo.LarkTaskVO;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class TaskController extends BaseController<LarkTaskBiz, LarkTask> {

    @Autowired
    private LarkTaskBiz larkTaskbiz;

    @Autowired
    private LarkTaskMemberBiz larkTaskMemberbiz;

    @Autowired
    private LarkTaskToTagBiz larkTaskToTagbiz;

    /**
     * 重写add 在新建任务时
     * @param larkTask
     * memberCode  执行人
     * @return
     */
    @Decrypt
    @RequestMapping(value = "/operation",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<LarkTask> add(@RequestBody LarkTask larkTask,
                                            @RequestParam("memberCode") String memberCode,
                                            HttpServletRequest request) {
        return larkTaskbiz.add(larkTask,memberCode,request);
    }


    /**
     * 自动更新项目进度  也可以手动调用
     * @param larkProject
     * @return
     */
    @Decrypt
    @RequestMapping(value = "/autoUpdateProgress",method = RequestMethod.POST)
    @ResponseBody
    public LarkProject autoUpdateProgress(@RequestBody LarkProject larkProject){
       return larkTaskbiz.autoUpdateProgress(larkProject);
    }

    /**
     * done更新任务完成状态  完成状态和 status执行状态不是一回事  因为要计算项目完成进度
     * @param larkTask
     * @return
     */
    @Decrypt
    @RequestMapping(value = "/taskDone",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<LarkTask> updateTaskStatus(@RequestBody LarkTask larkTask){
        return larkTaskbiz.updateTaskStatus(larkTask);
    }

    /**
     * @deprecated 暂时不用了
     * @param larkTask
     * @return
     */
    @Decrypt
    @RequestMapping(value = "/cancelTaskDone",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<LarkTask> cancelupdateTaskStatus(@RequestBody LarkTask larkTask){
        return larkTaskbiz.updateTaskStatus(larkTask);
    }

    /**
     * 排序
     * @param larkTasks
     * @return
     */
    @Decrypt
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
    @Decrypt
    @RequestMapping(value = "/deleted",method = RequestMethod.PUT)
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
            throw new ClientParameterInvalid("参数不可为空！");
        }
        return larkTaskbiz.getTaskByUserIdAndDone(map);
    }

    /**
     * 拆分请求第二步 getTasksByProjectId
     * 根据具体任务列id  获取具体任务集合 以及任务的具体信息
     * @param larkTaskVO
     * @return
     */
    @RequestMapping(value = "/getLarkTaskList",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<LarkTaskDto>> getLarkTaskList(
            @RequestBody LarkTaskVO larkTaskVO
            ){
        return larkTaskbiz.getLarkTaskList(larkTaskVO);
    }

    /**
     * 给任务设置标签
     * @param tagCode
     * @param taskCode
     * @return
     */
    @Decrypt
    @RequestMapping(value = "/setTagToTask",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse setTagToTask(@RequestParam("tagCode") String tagCode,@RequestParam("taskCode") String taskCode){
        LarkTaskToTag larkTaskToTag = new LarkTaskToTag();
        larkTaskToTag.setTagCode(tagCode);
        larkTaskToTag.setTaskCode(taskCode);
        larkTaskToTagbiz.insertSelective(larkTaskToTag);
        return new BaseResponse(200,"设置成功");
    }


    /**
     * @author fansq
     * @deprecation 标签页面 数据显示接口拆分 第二步
     * @return 返回任务和具体标签的对应关系
     * todo 其实不推荐使用map传参（可以做个参数数据模型）  维护很麻烦 不能直观的看到参数列表 swagger也识别不了map 分页的query需要map类型
     */
    @RequestMapping(value = "/getTaskAndTag",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<LarkTaskDto> getTaskAndTag(
            @RequestParam Map<String,Object> map){
        return larkTaskbiz.getTaskAndTag(map);
    }

    /**
     * fansq
     * excel文件导入
     * @param file
     * @return
     * @throws Exception
     */
    @Decrypt
    @PostMapping("/upload")
    @ResponseBody
    public ObjectRestResponse importExcel(@RequestParam("file") MultipartFile file) throws Exception{
        return larkTaskbiz.importExcel(file,larkTaskbiz);
    }

    /**
     * 复制任务
     * @param taskId
     * @return 复制的任务
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<LarkTask> copyTaskInfo(@RequestParam("taskId") String taskId){
        return larkTaskbiz.copyTaskInfo(taskId);
    }

    /**
     * 移动任务就是 update 任务 的 任务列id  task-> stagesCode
     */
}

