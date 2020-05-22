package com.github.hollykunge.security.admin.rpc.service;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.admin.annotation.FilterByDeletedAndOrderHandler;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.dictionary.OrgComponentEnum;
import com.github.hollykunge.security.admin.dictionary.OrgLevelEnum;
import com.github.hollykunge.security.admin.dto.biz.AdminUser;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.mapper.OrgMapper;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.admin.vo.OrgTree;
import com.github.hollykunge.security.common.exception.service.ClientParameterInvalid;
import com.github.hollykunge.security.common.exception.service.DatabaseDataException;
import com.github.hollykunge.security.common.util.TreeUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 9:51 2019/11/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrgRestService {
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private UserMapper userMapper;

    private final String Link = ".";

    public List<OrgTree> getTree(String userOrgCode, String groupType) throws Exception {
        if (StringUtils.isEmpty(userOrgCode) || StringUtils.isEmpty(groupType)) {
            throw new ClientParameterInvalid("当前用户的组织id或类型不能为空。");
        }
        Org org = orgMapper.selectByPrimaryKey(userOrgCode);
        if (org == null) {
            throw new DatabaseDataException("当前用户组织为空。");
        }
        String pathCode = org.getPathCode();
        if (StringUtils.isEmpty(pathCode)) {
            throw new DatabaseDataException("当前用户的组织无pathcode。");
        }
        String parentPathCode = null;
        //部门内
        if (Objects.equals(groupType, OrgComponentEnum.WITHIN_DEPARTMENT.getValue())) {
            //为上一级
            parentPathCode = getOrgPathCode(userOrgCode, pathCode, OrgLevelEnum.PARENT_DEPARTMENT.getValue());
        }
        //跨部门
        if (Objects.equals(groupType, OrgComponentEnum.OUTSIDE_DEPARTMENT.getValue())) {
            //为三级
            parentPathCode = getOrgPathCode(userOrgCode, pathCode, OrgLevelEnum.THIRD_PLACE.getValue());
        }
        //跨场所
        if (Objects.equals(groupType, OrgComponentEnum.OUTSIDE_PLACE.getValue())) {
            //为二级
            parentPathCode = getOrgPathCode(userOrgCode, pathCode, OrgLevelEnum.SECOND_DEPARTMENT.getValue());
        }
        if (StringUtils.isEmpty(parentPathCode)) {
            throw new DatabaseDataException("没有成功截取到上级pathcode。");
        }
        String codeTemp = parentPathCode.substring(0, parentPathCode.length() - 1);
        String parentCode = parentPathCode.substring(codeTemp.lastIndexOf(Link) + 1, codeTemp.length());
        List<Org> orgs = ((OrgRestService) AopContext.currentProxy()).getOrgs(parentPathCode);
        List<OrgTree> orgTrees = buildTree(orgs, parentCode, groupType);
        return orgTrees;
    }

    @FilterByDeletedAndOrderHandler
    public List<Org> getOrgs(String parentPathCode) {
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("pathCode", parentPathCode + "%");
        return orgMapper.selectByExample(example);
    }

    public List<AdminUser> getOrgUsers(String orgCode, String secretLevels, String pid) throws Exception {
        //如果orgCode为航天二院，则直接返回空数据
        if (AdminCommonConstant.NO_DATA_ORG_CODE.equals(orgCode)) {
            return new ArrayList();
        }
        List<String> secretList = null;
        if (!StringUtils.isEmpty(secretLevels)) {
            secretList = Stream.of(secretLevels.split(",")).collect(Collectors.toList());
        }
        List<AdminUser> users = userMapper.findBySecretsAndNotPidLikeOrgCode(secretList, pid, orgCode);
        return users;
    }

    /**
     * 获取对应的组织编码path
     *
     * @param userOrgCode 当前登录人的orgCode
     * @param path        当前登录人所对应的path
     * @param flag        如果为0查询父级，为2查询2级，为3查询3级
     * @return
     */
    private String getOrgPathCode(String userOrgCode, String path, String flag) {
        if (StringUtils.isEmpty(userOrgCode) || StringUtils.isEmpty(path) || StringUtils.isEmpty(flag)) {
            throw new ClientParameterInvalid("userOrgCode/path/flag参数为空。");
        }
        //父级
        if (Objects.equals(flag, OrgLevelEnum.PARENT_DEPARTMENT.getValue())) {
            return path.substring(0, getIndex(path, Integer.parseInt(OrgLevelEnum.PARENT_DEPARTMENT.getValue())));
        }
        //二级
        if (Objects.equals(flag, OrgLevelEnum.SECOND_DEPARTMENT.getValue())) {
            return path.substring(0, getIndex(path, Integer.parseInt(OrgLevelEnum.SECOND_DEPARTMENT.getValue())));
        }
        //三级
        if (Objects.equals(flag, OrgLevelEnum.THIRD_PLACE.getValue())) {
            return path.substring(0, getIndex(path, Integer.parseInt(OrgLevelEnum.THIRD_PLACE.getValue())));
        }
        return null;
    }

    private int getIndex(String path, int count) {
        int i = 0;
        for (int y = 1; y <= count; y++) {
            i = path.indexOf(".", i);
            i++;
        }
        return i;
    }

    private List<OrgTree> buildTree(List<Org> orgs, String parentTreeId, String flag) {
        List<OrgTree> trees = new ArrayList<OrgTree>();
        for (Org org : orgs) {
            OrgTree node = JSON.parseObject(JSON.toJSONString(org), OrgTree.class);
            node.setLabel(org.getOrgName());
            node.setOrder(org.getOrderId());
            trees.add(node);
        }
        Collections.sort(trees, Comparator.comparing(OrgTree::getOrder));
        //部门内使用携带parent方式创建树
        if (Objects.equals(flag, OrgComponentEnum.WITHIN_DEPARTMENT.getValue())) {
            return TreeUtil.bulidAsParent(trees, parentTreeId);
        }
        return TreeUtil.bulid(trees, parentTreeId);
    }
}
