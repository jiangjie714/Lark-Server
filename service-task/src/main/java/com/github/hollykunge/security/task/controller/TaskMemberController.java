package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应taskMember.js
 */
@RestController
@RequestMapping(value = "/task_member")
public class TaskMemberController {


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

}
