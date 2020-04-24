package com.github.hollykunge.security.admin.biz;

import com.ace.cache.annotation.Cache;
import com.ace.cache.annotation.CacheClear;
import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.admin.api.authority.ActionEntitySet;
import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.dictionary.MenuTypeEnum;
import com.github.hollykunge.security.admin.entity.*;
import com.github.hollykunge.security.admin.mapper.*;
import com.github.hollykunge.security.admin.vo.*;
import com.github.hollykunge.security.auth.client.config.SysAuthConfig;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 协同设计小组
 * @create 2017-06-12 8:48
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleBiz extends BaseBiz<RoleMapper, Role> {

    @Resource
    private ResourceRoleMapMapper resourceRoleMapMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleUserMapMapper roleUserMapMapper;
    @Resource
    private ElementMapper elementMapper;
    @Autowired
    private SysAuthConfig sysAuthConfig;

    @Override
    protected String getPageName() {
        return null;
    }

    public List<AdminUser> getRoleUsers(String roleId) {
        List<AdminUser> resultData = new ArrayList<>();
        List<User> usersByOrgId = userMapper.selectUsersByRoleId(roleId);
        usersByOrgId.stream().forEach(user -> {
            AdminUser frontUser = new AdminUser();
            BeanUtils.copyProperties(user, frontUser);
            resultData.add(frontUser);
        });
        return resultData;
    }

    //    @CacheClear(pre = "permission")
    public void modifyRoleUsers(String roleId, String users) {
        RoleUserMap roleParams = new RoleUserMap();
        roleParams.setRoleId(roleId);
        int deleteCount = roleUserMapMapper.delete(roleParams);
        if (deleteCount < 0) {
            throw new BaseException("系统异常错误...");
        }
        RoleUserMap roleUserMapDo;
        if (!StringUtils.isEmpty(users)) {
            String[] mem = users.split(",");
            for (String m : mem) {
                roleUserMapDo = new RoleUserMap();
                roleUserMapDo.setRoleId(roleId);
                roleUserMapDo.setUserId(m);
                //给基类赋值
                EntityUtils.setCreatAndUpdatInfo(roleUserMapDo);
                roleUserMapMapper.insertSelective(roleUserMapDo);
            }
        }
    }

    @Override
    @CacheClear(keys = {"permission:menu", "permission:u", "frontPermission{1}"})
    public void deleteById(String id) {
        super.deleteById(id);
    }

    @CacheClear(keys = {"permission:menu", "permission:u", "frontPermission{1}"})
    public void modifyAuthorityMenu(String roleId, List<AdminPermission> permissionList) {
        if (StringUtils.isEmpty(roleId) || permissionList.isEmpty()) {
            throw new BaseException("args is null...");
        }
        //用roleId删除所有与角色相关的资源
        Example resourceRoleExample = new Example(ResourceRoleMap.class);
        resourceRoleExample.createCriteria().andEqualTo("roleId", roleId);
        int deleteCount = resourceRoleMapMapper.deleteByExample(resourceRoleExample);
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
        ResourceRoleMap authority = null;
        for (String menu : permissionMenu) {
            findParentID(map, relationMenus, menu);
        }
        for (String menuId : relationMenus) {
            authority = new ResourceRoleMap();
            authority.setRoleId(roleId + "");
            authority.setResourceId(menuId);
            authority.setResourceType(AdminCommonConstant.RESOURCE_TYPE_MENU);
            EntityUtils.setCreatAndUpdatInfo(authority);
            resourceRoleMapMapper.insertSelective(authority);
        }
        //并行添加element到resourceRoleMap中
        permissionList.stream().forEach(adminPermission -> {
            adminPermission.getActionEntitySetList().stream().forEach(element -> {
                if (element.getDefaultCheck()) {
                    ResourceRoleMap resourceRoleMap = new ResourceRoleMap();
                    resourceRoleMap.setResourceId(element.getId());
                    resourceRoleMap.setResourceType(AdminCommonConstant.RESOURCE_TYPE_BTN);
                    resourceRoleMap.setRoleId(roleId);
                    EntityUtils.setCreatAndUpdatInfo(resourceRoleMap);
                    resourceRoleMapMapper.insertSelective(resourceRoleMap);
                }
            });
        });
    }

    /**
     * 将前台参数转化为菜单id集合，供findParentID()方法使用
     *
     * @param permissionList 权限菜单和element集合
     * @return
     */
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

    public Map<String,Object> getAuthorityMenu(String roleId) {
        //定义固定返回参数
        List<AdminPermission> resultPermission = new ArrayList<>();
        //获取所有的menu和所有的menu下的所有的Element
        //20-4-16 fansq 修改 过滤task 在角色资源修改的时候不显示
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("type","task");
        List<Menu> menus = menuMapper.selectByExample(example);
        for (Menu menu : menus) {
            //根据menuid获取所有的Menu下的Element
            Element params = new Element();
            params.setMenuId(menu.getId());
            //menuId下的element
            List<Element> allElement = elementMapper.select(params);
            //roleId下的element
            List<Element> resourceElement = elementMapper.
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

    /**
     * 将菜单下Element处理为，有权限的element中defaultCheck字段赋值为true
     * 返回参数统一数据格式，供前台页面使用
     *
     * @param allElement      所有的Element
     * @param resourceElement 按角色下的Element
     * @return
     */
    public List<AdminElement> setDefaultCheck(List<Element> allElement, List<Element> resourceElement) {
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
     * 通过用户id查询用户角色
     *
     * @param userId 用户id
     * @return 角色实体类
     */
    public List<Role> getRoleByUserId(String userId) {
        RoleUserMap roleUserParams = new RoleUserMap();
        roleUserParams.setUserId(userId);
        List<RoleUserMap> roleList = roleUserMapMapper.select(roleUserParams);
        List<Role> allRole = mapper.selectAll();
        List<Role> resultRole = new ArrayList<>();
        for (RoleUserMap roleUserMap :
                roleList) {
            resultRole.addAll(allRole.stream().filter((Role role) -> roleUserMap.getRoleId().equals(role.getId())).collect(Collectors.toList()));
        }
        return resultRole;
    }

    /**
     * 根据角色id获取角色所拥有的菜单和菜单element
     * ps：提供给远程服务
     *
     * @param roleId 角色id(约定：超级管理员的roleId和userid相同)
     * @return
     */
    //@Cache(key = "frontPermission{1}")
    public List<FrontPermission> frontAuthorityMenu(String roleId) {
        //定义固定返回参数
        List<FrontPermission> resultPermission = new ArrayList<>();
        //获取权限下的menu
        List<Menu> menus = null;
        //判断是否是系统的超级管理员
        if(Objects.equals(roleId,sysAuthConfig.getSysUsername())){
            menus = menuMapper.selectAll();
        }
        if(!Objects.equals(roleId,sysAuthConfig.getSysUsername())){
            menus = menuMapper.selectMenuByRoleId(roleId);
        }
        for (Menu menu : menus) {
            List<Element> elementList = null;
            if(!Objects.equals(roleId,sysAuthConfig.getSysUsername())){
                // roleId下的element
                elementList = elementMapper.getAuthorityMenuElement(roleId, menu.getId(), AdminCommonConstant.RESOURCE_TYPE_BTN);
            }
            if(Objects.equals(roleId,sysAuthConfig.getSysUsername())){
                Element paramTemp = new Element();
                paramTemp.setMenuId(menu.getId());
                elementList = elementMapper.select(paramTemp);
            }
            if (elementList.size() > 0) {
                FrontPermission frontPermission = this.transferFrontPermission(menu, roleId, elementList);
                resultPermission.add(frontPermission);
            }
        }
        return resultPermission;

    }

    /**
     * 将elements赋值给菜单
     *
     * @param menu     菜单实体类
     * @param roleId   角色
     * @param elements 功能集
     * @return AdminPermission供前端显示
     */
    private FrontPermission transferFrontPermission(Menu menu, String roleId, List<Element> elements) {
        //添加AdminPermission参数
        FrontPermission frontPermission = new FrontPermission();
        BeanUtils.copyProperties(menu, frontPermission);
        //单独处理menuid,roleId
        frontPermission.setMenuId(menu.getId());
        frontPermission.setRoleId(roleId);

        List<ActionEntitySet> actionEntitySet = JSON.parseArray(JSON.toJSONString(elements), ActionEntitySet.class);

        actionEntitySet.parallelStream().forEach(actionEntitySetEntity -> {
            actionEntitySetEntity.setDefaultCheck(true);
        });
        //给菜单赋值所有的Element
        frontPermission.setActionEntitySetList(actionEntitySet);
        return frontPermission;
    }

    @Override
    public void insertSelective(Role entity) {
        super.insertSelective(entity);
        //增加角色时，默认给角色三个权限，1.登录 2.工作舱 3.研讨服务
        String[] menuCodes = AdminCommonConstant.DEFAULT_MENU_PERMISSION_CODE_LIST.split(",");
        for (String menuCode :
                menuCodes) {
            Menu menu = new Menu();
            menu.setCode(menuCode);
            menu = menuMapper.selectOne(menu);
            if (menu == null) {
                throw new BaseException("系统中菜单信息集中不匹配code");
            }
            Element element = new Element();
            element.setMenuId(menu.getId());
            List<Element> elements = elementMapper.select(element);
            elements.stream().forEach((Element eleDTO) -> {
                ResourceRoleMap resourceRoleMap = new ResourceRoleMap();
                EntityUtils.setCreatAndUpdatInfo(resourceRoleMap);
                resourceRoleMap.setRoleId(entity.getId());
                resourceRoleMap.setResourceId(eleDTO.getId());
                resourceRoleMap.setResourceType(AdminCommonConstant.RESOURCE_TYPE_BTN);
                resourceRoleMapMapper.insertSelective(resourceRoleMap);
            });
        }

    }

//    @Override
//    public TableResultResponse<Role> selectByQuery(Query query) {
//        Example example = new Example(Role.class);
//        Example.Criteria criteria = example.createCriteria();
//        if (query.entrySet().size() > 0) {
//            for (Map.Entry<String, Object> entry : query.entrySet()) {
//                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%").andNotEqualTo("code", "系统管理员")
//                        .andNotEqualTo("code", "安全管理员")
//                        .andNotEqualTo("code", "日志审计员");
//            }
//        }else {
//            criteria.andNotEqualTo("code", "系统管理员")
//                    .andNotEqualTo("code", "安全管理员")
//                    .andNotEqualTo("code", "日志审计员");
//        }
//        criteria.andNotEqualTo("id",AdminCommonConstant.SUPER_SYSTEM_ID);
//        example.setOrderByClause("CRT_TIME DESC");
//        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
//        List<Role> list = mapper.selectByExample(example);
//        return new TableResultResponse<Role>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), list);
//    }
}

