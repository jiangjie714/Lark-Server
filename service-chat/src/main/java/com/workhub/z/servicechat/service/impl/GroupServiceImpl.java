package com.workhub.z.servicechat.service.impl;

import com.workhub.z.servicechat.service.GroupService;
import org.springframework.stereotype.Service;

@Service("groupService")
public class GroupServiceImpl implements GroupService {

   /* @Autowired
    IUserService iUserService;

    @Autowired
    ZzGroupService zzGroupService;

    @Autowired
    ZzUserGroupService zzUserGroupService;

    @Autowired
    ZzUserGroupDao zzUserGroupDao;

    *//**
     * 群组查询
     * @param groupId
     * @return
     * @throws NullPointerException
     * @throws RuntimeException
     *//*
    private ZzGroup getZzGroup(String groupId) throws NullPointerException,RuntimeException{
        if (StringUtil.isEmpty(groupId)) {
            throw new NullPointerException("groupId is null");
        }
        ZzGroup zzGroup = this.zzGroupService.queryById(groupId);
        if (null == zzGroup) {
            throw new RuntimeException("未查询到相关群组");
        }
        return zzGroup;
    }

    *//**
     * 创建群组
     * @param groupTaskDto
     * @return
     * @throws RuntimeException
     *//*

    @Override
    public boolean createGroup(GroupTaskDto<ZzGroup> groupTaskDto) throws Exception {
        if (null == groupTaskDto) {
            throw new NullPointerException("GroupTaskDto is null");
        }
        ZzGroup zzGroup = groupTaskDto.getZzGroup();
        if (null == zzGroup) {
            throw new NullPointerException("zzGroup is null");
        }
        UserInfo info = this.iUserService.info(groupTaskDto.getGroupId());
        if (null == info) {
            throw new RuntimeException("info is null");
        }
        *//*TODO 属性暂时未加 *//*
        if (Integer.parseInt(zzGroup.getLevels())<1) {
            throw new RuntimeException("群创建等级必须小于或者等于创建人等级");
        }
        putEntityNullToEmptyString(zzGroup);
        this.zzGroupService.insert(zzGroup);
//        if (insert == 0) throw  new RuntimeException("创群失败！");
        List<UserListDto> userList = groupTaskDto.getUserList();
        if (null == userList ||userList.isEmpty()) {
            throw new NullPointerException("userList is null");
        }
        Date date = new Date();
        userList.stream().filter(userListFilter -> Integer.parseInt(userListFilter.getUserLevels())>= Integer.parseInt(zzGroup.getLevels())).forEach(userListCreate ->{
            ZzUserGroup zzUserGroup = new ZzUserGroup();
            zzUserGroup.setId(RandomId.getUUID());
            zzUserGroup.setGroupId(zzGroup.getGroupId());
            String userId = userListCreate.getUserId();
            if (StringUtil.isEmpty(userId)) {
                throw new NullPointerException("userId is null");
            }
            zzUserGroup.setUserId(userId);
            zzUserGroup.setCreatetime(date);
            try {
                putEntityNullToEmptyString(zzUserGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
            zzUserGroupService.insert(zzUserGroup);
//            if (count == 0)new Error(JSONObject.toJSON(zzUserGroup)+"入群失败");
        });
        return true;
    }

    *//**
     * 加入群组
     * @param groupTaskDto
     * @return
     * @throws RuntimeException
     *//*
    @Override
    public boolean addGroup(GroupTaskDto groupTaskDto) throws RuntimeException {
        if (null == groupTaskDto) {
            throw new NullPointerException("GroupTaskDto is null");
        }
        ZzGroup zzGroup = this.getZzGroup(groupTaskDto.getGroupId());
        int levels = Integer.parseInt(zzGroup.getLevels());//群等级
        List<UserListDto> userList = groupTaskDto.getUserList();
        if (null == userList ||userList.isEmpty()) {
            throw new NullPointerException("userList is null");
        }
        List<UserListDto> collect = userList.stream().filter(userListFilter -> Integer.parseInt(userListFilter.getUserLevels()) < levels).collect(Collectors.toList());
        if (null != collect && !collect.isEmpty()) {
            throw new RuntimeException(JSONObject.toJSON(collect)+"该相关人员levels不满足进入条件");
        }
        Date date = new Date();
        userList.stream().filter(userListFilter -> Integer.parseInt(userListFilter.getUserLevels())>= levels).forEach(userListCreate ->{
            ZzUserGroup zzUserGroup = new ZzUserGroup();
            zzUserGroup.setId(RandomId.getUUID());
            zzUserGroup.setGroupId(zzGroup.getGroupId());
            String userId = userListCreate.getUserId();
            if (StringUtil.isEmpty(userId)) {
                throw new NullPointerException("userId is null");
            }
            zzUserGroup.setUserId(userId);
            zzUserGroup.setCreatetime(date);
            try {
                putEntityNullToEmptyString(zzUserGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
            zzUserGroupService.insert(zzUserGroup);
//            if (insert == 0)new Error(JSONObject.toJSON(zzUserGroup)+"入群失败");
        });
        return true;
    }

    *//**
     * 退出群组
     * @param groupTaskDto
     * @return
     * @throws RuntimeException
     *//*
    @Override
    public boolean escGroup(GroupTaskDto groupTaskDto) throws RuntimeException {
        if (null == groupTaskDto) {
            throw new RuntimeException("GroupTaskDto is null");
        }
        String groupId = groupTaskDto.getGroupId();
        ZzGroup zzGroup = this.zzGroupService.queryById(groupId);
        String id = groupTaskDto.getReviser();
        if (id != zzGroup.getCreator()) {
            throw new RuntimeException("创建人无法退群");
        }
        Long aLong = this.zzUserGroupDao.deleteByGroupIdAndUserId(groupId, id);
        if (aLong == 0) {
            return false;
        }
        return true;
    }

    *//**
     * 踢出群组
     * @param groupTaskDto
     * @return
     * @throws RuntimeException
     *//*
    @Override
    public boolean removeGroup(GroupTaskDto groupTaskDto) throws RuntimeException {
        if (null == groupTaskDto) {
            throw new RuntimeException("GroupTaskDto is null");
        }
        ZzGroup zzGroup = this.getZzGroup(groupTaskDto.getGroupId());
        if (groupTaskDto.getReviser() != zzGroup.getCreator()) {
            throw new RuntimeException("非创建人无法踢出群员");
        }
        List<UserListDto> userList = groupTaskDto.getUserList();
        if (null == userList ||userList.isEmpty()) {
            throw new NullPointerException("userList is null");
        }
        userList.stream().forEach(userListfor ->{
            Long count = this.zzUserGroupDao.deleteByGroupIdAndUserId(zzGroup.getGroupId(), userListfor.getUserId());
            if (count == 0) {
                new Error(JSONObject.toJSON(userListfor.getUserId())+"踢人失败");
            }
        });
        return true;
    }

    *//**
     * 关闭群组
     * @param groupTaskDto
     * @return
     * @throws RuntimeException
     *//*
    @Override
    public boolean closeGroup(GroupTaskDto groupTaskDto) throws RuntimeException {
        if (null == groupTaskDto) {
            throw new RuntimeException("GroupTaskDto is null");
        }
        ZzGroup zzGroup = this.zzGroupService.queryById(groupTaskDto.getGroupId());
        String id = groupTaskDto.getReviser();
        if (id != zzGroup.getCreator()) {
            throw new RuntimeException("非创建人无法关闭");
        }
        zzGroup.setIsclose("1");//TODO 目前默认1关闭
        zzGroup.setUpdateTime(new Date());
        zzGroup.setUpdator(id);
        return true;
    }

    *//**
     * 删除群组
     * @param groupTaskDto
     * @return
     * @throws RuntimeException
     *//*
    @Override
    @Transactional
    public boolean delGruop(GroupTaskDto groupTaskDto) throws RuntimeException {
        if (null == groupTaskDto) {
            throw new RuntimeException("GroupTaskDto is null");
        }
        ZzGroup zzGroup = this.getZzGroup(groupTaskDto.getGroupId());
        String id = groupTaskDto.getReviser();
        if (id != zzGroup.getCreator()) {
            throw new RuntimeException("非创建人无法删除群");
        }
        zzGroup.setIsdelete("1");//TODO 暂时默认1删除
        zzGroup.setUpdator(id);
        zzGroup.setUpdateTime(new Date());
        Integer update = zzGroupService.update(zzGroup);
        if (update == 0) {
            throw new RuntimeException("删除群组表失败！");
        }
        List<UserListDto> userList = groupTaskDto.getUserList();
        if (null == userList ||userList.isEmpty()) {
            throw new NullPointerException("userList is null");
        }
        userList.stream().forEach(userListfor ->{//TODO 批量删除优化?
            Long count = this.zzUserGroupDao.deleteByGroupIdAndUserId(zzGroup.getGroupId(), userListfor.getUserId());
            if (count == 0) {
                throw new RuntimeException("删除群组群员记录失败！");
            }
        });
        return true;
    }*/
}
