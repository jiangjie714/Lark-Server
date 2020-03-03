package com.github.hollykunge.security.task.controller;


import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应task.js
 */
@RestController
@RequestMapping(value = "/task")
public class TaskController {

    /**
     * 任务列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/task', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse list(@RequestBody Object data){
        // todo 暂时返回空 任务列表
        return new TableResultResponse();
    }

    /**
     * 获取任务标签列表
     * @param {*} data
     *   export function getListByTaskTag(data) {
     *       return $http.get('project/task/getListByTaskTag', data);
     *   }
     */
    @RequestMapping(value = "/getListByTaskTag",method = RequestMethod.GET)
    public TableResultResponse getListByTaskTag(@RequestBody Object data){
        // todo 暂时返回空 任务标签列表
        return new TableResultResponse();
    }

    /**
     * 获取我的任务列表
     * @param {*} data
     *   export function selfList(data) {
     *       return $http.get('project/task/selfList', data);
     *   }
     */
    @RequestMapping(value = "/selfList",method = RequestMethod.GET)
    public TableResultResponse selfList(@RequestBody Object data){
        // todo 暂时返回空 我的任务标签列表
        return new TableResultResponse();
    }

    /**
     * 添加任务资源
     * @param {*} data
     *   export function taskSources(data) {
     *       return $http.post('project/task/taskSources', data);
     *   }
     */
    @RequestMapping(value = "/taskSources",method = RequestMethod.POST)
    public BaseResponse taskSources(@RequestBody Object data){
        return new BaseResponse(200,"任务资源已添加！");
    }

    /**
     * 任务排序
     * @param {*} data
     *   export function sort(data) {
     *       return $http.post('project/task/sort', data);
     *   }
     */
    @RequestMapping(value = "/sort",method = RequestMethod.POST)
    public BaseResponse sort(@RequestBody Object data){
        return new BaseResponse(200,"排序成功！");
    }

    /**
     * 添加任务
     * @param {*} data
     *   export function save(data) {
     *       return $http.post('project/task/save', data);
     *   }
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ObjectRestResponse save(@RequestBody Object data){
        return new ObjectRestResponse().data("任务具体信息").msg("添加成功！");
    }

    /**
     * 编辑任务
     * @param {*} data
     *   export function edit(data) {
     *       return $http.post('project/task/edit', data);
     *   }
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public ObjectRestResponse edit(@RequestBody Object data){
        return new ObjectRestResponse().data("任务具体信息").msg("编辑成功！");
    }
    /**
     * 添加任务标签
     * @param {*} data
     *   export function taskToTags(data) {
     *       return $http.post('project/task/taskToTags', data);
     *   }
     */
    @RequestMapping(value = "/taskToTags",method = RequestMethod.POST)
    public BaseResponse taskToTags(@RequestBody Object data){
        return new BaseResponse(200,"任务标签已添加！");
    }

    /**
     * 设置标签
     * @param {*} data
     *   export function setTag(data) {
     *       return $http.post('project/task/setTag', data);
     *   }
     */
    @RequestMapping(value = "/setTag",method = RequestMethod.POST)
    public BaseResponse setTag(@RequestBody Object data){
        return new BaseResponse(200,"标签设置成功！");
    }

    /**
     * 点赞
     * @param {*} code
     * @param {*} like
     *   export function like(code, like) {
     *       return $http.post('project/task/like', {like: like, taskCode: code});
     *   }
     */
    @RequestMapping(value = "/like",method = RequestMethod.POST)
    public BaseResponse like(@RequestParam("like") String like,@RequestParam("taskCode") String code){
        return new BaseResponse(200,"已点赞");
    }

    /**
     * 标星
     * @param {*} code
     * @param {*} star
     *   export function star(code, star) {
     *       return $http.post('project/task/star', {star: star, taskCode: code});
     *   }
     */
    @RequestMapping(value = "/star",method = RequestMethod.POST)
    public BaseResponse star(@RequestParam("star") String star,@RequestParam("taskCode") String code){
        return new BaseResponse(200,"已标星");
    }

    /**
     * 创建评论
     * @param {*} code
     * @param {*} comment
     * @param {*} mentions
     *   export function createComment(code, comment, mentions) {
     *       return $http.post('project/task/createComment', {taskCode: code, comment: comment, mentions: mentions});
     *   }
     */
    @RequestMapping(value = "/createComment",method = RequestMethod.POST)
    public BaseResponse createComment(
            @RequestParam("taskCode") String taskCode,
            @RequestParam("comment") String comment,
            @RequestParam("mentions") String mentions){
        return new BaseResponse(200,"评论创建成功！");
    }

    /**
     * 指派任务
     * @param {*} data
     *   export function assignTask(data) {
     *       return $http.post('project/task/assignTask', data);
     *   }
     */
    @RequestMapping(value = "/assignTask",method = RequestMethod.POST)
    public BaseResponse assignTask(@RequestBody Object data){
        return new BaseResponse(200,"任务已指派！");
    }

    /**
     * 批量指派任务
     * @param {*} data
     *   export function batchAssignTask(data) {
     *       return $http.post('project/task/batchAssignTask', data);
     *   }
     */
    @RequestMapping(value = "/batchAssignTask",method = RequestMethod.POST)
    public BaseResponse batchAssignTask(@RequestBody Object data){
        return new BaseResponse(200,"批量指派任务成功！");
    }

    /**
     * 查看任务
     * @param {*} code
     *   export function read(code) {
     *       return $http.get('project/task/read', {taskCode: code});
     *   }
     */
    @RequestMapping(value = "/read",method = RequestMethod.GET)
    public ObjectRestResponse read(@RequestParam("taskCode") String taskCode){
        return new ObjectRestResponse().data("任务详情").msg("查询成功！");
    }

    /**
     * 完成任务
     * @param {*} code
     * @param {*} done
     *   export function taskDone(code, done) {
     *       return $http.post('project/task/taskDone', {taskCode: code, done: done});
     *   }
     */
    @RequestMapping(value = "/taskDone",method = RequestMethod.POST)
    public BaseResponse taskDone(@RequestParam("taskCode") String taskCode,@RequestParam("done") String done){
        return new BaseResponse(200,"任务已完成！");
    }

    /**
     * 设置为私有
     * @param {*} code
     * @param {*} isPrivate
     *   export function setPrivate(code, isPrivate) {
     *       return $http.post('project/task/setPrivate', {taskCode: code, private: isPrivate});
     *   }
     */
    @RequestMapping(value = "/setPrivate",method = RequestMethod.POST)
    public BaseResponse setPrivate(@RequestParam("taskCode") String taskCode,@RequestParam("private") String isPrivate){
        return new BaseResponse(200,"已设置为私有！");
    }

    /**
     * 回收站
     * @param {*} code
     *   export function recycle(code) {
     *       return $http.post('project/task/recycle', {taskCode: code});
     *   }
     */
    @RequestMapping(value = "/recycel",method = RequestMethod.POST)
    public ObjectRestResponse<Object> recycle(@RequestParam("taskCode") String taskCode){
        return new ObjectRestResponse<Object>().data("回收具体数据").msg("已移动到回收站！");
    }

    /**
     * 批量回收站
     * @param {*} data
     *   export function recycleBatch(data) {
     *       return $http.post('project/task/recycleBatch', data);
     *   }
     */
    @RequestMapping(value = "/recycleBatch",method = RequestMethod.POST)
    public ObjectRestResponse<Object> recycleBatch(@RequestBody Object data){
        return new ObjectRestResponse<Object>().data("回收具体数据").msg("已移动到回收站！");
    }

    /**
     * 还原
     * @param {*} code
     *   export function recovery(code) {
     *       return $http.post('project/task/recovery', {taskCode: code});
     *   }
     */
    @RequestMapping(value = "/recovery",method = RequestMethod.POST)
    public ObjectRestResponse<Object> recovery(@RequestParam("fileCode") String fileCode){
        return new ObjectRestResponse<Object>().data("还原具体数据").msg("以还原！");
    }

    /**
     * 删除
     * @param {*} code
     *   export function del(code) {
     *       return $http.delete('project/task/delete', {taskCode: code});
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestParam("taskCode") String taskCode){
        return new BaseResponse(200,"已删除！");
    }

    /**
     * 获取项目总天数
     * @param {*} data
     *   export function dateTotalForProject(data) {
     *       return $http.get('project/task/dateTotalForProject', data);
     *   }
     */
    @RequestMapping(value = "/dateTotalForProject",method = RequestMethod.GET)
    public ObjectRestResponse<Object> dateTotalForProject(@RequestBody Object data){
        return new ObjectRestResponse<Object>().data("项目总天数").msg("查询成功！");
    }

    /**
     * 获取任务日志
     * @param {*} data
     *   export function logs(data) {
     *       return $http.get('project/task/taskLog', data);
     *   }
     */
    @RequestMapping(value = "/taskLog",method = RequestMethod.GET)
    public TableResultResponse taskLog(@RequestBody Object data){
        // todo 暂时返回空 任务日志列表
        return new TableResultResponse();
    }

    /**
     * 获取我的项目日志
     * @param {*} data
     *   export function getLogBySelfProject(data) {
     *       return $http.get('project/project/getLogBySelfProject', data);
     *   }
     */
    @RequestMapping(value = "/getLogBySelfProject",method = RequestMethod.GET)
    public TableResultResponse getLogBySelfProject(@RequestBody Object data){
        // todo 暂时返回空 我的项目日志列表
        return new TableResultResponse();
    }

    /**
     * 新增任务工作时间
     * @param {*} data
     *   export function saveTaskWorkTime(data) {
     *       return $http.post('project/task/saveTaskWorkTime', data);
     *   }
     */
    @RequestMapping(value = "/saveTaskWorkTime",method = RequestMethod.POST)
    public BaseResponse saveTaskWorkTime(@RequestBody Object data){
        return new BaseResponse(200,"任务工作时间新增成功！");
    }

    /**
     * 编辑任务工作时间
     * @param {*} data
     *   export function editTaskWorkTime(data) {
     *       return $http.post('project/task/editTaskWorkTime', data);
     *   }
     */
    @RequestMapping(value = "/editTaskWorkTime",method = RequestMethod.POST)
    public BaseResponse editTaskWorkTime(@RequestBody Object data){
        return new BaseResponse(200,"任务工作时间修改成功！");
    }

    /**
     * 删除任务工作时间
     *   export function delTaskWorkTime(data) {
     *       return $http.delete('project/task/delTaskWorkTime', data);
     *   }
     */
    @RequestMapping(value = "/delTaskWorkTime",method = RequestMethod.DELETE)
    public BaseResponse delTaskWorkTime(@RequestBody Object data){
        return new BaseResponse(200,"任务工作时间删除成功！");
    }

}
