package com.github.hollykunge.security.admin.rpc.service;

import com.github.hollykunge.security.admin.api.authority.ActionEntitySet;
import com.github.hollykunge.security.admin.api.authority.FrontPermission;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.biz.*;
import com.github.hollykunge.security.admin.config.mq.ProduceSenderConfig;
import com.github.hollykunge.security.admin.entity.Element;
import com.github.hollykunge.security.admin.entity.Menu;
import com.github.hollykunge.security.admin.entity.Role;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.util.PassWordEncoderUtil;
import com.github.hollykunge.security.admin.vo.*;
import com.github.hollykunge.security.auth.client.config.SysAuthConfig;
import com.github.hollykunge.security.auth.client.jwt.UserAuthUtil;

import com.github.hollykunge.security.common.exception.service.ClientParameterInvalid;
import com.github.hollykunge.security.common.exception.service.ServerLeadBizException;
import com.github.hollykunge.security.common.util.StringHelper;

import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.common.vo.mq.HotMapVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 初始化用户权限服务
 *
 * @author 协同设计小组
 * @date 2017/9/12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionService {
    @Autowired
    private RoleBiz roleBiz;
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private MenuBiz menuBiz;
    @Autowired
    private ElementBiz elementBiz;
    @Autowired
    private UserAuthUtil userAuthUtil;
    @Autowired
    private PositionBiz positionBiz;

    @Autowired
    private ProduceSenderConfig produceSenderConfig;
    @Autowired
    private SysAuthConfig sysAuthConfig;

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public AdminUser getUserByUserId(String userId) {
        AdminUser info = new AdminUser();
        User user = userBiz.getUserByUserId(userId);
        BeanUtils.copyProperties(user, info);
        info.setId(user.getId());
        return info;
    }

    /**
     * 验证用户
     *
     * @param userPid
     * @param password
     * @return
     */
    public AdminUser validate(String userPid, String password) {
        AdminUser info = new AdminUser();
        //判断是否为系统超级管理员
        if (Objects.equals(userPid, sysAuthConfig.getSysUsername())) {
            if (!Objects.equals(password, sysAuthConfig.getSysPassword())) {
                throw new ClientParameterInvalid("超级管理员密码错误");
            }
            info.setId(sysAuthConfig.getSysUsername());
            info.setPId(sysAuthConfig.getSysUsername());
            info.setPassword(sysAuthConfig.getSysPassword());
            info.setSecretLevel("90");
            info.setName(sysAuthConfig.getSysUsername());
            return info;
        }
        User user = userBiz.getUserByUserPid(userPid);
        if (user == null) {
            throw new ClientParameterInvalid("没有该用户");
        }
        if (!PassWordEncoderUtil.ENCODER.matches(password, user.getPassword())) {
            throw new ClientParameterInvalid("密码错误");
        }
        BeanUtils.copyProperties(user, info);
        info.setId(user.getId());
        return info;
    }

    /**
     * 获取所有的资源权限，包括菜单和按钮
     *
     * @return
     */
    public List<FrontPermission> getAllPermission() {
        List<Menu> menus = menuBiz.selectListAll();
        List<FrontPermission> result = new ArrayList<FrontPermission>();
        menu2permission(menus, result);

        List<Element> elements = elementBiz.selectListAll();
        element2permission(result, elements);
        return result;
    }

    /**
     * 菜单权限
     *
     * @param menus
     * @param result
     */
    private void menu2permission(List<Menu> menus, List<FrontPermission> result) {
        FrontPermission info;
        for (Menu menu : menus) {
            info = new FrontPermission();
            info.setMenuId(menu.getId());
            info.setTitle(menu.getTitle());
            info.setUri(menu.getUri());
            info.setPermissionId(menu.getPermissionId());
            result.add(info);
        }
    }

    /**
     * 根据userId获取角色所属菜单功能
     *
     * @param userId
     * @return 20-2-21 fansq 修改异常类型 baseException ->ClientInvalidException
     */
    public List<FrontPermission> getPermissionByUserId(String userId) {
        String roleId = "";
        if (Objects.equals(userId, sysAuthConfig.getSysUsername())) {
            roleId = userId;
        } else {
            List<Role> roleByUserId = roleBiz.getRoleByUserId(userId);
            if (roleByUserId.size() == 0) {
                throw new ServerLeadBizException("用户"+userId+"无角色信息。");
            }
            roleId = roleByUserId.get(0).getId();
        }
        return roleBiz.frontAuthorityMenu(roleId);
    }

    /**
     * 元素权限
     *
     * @param result
     * @param elements
     */
    private void element2permission(List<FrontPermission> result, List<Element> elements) {

        for (FrontPermission frontPermission : result) {

            List<Element> tempElement = elements.stream()
                    .filter((Element e) -> frontPermission.getMenuId().contains(e.getMenuId()))
                    .collect(Collectors.toList());
            ActionEntitySet info;
            List<ActionEntitySet> actionEntitySets = new ArrayList<>();
            for (Element element : tempElement) {


                info = new ActionEntitySet();

                info.setDefaultCheck(true);
                info.setDescription(element.getDescription());
                info.setMethod(element.getMethod());

                actionEntitySets.add(info);
            }
            frontPermission.setActionEntitySetList(actionEntitySets);
            frontPermission.setMethods(StringHelper.getObjectValue(actionEntitySets));
        }

    }

    /**
     * 获取前端用户信息
     *
     * @param token
     * @return
     * @throws Exception
     */
    public FrontUser getUserInfo(String token) throws Exception {
        String userId = userAuthUtil.getInfoFromToken(token).getId();
        //这个位置不可能出现异常，如果userid为null，token解析的时候已经报异常了
//        if (userId == null) {
//            //20-2-21 fansq添加异常返回类型
//            throw new UserTokenException("解析token异常。");
//        }
        FrontUser frontUser = new FrontUser();
        //如果是超级管理员，则显示所有的菜单和操作
        if (Objects.equals(sysAuthConfig.getSysUsername(), userId)) {
            frontUser.setId(userAuthUtil.getInfoFromToken(token).getId());
            frontUser.setName(userAuthUtil.getInfoFromToken(token).getName());
            frontUser.setPId(userAuthUtil.getInfoFromToken(token).getUniqueName());
            frontUser.setSecretLevel(userAuthUtil.getInfoFromToken(token).getSecretLevel());
            frontUser.setPositions(userAuthUtil.getInfoFromToken(token).getId());
        }
        if (!Objects.equals(sysAuthConfig.getSysUsername(), userId)) {
            User user = userBiz.getUserByUserId(userId);
            BeanUtils.copyProperties(user, frontUser);
            frontUser.setId(user.getId());
            frontUser.setPositions(String.valueOf(positionBiz.getPositionIdByUserId(userId)));
        }
        UserRole userRole = this.getUserRoleByUserId(userId);
        frontUser.setUserRole(userRole);
        //发送消息到mq
        HotMapVO hotMapVO = new HotMapVO();
        hotMapVO.setUserId(userId);
        ZoneId zoneId = ZoneId.systemDefault();
        ChronoZonedDateTime<LocalDate> zonedDateTime = LocalDate.now().atStartOfDay(zoneId);
        Date nowDate = Date.from(zonedDateTime.toInstant());
        hotMapVO.setMapDate(nowDate);
        produceSenderConfig.sendAndNoConfirm(UUIDUtils.generateShortUuid(), hotMapVO);
        return frontUser;
    }

    /**
     * 获取用户角色信息
     *
     * @param userId
     * @return
     */
    public UserRole getUserRoleByUserId(String userId) {
        UserRole userRole = new UserRole();
        //如果是超级管理员
        if (Objects.equals(userId, sysAuthConfig.getSysUsername())) {
            List<FrontPermission> frontPermissionList = this.getPermissionByUserId(userId);
            userRole.setFrontPermissionList(frontPermissionList);
            userRole.setId("system");
            userRole.setName("超级管理员");
            return userRole;
        }
        List<Role> roleList = roleBiz.getRoleByUserId(userId);
        //使用list可能有点不太对劲
        BeanUtils.copyProperties(roleList.get(0), userRole);
        List<FrontPermission> frontPermissionList = this.getPermissionByUserId(userId);
        userRole.setFrontPermissionList(frontPermissionList);
        return userRole;
    }
}
