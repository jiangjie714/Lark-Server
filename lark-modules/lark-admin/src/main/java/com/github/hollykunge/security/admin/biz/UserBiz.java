package com.github.hollykunge.security.admin.biz;

import com.ace.cache.annotation.CacheClear;
import com.github.hollykunge.security.admin.annotation.FilterByDeletedAndOrderHandler;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.dto.biz.ChangeUserPwdDto;
import com.github.hollykunge.security.admin.entity.*;
import com.github.hollykunge.security.admin.mapper.OrgMapper;
import com.github.hollykunge.security.admin.mapper.PositionUserMapMapper;
import com.github.hollykunge.security.admin.mapper.RoleUserMapMapper;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.admin.rpc.service.PermissionService;
import com.github.hollykunge.security.admin.util.EasyExcelUtil;
import com.github.hollykunge.security.admin.util.ExcelListener;
import com.github.hollykunge.security.admin.util.PassWordEncoderUtil;
import com.github.hollykunge.security.auth.client.config.SysAuthConfig;
import com.github.hollykunge.security.auth.client.jwt.UserAuthUtil;
import com.github.hollykunge.security.auth.common.util.jwt.IJWTInfo;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.exception.service.ClientParameterInvalid;
import com.github.hollykunge.security.common.exception.service.DatabaseDataException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.SpecialStrUtils;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.common.vo.RpcUserInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author 协同设计小组
 * @create 2017-06-08 16:23
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper, User> {
    @Value("${admin.create-user.defaultPassword}")
    private String defaultPassword;

    @Resource
    private RoleUserMapMapper roleUserMapMapper;

    @Autowired
    private PositionUserMapMapper positionUserMapMapper;

    @Resource
    private OrgMapper orgMapper;

    @Autowired
    private OrgBiz orgBiz;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SysAuthConfig sysAuthConfig;
    @Autowired
    private UserAuthUtil userAuthUtil;
    @Autowired
    private UserBiz userBiz;

    public User addUser(User entity) {
        // 这个判断应该交给前端做
        if (SpecialStrUtils.check(entity.getName())) {
            throw new ClientParameterInvalid("姓名中不能包含特殊字符。");
        }
        //校验身份证是否在数据库中存在
        User user = new User();
        user.setPId(entity.getPId());
        if (mapper.selectCount(user) > 0) {
            throw new ClientParameterInvalid("身份证号已存在。");
        }
        entity.setPId(entity.getPId().toLowerCase());
        //统一使用密码工具类
        String password = PassWordEncoderUtil.ENCODER.encode(defaultPassword);
        entity.setPassword(password);
        EntityUtils.setCreatAndUpdatInfo(entity);
        //用户新增添加默认字段
        entity.setDeleted("0");
        //fansq 19-11-27 添加字段默认值
        if(StringUtils.isEmpty(entity.getEmpCode())){
            entity.setEmpCode(UUIDUtils.generateShortUuid());
        }
        if(StringUtils.isEmpty(entity.getAvatar())){
            entity.setAvatar(AdminCommonConstant.USER_AVATAR);
        }
        if(StringUtils.isEmpty(entity.getSecretLevel())){
            entity.setSecretLevel(AdminCommonConstant.USER_LEVEL);
        }
        if (StringUtils.isEmpty(entity.getOrderId())) {
            entity.setOrderId(Long.parseLong(99999 + ""));
        }
        mapper.insertSelective(entity);
        return entity;
    }

    @Override
    @CacheClear(pre = AdminCommonConstant.CACHE_KEY_RPC_USER+"{1.id}")
    public void updateSelectiveById(User entity) {
        if (SpecialStrUtils.check(entity.getName())) {
            throw new ClientParameterInvalid("姓名中不能包含特殊字符。");
        }
        super.updateSelectiveById(entity);
    }

    /**
     * 用户删除  根据用户id同步删除角色 和 权限 两张关联表的数据
     */
    @Override
    public void deleteById(String id) {
        RoleUserMap roleUserMap = new RoleUserMap();
        roleUserMap.setUserId(id);
        roleUserMapMapper.delete(roleUserMap);
        PositionUserMap positionUser = new PositionUserMap();
        positionUser.setUserId(id);
        positionUserMapMapper.delete(positionUser);
        super.deleteById(id);
    }

    /**
     * 通过userId，主键获取用户
     *
     * @param userId
     * @return
     */
    public User getUserByUserId(String userId) {
        User user = new User();
        user.setId(userId);
        return mapper.selectOne(user);
    }

    /**
     * 用户登录通过身份证号获取用户
     *
     * @param pid
     * @return
     */
    public User getUserByUserPid(String pid) {
        User user = new User();
        //用户登录时通过身份证号当做用户名登录
        user.setPId(pid);
        return mapper.selectOne(user);
    }

    public List<User> getUsers() {
        return mapper.selectAll();
    }

    @Override
    protected String getPageName() {
        return null;
    }

    /**
     * 修改用户的角色或添加用户角色
     *
     * @param userId
     * @param roles
     */
    public void modifyRoles(String userId, String roles) {
        if (StringUtils.isEmpty(userId)) {
            throw new ClientParameterInvalid("用户id为空。");
        }
        RoleUserMap roleUserMap = new RoleUserMap();
        roleUserMap.setUserId(userId);
        //删除用户角色
        roleUserMapMapper.delete(roleUserMap);
        if (!StringUtils.isEmpty(roles)) {
            String[] roleArray = roles.split(",");
            for (String role : roleArray) {
                roleUserMap.setRoleId(role);
                EntityUtils.setCreatAndUpdatInfo(roleUserMap);
            roleUserMapMapper.insertSelective(roleUserMap);
            }
        }
    }

    /**
     * fansq
     * 19-12-2
     * 增加和修改用户权限部门
     * @param positionsIds
     * @param userId
     */
    public void insertUserPosition(String positionsIds,String userId){
        PositionUserMap positionUser = new PositionUserMap();
        positionUser.setUserId(userId);
        positionUserMapMapper.delete(positionUser);
        if (!StringUtils.isEmpty(positionsIds)) {
            String[] poiArr = positionsIds.split(",");
            for (String poi : poiArr) {
                PositionUserMap positionUserMapDo = new PositionUserMap();
                positionUserMapDo.setPositionId(poi);
                positionUserMapDo.setUserId(userId);
                //给基类赋值
                EntityUtils.setCreatAndUpdatInfo(positionUserMapDo);
                positionUserMapMapper.insertSelective(positionUserMapDo);
            }
        }
    }

    @Override
    public TableResultResponse<User> selectByQuery(Query query) {
        Class<User> clazz = (Class<User>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        if (query.entrySet().size() > 0) {

            for (Map.Entry<String, Object> entry : query.entrySet()) {
                // 如果orgCode为航天二院组织编码，则返回的数据为空
                if (AdminCommonConstant.NO_DATA_ORG_CODE.equals(entry.getValue().toString())) {
                    return new TableResultResponse<User>(query.getPageSize(), query.getPageNo(), 0, 0, new ArrayList<>());
                }
                if (SpecialStrUtils.check(entry.getValue().toString())) {
                    throw new ClientParameterInvalid("查询条件不能包含特殊字符...");
                }
                if ("name".equals(entry.getKey())) {
                    criteria.andCondition("(REFA || REFB || NAME) like " + "'%'||'" + entry.getValue().toString() + "'||'%'")
                            .orCondition("(upper(REFA) || upper(REFB) || NAME) like " + "'%'||'" + entry.getValue().toString() + "'||'%'")
                            .orCondition("(REFA || REFB || upper(NAME)) like " + "'%'||'" + entry.getValue().toString().toUpperCase() + "'||'%'")
                            .orCondition("(REFA || REFB || lower(NAME)) like " + "'%'||'" + entry.getValue().toString().toLowerCase() + "'||'%'")
                            .andNotEqualTo("PId", "3")
                            .andNotEqualTo("PId", "4");
                }else {
                    criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%").andNotEqualTo("PId", "2")
                            .andNotEqualTo("PId", "3")
                            .andNotEqualTo("PId", "4");
                }
            }
        }else {
            criteria.andNotEqualTo("PId", "2")
                    .andNotEqualTo("PId", "3")
                    .andNotEqualTo("PId", "4");
        }
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<User> pageUsers = ((UserBiz) AopContext.currentProxy()).getPageUsers(example);
        pageUsers.forEach((User user) ->{
            String orgCode = user.getOrgCode();
            if(!StringUtils.isEmpty(orgCode)){
                Org org = orgBiz.selectById(orgCode);
                if(org != null){
                    user.setOrgName(org.getPathName());
                }
            }
        });
        return new TableResultResponse<User>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), pageUsers);
    }
    @FilterByDeletedAndOrderHandler
    public List<User> getPageUsers(Example example){
        return mapper.selectByExample(example);
    }

    /**
     * 根据姓名匹配返回人员
     * @param nameLike
     * @return
     */
    public List<User> selectUserByNameLike(String nameLike){
        if(StringUtils.isEmpty(nameLike)){
            throw new ClientParameterInvalid("用户名为空。");
        }
        List<User> users = mapper.selectUserByNameLike(nameLike);
        return users;
    }

    @Override
    public User selectById(String id) {
        User user = super.selectById(id);
        String orgCode = user.getOrgCode();
        if(StringUtils.isEmpty(orgCode)||"".equals(orgCode)){
            throw new DatabaseDataException("当前用户没有组织编码。");
        }
        Org org = orgBiz.selectById(orgCode);
        user.setOrgName(org.getPathName());
        return user;
    }

    public void insertUserExcel(List<User> users){
        userMapper.insertUserExcel(users);
    }

    /**
     * 20-2-23
     * fansq 修改 把controller中的业务逻辑移到userbiz中
     * @param file
     * @param userBiz
     * @return
     * @throws IOException
     */
    public ObjectRestResponse importExcel(MultipartFile file,UserBiz userBiz) throws IOException {
        ExcelListener excelListener = EasyExcelUtil.importExcel(file.getInputStream(),userBiz,roleUserMapMapper,positionUserMapMapper,userMapper,orgMapper);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
            objectRestResponse.setStatus(CommonConstants.HTTP_SUCCESS);
            objectRestResponse.setMessage("导入成功！");
            return objectRestResponse;
    }

    /**
     * 20-2-23
     * fansq 将controller中业务逻辑移动到userbiz
     * @param params
     * @param httpServletResponse
     * @throws Exception
     */
    public void exportUserExcelWeb(Map<String, Object> params, HttpServletResponse httpServletResponse) throws Exception {
        Object excelType = params.get("type");
        String type = excelType == null ? "" : excelType.toString();
        if(type!=null){
            params.remove("type");
        }
        List<User> userExcelList = getUser(params);
        String fileName ="用户信息";
        String sheetName = "用户数据";
        EasyExcelUtil.exportWeb(type,httpServletResponse,fileName,sheetName,User.class,userExcelList);
    }

    /**
     * fansq
     * 根据导出规则获取用户数据
     * @param params
     */
    public  List<User> getUser(Map<String, Object> params){
        Example example = new Example(User.class);
        Query query = new Query(params);
        if(query.entrySet().size()>0) {
            Example.Criteria criteria = example.createCriteria();
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
            }
        }
        return userMapper.selectByExample(example);
    }
    /**
     * 修改用户密码业务
     * @param changeUserPwdDto
     */
    public void changeUserPwd(ChangeUserPwdDto changeUserPwdDto, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        //解析token
        IJWTInfo tokenUser = userAuthUtil.getInfoFromToken(token);
        if(Objects.equals(tokenUser.getUniqueName(),sysAuthConfig.getSysUsername())){
            throw new ClientParameterInvalid("超级管理员不能修改密码");
        }
        //校验原始的用户名和密码是否正确
        User dataUser = userBiz.getUserByUserPid(changeUserPwdDto.getUsername());
        if(dataUser == null){
            throw new ClientParameterInvalid("用户不存在...");
        }
        if (!PassWordEncoderUtil.ENCODER.matches(changeUserPwdDto.getOldPassword(), dataUser.getPassword())) {
            throw new ClientParameterInvalid("密码错误...");
        }
        User tempUser = new User();
        tempUser.setId(dataUser.getId());
        tempUser.setPassword(PassWordEncoderUtil.ENCODER.encode(changeUserPwdDto.getNewPassword()));
        tempUser.setUpdTime(new Date());
        tempUser.setUpdUser(tokenUser.getId());
        tempUser.setUpdName(tokenUser.getName());
        userMapper.updateByPrimaryKeySelective(tempUser);
    }
    /**
     * fansq
     * admin服务给task服务提供成员信息获取
     * @param userIdList
     * @return
     */
    public ObjectRestResponse<List<RpcUserInfo>> getUserInfo(List<String> userIdList){
        List<RpcUserInfo> rpcUserInfos = new ArrayList<>();
        for (String userId:userIdList){
            User user = userMapper.selectByPrimaryKey(userId);
            RpcUserInfo rpcUserInfo = new RpcUserInfo();
            BeanUtils.copyProperties(user,rpcUserInfo);
            rpcUserInfos.add(rpcUserInfo);
        }
        return new ObjectRestResponse<>().data(rpcUserInfos);
    }
}
