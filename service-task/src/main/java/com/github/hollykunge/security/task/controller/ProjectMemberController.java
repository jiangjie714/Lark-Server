package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应ProjectMember.js
 */
@RestController
@RequestMapping(value = "/project_member")
public class ProjectMemberController {


    /**
     * 查询邀请成员
     * @param {*} keyword
     * @param {*} code
     *   export function searchInviteMember(keyword, code) {
     *       return $http.get('project/project_member/searchInviteMember', {keyword: keyword, projectCode: code});
     *   }
     */
    @RequestMapping(value = "/searchInviteMember",method = RequestMethod.GET)
    public TableResultResponse<Object> searchInviteMember(@RequestParam("keyword") String keyword, @RequestParam("projectCode") String code){
        // todo 暂时返回空 应该分页显示信息
        return new TableResultResponse();
    }
    /**
     * 邀请成员
     * @param {*} memberCode
     * @param {*} code
     *   export function inviteMember(memberCode, code) {
     *       return $http.post('project/project_member/inviteMember', {memberCode: memberCode, projectCode: code});
     *   }
     */
    @RequestMapping(value = "/inviteMember",method = RequestMethod.POST)
    public BaseResponse inviteMember(@RequestParam("memberCode") String memberCode,@RequestParam("projectCode") String code){
        return new BaseResponse(200,"已发送邀请");
    }
    /**
     * 移除成员
     * @param {*} memberCode
     * @param {*} code
     *   export function removeMember(memberCode, code) {
     *       return $http.post('project/project_member/removeMember', {memberCode: memberCode, projectCode: code});
     *   }
     */
    @RequestMapping(value = "/removeMember",method = RequestMethod.POST)
    public BaseResponse removeMember(@RequestParam("memberCode") String memberCode,@RequestParam("projectCode") String code){
        return new BaseResponse(200,"成员已移除！");
    }
    /**
     * 成员列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/project_member/index', data);
     *   }
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public TableResultResponse index(@RequestBody Object data){
        //new TableResultResponse(); 文件列表分页
        // todo 暂时返回空
        return new TableResultResponse();
    }

}
