package com.github.hollykunge.security.admin.rpc;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.rpc.service.OrgRestService;
import com.github.hollykunge.security.admin.vo.OrgTree;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 组织对外提供接口
 * @since: Create in 9:48 2019/11/20
 */
@RestController
@RequestMapping("/api/org")
public class OrgRest {
    @Autowired
    private OrgRestService orgRestService;
    /**
     * todo:使用
     * 权限组织树接口
     * @param level
     * @param userOrgCode
     * @return
     */
    @RequestMapping(value = "treeComponent", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<OrgTree>> tree(@RequestParam String level,
                                                @RequestParam String userOrgCode) throws Exception{
        List<OrgTree> trees = orgRestService.getTree(userOrgCode, level);
        return new ListRestResponse<>("",trees.size(),trees);
    }

    /**
     * todo:使用
     * @param orgCode
     * @param secretLevels
     * @param pid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "treeUsers", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<AdminUser>> treeUsers(@RequestParam String orgCode,
                                                       @RequestParam String secretLevels,
                                                       @RequestParam String pid) throws Exception{
        List<AdminUser> treeUsers = orgRestService.getOrgUsers(orgCode, secretLevels,pid);
        return new ListRestResponse("",treeUsers.size(),treeUsers);
    }
}
