package com.github.hollykunge.security.admin.biz;

import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.dictionary.MenuTypeEnum;
import com.github.hollykunge.security.admin.entity.*;
import com.github.hollykunge.security.admin.mapper.AdminElementTaskMapper;
import com.github.hollykunge.security.admin.mapper.AdminResourceRoleMapTaskMapper;
import com.github.hollykunge.security.admin.mapper.ElementMapper;
import com.github.hollykunge.security.admin.mapper.MenuMapper;
import com.github.hollykunge.security.admin.vo.AdminElement;
import com.github.hollykunge.security.admin.vo.AdminPermission;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fansq
 * @since 20-4-13
 * @deprecation
 */
@Service
public class ElementTaskBiz extends BaseBiz<AdminElementTaskMapper, AdminElementTask> {

    @Autowired
    private AdminResourceRoleMapTaskMapper adminResourceRoleMapTaskMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private AdminElementTaskMapper adminElementTaskMapper;
    /**
     * 修改角色对应资源列表
     * @param roleId
     * @param permissionList
     */
    public void modifyAuthorityMenu(String roleId, List<AdminPermission> permissionList) {
        if (StringUtils.isEmpty(roleId) || permissionList.isEmpty()) {
            throw new BaseException("args is null...");
        }
        //用roleId删除所有与角色相关的资源
        Example resourceRoleExample = new Example(ResourceRoleMap.class);
        resourceRoleExample.createCriteria().andEqualTo("roleId", roleId);
        int deleteCount = adminResourceRoleMapTaskMapper.deleteByExample(resourceRoleExample);
        if (deleteCount < 0) {
            throw new BaseException("系统异常错误...");
        }
        //删除完成后，重新插入menu到资源表中
        List<Menu> menuList = menuMapper.selectAll();
        Map<String, String> map = new HashMap<String, String>(256);
        for (Menu menu : menuList) {
            map.put(menu.getId(), menu.getParentId());
        }
        Set<String> relationMenus = new HashSet<String>();
        List<String> permissionMenu = this.getPermissionMenu(permissionList);
        relationMenus.addAll(permissionMenu);
        AdminResourceRoleMapTask authority = null;
        for (String menu : permissionMenu) {
            findParentID(map, relationMenus, menu);
        }
        for (String menuId : relationMenus) {
            authority = new AdminResourceRoleMapTask();
            authority.setRoleId(roleId + "");
            authority.setResourceId(menuId);
            authority.setResourceType(AdminCommonConstant.RESOURCE_TYPE_MENU);
            EntityUtils.setCreatAndUpdatInfo(authority);
            adminResourceRoleMapTaskMapper.insertSelective(authority);
        }
        //并行添加element到resourceRoleMap中
        permissionList.stream().forEach(adminPermission -> {
            adminPermission.getActionEntitySetList().stream().forEach(element -> {
                if (element.getDefaultCheck()) {
                    AdminResourceRoleMapTask resourceRoleMap = new AdminResourceRoleMapTask();
                    resourceRoleMap.setResourceId(element.getId());
                    resourceRoleMap.setResourceType(AdminCommonConstant.RESOURCE_TYPE_BTN);
                    resourceRoleMap.setRoleId(roleId);
                    EntityUtils.setCreatAndUpdatInfo(resourceRoleMap);
                    adminResourceRoleMapTaskMapper.insertSelective(resourceRoleMap);
                }
            });
        });
    }

    private List<String> getPermissionMenu(List<AdminPermission> permissionList) {
        List<String> listResult = new ArrayList<>();
        if (permissionList.isEmpty()) {
            throw new BaseException("参数为空....");
        }
        permissionList.stream().filter(permissionEntity -> permissionEntity.getActionEntitySetList().stream()
                .anyMatch(actionEntitySet -> actionEntitySet.getDefaultCheck() == true))
                .forEach(adminPermission -> {
                    listResult.add(adminPermission.getMenuId());
                });
        return listResult;
    }
    private void findParentID(Map<String, String> map, Set<String> relationMenus, String id) {
        String parentId = map.get(id);
        if (String.valueOf(AdminCommonConstant.ROOT).equals(id)) {
            return;
        }
        relationMenus.add(parentId);
        findParentID(map, relationMenus, parentId);
    }

    /**
     * 给前端获取指定角色资源列表使用  包含指定所有资源，然后根据角色产生是否选中
     * @param roleId
     * @return
     */
    public Map<String,Object> getAuthorityMenu(String roleId) {
        //定义固定返回参数
        List<AdminPermission> resultPermission = new ArrayList<>();
        //获取所有的menu和所有的menu下的所有的Element
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type","task");
        criteria.andEqualTo("uri","/task");
        criteria.andEqualTo("title","任务管理");
        List<Menu> menus = menuMapper.selectByExample(example);
        for (Menu menu : menus) {
            //根据menuid获取所有的Menu下的Element
            AdminElementTask params = new AdminElementTask();
            params.setMenuId(menu.getId());
            //menuId下的element
            List<AdminElementTask> allElement = adminElementTaskMapper.select(params);
            //roleId下的element
            List<AdminElementTask> resourceElement = adminElementTaskMapper.
                    getAuthorityMenuElement(roleId, menu.getId(), AdminCommonConstant.RESOURCE_TYPE_BTN);

            List<AdminElement> menuElement = this.setDefaultCheck(allElement, resourceElement);
            //添加AdminPermission参数
            AdminPermission adminPermission = new AdminPermission();
            BeanUtils.copyProperties(menu, adminPermission);
            //单独处理menuid,roleId
            adminPermission.setMenuId(menu.getId());
            adminPermission.setRoleId(roleId);
            //给菜单赋值所有的Element
            adminPermission.setActionEntitySetList(menuElement);
            resultPermission.add(adminPermission);
        }
        Map<String,Object> result = new HashMap<>();
        if(resultPermission.size() > 0){
            for(MenuTypeEnum menuTypeEnum : MenuTypeEnum.values()){
                List<AdminPermission> collect = resultPermission
                        .parallelStream()
                        .filter(adminPermission -> Objects.equals(menuTypeEnum.name(), adminPermission.getType()))
                        .collect(Collectors.toList());
                result.put(menuTypeEnum.name(),collect);
            }
        }
        return result;
    }


    public List<AdminElement> setDefaultCheck(List<AdminElementTask> allElement, List<AdminElementTask> resourceElement) {
        //menu对应的element接受参数
        List<AdminElement> menuElement = new ArrayList<>();
        //两种Element进行比对，如果根据角色id获取的Element在所有的Element下
        //则defaultcheck至为true，否则为false
        allElement.stream().forEach(aElement -> {
            AdminElement rAdminElement = new AdminElement();
            if (resourceElement.stream().anyMatch(matchEntity -> aElement.getId().equals(matchEntity.getId()))) {
                rAdminElement.setDefaultCheck(true);
            } else {
                rAdminElement.setDefaultCheck(false);
            }
            BeanUtils.copyProperties(aElement, rAdminElement);
            menuElement.add(rAdminElement);
        });
        return menuElement;
    }

    /**
     * 给task服务 点击具体项目进入项目根据资源渲染界面使用
     * @return
     */
    public List<AdminElementTask> getAuthorityMenuByRoleId(String roleId) {
        //roleId下的element
        List<AdminElementTask> resourceElement = adminElementTaskMapper.
                getAuthorityMenuElementTask(roleId, AdminCommonConstant.RESOURCE_TYPE_BTN);
        return resourceElement;
    }
    @Override
    protected String getPageName() {
        return null;
    }

}
