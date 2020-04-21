package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.task.biz.LarkTaskMemberbiz;
import com.github.hollykunge.security.task.entity.LarkTaskMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应taskMember.js
 */
@RestController
@RequestMapping(value = "/task_member")
public class TaskMemberController extends BaseController<LarkTaskMemberbiz, LarkTaskMember> {

    @Autowired
    private LarkTaskMemberbiz larkTaskMemberbiz;

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
     * 任务成员列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/task_member', data);
     *   }
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public TableResultResponse list(@RequestBody Object data){
        // todo 暂时返回空 任务成员列表
        return new TableResultResponse();
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
            baseBiz.updateSelectiveById(larkTaskMember);
        }
        return new BaseResponse(200,"任务已指派！");
    }

}
