package com.github.hollykunge.security.admin.biz;

import com.ace.cache.annotation.CacheClear;
import com.github.hollykunge.security.admin.annotation.FilterByDeletedAndOrderHandler;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.*;
import com.github.hollykunge.security.admin.mapper.OrgMapper;
import com.github.hollykunge.security.admin.mapper.PositionUserMapMapper;
import com.github.hollykunge.security.admin.mapper.RoleUserMapMapper;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.admin.util.EasyExcelUtil;
import com.github.hollykunge.security.admin.util.ExcelListener;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.constant.UserConstant;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.SpecialStrUtils;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public User addUser(User entity) {
        if (SpecialStrUtils.check(entity.getName())) {
            throw new BaseException("姓名中不能包含特殊字符...");
        }
        //校验身份证是否在数据库中存在
        User user = new User();
        user.setPId(entity.getPId());
        if (mapper.selectCount(user) > 0) {
            throw new BaseException("身份证已存在...");
        }
        entity.setPId(entity.getPId().toLowerCase());
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(defaultPassword);
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
            throw new BaseException("姓名中不能包含特殊字符...");
        }
        super.updateSelectiveById(entity);
    }

    @Override
//    @CacheClear(keys = {"user","userByPid"})
    /**
     * 用户删除  根据用户id同步删除角色 和 权限 两张关联表的数据
     */
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
//    @Cache(key = "userByPid{1}")
    public User getUserByUserPid(String pid) {
        User user = new User();
        //用户登录时通过身份证号当做用户名登录
        user.setPId(pid);
        return mapper.selectOne(user);
    }

    //    @Cache(key = "user")
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
            throw new BaseException("userId参数为null...");
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
        int deleteCount = positionUserMapMapper.delete(positionUser);
        PositionUserMap positionUserMapDo;
        if (!StringUtils.isEmpty(positionsIds)) {
            String[] poiArr = positionsIds.split(",");
            for (String poi : poiArr) {
                positionUserMapDo = new PositionUserMap();
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
                //如果orgCode为航天二院组织编码，则返回的数据为空
                if (AdminCommonConstant.NO_DATA_ORG_CODE.equals(entry.getValue().toString())) {
                    return new TableResultResponse<User>(query.getPageSize(), query.getPageNo(), 0, 0, new ArrayList<>());
                }
                if (SpecialStrUtils.check(entry.getValue().toString())) {
                    throw new BaseException("查询条件不能包含特殊字符...");
                }
                if (entry.getKey().equals("name")) {
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
        pageUsers.stream().forEach((User user) ->{
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
        List<User> users = mapper.selectUserByNameLike(nameLike);
        return users;
    }

    @Override
    public User selectById(String id) {
        User user = super.selectById(id);
        Org org = orgBiz.selectById(user.getOrgCode());
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
        List<User> userEasyExcelList = userMapper.selectByExample(example);
        return userEasyExcelList;
    }
}