package com.github.hollykunge.security.admin.rest;

import com.alibaba.excel.exception.ExcelAnalysisException;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.biz.OrgBiz;
import com.github.hollykunge.security.admin.biz.PositionBiz;
import com.github.hollykunge.security.admin.biz.RoleBiz;
import com.github.hollykunge.security.admin.biz.UserBiz;
import com.github.hollykunge.security.admin.entity.Position;
import com.github.hollykunge.security.admin.entity.Role;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.mapper.OrgMapper;
import com.github.hollykunge.security.admin.mapper.PositionUserMapMapper;
import com.github.hollykunge.security.admin.mapper.RoleUserMapMapper;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.admin.rpc.service.PermissionService;
import com.github.hollykunge.security.admin.util.EasyExcelUtil;
import com.github.hollykunge.security.admin.util.ExcelListener;
import com.github.hollykunge.security.admin.vo.FrontUser;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.auth.ClientInvalidException;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.common.util.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户相关信息接口
 *
 * @author 协同设计小组
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController<UserBiz, User> {
    @Value("${auth.user.token-header}")
    private String headerName;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleBiz roleBiz;

    @Autowired
    private OrgBiz orgBiz;

    @Autowired
    private PositionBiz positionBiz;

    @Autowired
    private UserBiz userBiz;


    /**
     * todo:使用
     * 登录获取用户人信息
     *
     * @param token
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/front/info", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<?> getUserInfo(String token, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(token)) {
            token = request.getHeader(headerName);
        }
        FrontUser userInfo = permissionService.getUserInfo(token);
        return new ObjectRestResponse().data(userInfo).msg("").rel(true);
    }

    /**
     * todo:使用
     * 添加用户接口
     * ps：由于添加用户的时候需要给用户角色，故而单独提供一个
     * fansq  19-12-2 添加接口整合
     * 添加用户的接口，该接口携带返回值
     *
     * @param entity 用户实体
     * @return 用户id
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<AdminUser> addUser(@RequestBody User entity,
                                                 @RequestParam("roles") String roles,
                                                 @RequestParam("positions") String positions) {
        //添加用户信息
        User user = baseBiz.addUser(entity);
        //未防止暴露密码使用AdminUser携带id返回前端
        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(user, adminUser);
        //添加用户角色信息
        baseBiz.modifyRoles(adminUser.getId(), roles);
        //添加用户权限信息
        baseBiz.insertUserPosition(positions, adminUser.getId());
        return new ObjectRestResponse<AdminUser>().data(adminUser).rel(true);
    }

    /**
     * todo:使用
     * 更新用户信息
     * fansq 10-12-2 接口整合
     *
     * @param entity
     * @param roles
     * @param positions
     * @return
     */
    @RequestMapping(value = "userInfo", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<User> updateUser(@RequestBody User entity,
                                               String roles,
                                               String positions) {
        entity.setPId(entity.getPId().toLowerCase());
        baseBiz.updateSelectiveById(entity);
        //添加用户角色信息
        if (!StringUtils.isEmpty(roles)) {
            baseBiz.modifyRoles(entity.getId(), roles);
        }
        //添加用户权限信息
        if (!StringUtils.isEmpty(positions)) {
            baseBiz.insertUserPosition(positions, entity.getId());
        }
        return new ObjectRestResponse<User>().rel(true);
    }

    /**
     * todo:使用
     * 获取用户信息
     * fansq 19-12-2 接口整合
     *
     * @param userId 用户id
     * @return
     */
    @RequestMapping(value = "userInfo", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<Object>> getUser(@RequestParam("id") String userId) {
        List<Role> roleList = roleBiz.getRoleByUserId(userId);
        List<Position> positionList = positionBiz.getPositionsByUserId(userId);
        List<Object> list = new ArrayList<>();
        list.add(roleList);
        list.add(positionList);
        return new ListRestResponse("查询成功！", list.size(), list);
    }

    /**
     * 用户删除  根据用户id同步删除角色 和 权限 两张关联表的数据
     * fansq 19-12-2
     *
     * @param id
     * @return
     */
    @Override
    public ObjectRestResponse<User> remove(String id) {
        return super.remove(id);
    }

    /**
     * todo:使用
     * 给用户设置角色接口
     *
     * @param userId 用户id
     * @param roles  角色集（以“，”隔开的字符串）
     * @return
     */
    @RequestMapping(value = "/role", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUserRoles(@RequestParam("userId") String userId, @RequestParam("roles") String roles) {
        baseBiz.modifyRoles(userId, roles);
        return new ObjectRestResponse().rel(true).msg("");
    }

    /**
     * todo:使用
     * 根据用户id获取用户角色
     *
     * @param userId 用户id
     * @return 角色
     */
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<Role>> getRolesByUserId(@RequestParam("id") String userId) {
        List<Role> roleList = roleBiz.getRoleByUserId(userId);
        return new ListRestResponse("", roleList.size(), roleList);
    }

    /**
     * fansq 19-11-28
     * 根据用户id获取权限信息
     *
     * @param userId 用户id
     * @return 权限列表
     */
    @RequestMapping(value = "/getPositionByUserId", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<Position>> getPositionsByUserId(@RequestParam("userId") String userId) {
        List<Position> positions = positionBiz.getPositionsByUserId(userId);
        return new ListRestResponse<>("查询成功", positions.size(), positions);
    }


    /**
     * 根据姓名匹配返回人员
     *
     * @param nameLike 用户id
     * @return 角色
     * fansq 修改 添加异常 ClientInvalidException
     */
    @RequestMapping(value = "/nameLike", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<AdminUser>> getUserByNameLike(@RequestParam("nameLike") String nameLike) {
        List<AdminUser> adminUsers = new ArrayList<>();
        if (StringUtils.isEmpty(nameLike)) {
            throw new ClientInvalidException("Name is empty");
        } else {
            List<User> users = baseBiz.selectUserByNameLike(nameLike);
            users.stream().forEach(user -> {
                AdminUser adminUser = new AdminUser();
                BeanUtils.copyProperties(user, adminUser);
                adminUser.setPathName(orgBiz.getPathName(user.getOrgCode()).get(0).getPathName());
                adminUsers.add(adminUser);
            });
        }
        return new ListRestResponse("", adminUsers.size(), adminUsers);
    }

    /**
     * todo:使用
     *
     * @param params
     * @return
     * fansq 修改 添加异常 ClientInvalidException
     */
    @RequestMapping(value = "/findUsers", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<User> findUsers(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("name"))) {
            //return new TableResultResponse<>();
            throw new ClientInvalidException("params is empty");
        }
        return baseBiz.selectByQuery(new Query(params));
    }

    /**
     * fansq 导出给前端
     *
     * @param params
     * @param httpServletResponse
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportExcelWeb", method = RequestMethod.GET)
    public void exportUserExcelWeb(
            @RequestParam Map<String, Object> params,
            HttpServletResponse httpServletResponse) throws Exception {
        userBiz.exportUserExcelWeb(params,httpServletResponse);
    }

    /**
     * fansq
     * excel文件导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    @ResponseBody
    public ObjectRestResponse importExcel(@RequestParam("file") MultipartFile file) throws Exception{
        return userBiz.importExcel(file,userBiz);
    }
}
