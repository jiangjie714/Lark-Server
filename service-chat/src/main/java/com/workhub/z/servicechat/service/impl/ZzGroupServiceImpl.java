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
        addVo.setCode(SocketMsgDetailTypeEnum.GROUP_EDIT);
        List<String> userList = zzGroupDao.queryGroupUserIdListByGroupId(zzGroup.getGroupId());
        int groupEditType = 5;
        GroupTaskDto addDto = getSendSocketGroupInf(zzGroup.getGroupId(),userList,groupEditType);
        addVo.setData(addDto);
        socketMsgVo.setMsg(addVo);
        rabbitMqMsgProducer.sendSocketMsg(socketMsgVo);
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

    /**
     * 群解散
     * @param groupId
     * @param userId
     * @param userName
     * @return 1成功0校验失败，只有群主可以解散群
     */
    @Override
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public int dissolveGroup(String groupId,String userId,String userName){
        int success = 1,validateError = 0;
        int validateSuccss = 1;
        if(validDissolveGroup(groupId,userId)!=validateSuccss){
            return validateError;
        }
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
        zzGroupStatus.setOperateType(MessageType.FLOW_DISSOLUTION);
        zzGroupStatus.setGroupId(groupId);
        zzGroupStatus.setOperateTime(new Date());
        zzGroupStatus.setDescribe(zzGroupStatus.getOperatorName()+ "解散了群");
        //zzGroupStatusService.add(zzGroupStatus);
        rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
        //发送解绑消息
        SocketMsgVo unBindVo = new SocketMsgVo();
        unBindVo.setCode(SocketMsgTypeEnum.UNBIND_USER);
        SocketMsgDetailVo unBindDetailVo = new SocketMsgDetailVo();
        unBindDetailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);
        SocketTeamBindVo teamAndUsersInf =  new SocketTeamBindVo();
        teamAndUsersInf.setTeamId(groupId);
        teamAndUsersInf.setUserList(userIds);
        //解绑后给每个人发消息
        SocketMsgDetailVo afterUnBindDetailVo = new SocketMsgDetailVo();
        afterUnBindDetailVo.setCode(SocketMsgDetailTypeEnum.GROUP_DISSOVLE);
        SocketGroupDissolveVo socketGroupDissolveVo = new SocketGroupDissolveVo();
        socketGroupDissolveVo.setGroupId(groupId);
        afterUnBindDetailVo.setData(socketGroupDissolveVo);
        teamAndUsersInf.setMsg(afterUnBindDetailVo);
        //解散群，全量发送即可
        teamAndUsersInf.setWholeFlg(true);

        unBindDetailVo.setData(teamAndUsersInf);
        unBindVo.setMsg(unBindDetailVo);
        rabbitMqMsgProducer.sendSocketMsg(unBindVo);
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
        return success;
    }

    /**
     * 群解散校验
     * @param groupId
     * @param userId
     * @return
     */
    private int validDissolveGroup(String groupId,String userId){
        int success = 1,error = 0;
        ZzGroup zzGroup = this.zzGroupDao.queryById(groupId);
        if(zzGroup!=null && zzGroup.getGroupOwnerId().equals(userId)){
            return success;
        }
        return error;
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
    /**
     *
     * @param groupEditDto
     * @param userId
     * @param userName
     * @return 群编辑 1成功 -1失败 0群成员过多：秘密限制50以内，机密限制100以内,2 解散群成功,3 校验失败 当前人必须在群组内 4 校验失败 不能删除群主 5 解散群失败
     */
    @Override
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public int groupMemberEdit(GroupEditDto groupEditDto, String userId, String userName){
        int success = 1,error = -1,tooManyMembers = 0, successDissolve = 2,validErrorNotInGroup = 3,validErrorDelOwner = 4,errorDissolve = 5;
        int operatorNotInGroup = 0,cannotDelOwner = -1;
        List<GroupEditUserList> userListDtos = groupEditDto.getUserList();
        String groupId = groupEditDto.getGroupId();
        int valid = groupMemberEditValidate(groupId,userId,userListDtos);
        if(valid == operatorNotInGroup){
            return  validErrorNotInGroup;
        }else if(valid == cannotDelOwner){
            return validErrorDelOwner;
        }
        //如果只有群主自己了，解散群
        if(userListDtos.size()==1){
            int successDisslove = 1;
            int dissolveRes = dissolveGroup(groupId,userId,userName);
            if(successDisslove!=dissolveRes){
                return errorDissolve;
            }
            return successDissolve;
        }
        List<ChatAdminUserVo> addUserInfoList = null;
        List<ChatAdminUserVo> removeUserInfoList = null;
        try {
            ZzGroup zzGroupNow = zzGroupDao.queryById(groupId);
            if(zzGroupNow == null){
                return error;
            }
            //秘密限制100以内，机密限制50以内
            if((userListDtos.size()>100 && zzGroupNow.getLevels().equals(MessageType.NORMAL_SECRECT_LEVEL)) ||
                    (userListDtos.size()>50 && zzGroupNow.getLevels().equals(MessageType.HIGH_SECRECT_LEVEL))
            ){
                return tooManyMembers;
            }
            List<String> userList = zzGroupDao.queryGroupUserIdListByGroupId(groupId);
            List<String> nowUserList =  new ArrayList<>();
            for(GroupEditUserList nowUser :userListDtos){
                nowUserList.add(Common.nulToEmptyString(nowUser.getId()));
            }
            TeamMemberChangeListVo memberChangeListVo = Common.teamMemberChangeInf(userList,nowUserList);
            List<String> addUserList = memberChangeListVo.getAddList();
            List<String> delUserList = memberChangeListVo.getDelList();
            List<String> noMoveList = memberChangeListVo.getNoMoveList();
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
            if(delUserList!=null && delUserList.size()!=0){
                this.zzUserGroupDao.deleteByGroupIdAndUserIdList(groupId,delUserList) ;
            }
            ZzGroup zzGroup = zzGroupDao.queryById(groupId);
            //如果有新增人员 发送消息
            if(addUserList!=null  && !addUserList.isEmpty() ){
                addUserInfoList = iUserService.userList(Joiner.on(",").join(addUserList));
                String[] addCacheInfBack = dealRedisCacheAdd(groupId,addUserInfoList);
                String addIds = addCacheInfBack[0];
                String addNames = addCacheInfBack[1];
                //发送群绑定消息
                sendSocketMsgAdd(groupId,addUserList,nowUserList);
                //发送群流水消息
                sendGroupAddUserInf(groupId,userId,userName,addIds,addNames);
            }
            //如果有删除人员发送消息
            if(delUserList!=null && !delUserList.isEmpty()){
                removeUserInfoList = iUserService.userList(Joiner.on(",").join(delUserList));
                String[] delCacheInfBack = dealRedisCacheDelete(groupId,removeUserInfoList);
                String delIds = delCacheInfBack[0];
                String delNames = delCacheInfBack[1];
                //发送群绑定消息
                sendSocketMsgDelete(groupId,delUserList);
                //发送群流水消息
                sendGroupDeleteUserInf(groupId,userId,userName,delIds,delNames);
            }
            if((addUserList!=null  && !addUserList.isEmpty()) || (delUserList!=null && !delUserList.isEmpty())){
                //发送给未移动人员，通知群变动
                sendSocketMsgNoMove(groupId,noMoveList,nowUserList);
            }
        } catch (Exception e) {
            log.error("编辑群组人员出错！！！");
            log.error(Common.getExceptionMessage(e));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if(addUserInfoList!=null){
                deleteDirtyCacheIfError(addUserInfoList);
            }
            if(removeUserInfoList!=null){
                deleteDirtyCacheIfError(removeUserInfoList);
            }
            return  error;
        }
        return success;
    }
    /**
     * 给未移动的人员发送群变更消息
     * @param groupId
     * @param users
     * @param nowUserList
     */
    private void sendSocketMsgNoMove(String groupId,List<String> users,List<String> nowUserList){
        //因为要区分出新加人员、未移动人员的消息，避免重复收到消息，这里不能使用群体消息,需要变量未移动人员，发单条消息
        SocketMsgVo groupEditSocketVo = new SocketMsgVo();
        groupEditSocketVo.setCode(SocketMsgTypeEnum.SINGLE_MSG);
        SocketMsgDetailVo groupDetailVo = new SocketMsgDetailVo();
        groupDetailVo.setCode(SocketMsgDetailTypeEnum.GROUP_EDIT);
        int groupEditType = 5;
        GroupTaskDto groupTaskDto = getSendSocketGroupInf(groupId,nowUserList,groupEditType);
        groupDetailVo.setData(groupTaskDto);
        groupEditSocketVo.setMsg(groupDetailVo);

        for(String user:users){
            groupEditSocketVo.setReceiver(user);
            rabbitMqMsgProducer.sendSocketMsg(groupEditSocketVo);
        }

    }
    /**
     * 添加群人员处理缓存
     * @param groupId
     * @param addUserInfoList
     * @param addUserList
     * @return
     */
    private String[] dealRedisCacheAdd(String groupId,List<ChatAdminUserVo> addUserInfoList){
            String userNames = "";
            String userIds = "";
            for (ChatAdminUserVo userInfo:addUserInfoList){
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
            if(!userNames.equals("")){
                userNames = userNames.substring(1);
                userIds = userIds.substring(1);
            }
            return  new String[]{userIds,userNames};
    }

    /**
     * 添加群成员给添加群成员的人发送消息
     * @param groupId
     * @param users
     */
    private void sendSocketMsgAdd(String groupId,List<String> users,List<String> nowUserList){
        SocketMsgVo msgVo = new SocketMsgVo();
        msgVo.setCode(SocketMsgTypeEnum.BIND_USER);
        msgVo.setSender("");
        msgVo.setReceiver("");
        SocketTeamBindVo socketTeamBindVo  = new SocketTeamBindVo();
        socketTeamBindVo.setTeamId(groupId);
        socketTeamBindVo.setUserList(users);
        SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
        detailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);

        //绑定完成后发送新建群的消息给新加入的人
        SocketMsgDetailVo groupDetailVo = new SocketMsgDetailVo();
        groupDetailVo.setCode(SocketMsgDetailTypeEnum.GROUP_CREATE);
        int groupCreateType = 0;
        GroupTaskDto groupTaskDto = getSendSocketGroupInf(groupId,nowUserList,groupCreateType);
        groupDetailVo.setData(groupTaskDto);
        socketTeamBindVo.setMsg(groupDetailVo);
        //需要增量发送，因为群里有些人是不动的，不能都给他们发群创建消息
        socketTeamBindVo.setWholeFlg(false);

        detailVo.setData(socketTeamBindVo);
        msgVo.setMsg(detailVo);
        rabbitMqMsgProducer.sendSocketMsg(msgVo);
    }

    /**
     * 群编辑成员，增加时候，记录群流水
     * @param groupId
     * @param operator
     * @param operatorName
     * @param addIs
     * @param addNames
     */
    private void sendGroupAddUserInf(String groupId,String operator,String operatorName,String addIs,String addNames){
        ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
        zzGroupStatus.setId(RandomId.getUUID());
        zzGroupStatus.setOperatorName(operatorName);
        zzGroupStatus.setOperator(operator);
        zzGroupStatus.setOperateTime(new Date());
        zzGroupStatus.setOperateType(MessageType.FLOW_ADD_MEMBER);//添加人员
        zzGroupStatus.setGroupId(groupId);
        String describe = zzGroupStatus.getOperatorName()+
                "邀请以下人员加入群："+addNames+"；人员id："+addIs;
        zzGroupStatus.setDescribe(describe);
        rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
    }
    /**
     * 删除群人员处理缓存
     * @param groupId
     * @param delUserInfoList
     * @return
     */
    private String[] dealRedisCacheDelete(String groupId,List<ChatAdminUserVo> delUserInfoList){
        String userNames = "";
        String userIds = "";
        for (ChatAdminUserVo userInfo:delUserInfoList){
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
        if(!userNames.equals("")){
            userNames = userNames.substring(1);
            userIds = userIds.substring(1);
        }
        return  new String[]{userIds,userNames};
    }

    /**
     * 删除群成员发送消息
     * @param groupId
     * @param users
     */
    private void sendSocketMsgDelete(String groupId,List<String> users){
        SocketMsgVo msgVo2 = new SocketMsgVo();
        msgVo2.setCode(SocketMsgTypeEnum.UNBIND_USER);
        SocketMsgDetailVo detailVo = new SocketMsgDetailVo();
        detailVo.setCode(SocketMsgDetailTypeEnum.DEFAULT);
        SocketTeamBindVo socketTeamBindVo2  = new SocketTeamBindVo();
        socketTeamBindVo2.setTeamId(groupId);
        socketTeamBindVo2.setUserList(users);
        //给每一个被踢出的人发消息，告知被踢出了
        SocketMsgDetailVo outDetailSocketVo = new SocketMsgDetailVo();
        outDetailSocketVo.setCode(SocketMsgDetailTypeEnum.GROUP_QUIT);
        SocketGroupDeleteMemberVo socketGroupDeleteMemberVo = new SocketGroupDeleteMemberVo();
        socketGroupDeleteMemberVo.setGroupId(groupId);
        outDetailSocketVo.setData(socketGroupDeleteMemberVo);
        socketTeamBindVo2.setMsg(outDetailSocketVo);
        //增量发送，因为踢出人员，还有其他人员在群里，不可能发送全量告诉所有人被t了
        socketTeamBindVo2.setWholeFlg(false);
        detailVo.setData(socketTeamBindVo2);
        msgVo2.setMsg(detailVo);
        rabbitMqMsgProducer.sendSocketMsg(msgVo2);
    }

    /**
     * 群编辑成员，删除时候，记录群流水
     * @param groupId
     * @param operator
     * @param operatorName
     * @param delIs
     * @param delNames
     */
    private void sendGroupDeleteUserInf(String groupId,String operator,String operatorName,String delIs,String delNames){
        ZzGroupStatus zzGroupStatus = new ZzGroupStatus();
        zzGroupStatus.setId(RandomId.getUUID());
        zzGroupStatus.setOperatorName(operatorName);
        zzGroupStatus.setOperator(operator);
        zzGroupStatus.setOperateType(MessageType.FLOW_DELETE_MEMBER);
        zzGroupStatus.setGroupId(groupId);
        String describe = zzGroupStatus.getOperatorName()+
                "从群里删除以下人员："+delNames+"；人员id："+delIs;
        zzGroupStatus.setDescribe(describe);
        zzGroupStatus.setOperateTime(new Date());
        rabbitMqMsgProducer.sendMsgGroupChange(zzGroupStatus);
    }

    /**
     * 群编辑如果失败，删除可能的脏数据
     * @param userInfos
     */
    private void deleteDirtyCacheIfError(List<ChatAdminUserVo> userInfos){
        for (ChatAdminUserVo userInfo:userInfos){
            //redis 缓存处理 把用户脏数据删除
            String key = CacheConst.userGroupIds+":"+userInfo.getId();
            boolean keyExist = RedisUtil.isKeyExist(key);
            if(keyExist){
                RedisUtil.remove(key);
            }
        }
    }
    /**
     * 群编辑校验
     * @param groupId
     * @param userId
     * @param userListDtos
     * @return 1校验通过 0 操作人不在群组 -1不能删除群主
     */
    public int groupMemberEditValidate(String groupId,String userId,List<GroupEditUserList> userListDtos){
        int success = 1,operatorNotInGroup = 0,cannotDelOwner = -1;
        //邀请入群，本人必须在群组内
        List<String>  memberList = queryGroupUserIdListByGroupId(groupId);
        if(!memberList.contains(userId)){
            //操作失败，操作人不在群组内
            return operatorNotInGroup;
        }
        ZzGroup zzGroupNow = queryById(groupId);
        String groupOwner = zzGroupNow.getGroupOwnerId();
        boolean delGroupOwnerFlg = true;
        if(userListDtos!=null){
            for(GroupEditUserList nowUser :userListDtos){
                //不能删除群主
                if(nowUser.getId().equals(groupOwner)){
                    delGroupOwnerFlg =false;
                    break;
                }
            }
        }
        if(delGroupOwnerFlg){
            //操作失败，不能删除群主
            return cannotDelOwner;
        }
        return success;
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
            }
            // TODO: 2019/6/3 群头像生成

        }catch (Exception e){
            log.error("创建研讨群组出错");
            log.error(Common.getExceptionMessage(e));
            res.rel(false);
            res.data("系统出错");
            /*//手动回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return res;
    }

    /**
     * 获取群创建或者编辑信息给前端推送
     * @param groupId 群id
     * @param userList 用户列表
     * @param type 编辑类型 0 创建 1 编辑（该参数目前应该没用）
     * @return
     */
    @Override
    public GroupTaskDto  getSendSocketGroupInf(String groupId,List<String> userList,int type){

        GroupTaskDto groupTaskDto = new GroupTaskDto();
        ZzGroup zzGroupInfo = queryById(groupId);
        List<UserListDto> groupUserList = new ArrayList<UserListDto>();
        String ids = userList.stream().collect(Collectors.joining(","));
        List<ChatAdminUserVo> userInfoList = iUserService.userList(ids);
        for(ChatAdminUserVo userVo : userInfoList){
            UserListDto user = new UserListDto();
            user.setImg(userVo.getAvatar());
            user.setUserId(userVo.getId());
            user.setUserLevels(userVo.getSecretLevel());
            groupUserList.add(user);
        }
        groupTaskDto.setType(type);
        groupTaskDto.setGroupId(groupId);
        groupTaskDto.setUserList(groupUserList);
        groupTaskDto.setTimestamp(zzGroupInfo.getCreateTime());
        groupTaskDto.setReviser(zzGroupInfo.getCreator());
        groupTaskDto.setZzGroup(zzGroupInfo);
        return  groupTaskDto;
    }
}