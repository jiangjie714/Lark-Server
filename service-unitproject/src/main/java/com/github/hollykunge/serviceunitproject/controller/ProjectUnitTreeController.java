package com.github.hollykunge.serviceunitproject.controller;

import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.serviceunitproject.common.AdminUser;
import com.github.hollykunge.serviceunitproject.common.ProjectUnitTree;
import com.github.hollykunge.serviceunitproject.model.ProjectUnit;
import com.github.hollykunge.serviceunitproject.serviceimpl.ProjectUnitUserBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author dk
 */

@Controller
@RequestMapping("project")

public class ProjectUnitTreeController extends BaseController<ProjectUnitUserBiz, ProjectUnit> {

    @Autowired
    ProjectUnitUserBiz projectUnitUserBiz;

    /**
     * 通过项目节点获取所属的项目团队成员
     *
     * @param
     * @RequestParam("unitID") String unitID
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<AdminUser>> getUsers(@RequestParam("unitID") String unitID, @RequestParam String secretLevels,
                                                      @RequestParam String pId) {

        if (StringUtils.isEmpty(unitID)) {
            unitID = "projectunitroot";
        }
        List projectUnitUsers = baseBiz.getProjectUnit(unitID, secretLevels, pId);
        return new ListRestResponse("", projectUnitUsers.size(), projectUnitUsers);
    }


    /**
     * 根据父级id取下面的项目单元ID
     * ps：如果parent为null时默认取root下的组织
     *
     * @param
     * @return
     * @RequestParam("parentTreeId") String parentTreeId,@RequestParam String pId
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<ProjectUnitTree>> tree(@RequestParam("parentTreeId") String parentTreeId, @RequestParam String pId) {
        parentTreeId = "";
        if (StringUtils.isEmpty(parentTreeId)) {
            parentTreeId = "root";
        }
        List<ProjectUnitTree> tree = projectUnitUserBiz.getLastTree(baseBiz.selectListAll(), parentTreeId, pId);

        return new ListRestResponse("", tree.size(), tree);
    }


}