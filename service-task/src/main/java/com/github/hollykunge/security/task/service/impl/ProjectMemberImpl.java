package com.github.hollykunge.security.task.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.task.entity.Project;
import com.github.hollykunge.security.task.entity.ProjectMember;
import com.github.hollykunge.security.task.entity.ProjectRole;
import com.github.hollykunge.security.task.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author fansq
 * @since 20-3-6
 * @deprecation 项目成员
 */
@Service
public class ProjectMemberImpl implements ProjectMemberService {

    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 邀请成员加入
     * @param
     */
    @Override
    public void inviteMember(String memberCode, String code) {
        Query query = new Query(Criteria.where("project_id").is(code));
        Project project = mongoTemplate.findOne(query,Project.class);
        List<ProjectMember> userIDList = project.getUserList();
        if(userIDList!=null){
            if(userIDList.stream().anyMatch(projectMember -> projectMember.getUserId().equals(memberCode))){
                throw new BaseException("项目中已存在该成员!");
            };
        }else{
            userIDList = new ArrayList<>();
        }
        ProjectMember projectMember = new ProjectMember();
        projectMember.setUserId(memberCode);
        userIDList.add(projectMember);
        Update update = new Update();
        update.set("userList",userIDList);
        mongoTemplate.updateFirst(query,update,Project.class);
    }

    /**
     * 分配角色
     * @param memberCode
     * @param roleCode
     */
    @Override
    public void assignRoles(String memberCode, String roleCode,String code) {
        Query query = new Query(Criteria.where("project_id").is(code));
        Project project = mongoTemplate.findOne(query,Project.class);
        List<ProjectMember> userIDList = project.getUserList();
        if(!userIDList.stream().anyMatch(projectMember -> projectMember.getUserId().equals(memberCode))){
            throw new BaseException("项目中不存在该成员！");
        }
        ProjectMember pm = new ProjectMember();
        List<ProjectMember> projectMemberList = userIDList.stream().filter(new Predicate<ProjectMember>() {
            @Override
            public boolean test(ProjectMember projectMember) {
                if (projectMember.getUserId().equals(memberCode)) {
                    BeanUtil.copyProperties(projectMember,pm);
                    return false;
                }
                return true;
            }
        }).collect(Collectors.toList());
        ProjectRole projectRole = new ProjectRole();
        projectRole.setRoleId(roleCode);
        pm.setAuthorize(projectRole);
        projectMemberList.add(pm);
        Update update = new Update();
        update.set("userList",projectMemberList);
        mongoTemplate.updateFirst(query,update,Project.class);
    }
}
