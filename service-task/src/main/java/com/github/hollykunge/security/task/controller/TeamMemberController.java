package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应teamMember.js
 */
@RestController
@RequestMapping(value = "/team_member")
public class TeamMemberController {

    /**
     * 搜索并邀请成员
     * @param {*} keyword
     * @param {*} teamCode
     *   export function searchInviteMember(keyword, teamCode) {
     *       return $http.get('project/team_member/searchInviteMember', {keyword: keyword, teamCode: teamCode});
     *   }
     */
    @RequestMapping(value = "/searchInviteMember",method = RequestMethod.GET)
    public BaseResponse searchInviteMember(@RequestParam("keyword") String keyword, @RequestParam("teamCode") String teamCode){
        return new BaseResponse(200,"邀请成功！");
    }


    /**
     * 邀请成员
     * @param {*} accountCode
     * @param {*} teamCode
     *   export function inviteMember(accountCode, teamCode) {
     *       return $http.post('project/team_member/inviteMember', {accountCode: accountCode, teamCode: teamCode});
     *   }
     */
    @RequestMapping(value = "/inviteMember",method = RequestMethod.POST)
    public BaseResponse inviteMember(@RequestParam("accountCode") String accountCode, @RequestParam("teamCode") String teamCode){
        return new BaseResponse(200,"邀请成功！");
    }

    /**
     * 删除成员
     * @param {*} accountCode
     * @param {*} teamCode
     *   export function removeMember(accountCode, teamCode) {
     *       return $http.delete('project/team_member/removeMember', {accountCode: accountCode, teamCode: teamCode});
     *   }
     */
    @RequestMapping(value = "/removeMember",method = RequestMethod.DELETE)
    public BaseResponse removeMember(@RequestParam("accountCode") String accountCode, @RequestParam("teamCode") String teamCode){
        return new BaseResponse(200,"删除成功！");
    }

    /**
     * 成员列表
     * @param {*} data
     *   export function list(data) {
     *       return $http.get('project/team_member/index', data);
     *   }
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public TableResultResponse index(@RequestBody Object data){
        // todo 暂时返回空 成员列表
        return new TableResultResponse();
    }

    /**
     * 团队详情
     * @param {*} data
     *   export function detail(data) {
     *       return $http.get('project/team_member/detail', data);
     *   }
     */
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    public ObjectRestResponse detail(@RequestBody Object data){
        // todo 暂时返回空 成员列表
        return new ObjectRestResponse().data("团队详情");
    }

}
