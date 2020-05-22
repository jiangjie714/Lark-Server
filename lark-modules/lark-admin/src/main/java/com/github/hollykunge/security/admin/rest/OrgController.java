package com.github.hollykunge.security.admin.rest;

import com.alibaba.fastjson.JSON;
import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.github.hollykunge.security.admin.annotation.FilterByDeletedAndOrderHandler;
import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.biz.UserBiz;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.dto.biz.AdminUser;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.vo.OrgTreeAll;
import com.github.hollykunge.security.common.exception.service.DatabaseDataException;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.TreeUtil;
import io.swagger.annotations.Api;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author dk
 */

@Controller
@RequestMapping("org")
@Api("组织管理")
public class OrgController extends BaseController<OrgBiz, Org> {
    @Autowired
    private UserBiz userBiz;

    @Autowired
    private OrgBiz orgBiz;

    /**
     * todo:使用
     * fansq
     * @param  org 参数列表
     * @return 结果集
     */
    @Override
    @Decrypt
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Org> add(@RequestBody Org org) {
        org.setDeleted(AdminCommonConstant.ORG_DELETED_CODE);
        org.setPathCode(org.getPathCode()+org.getOrgCode()+AdminCommonConstant.ORG_PATH_CODE);
        org.setPathName(org.getPathName()+org.getOrgName()+AdminCommonConstant.ORG_PATH_NAME);
        org.setOrgLevel(org.getOrgLevel()+1);
        orgBiz.insertSelective(org);
        return new ObjectRestResponse<Org>().rel(true);
    }

    /**
     * todo:使用
     * fansq
     * 更新方法
     * 同步更新所有子级的pathname  pathcode
     * @param org
     * @return
     */
    @Override
    @Decrypt
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<Org> update(@RequestBody Org org) {
        String parentPathName = org.getPathName();
        String parentPathCode = org.getPathCode();
        org.setPathName(parentPathName+org.getOrgName()+AdminCommonConstant.ORG_PATH_NAME);
        org.setPathCode(parentPathCode+org.getId()+AdminCommonConstant.ORG_PATH_CODE);
        Org o = new Org();
        o.setParentId(org.getId());
        List<Org> orgs = baseBiz.selectList(o);
        baseBiz.updateSelectiveById(org);
        //根据id查找子节点
        if(orgs.size()>0){
            for(Org or:orgs){
                or.setPathName(org.getPathName()+or.getOrgName()+AdminCommonConstant.ORG_PATH_NAME);
                or.setPathCode(org.getPathCode()+or.getId()+AdminCommonConstant.ORG_PATH_CODE);
                baseBiz.updateSelectiveById(or);
            }
        }
        return new ObjectRestResponse<Org>().rel(true);
    }

    /**
     * todo:使用
     * fansq
     * 删除方法  如果组织下存在用户不能进行删除
     * @param id
     * @return
     * fansq 添加异常 ClientInvalidException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Override
    public ObjectRestResponse<Org> remove(@PathVariable String id) {
        User user = new User();
        user.setOrgCode(id);
        user.setDeleted("0");
        Org org = new Org();
        org.setParentId(id);
        List<User> userList = userBiz.selectList(user);
        List<Org> orgList = orgBiz.selectList(org);
        if(orgList.size() > 0){
            throw new DatabaseDataException("选择组织包含子节点，无法删除。");
        }
        if(userList.size()>0){
            throw new DatabaseDataException("选择组织包含用户，无法删除。");
        }
        return super.remove(id);
    }

    /**
     * 通过orgCode获取所属用户
     *
     * @param map 组织机构代码
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<AdminUser>> getUsers(@RequestParam Map<String,String> map, HttpServletRequest request) {
        String orgCode = map.get("orgCode");
        String secretLevels = map.get("secretLevels");
        String pId = map.get("PId");
        String groupType = map.get("grouptype");
        String userOrgCode = map.get("userOrgCode");
        List<AdminUser> orgUsers = baseBiz.getOrgUsers(orgCode, secretLevels, pId,groupType,userOrgCode);
        return new ListRestResponse("", orgUsers.size(), orgUsers);
    }


    /**
     * 通过orgId修改组织所属用户
     *
     * @param id    组织id
     * @param users 以逗号分隔的userId
     */
    @Decrypt
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUsers(@RequestParam("orgId") String id, @RequestParam("users") String users) {
        baseBiz.modifyOrgUsers(id, users);
        return new ObjectRestResponse().rel(true).msg("");
    }


    /**
     * todo:使用
     * fansq
     * 新的获取组织树方法 增加 level pathcode pathname 属性
     * 11-26-16:16
     * @param map 参数
     * @return 结果集
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<OrgTreeAll>> tree(@RequestParam Map<String, String> map) {
        String parentTreeId = map.get("parentTreeId");
        if (StringUtils.isEmpty(parentTreeId)) {
            parentTreeId = AdminCommonConstant.ROOT;
        }
        List<Org> orgs = ((OrgController) AopContext.currentProxy()).getOrgs();
        List<OrgTreeAll> tree = getTree(orgs, parentTreeId);
        return new ListRestResponse("", tree.size(), tree);
    }
    @FilterByDeletedAndOrderHandler
    public List<Org> getOrgs(){
        return baseBiz.selectListAll();
    }

    private List<OrgTreeAll> getTree(List<Org> orgs, String parentTreeId) {
        List<OrgTreeAll> trees = new ArrayList<>();
        for (Org org : orgs) {
            String jsonNode = JSON.toJSONString(org);
            OrgTreeAll node = JSON.parseObject(jsonNode, OrgTreeAll.class);
            node.setLabel(org.getOrgName());
            node.setOrder(org.getOrderId());
            node.setLevel(org.getOrgLevel());
            node.setPathCode(org.getPathCode());
            node.setPathName(org.getPathName());
            trees.add(node);
        }
        trees.sort(Comparator.comparing(OrgTreeAll::getOrder));
        return TreeUtil.bulid(trees, parentTreeId);
    }

    @PostMapping("/importOrg")
    @ResponseBody
    public ObjectRestResponse importExcel(@RequestParam("file") MultipartFile file) throws Exception{
        return orgBiz.importExcel(file);
    }
}
