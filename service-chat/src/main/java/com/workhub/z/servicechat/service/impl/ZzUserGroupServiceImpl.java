package com.workhub.z.servicechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.common.vo.rpcvo.ContactVO;
import com.github.hollykunge.security.common.vo.rpcvo.MessageContent;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.ChatAdminUserVo;
import com.workhub.z.servicechat.VO.GroupListVo;
import com.workhub.z.servicechat.VO.NoReadVo;
import com.workhub.z.servicechat.VO.UserNewMsgVo;
import com.workhub.z.servicechat.config.CacheConst;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.dao.ZzUserGroupDao;
import com.workhub.z.servicechat.entity.group.ZzGroup;
import com.workhub.z.servicechat.entity.group.ZzUserGroup;
import com.workhub.z.servicechat.model.RawMessageDto;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.redis.RedisListUtil;
import com.workhub.z.servicechat.redis.RedisUtil;
import com.workhub.z.servicechat.service.AdminUserService;
import com.workhub.z.servicechat.service.ZzGroupService;
import com.workhub.z.servicechat.service.ZzMsgReadRelationService;
import com.workhub.z.servicechat.service.ZzUserGroupService;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.workhub.z.servicechat.config.Common.putEntityNullToEmptyString;

//import com.workhub.z.servicechat.VO.ContactVO;

/**
 * 用户群组映射表(ZzUserGroup)表服务实现类
 *
 * @author 忠
 * @since 2019-05-10 14:22:54
 */
@Service("zzUserGroupService")
public class ZzUserGroupServiceImpl implements ZzUserGroupService {
    private static Logger log = LoggerFactory.getLogger(ZzUserGroupServiceImpl.class);
    @Resource
    private ZzUserGroupDao zzUserGroupDao;

    @Autowired
    private ZzMsgReadRelationService zzMsgReadRelationService;

    @Autowired
    private ZzGroupService zzGroupService;

    @Autowired
    private AdminUserService iUserService;
    @Autowired
    private RabbitMqMsgProducer rabbitMqMsgProducer;
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ZzUserGroup queryById(String id) {
        ZzUserGroup zzUserGroup = this.zzUserGroupDao.queryById(id);
        try {
            Common.putVoNullStringToEmptyString(zzUserGroup);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
        }

        return zzUserGroup;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ZzUserGroup> queryAllByLimit(int offset, int limit) {
        return this.zzUserGroupDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param zzUserGroup 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public void insert(ZzUserGroup zzUserGroup) {
        try {
            putEntityNullToEmptyString(zzUserGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int insert = this.zzUserGroupDao.insert(zzUserGroup);
//        return insert;
    }

   /* @Override
    protected String getPageName() {
        return null;
    }
*/
    /**
     * 修改数据
     *
     * @param zzUserGroup 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public Integer update(ZzUserGroup zzUserGroup) {
        int update = this.zzUserGroupDao.update(zzUserGroup);
        return update;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteById(String id) {
        return this.zzUserGroupDao.deleteById(id) > 0;
    }

    @Override
    public PageInfo<GroupListVo> groupUserList(String id, int page, int size) throws Exception {
        if (StringUtil.isEmpty(id)) {
            throw new NullPointerException("id is null");
        }
        //以前写的分页暂时注释，应该有查询冗余问题
        /*Page<Object> pageMassage = PageHelper.startPage(page, size);
        pageMassage.setTotal(this.zzUserGroupDao.groupListTotal(id));
        int startRow = pageMassage.getStartRow();
        int endRow = pageMassage.getEndRow();
        PageInfo<GroupListVo> pageInfoGroupInfo = new PageInfo<GroupListVo>();
        pageInfoGroupInfo.setList(this.zzUserGroupDao.groupList(id,startRow,endRow));
        pageInfoGroupInfo.setTotal(pageMassage.getTotal());
        pageInfoGroupInfo.setStartRow(startRow);
        pageInfoGroupInfo.setEndRow(endRow);
        pageInfoGroupInfo.setPages(pageMassage.getPages());
        pageInfoGroupInfo.setPageNum(page);
        pageInfoGroupInfo.setPageSize(size);
        return pageInfoGroupInfo;*/

        //新写查询分页
        PageHelper.startPage(page, size);
        List<GroupListVo> list = this.zzUserGroupDao.groupList(id);
        Common.putVoNullStringToEmptyString(list);
        PageInfo<GroupListVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public Long groupUserListTotal(String id) throws Exception {
        return this.zzUserGroupDao.groupListTotal(id);
    }

    @Override
    public List<UserNewMsgVo> getUserNewMsgList2(String id) {
        List<UserNewMsgVo> list=this.zzUserGroupDao.getUserNewMsgList2(id);
        try {
            Common.putVoNullStringToEmptyString(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
        }
        return list;
    }
    @Override
    public List<RawMessageDto> getUserNewMsgList(String id) {
        List<RawMessageDto> list=this.zzUserGroupDao.getUserNewMsgList(id);
        try {
            Common.putVoNullStringToEmptyString(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
        }
        return list;
    }

    @Override
    public List<ContactVO> getContactVOList2(String id) {
        List<UserNewMsgVo> userNewMsgList = this.getUserNewMsgList2(id);
        // TODO: 2019/6/12 是否@
        // TODO: 2019/6/12 私有化定制
        List<ContactVO> list = new ArrayList<ContactVO>();
        //mq添加消息发送 开发测试用begin
        /*try {
            ContactVO vo=new ContactVO();
            vo.setUnreadNum(1);
            vo.setAtMe(true);
            vo.setAvatar("1111");
            vo.setId("223323");
            MessageContent mes = new MessageContent();
            mes.setExtension("123");
            mes.setType(0);
            mes.setId("11111");
            mes.setSecretLevel(40);
            mes.setTitle("ceshi");
            mes.setUrl("www.baidu.com");
            vo.setLastMessage(mes);
            list.add(vo);

            Map<String,List<ContactVO>> data=new HashMap<>();
            data.put(id,list);//当前登录人的id作为key，联系人列表作为value
            rabbitMqMsgProducer.sendMsg(data);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        //mq添加消息发送 开发测试用end
        List<NoReadVo> noReadVos = zzMsgReadRelationService.queryNoReadCountList(id);
        if(userNewMsgList == null|| userNewMsgList.isEmpty()){ return list;}
        userNewMsgList.stream().forEach(n ->{
            ContactVO contactVO = new ContactVO();
            if ("GROUP".equals(n.getTableType())) {
                ZzGroup group = new ZzGroup();
                group = zzGroupService.queryById(n.getMsgSener());
                contactVO.setId(n.getMsgSener());
                ChatAdminUserVo userInfo = iUserService.getUserInfo(n.getMsgReceiver());
//                JSON.toJavaObject(JSON.parseObject(n.getMsg()), MessageContent.class);
//                MessageContent testProcessInfo = (MessageContent)JSONObject.toBean(n.getMsg(), MessageContent.class);
                contactVO.setLastMessage(JSON.toJavaObject(JSON.parseObject(n.getMsg()), MessageContent.class));
                contactVO.setFullTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(n.getSendTime()==null?(new Date()):n.getSendTime()));
                if(new SimpleDateFormat("yyyy-MM-dd").format(n.getSendTime()).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){//格式化为相同格式
                    contactVO.setTime(new SimpleDateFormat("HH:mm").format(n.getSendTime()));
                }else {
                    contactVO.setTime(new SimpleDateFormat("MM-dd").format(n.getSendTime()));
                }
                contactVO.setAvatar(group.getGroupImg());
                contactVO.setName(group.getGroupName());
                contactVO.setSender(userInfo.getName());
                contactVO.setAtMe(false);
                contactVO.setIsTop(false);
                contactVO.setIsMute(false);
//               群组密级
                contactVO.setSecretLevel(Integer.parseInt(group.getLevels()));
                contactVO.setGroupOwnerId(Common.nulToEmptyString(group.getGroupOwnerId()));
                contactVO.setGroupOwnerName(Common.nulToEmptyString(group.getGroupOwnerName()));
                try {
                    contactVO.setMemberNum(Math.toIntExact(this.zzGroupService.groupUserListTotal(group.getGroupId())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                contactVO.setIsGroup(n.getTableType().equals("GROUP"));
                contactVO.setUnreadNum(zzMsgReadRelationService.queryNoReadMsgBySenderAndReceiver(group.getGroupId(),id));
            } else if ("USER".equals(n.getTableType())) {
                ChatAdminUserVo userInfo = iUserService.getUserInfo(n.getMsgSener());
                contactVO.setId(n.getMsgSener());
                contactVO.setLastMessage(JSON.toJavaObject(JSON.parseObject(n.getMsg()), MessageContent.class));
                contactVO.setFullTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(n.getSendTime()==null?(new Date()):n.getSendTime()));
                if(new SimpleDateFormat("yyyy-MM-dd").format(n.getSendTime()).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){//格式化为相同格式
                    contactVO.setTime(new SimpleDateFormat("HH:mm").format(n.getSendTime()));
                }else {
                    contactVO.setTime(new SimpleDateFormat("MM-dd").format(n.getSendTime()));
                }
                contactVO.setAvatar(userInfo.getAvatar());
                contactVO.setName(userInfo.getName());
                contactVO.setSender("");
                contactVO.setAtMe(false);
                contactVO.setIsTop(false);
                contactVO.setIsMute(false);
                contactVO.setIsGroup(n.getTableType().equals("GROUP"));
                contactVO.setSecretLevel(Integer.parseInt(userInfo.getSecretLevel()));
                contactVO.setUnreadNum(zzMsgReadRelationService.queryNoReadMsgBySenderAndReceiver(n.getMsgSener(),n.getMsgReceiver()));
            }
//            for (int j = 0; j < noReadVos.size(); j++) {
//                if (noReadVos.get(j).getSender() == n.getMsgSener()){
//                    contactVO.setUnreadNum(noReadVos.get(j).getMsgCount());
//                }
//            }
//            if (noReadVos == null|| noReadVos.isEmpty()) contactVO.setUnreadNum(0);
//            else {noReadVos.stream().forEach(m ->{
//                if (m.getSender() == userNewMsgList.get(i).getMsgSener()){
//                    contactVO.setUnreadNum(m.getMsgCount());
//                }
//            });
            list.add(contactVO);
        });
        Map<String,List<ContactVO>> data=new HashMap<>();
        try {
            Common.putVoNullStringToEmptyString(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
        }
        data.put(id,list);//当前登录人的id作为key，联系人列表作为value
        rabbitMqMsgProducer.sendMsg(data);
        return list;
    }
    @Override
    public List<ContactVO> getContactVOList(String id) {
        List<RawMessageDto> userNewMsgList = this.getUserNewMsgList(id);
        // TODO: 2019/6/12 是否@
        // TODO: 2019/6/12 私有化定制
        List<ContactVO> list = new ArrayList<ContactVO>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //mq添加消息发送 开发测试用begin
        /*try {
            ContactVO vo=new ContactVO();
            vo.setUnreadNum(1);
            vo.setAtMe(true);
            vo.setAvatar("1111");
            vo.setId("223323");
            MessageContent mes = new MessageContent();
            mes.setExtension("123");
            mes.setType(0);
            mes.setId("11111");
            mes.setSecretLevel(40);
            mes.setTitle("ceshi");
            mes.setUrl("www.baidu.com");
            vo.setLastMessage(mes);
            list.add(vo);

            Map<String,List<ContactVO>> data=new HashMap<>();
            data.put(id,list);//当前登录人的id作为key，联系人列表作为value
            rabbitMqMsgProducer.sendMsg(data);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        //mq添加消息发送 开发测试用end
        List<NoReadVo> noReadVos = zzMsgReadRelationService.queryNoReadCountList(id);
        if(userNewMsgList == null|| userNewMsgList.isEmpty()){ return list;}
        userNewMsgList.stream().forEach(n ->{
            ContactVO contactVO = new ContactVO();
            if ("GROUP".equals(n.getType())) {
                ZzGroup group = new ZzGroup();
                group = zzGroupService.queryById(n.getSenderid());
                contactVO.setId(n.getSenderid());
                MessageContent content = new MessageContent();
                content.setTitle(n.getMsg());
                content.setId(n.getFileid());
                content.setExtension(n.getFileext());
                content.setSecretLevel(Integer.parseInt(n.getLevels()));
                content.setType(Integer.parseInt(n.getFiletype()));
                contactVO.setLastMessage(content);
                contactVO.setFullTime(n.getCreatetime());

                Date sDate = null;
                try {
                     sDate = sf.parse(n.getCreatetime());
                } catch (ParseException e) {
                    log.error("消息日期转换错误");
                    log.error(Common.getExceptionMessage(e));
                    sDate = new Date();
                }
                if(new SimpleDateFormat("yyyy-MM-dd").format(sDate).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){//格式化为相同格式
                    contactVO.setTime(new SimpleDateFormat("HH:mm").format(sDate));
                }else {
                    contactVO.setTime(new SimpleDateFormat("MM-dd").format(sDate));
                }
                contactVO.setAvatar(group.getGroupImg());
                contactVO.setName(group.getGroupName());
                contactVO.setSender(n.getReceivername());
                contactVO.setAtMe(false);
                contactVO.setIsTop(false);
                contactVO.setIsMute(false);
//               群组密级
                contactVO.setSecretLevel(Integer.parseInt(group.getLevels()));
                contactVO.setGroupOwnerId(Common.nulToEmptyString(group.getGroupOwnerId()));
                contactVO.setGroupOwnerName(Common.nulToEmptyString(group.getGroupOwnerName()));
                try {
                    contactVO.setMemberNum(Math.toIntExact(this.zzGroupService.groupUserListTotal(group.getGroupId())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                contactVO.setIsGroup(n.getType().equals("GROUP"));
                contactVO.setUnreadNum(zzMsgReadRelationService.queryNoReadMsgBySenderAndReceiver(group.getGroupId(),id));
            } else if ("USER".equals(n.getType())) {
                contactVO.setId(n.getSenderid());
                MessageContent content = new MessageContent();
                content.setTitle(n.getMsg());
                content.setId(n.getFileid());
                content.setExtension(n.getFileext());
                content.setSecretLevel(Integer.parseInt(n.getLevels()));
                content.setType(Integer.parseInt(n.getFiletype()));
                contactVO.setLastMessage(content);
                contactVO.setFullTime(n.getCreatetime());
                Date sDate = null;
                try {
                    sDate = sf.parse(n.getCreatetime());
                } catch (ParseException e) {
                    log.error("消息日期转换错误");
                    log.error(Common.getExceptionMessage(e));
                    sDate = new Date();
                }
                if(new SimpleDateFormat("yyyy-MM-dd").format(sDate).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){//格式化为相同格式
                    contactVO.setTime(new SimpleDateFormat("HH:mm").format(sDate));
                }else {
                    contactVO.setTime(new SimpleDateFormat("MM-dd").format(sDate));
                }
                contactVO.setAvatar(n.getSenderavatar());
                contactVO.setName(n.getSendername());
                contactVO.setSender("");
                contactVO.setAtMe(false);
                contactVO.setIsTop(false);
                contactVO.setIsMute(false);
                contactVO.setIsGroup(n.getType().equals("GROUP"));
                contactVO.setSecretLevel(Integer.parseInt(n.getSenderlevels()));
                contactVO.setUnreadNum(zzMsgReadRelationService.queryNoReadMsgBySenderAndReceiver(n.getSenderid(),n.getReceiverid()));
            }
            list.add(contactVO);
        });
        Map<String,List<ContactVO>> data=new HashMap<>();
        try {
            Common.putVoNullStringToEmptyString(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(Common.getExceptionMessage(e));
        }
        data.put(id,list);//当前登录人的id作为key，联系人列表作为value
        rabbitMqMsgProducer.sendMsg(data);
        return list;
    }
    /**
     * 修改用户群个性化信息--是否置顶
     * @param userId 用户id；groupId 群id；topFlg 1置顶，0不置顶
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
   @Override
   public String setUserGroupTop(String userId, String gourpId, String topFlg) throws Exception{
        String res="1";
        int i=this.zzUserGroupDao.setUserGroupTop( userId, gourpId, topFlg);
        if(i==0){
            res = "0";
        }
        return  res;
    }
    /**
     * 修改用户群个性化信息--是否置顶
     * @param userId 用户id；groupId 群id；muteFlg 1免打扰，0否
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    @Override
    public String setUserGroupMute(String userId, String gourpId, String topFlg) throws Exception{
        String res="1";
        int i=this.zzUserGroupDao.setUserGroupMute( userId, gourpId, topFlg);
        if(i==0){
            res = "0";
        }
        return  res;
    }
    //获取群里有多少个成员
    @Override
    public int getGroupUserCount(String groupid)throws Exception{
        int i=this.zzUserGroupDao.getGroupUserCount(groupid);
        return  i;
    }
    //获取群前九个人的头像地址
    @Override
    public List<String> getGroupUserHeadList(String groupid)throws Exception{
        List<String> res=this.zzUserGroupDao.getGroupUserHeadList(groupid);
        return  res;
    }

    @Override
    public Long queryInGroup(String userId, String gourpId) throws Exception {
        return this.zzUserGroupDao.queryInGroup(userId,gourpId);
    }
    /**查询会议所有人员，供给系统内部方法调用*/
    @Override
   //@Cacheable(value = CacheConst.userGroupIds,key = "#p0")
    public List<String> getGroupByUserId(String userId){
        List<String> groupList = null;
        //是否含有key
        String key = CacheConst.userGroupIds+":"+userId;
        boolean keyExist = RedisUtil.isKeyExist(key);
        //todo 暂时不走缓存
        keyExist = false;
        if(keyExist){
            //如果含有key 直接返回
            return RedisListUtil.getList(key);
        }else {
            //如果不含有key ，进行缓存
            groupList = this.zzUserGroupDao.getGroupByUserId(userId);
            if(groupList!=null && groupList.size()>0){
                RedisListUtil.putList(key,groupList);
            }

        }
        return groupList;
    }
}