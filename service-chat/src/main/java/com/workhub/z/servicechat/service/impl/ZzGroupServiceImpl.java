package com.workhub.z.servicechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.dao.ZzGroupDao;
import com.workhub.z.servicechat.dao.ZzUserGroupDao;
import com.workhub.z.servicechat.entity.group.ZzGroup;
import com.workhub.z.servicechat.entity.group.ZzGroupStatus;
import com.workhub.z.servicechat.entity.group.ZzUserGroup;
import com.workhub.z.servicechat.model.GroupEditDto;
import com.workhub.z.servicechat.model.GroupEditUserList;
import com.workhub.z.servicechat.model.GroupTaskDto;
import com.workhub.z.servicechat.model.UserListDto;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.redis.RedisListUtil;
import com.workhub.z.servicechat.redis.RedisUtil;
import com.workhub.z.servicechat.service.AdminUserService;
import com.workhub.z.servicechat.service.ZzGroupService;
import com.workhub.z.servicechat.service.ZzGroupStatusService;
import com.workhub.z.servicechat.service.ZzUserGroupService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.workhub.z.servicechat.config.Common.putEntityNullToEmptyString;
import static com.workhub.z.servicechat.config.MessageType.*;
import static com.workhub.z.servicechat.config.RandomId.getUUID;

/**
 * 群组表(ZzGroup)表服务实现类
 *
 * @author 忠
 * @since 2019-05-10 14:29:32
 */
@Service("zzGroupService")
public class ZzGroupServiceImpl implements ZzGroupService {
    private static Logger log = LoggerFactory.getLogger(ZzGroupServiceImpl.class);
    @Resource
    private ZzGroupDao zzGroupDao;

    @Resource
    private ZzUserGroupDao zzUserGroupDao;

    @Autowired
    private AdminUserService iUserService;
    @Autowired
    private RabbitMqMsgProducer rabbitMqMsgProducer;
    @Autowired
    private ZzGroupStatusService zzGroupStatusService;
    @Autowired
    private ZzUserGroupService zzUserGroupService;
    /**
     * 通过ID查询单条数据
     *
     * @param groupId 主键
     * @return 实例对象
     */
    @Override
    public ZzGroup queryById(String groupId) {
        return this.zzGroupDao.queryById(groupId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ZzGroup> queryAllByLimit(int offset, int limit) {
        return this.zzGroupDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param zzGroup 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public void insert(ZzGroup zzGroup) {
        try {
            putEntityNullToEmptyString(zzGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.zzGroupDao.addGroup(zzGroup);
//        return insert;
    }

    /**
     * 修改数据
     *
     * @param zzGroup 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public Integer update(ZzGroup zzGroup) {
        int update = this.zzGroupDao.update(zzGroup);
       //通知前端更新
        SocketMsgVo socketMsgVo =  new SocketMsgVo();
        socketMsgVo.setCode(SocketMsgTypeEnum.TEAM_MSG);
        socketMsgVo.setReceiver(zzGroup.getGroupId());

        SocketMsgDetailVo addVo = new SocketMsgDetailVo();
        addVo.setCode(SocketMsgDetailTypeEnum.GROUP_EDIT);//消息类型群编辑
        GroupTaskDto addDto = new GroupTaskDto();
        addDto.setType(GROUP_JOIN_MSG);//群编辑类型：加入群
        addDto.setGroupId(zzGroup.getGroupId());
        addDto.setTimestamp(new Date());
        addDto.setReviser(zzGroup.getGroupOwnerId());//
        List<String> userList = zzGroupDao.queryGroupUserIdListByGroupId(zzGroup.getGroupId());
        String addUserIds = "";
        for(int i=0;i<userList.size();i++){
            if(i==0){
                addUserIds = userList.get(i);
            }else {
                addUserIds += ","+userList.get(i);
            }
        }
        List<UserListDto> addUserList = new ArrayList<>();
        List<ChatAdminUserVo> addUserInfoList = null;
        if(!addUserIds.equals("")) {
            addUserInfoList = iUserService.userList(addUserIds);
            for (ChatAdminUserVo userInfo : addUserInfoList) {
                UserListDto userListDto = new UserListDto();
                userListDto.setUserId(userInfo.getId());
                userListDto.setImg(userInfo.getAvatar());
                userListDto.setUserLevels(userInfo.getSecretLevel());
                addUserList.add(userListDto);
            }
        }
        addDto.setUserList(addUserList);
        addDto.setZzGroup(zzGroup);
        addVo.setData(addDto);
        socketMsgVo.setMsg(addVo);
        //校验消息
        CheckSocketMsgVo cRes = Common.checkSocketMsg(socketMsgVo);
        //只有消息合法才去绑定socket通信频道
        if(cRes.getRes()){
            rabbitMqMsgProducer.sendSocketTeamMsg(socketMsgVo);
        }

        return update;
    }

    /**
     * 通过主键删除数据
     *
     * @param groupId 主键
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteById(String groupId) {
        return this.zzGroupDao.deleteById(groupId) > 0;
    }

    @Override
    public PageInfo<GroupUserListVo> groupUserList(String id, int page, int size) throws Exception {
//        if (StringUtil.isEmpty(id)) throw new NullPointerException("id is null");
//        Page<Object> pageMassage = PageHelper.startPage(page, size);
//        pageMassage.setTotal(this.zzGroupDao.groupUserListTotal(id));
//        int startRow = pageMassage.getStartRow();
//        int endRow = pageMassage.getEndRow();
//        List<GroupUserListVo> groupUserListVos = this.zzGroupDao.groupUserList(id, startRow, endRow);
//        PageInfo<GroupUserListVo> pageInfoGroupInfo = new PageInfo<GroupUserListVo>();
//        if (groupUserListVos ==null || groupUserListVos.isEmpty()) return pageInfoGroupInfo;
//        List<String> setStr = new ArrayList<>();
//        groupUserListVos.stream().forEach(groupUserListVosList -> {
//            setStr.add(groupUserListVosList.getUserId());
//        });
//        List<UserInfo> userInfos = iUserService.userList(setStr);
//        groupUserListVos.stream().forEach(groupUserListVosList ->{
//            userInfos.stream().filter(userInfosFilter ->userInfosFilter.getId().equals(groupUserListVosList.getUserId())).forEach(userInfosList ->{
//                groupUserListVosList.setLevels("1"/*TODO*/);
//                groupUserListVosList.setFullName(userInfosList.getName());
//                groupUserListVosList.setPassword(userInfosList.getPassword());
//                groupUserListVosList.setVip(userInfosList.getDemo());//TODO
//            });
//        });
//        ZzGroup zzGroup = this.zzGroupDao.queryById(id);
//        if (zzGroup ==null) throw new RuntimeException("未查询到群组记录");
//
//        List<GroupUserListVo> resultList = this.orderByGroupUser(groupUserListVos, zzGroup.getCreator());
//
//        pageInfoGroupInfo.setList(resultList);
//        pageInfoGroupInfo.setTotal(pageMassage.getTotal());
//        pageInfoGroupInfo.setStartRow(startRow);
//        pageInfoGroupInfo.setEndRow(endRow);
//        pageInfoGroupInfo.setPages(pageMassage.getPages());
//        pageInfoGroupInfo.setPageNum(page);
//        pageInfoGroupInfo.setPageSize(size);
//        return pageInfoGroupInfo;
        return null;
    }

    @Override
    public Long groupUserListTotal(String groupId) throws Exception {
        return this.zzGroupDao.groupUserListTotal(groupId);
    }

    @Override
    public List<String> queryGroupUserIdListByGroupId(String groupId) {
        return this.zzGroupDao.queryGroupUserIdListByGroupId(groupId);
    }

    @Override
    public List<ZzGroup> queryGroupListByUserId(String id) throws Exception {
        return this.zzGroupDao.queryGroupListByUserId(id);
    }

    private List<GroupUserListVo> orderByGroupUser(List<GroupUserListVo> groupUserListVos, String creator) throws RuntimeException{
        List<GroupUserListVo> resultList = groupUserListVos.stream()
                .filter(listf -> !listf.getUserId().equals(creator)&&listf.getVip()!=0)
                .sorted((a, b) -> a.getVip() - b.getVip())
                .collect(Collectors.toList());
        try {
            resultList.add(0,groupUserListVos.stream()
                    .filter(listf -> listf.getUserId().equals(creator))
                    .collect(Collectors.toList())
                    .get(0));
        } catch (Exception e) {
            throw new RuntimeException("人员排序时未找到创建人");
        }
        resultList.addAll(groupUserListVos.stream()
                .filter(listf -> !listf.getUserId().equals(creator) && listf.getVip() != 0)
                .collect(Collectors.toList()));
        return resultList;
    }
    /**
     * 逻辑删除群
     * @param groupId 群id
     * @return  1成功；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    @Override
    public String deleteGroupLogic(String groupId, String delFlg,String userId) {
        int i=this.zzGroupDao.deleteGroupLogic( groupId, delFlg, userId);
        return  "1";
    }
    /**
     * 获取群成员列表
     */
    @Override
    public String getGroupUserList(String groupId) {

            List<String> userIdList = this.zzGroupDao.queryGroupUserIdListByGroupId(groupId);
            StringBuilder ids = new StringBuilder();
            for(String temp:userIdList){
                ids.append(temp+",");
            }
            if(ids.length()>0){
                ids.setLength(ids.length()-1);
            }
            //测试使用，给前端返回测试数据
           /* List<UserInfoVo> data=new ArrayList<>();
            for(int i=0;i<5;i++){
                UserInfoVo vo=new UserInfoVo();
                vo.setId("id"+i);
                vo.setName("name"+i);
                vo.setOnline((i%2)+"");
                vo.setAvartar("http://10.11.24.5:80/group1/M00/00/00/CgxhIl0N9FOALihUAADc6-sdZkU837.jpg");
                data.add(vo);
            }
            ListRestResponse res = new ListRestResponse("200",data.size(),data);*/
            return  ids.toString();
    }
    //群组信息监控
    //param:page 页码 size 每页几条;group_name群组名称；creator创建人姓名；level密级；
    // dateBegin创建时间开始；dateEnd创建时间结束；pname项目名称；isclose是否关闭
    @Override
    public TableResultResponse<GroupVo> groupListMonitoring(Map<String,String> params) throws Exception{
        int page=Integer.valueOf(Common.nulToEmptyString(params.get("page")));
        int size=Integer.valueOf(Common.nulToEmptyString(params.get("size")));
        PageHelper.startPage(page, size);
        List<GroupVo> dataList =this.zzGroupDao.groupListMonitoring(params);
        //null的String类型属性转换空字符串
        Common.putVoNullStringToEmptyString(dataList);
        ChatAdminUserVo userInfo=null;
        for(GroupVo groupVO:dataList){
            userInfo=iUserService.getUserInfo(Common.nulToEmptyString(groupVO.getCreator()));
            groupVO.setCreatorName(userInfo==null?"":userInfo.getName());
        }
        PageInfo<GroupVo> pageInfo = new PageInfo<>(dataList);
        TableResultResponse<GroupVo> res = new TableResultResponse<GroupVo>(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );
        return res;
    }

    @Override
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public void dissolveGroup(String groupId,String userId,String userName){
        List<String> userIds = this.zzGroupDao.queryGroupUserIdListByGroupId(groupId);
        zzUserGroupDao.deleteByGroupId(groupId);
        ZzGroup group = new ZzGroup();
        group.setGroupId(groupId);
        group.setIsdelete("1");
        group.setUpdator(userId);
        group.setUpdateTime(new Date());
        zzGroupDao.update(group);
        //记录群状态变动begin
        ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
        zzGroupStatus.setId(RandomId.getUUID());
        zzGroupStatus.setOperatorName(userName);
        zzGroupStatus.setOperator(userId);
        zzGroupStatus.setOperateType(MessageType.FLOW_DISSOLUTION);//踢出人员
        zzGroupStatus.setGroupId(groupId);
        zzGroupStatus.setOperateTime(new Date());
        zzGroupStatus.setDescribe(zzGroupStatus.getOperatorName()+ "解散了群");
        //zzGroupStatusService.add(zzGroupStatus);
        rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
        //记录群状态变动end
        try {
            //处理缓存begin
            if(userIds != null){
                for (String temp : userIds){
                    String key = CacheConst.userGroupIds+":"+temp;
                    boolean keyExist = RedisUtil.isKeyExist(key);
                    //如果key存在更新缓存，把最新的数据加入进去
                    if(keyExist){
                        RedisListUtil.removeSingle(key,groupId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("解散取处理redis缓存报错！！！");
            log.error(Common.getExceptionMessage(e));
        }

        //处理缓存end
    }

    @Override
    public void removeMember(String groupId, String userId){
        if (zzUserGroupDao.deleteByGroupIdAndUserId(groupId, userId)>0){
            //rabbitMqMsgProducer.sendMsgEditGroup("移除组成员");
        }

    }

    @Override
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public void addMember(String groupId, String userIds){
        String userIdsTemp = "";
//        JSONArray userIdJson = JSONObject.parseArray(userIds);
        if(StringUtils.isEmpty(userIds)){
            throw new BaseException("添加的人员不能为空");
        }
        List<String> userIdJson = Arrays.asList(userIds.split(","));

        //更新关联表
        zzUserGroupDao.deleteByGroupId(groupId);
        for (int i = 0; i < userIdJson.size(); i++) {
            ZzUserGroup userGroup = new ZzUserGroup();
            userGroup.setCreatetime(new Date());
            userGroup.setId(getUUID());
            userGroup.setGroupId(groupId);
//            userGroup.setUserId(userIdJson.getString(i));
            userGroup.setUserId(userIdJson.get(i));
            zzUserGroupDao.insert(userGroup);
        }

    }
    //群编辑 1成功 -1失败 0群成员过多：秘密限制50以内，机密限制100以内
    @Override
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public int groupMemberEdit(GroupEditDto groupEditDto, String userId, String userName){

        List<ChatAdminUserVo> addUserInfoList = null;
        List<ChatAdminUserVo> removeUserInfoList = null;
        try {
            String groupId = groupEditDto.getGroupId();
            List<GroupEditUserList> userListDtos = groupEditDto.getUserList();

            ZzGroup zzGroupNow = zzGroupDao.queryById(groupId);
            if(zzGroupNow == null){
                return -1;
            }
            //秘密限制100以内，机密限制50以内
            if((userListDtos.size()>100 && zzGroupNow.getLevels().equals(MessageType.NORMAL_SECRECT_LEVEL)) ||
                    (userListDtos.size()>50 && zzGroupNow.getLevels().equals(MessageType.HIGH_SECRECT_LEVEL))
            ){
                return 0;
            }
            List<String> userList = zzGroupDao.queryGroupUserIdListByGroupId(groupId);
            List<String> nowUserList =  new ArrayList<>();
            for(GroupEditUserList nowUser :userListDtos){
                nowUserList.add(Common.nulToEmptyString(nowUser.getId()));
            }
            TeamMemberChangeListVo memberChangeListVo = Common.teamMemberChangeInf(userList,nowUserList);
            List<String> addUserList = memberChangeListVo.getAddList();
            List<String> delUserList = memberChangeListVo.getDelList();
            //处理群成员begin
            //添加
            if(addUserList!=null && addUserList.size()!=0){
                List<ZzUserGroup> userGroupList = new ArrayList<>();
                for(int i=0;i<addUserList.size();i++){
                    ZzUserGroup zzUserGroup = new ZzUserGroup();
                    zzUserGroup.setId(RandomId.getUUID());
                    zzUserGroup.setGroupId(groupId);
                    zzUserGroup.setUserId(addUserList.get(i));
                    userGroupList.add(zzUserGroup);
                }
                this.zzUserGroupDao.addMemeberList(groupId,userId,userGroupList) ;
            }
            //删除
            if(delUserList!=null && delUserList.size()!=0){
                this.zzUserGroupDao.deleteByGroupIdAndUserIdList(groupId,delUserList) ;
            }
            //处理群成员end
            ZzGroup zzGroup = zzGroupDao.queryById(groupId);

            //如果有新增人员 发送消息
            List msgUserList = new ArrayList();
            if(addUserList!=null  && addUserList.size()!=0 ){
                addUserInfoList = iUserService.userList(Joiner.on(",").join(addUserList));
                String userNames = "";
                String userIds = "";
                for (ChatAdminUserVo userInfo:addUserInfoList){
                    msgUserList.add(userInfo.getId());
                    userNames += ","+userInfo.getName();
                    userIds += ","+userInfo.getId();
                    //redis 缓存处理 把用户的群列表缓存更新
                    String key = CacheConst.userGroupIds+":"+userInfo.getId();
                    boolean keyExist = RedisUtil.isKeyExist(key);
                    //如果key存在更新缓存，把最新的数据加入进去
                    if(keyExist){
                        RedisListUtil.putSingleWithoutDup(key,groupId);
                    }

                }
                SocketMsgVo msgVo = new SocketMsgVo();
                msgVo.setCode(SocketMsgTypeEnum.BIND_USER);
                msgVo.setSender("");
                msgVo.setReceiver("");
                SocketTeamBindVo socketTeamBindVo  = new SocketTeamBindVo();
                socketTeamBindVo.setTeamId(groupId);
                socketTeamBindVo.setUserList(msgUserList);
                SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
                detailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);
                detailVo.setData(socketTeamBindVo);
                msgVo.setMsg(detailVo);
                //校验消息
                CheckSocketMsgVo cRes = Common.checkSocketMsg(msgVo);
                //只有消息合法才去绑定socket通信频道
                if(cRes.getRes()){
                    rabbitMqMsgProducer.sendSocketTeamBindMsg(msgVo);
                }
                //记录群状态变动begin
                if(!userNames.equals("")){
                    userNames = userNames.substring(1);
                    userIds = userIds.substring(1);
                }
                ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
                zzGroupStatus.setId(RandomId.getUUID());
                zzGroupStatus.setOperatorName(userName);
                zzGroupStatus.setOperator(userId);
                zzGroupStatus.setOperateTime(new Date());
                zzGroupStatus.setOperateType(MessageType.FLOW_ADD_MEMBER);//添加人员
                zzGroupStatus.setGroupId(zzGroup.getGroupId());
                String describe = zzGroupStatus.getOperatorName()+
                        "邀请以下人员加入群："+userNames+"；人员id："+userIds;
                zzGroupStatus.setDescribe(describe);
                //zzGroupStatusService.add(zzGroupStatus);
                log.info("发出群变更消息："+ JSONObject.toJSONString(zzGroupStatus));
                rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
                //记录群状态变动end
            }


            //如果有删除人员发送消息
            List msgUserList2 = new ArrayList();
            if(delUserList!=null && delUserList.size()!=0){
                removeUserInfoList = iUserService.userList(Joiner.on(",").join(delUserList));
                String userNames = "";
                String userIds = "";
                for (ChatAdminUserVo userInfo:removeUserInfoList){
                    msgUserList2.add(userInfo.getId());
                    userNames += ","+userInfo.getName();
                    userIds += ","+userInfo.getId();
                    //redis 缓存处理 把用户的群列表缓存更新
                    String key = CacheConst.userGroupIds+":"+userInfo.getId();
                    boolean keyExist = RedisUtil.isKeyExist(key);
                    //如果key存在更新缓存，把最新的数据加入进去
                    if(keyExist){
                        RedisListUtil.removeSingle(key,groupId);
                    }

                }
                SocketMsgVo msgVo2 = new SocketMsgVo();
                msgVo2.setCode(SocketMsgTypeEnum.UNBIND_USER);
                msgVo2.setSender("");
                msgVo2.setReceiver("");
                SocketTeamBindVo socketTeamBindVo2  = new SocketTeamBindVo();
                socketTeamBindVo2.setTeamId(groupId);
                socketTeamBindVo2.setUserList(msgUserList2);
                SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
                detailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);
                detailVo.setData(socketTeamBindVo2);
                msgVo2.setMsg(detailVo);
                //校验消息
                CheckSocketMsgVo cRes = Common.checkSocketMsg(msgVo2);
                //只有消息合法才去绑定socket通信频道
                if(cRes.getRes()){
                    rabbitMqMsgProducer.sendSocketTeamUnBindMsg(msgVo2);
                }
                //记录群状态变动begin
                if(!userNames.equals("")){
                    userNames = userNames.substring(1);
                    userIds = userIds.substring(1);
                }
                ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
                zzGroupStatus.setId(RandomId.getUUID());
                zzGroupStatus.setOperatorName(userName);
                zzGroupStatus.setOperator(userId);
                zzGroupStatus.setOperateType(MessageType.FLOW_DELETE_MEMBER);//踢出人员
                zzGroupStatus.setGroupId(zzGroup.getGroupId());
                String describe = zzGroupStatus.getOperatorName()+
                        "从群里删除以下人员："+userNames+"；人员id："+userIds;
                zzGroupStatus.setDescribe(describe);
                zzGroupStatus.setOperateTime(new Date());
                //zzGroupStatusService.add(zzGroupStatus);
                log.info("发出群变更消息："+ JSONObject.toJSONString(zzGroupStatus));
                rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
                //记录群状态变动end
            }
        } catch (Exception e) {
            log.error("编辑群组人员出错！！！");
            log.error(Common.getExceptionMessage(e));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if(addUserInfoList!=null){
                for (ChatAdminUserVo userInfo:addUserInfoList){
                    //redis 缓存处理 把用户脏数据删除
                    String key = CacheConst.userGroupIds+":"+userInfo.getId();
                    boolean keyExist = RedisUtil.isKeyExist(key);
                    if(keyExist){
                        RedisUtil.remove(key);
                    }

                }
            }
            if(removeUserInfoList!=null){

            }
            for (ChatAdminUserVo userInfo:removeUserInfoList){
                //redis 缓存处理 把脏数据删除
                String key = CacheConst.userGroupIds+":"+userInfo.getId();
                boolean keyExist = RedisUtil.isKeyExist(key);
                //如果key存在更新缓存，把最新的数据加入进去
                if(keyExist){
                    RedisUtil.remove(key);
                }

            }
            return  -1;
        }
        return 1;
    }
    /**
     * 创建群
     * @param groupJson
     * @return
     */
    @Override
    @Transactional
    public ObjectRestResponse createGroup(String groupJson){
        ObjectRestResponse res = new ObjectRestResponse();
        res.rel(true);
        res.msg("200");
        ZzGroup zzGroupInfo = new ZzGroup();
        JSONArray userJsonArray = null;
        try {
            /**返回结果*/
            res.data(zzGroupInfo.getGroupId());
            JSONObject jsonObject = JSONObject.parseObject(groupJson);
            userJsonArray = JSONObject.parseArray(jsonObject.getString("userList"));
            //判断是否超过上限begin
            //秘密限制100以内，机密限制50以内
            if((userJsonArray.size()>100 && jsonObject.getString("levels").equals(NORMAL_SECRECT_LEVEL)) ||
                    (userJsonArray.size()>50 && jsonObject.getString("levels").equals(HIGH_SECRECT_LEVEL))
            ){
                res.rel(false);
                res.data("人员超限");
                return  res;
            }
            //判断是超过上限end
            zzGroupInfo.setGroupId(jsonObject.getString("groupId"));
            zzGroupInfo.setGroupName(jsonObject.getString("groupName"));
            zzGroupInfo.setCreator(jsonObject.getString("creator"));
            zzGroupInfo.setGroupOwnerId(jsonObject.getString("creator"));
            ChatAdminUserVo userInfo = iUserService.getUserInfo(jsonObject.getString("creator"));
            if(userInfo!=null){
                zzGroupInfo.setCreatorName(Common.nulToEmptyString(userInfo.getName()));
                zzGroupInfo.setGroupOwnerName(Common.nulToEmptyString(userInfo.getName()));
            }
            zzGroupInfo.setGroupDescribe(jsonObject.getString("groupDescribe"));
            zzGroupInfo.setUpdator(jsonObject.getString("updator"));
            zzGroupInfo.setPname(jsonObject.getString("pname"));
            zzGroupInfo.setScop(jsonObject.getString("scop"));
            zzGroupInfo.setLevels(jsonObject.getString("levels"));
            zzGroupInfo.setIsclose("0");
            zzGroupInfo.setIsdelete("0");
            zzGroupInfo.setIspublic("0");
            zzGroupInfo.setCreateTime(new Date());
            zzGroupInfo.setUpdateTime(new Date());
            //根据全部人员生成群组头像
            zzGroupInfo.setGroupImg(Common.imgUrl);
            // groupService.insert(zzGroupInfo);//创建讨论组
            zzGroupInfo.setIscross(Common.nulToEmptyString(jsonObject.getString("groupType")));

            this.insert(zzGroupInfo);//创建讨论组
            //创建群end

            List<UserListDto> userList = new ArrayList<UserListDto>();
            //遍历用户begin
            for (int i = 0; i < userJsonArray.size(); i++) {
                JSONObject userJson = JSONObject.parseObject(userJsonArray.getString(i));
                UserListDto userListDto = new UserListDto();
                userListDto.setUserId(userJson.getString("userId"));
                userListDto.setUserLevels(userJson.getString("userLevels"));
                userListDto.setImg(userJson.getString("img"));
                userList.add(userListDto);
                //加入关系表
                ZzUserGroup userGroup = new ZzUserGroup();
                userGroup.setCreatetime(new Date());
                userGroup.setId(getUUID());
                userGroup.setGroupId(zzGroupInfo.getGroupId());
                userGroup.setUserId(userListDto.getUserId());
                this.zzUserGroupService.insert(userGroup);
                //遍历用户end
                //返回创建结果
                //redis 缓存处理 把用户的群列表缓存更新
                try {
                    String key = CacheConst.userGroupIds+":"+userJson.getString("userId");
                    boolean keyExist = RedisUtil.isKeyExist(key);
                    //如果key存在更新缓存，把最新的数据加入进去
                    if(keyExist){
                        RedisListUtil.putSingleWithoutDup(key,zzGroupInfo.getGroupId());
                    }
                } catch (Exception e) {
                    log.error("创建群redis报错！！！");
                    log.error(Common.getExceptionMessage(e));
                }
            }
            // TODO: 2019/6/3 群头像生成

        }catch (Exception e){
            log.error("创建研讨群组出错");
            log.error(Common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
            /*//手动回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            for (int i = 0; i < userJsonArray.size(); i++) {
            JSONObject userJson = JSONObject.parseObject(userJsonArray.getString(i));
            String key = CacheConst.userGroupIds+":"+userJson.getString("userId");
            boolean keyExist = RedisUtil.isKeyExist(key);
            //如果key存在更新缓存，把脏数据删除
            if(keyExist){
                RedisUtil.remove(key);
            }

            }
        }
        return res;
    }
}