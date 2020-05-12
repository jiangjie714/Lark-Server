package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.task.biz.LarkTaskMemberBiz;
import com.github.hollykunge.security.task.entity.LarkTaskMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应taskMember.js
 */
@RestController
@RequestMapping(value = "/task_member")
public class TaskMemberController extends BaseController<LarkTaskMemberBiz, LarkTaskMember> {

    @Autowired
    private LarkTaskMemberBiz larkTaskMemberbiz;

    /**
     * 重写add 新增任务执行人
     * @param larkTaskMember
     * @return
     */
    @Override
    public ObjectRestResponse<LarkTaskMember> add(LarkTaskMember larkTaskMember) {
        larkTaskMember.setId(UUIDUtils.generateShortUuid());
        larkTaskMember.setJoinTime(new Date());
        return super.add(larkTaskMember);
    }

    /**
     * 更新任务执行人
     * @param modifyMemberId 修改后的执行人id
     * @param modifiedMemberId 修改前的执行人id
     * @return 修改结果
     */
    @RequestMapping(value = "/updateTaskMember",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<LarkTaskMember> updateTaskMember(
            @RequestParam("modifyMemberId") String modifyMemberId,
            @RequestParam("modifiedMemberId") String modifiedMemberId){
        return larkTaskMemberbiz.updateTaskMember(modifyMemberId,modifiedMemberId);
    }
    /**
     * 拆分请求第三步 getTasksByProjectId
     * 获取任务成员列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/task_member', data);
     *   }
     */
    @RequestMapping(value = "/getTaskMemberList",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<LarkTaskMember>> getTaskMemberList(@RequestParam("taskCode") String taskCode){
        Example example = new Example(LarkTaskMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskCode",taskCode);
        List<LarkTaskMember> larkTaskMembers = baseBiz.selectByExample(example);
        return new ObjectRestResponse<>().data(larkTaskMembers).rel(true);
    }
    /**
     * 批量邀请成员
     * @param {*} data
     *   export function inviteMemberBatch(data) {
     *       return $http.post('project/task_member/inviteMemberBatch', data);
     *   }
     */
    @RequestMapping(value = "/inviteMemberBatch",method = RequestMethod.POST)
    public BaseResponse inviteMemberBatch(@RequestBody Object data){
        return new BaseResponse(200,"已批量邀请成员！");
    }

    /**
     * 设置本列所有任务执行者
     * @param larkTaskMembers
     * @return
     */
    @RequestMapping(value = "/updateTasksAssignTo",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updateTasksAssignTo(@RequestBody List<LarkTaskMember> larkTaskMembers){
        for(LarkTaskMember larkTaskMember:larkTaskMembers){
            LarkTaskMember larkTaskMember1 = new LarkTaskMember();
            larkTaskMember1.setTaskCode(larkTaskMember.getTaskCode());
            larkTaskMember1.setIsExecutor(0);
            baseBiz.delete(larkTaskMember1);
            larkTaskMember.setIsExecutor(0);
            baseBiz.insertSelective(larkTaskMember);
        }
        return new BaseResponse(200,"任务已指派！");
    }

    /**
     * 任务邀请人
     * 返回1.项目参与人  2.子项目参与人
     *
     * @return
     */
    public ObjectRestResponse<List<Object>> getProjectUser(@RequestParam("projectCode") String projectCode,
                                                           @RequestParam("taskCode") String taskCode){
        return larkTaskMemberbiz.getProjectUser(projectCode,taskCode);
    }
}
