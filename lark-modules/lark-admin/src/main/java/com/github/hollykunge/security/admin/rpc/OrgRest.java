package com.github.hollykunge.security.admin.rpc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.admin.annotation.FilterByDeletedAndOrderHandler;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.rpc.service.OrgRestService;
import com.github.hollykunge.security.admin.vo.OrgTree;
import com.github.hollykunge.security.admin.vo.StatesOrgVo;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
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
    @Autowired
    private OrgBiz orgBiz;
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

    /**
     * 统计页面的组件，下拉组织树接口
     * @return
     */
    @RequestMapping(value = "stateTree", method = RequestMethod.POST)
    @ResponseBody
    public ListRestResponse tree(@RequestBody(required = false) Integer[] orgLevels){
        List<Org> orgs = getOrgs(orgLevels);
        List<StatesOrgVo> statesOrgVos = JSONArray.parseArray(JSONObject.toJSONString(orgs), StatesOrgVo.class);
        return new ListRestResponse("",statesOrgVos.size(),statesOrgVos);
    }
    @FilterByDeletedAndOrderHandler
    public List<Org> getOrgs(Integer[] orgLevels){
        if(orgLevels == null || orgLevels.length == 0){
            throw new BaseException("组织层级不能为空...");
        }
        Example orgEx = new Example(Org.class);
        Example.Criteria  criteria= orgEx.createCriteria();
        criteria.andIn("orgLevel", Arrays.asList(orgLevels));
        criteria.andEqualTo("deleted","0");
        criteria.andIsNotNull("orgCode");
        return orgBiz.selectByExample(orgEx);
    }
}
