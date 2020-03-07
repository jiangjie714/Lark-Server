package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.task.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应ProjectMember.js
 */
@RestController
@RequestMapping(value = "/project_member")
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;
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
     * 20-3-6 修改
     * 邀请成员
     * @param {*} memberCode
     * @param {*} code
     *   export function inviteMember(memberCode, code) {
     *       return $http.post('project/project_member/inviteMember', {memberCode: memberCode, projectCode: code});
     *   }
     */
    @RequestMapping(value = "/inviteMember",method = RequestMethod.POST)
    public BaseResponse inviteMember(@RequestParam("memberCode") String memberCode,@RequestParam("projectCode") String code){
        projectMemberService.inviteMember(memberCode,code);
        return new BaseResponse(200,"已发送邀请");
    }

    /**
     * 分配角色
     * @param memberCode
     * @param roleCode
     * @param code
     * @return
     *
     * {
     * 	"_id" : ObjectId("5e629a2243dbdf0a2c1aff39"),
     * 	"project_id" : "1",
     * 	"name" : "fansq测试项目",
     * 	"_class" : "com.github.hollykunge.security.task.entity.Project",
     * 	"user_list" : [
     *                {
     * 			"user_id" : "1",
     * 			"authorize" : {
     * 				"role_id" : "1"
     *            }
     *        }
     * 	]
     * }
     */
    @RequestMapping(value = "/assignRoles",method = RequestMethod.POST)
    public BaseResponse assignRoles(@RequestParam("memberCode") String memberCode,
                                    @RequestParam("roleCode") String roleCode,
                                    @RequestParam("projectCode") String code){
        projectMemberService.assignRoles(memberCode,roleCode,code);
        return new BaseResponse(200,"成员角色已分配!");
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
