package com.github.hollykunge.security.task.service;

/**
 * @author fansq
 * @since 20-3-6
 * @deprecation 项目成员
 */
public interface ProjectMemberService {

    /**
     * 邀请成员
     * @param memberCode
     * @param code
     */
    void inviteMember(String memberCode, String code);

    /**
     * 给成员分配角色
     * @param memberCode
     * @param roleCode
     * @param code
     */
    void assignRoles(String memberCode, String roleCode, String code);
}
