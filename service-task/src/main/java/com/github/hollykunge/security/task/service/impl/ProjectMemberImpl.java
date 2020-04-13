package com.github.hollykunge.security.task.service.impl;


import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.task.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void inviteMember(String memberCode, String code) {

    }

    @Override
    public void assignRoles(String memberCode, String roleCode, String code) {

    }
}
