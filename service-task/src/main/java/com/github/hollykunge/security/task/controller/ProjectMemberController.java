package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.task.biz.LarkProjectMemberbiz;
import com.github.hollykunge.security.task.entity.LarkProjectMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应ProjectMember.js
 */
@RestController
@RequestMapping(value = "/project_member")
public class ProjectMemberController extends BaseController<LarkProjectMemberbiz,LarkProjectMember> {

    @Autowired
    private LarkProjectMemberbiz larkProjectMemberbiz;

    /**
     * todo 不需要了
     * 查询邀请成员
     * @param {*} keyword
     * @param {*} code
     *   export function searchInviteMember(keyword, code) {
     *       return $http.get('project/project_member/searchInviteMember', {keyword: keyword, projectCode: code});
     *   }
     */
    @RequestMapping(value = "/searchInviteMember",method = RequestMethod.GET)
    public TableResultResponse<Object> searchInviteMember(@RequestParam("keyword") String keyword, @RequestParam("projectCode") String code){
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
    public BaseResponse inviteMember(@RequestParam("memberCode") String memberCode, @RequestParam("projectCode") String code){
        larkProjectMemberbiz.sendInviteMemberMessage(code);
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
        if(StringUtils.isEmpty(memberCode)){
            throw new BaseException("成员不可为空！");
        }
        if(StringUtils.isEmpty(code)){
            throw new BaseException("项目不可为空！");
        }
        if(StringUtils.isEmpty(roleCode)){
            throw new BaseException("成员角色不可为空！");
        }
        LarkProjectMember larkProjectMember = new LarkProjectMember();
        larkProjectMember.setProjectCode(code);
        larkProjectMember.setAuthorize(roleCode);
        larkProjectMember.setMemberCode(memberCode);
        larkProjectMemberbiz.updateSelectiveById(larkProjectMember);
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
    public ObjectRestResponse<LarkProjectMember> removeMember(@RequestParam("memberCode") String memberCode, @RequestParam("projectCode") String code){
        if(StringUtils.isEmpty(memberCode)){
            throw  new BaseException("成员id不可为空！");
        }
        if(StringUtils.isEmpty(code)){
            throw  new BaseException("项目id不可为空！");
        }
        return larkProjectMemberbiz.deleteUserForProject(memberCode,code);

    }
    /**
     * 成员列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/project_member/index', data);
     *   }
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public TableResultResponse index(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        return  larkProjectMemberbiz.selectByQueryUserInfo(query);
    }

}
