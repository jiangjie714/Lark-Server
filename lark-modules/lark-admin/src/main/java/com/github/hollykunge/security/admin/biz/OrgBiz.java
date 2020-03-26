package com.github.hollykunge.security.admin.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.admin.annotation.FilterByDeletedAndOrderHandler;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.api.dto.OrgUser;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.entity.OrgUserMap;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.mapper.OrgMapper;
import com.github.hollykunge.security.admin.mapper.OrgUserMapMapper;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.admin.redis.service.IRedisDataBaseService;
import com.github.hollykunge.security.admin.rpc.service.UserRestService;
import com.github.hollykunge.security.admin.util.EasyExcelUtil;
import com.github.hollykunge.security.admin.util.ExcelListener;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.constant.CommonConstants;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 云雀小组
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class OrgBiz extends BaseBiz<OrgMapper, Org> {

    @Resource
    private OrgUserMapMapper orgUserMapMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserBiz userBiz;
    @Resource
    IRedisDataBaseService iRedisDataBaseService;
    @Autowired
    private UserRestService userRestService;
    @Autowired
    private OrgMapper orgMapper;
    //用户缓存namespace
    private final String USER_ONLINE_KEY = "users_on_line:";
    private final String USER_ONLINE = "700";
    private final String USER_OFFLINE = "701";
    @FilterByDeletedAndOrderHandler
    public List<AdminUser> getOrgUsers(String orgCode, String secretLevels, String PId, String grouptype, String localUserOrg) {
        //如果orgCode为航天二院，则直接返回空数据
        if (AdminCommonConstant.NO_DATA_ORG_CODE.equals(orgCode)) {
            return new ArrayList<AdminUser>();
        }
        User userParams = new User();
        userParams.setOrgCode(orgCode);
        Example userExample = new Example(User.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andLike("orgCode", orgCode + "%");
        if (!StringUtils.isEmpty(secretLevels)) {
            String[] secretLevelArray = secretLevels.split(",");
            List<String> secretList = Arrays.asList(secretLevelArray);
            for (String secretLevel : secretLevelArray) {
                criteria.andIn("secretLevel", secretList);
            }
        }
        if (!StringUtils.isEmpty(PId)) {
            criteria.andNotEqualTo("PId", PId);
        }
        List<User> users = userMapper.selectByExample(userExample);
        List<AdminUser> userList;
        userList = JSON.parseArray(JSON.toJSONString(users), AdminUser.class);
        //科室内部
        if ("0".equals(grouptype)) {
            userList = userList.stream().filter(new Predicate<AdminUser>() {
                @Override
                public boolean test(AdminUser adminUser) {
                    if (StringUtils.isEmpty(adminUser.getOrgCode())) {
                        return false;
                    }
                    return adminUser.getOrgCode().equals(localUserOrg);
                }
            }).collect(Collectors.toList());
        }
        //跨科室
        if ("1".equals(grouptype)) {
            String parentOrgCode;
            //过滤外网错误数据，内网正常人员不会出现5为以下的orgCode
            if (localUserOrg.length() > 6) {
                //为院机关
                if (!AdminCommonConstant.ORG_CODE_INSTITUTE_ORGAN.equals(parentOrgCode = localUserOrg.substring(0, 6))) {
                    parentOrgCode = localUserOrg.substring(0, 4);
                }
            } else {
                parentOrgCode = localUserOrg;
            }
            final String finalParentOrgCode = parentOrgCode;
            userList = userList.stream().filter(new Predicate<AdminUser>() {
                @Override
                public boolean test(AdminUser adminUser) {
                    if (StringUtils.isEmpty(adminUser.getOrgCode())) {
                        return false;
                    }
                    //内网中不可能出现人员orgcode长度小于6的值，此处过滤外网错误数据
                    if (adminUser.getOrgCode().length() < 6) {
                        return false;
                    }
                    return adminUser.getOrgCode().substring(0, 6).contains(finalParentOrgCode);
                }
            }).collect(Collectors.toList());
        }
        //跨场所显示全部数据
        return userList;
    }

    //    @CacheClear(pre = "permission")
    public void modifyOrgUsers(String orgId, String users) {
        OrgUserMap orgUserMap = new OrgUserMap();
        orgUserMap.setOrgId(orgId);
        orgUserMapMapper.delete(orgUserMap);
        if (!StringUtils.isEmpty(users)) {
            String[] mem = users.split(",");
            for (String m : mem) {
                orgUserMap.setUserId(m);
                EntityUtils.setCreatAndUpdatInfo(orgUserMap);
                orgUserMapMapper.insertSelective(orgUserMap);
            }
        }
    }

    @Override
    protected String getPageName() {
        return null;
    }

    @Override
    public void insertSelective(Org entity) {
        EntityUtils.setCreatAndUpdatInfo(entity);
        entity.setId(entity.getOrgCode());
        mapper.insertSelective(entity);
    }

    @FilterByDeletedAndOrderHandler
    public List<OrgUser> getChildOrgUser(String parentCode) throws Exception{
        List<OrgUser> result = new ArrayList<>();
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",parentCode);
        List<Org> orgs = mapper.selectByExample(example);
        if(orgs.size() > 0){
            return setOrgUserList(result,orgs,null);
        }
        User userTemp = new User();
        userTemp.setOrgCode(parentCode);
        List<User> users = userBiz.selectList(userTemp);
        if(users.size() > 0){
            return setOrgUserList(result,null,users);
        }
        return result;
    }

    private List<com.github.hollykunge.security.admin.api.dto.OrgUser> setOrgUserList(List<com.github.hollykunge.security.admin.api.dto.OrgUser> orgUserList,
                                List<Org> orgs,
                                List<User> users){
        if(orgs != null){
            orgUserList = JSONArray.parseArray(JSONObject.toJSONString(orgs),com.github.hollykunge.security.admin.api.dto.OrgUser.class);
            setUserOrOrgFlag(orgUserList,AdminCommonConstant.CONTACT_FLAG_ORGNODE);
        }
        if(users != null){
            orgUserList = JSONArray.parseArray(JSONObject.toJSONString(users),com.github.hollykunge.security.admin.api.dto.OrgUser.class);
            setUserOrOrgFlag(orgUserList,AdminCommonConstant.CONTACT_FLAG_USERNODE);
            //先以在线状态降序，再以orderId升序
//            orgUserList.sort(Comparator.comparing(com.github.hollykunge.security.admin.api.dto.OrgUser::getOnline)
//                    .reversed().thenComparing(com.github.hollykunge.security.admin.api.dto.OrgUser::getOrderId));
        }
        return orgUserList;
    }

    private void setUserOrOrgFlag(List<com.github.hollykunge.security.admin.api.dto.OrgUser> orgUserList,
                                  String flag){
        orgUserList.stream().forEach(orgUser ->{
            orgUser.setScopedSlotsTitle(flag);
            if(Objects.equals(AdminCommonConstant.CONTACT_FLAG_USERNODE,flag)){
                orgUser.setOnline(true);
                //读取缓存判断用户是否在线
                String online = iRedisDataBaseService.get(USER_ONLINE_KEY+orgUser.getId());
                if(online==null || USER_OFFLINE.equals(online)){
                    orgUser.setOnline(false);
                }
                String userInfo = userRestService.getUserInfo(null, orgUser.getId());
                List<AdminUser> adminUsers = JSONArray.parseArray(userInfo, AdminUser.class);
                if(adminUsers.size() > 0){
                    orgUser.setPathName(adminUsers.get(0).getPathName());
                }
            }
        });
    }

    public List<Org> getPathName(String orgCode) {
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgCode",orgCode);
        List<Org> orgs = this.selectByExample(example);
        return orgs;
    }


    /**
     * fansq
     * 20-2-23
     * 添加组织数据导入
     * @param file
     * @return
     * @throws IOException
     */
    public ObjectRestResponse importExcel(MultipartFile file) throws Exception {
        ExcelListener excelListener = EasyExcelUtil.importOrgExcel(file.getInputStream(),orgMapper);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
            objectRestResponse.setStatus(CommonConstants.HTTP_SUCCESS);
            objectRestResponse.setMessage("导入成功！");
            return objectRestResponse;
    }

    /**
     *
     * @param orgCode
     * @param orgLevel
     * @return
     */
    public List<Org> findOrgByLevelAndParentId(String orgCode, Integer orgLevel) {
        return orgMapper.findOrgByLevelAndParentId(orgCode,orgLevel);
    }
}