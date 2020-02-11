package com.workhub.z.servicechat.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.workhub.z.servicechat.VO.GroupEditVO;
import com.workhub.z.servicechat.VO.MsgSendStatusVo;
import com.workhub.z.servicechat.VO.SocketMsgVo;
import com.workhub.z.servicechat.VO.SocketTeamBindVo;
import com.workhub.z.servicechat.config.ImageUtil;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.UserInfo;
import com.workhub.z.servicechat.entity.ZzGroup;
import com.workhub.z.servicechat.entity.ZzUserGroup;
import com.workhub.z.servicechat.service.IUserService;
import com.workhub.z.servicechat.model.GroupTaskDto;
import com.workhub.z.servicechat.model.UserListDto;
import com.workhub.z.servicechat.rabbitMq.RabbitMqMsgProducer;
import com.workhub.z.servicechat.service.ZzGroupService;
import com.workhub.z.servicechat.service.ZzGroupStatusService;
import com.workhub.z.servicechat.service.ZzUserGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

import static com.workhub.z.servicechat.config.MessageType.*;
import static com.workhub.z.servicechat.config.RandomId.getUUID;
import static com.workhub.z.servicechat.config.common.imgUrl;

@Service
public class ProcessEditGroup extends AbstractMsgProcessor{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ZzUserGroupService userGroupService;
    @Autowired
    ZzGroupService groupService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ZzGroupStatusService zzGroupStatusService;
    @Autowired
    RabbitMqMsgProducer rabbitMqMsgProducer;
    @Autowired
    RedisTemplate redisTemplate;
    // TODO: 2019/6/4 分类处理群组编辑
    public MsgSendStatusVo processManage(String userId, String message) throws IOException {
        MsgSendStatusVo msgSendStatusVo = new MsgSendStatusVo();
//        GroupTaskDto groupTaskDto = toGroupTaskDto(message);
        GroupTaskDto groupTaskDto = JSONObject.parseObject(message,GroupTaskDto.class);
        switch (groupTaskDto.getType()){
            case GROUP_JOIN_MSG://这个分支目前走不进来了
                // TODO: 2019/6/4 处理加入群组消息，1绑定用户到群组
//                Tio.bindGroup(channelContext,groupTaskDto.getGroupId());
                try {
                     joinGroup(userId,groupTaskDto);
                }catch (Exception e){
                    e.printStackTrace();
                }

//                groupTaskDto.

            case GROUP_INVITE_MSG:
                // TODO: 2019/6/4  1.存入数据库 2.生成群组头像 3.向用户分发加入群组消息
//                createGroup(channelContext,message);
                try {
                    joinGroup(userId,groupTaskDto);
//                    createGroupHeadsImg(groupTaskDto.getGroupId());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case GROUP_EXIT_MSG:
                // TODO: 2019/6/4 退出群组
                    break;
            case GROUP_CLOSE_MSG:
                break;
        }
        return msgSendStatusVo;
    }

    public boolean joinGroup(String userId,GroupTaskDto<ZzGroup> groupTaskDto) throws Exception{
        //如果是加入：人员列表只有一个用户，他们自己；如果是邀请，人员列表可能多个
//        for ( UserListDto userInfo:groupTaskDto.getUserList()){
            ZzUserGroup userGroup = new ZzUserGroup();
            userGroup.setCreatetime(new Date());
            userGroup.setId(getUUID());
            userGroup.setGroupId(groupTaskDto.getGroupId());
            userGroup.setUserId(groupTaskDto.getReviser());
//            userGroupService.insert(userGroup);
            //创建群头像
//            createGroupHeadsImg(groupTaskDto.getGroupId());
//        }
        //todo 发消息后期改成前端连接信息中心
        //todo 改成socket代码规范
        SocketMsgVo msgVo = new SocketMsgVo();
        msgVo.setCode(MessageType.SOCKET_TEAM_BIND);
        msgVo.setSender("");
        msgVo.setReceiver("");
        SocketTeamBindVo socketTeamBindVo  = new SocketTeamBindVo();
        socketTeamBindVo.setTeamId(groupTaskDto.getGroupId());
        List userList = new ArrayList();
        userList.add(userId);
        socketTeamBindVo.setUserList(userList);
        msgVo.setMsg(socketTeamBindVo);
        rabbitMqMsgProducer.sendSocketTeamBindMsg(msgVo);
        //System.out.println(channelContext.userid + "joinGroup" +  groupTaskDto.getGroupId() + groupTaskDto.getZzGroup().getGroupName());
//        Tio.sendToGroup(channelContext.getGroupContext(),groupTaskDto.getGroupId(),super.getWsResponse("爷们来了"));

        /*for ( UserListDto userInfo:groupTaskDto.getUserList()){
            ZzUserGroup userGroup = new ZzUserGroup();
            userGroup.setCreatetime(new Date());
            userGroup.setId(getUUID());
            userGroup.setGroupId(groupTaskDto.getGroupId());
            userGroup.setUserId(userInfo.getUserId());
            userGroupService.insert(userGroup);

            GroupEditVO groupEditVO = new GroupEditVO();
            groupTaskDto.setType(GROUP_INVITE_MSG);
            groupEditVO.setCode(GROUP_EDIT);
            groupTaskDto.setUserList(null);
            groupEditVO.setData(groupTaskDto);
            String msg = JSON.toJSONString(groupEditVO);
            Tio.sendToUser(channelContext.getGroupContext(),userInfo.getUserId(),this.getWsResponse(msg));
        }*/
        return true;
    }
    //生成群头像九宫格
    public boolean createGroupHeadsImg(String groupId) throws Exception{
        //获取群人员数量
        int groupMembers = userGroupService.getGroupUserCount(groupId);
        if(groupMembers>=10){//如果大于十个人
            return true;//返回，不必更新头像，因为之前的九个人头像已经占满了九宫格
        }
        String newGroupHeadsImgPath = "C:\\Users\\Public\\Pictures\\Sample Pictures\\"+groupId+".jpg";
        List<String> memberHeadsList = userGroupService.getGroupUserHeadList(groupId);
        ImageUtil.generate(memberHeadsList,newGroupHeadsImgPath);
        //更新数据库头像
        ZzGroup zzGroup = new ZzGroup();
        zzGroup.setGroupId(groupId);
        zzGroup.setGroupImg(newGroupHeadsImgPath);
        groupService.update(zzGroup);
        return true;
    }
    // TODO: 2019/6/3 退出群组

    //群创建
    @Transactional
    public MsgSendStatusVo createGroup(String userId,String msg) throws IOException {
        MsgSendStatusVo msgSendStatusVo = new MsgSendStatusVo();
        ZzGroup zzGroupInfo = new ZzGroup();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String message = jsonObject.getString("data");
        JSONObject groupJson = JSONObject.parseObject(message);
        JSONArray userJsonArray = JSONObject.parseArray(groupJson.getString("userList"));
        zzGroupInfo.setGroupId(groupJson.getString("groupId"));
        zzGroupInfo.setGroupName(groupJson.getString("groupName"));
        zzGroupInfo.setCreator(groupJson.getString("creator"));
        zzGroupInfo.setGroupOwnerId(groupJson.getString("creator"));
        Map p2 = new HashMap<>(16);
        p2.put("userid",groupJson.getString("creator"));
        UserInfo userInfo = iUserService.getUserInfo(p2);
        if(userInfo!=null){
            zzGroupInfo.setCreatorName(common.nulToEmptyString(userInfo.getName()));
            zzGroupInfo.setGroupOwnerName(common.nulToEmptyString(userInfo.getName()));
        }
        zzGroupInfo.setGroupDescribe(groupJson.getString("groupDescribe"));
        zzGroupInfo.setUpdator(groupJson.getString("updator"));
        zzGroupInfo.setPname(groupJson.getString("pname"));
        zzGroupInfo.setScop(groupJson.getString("scop"));
        zzGroupInfo.setLevels(groupJson.getString("levels"));
        zzGroupInfo.setIsclose("0");
        zzGroupInfo.setIsdelete("0");
        zzGroupInfo.setIspublic("0");
        zzGroupInfo.setCreateTime(new Date());
        zzGroupInfo.setUpdateTime(new Date());
//      根据全部人员生成群组头像
        zzGroupInfo.setGroupImg(imgUrl);
        zzGroupInfo.setIscross(common.nulToEmptyString(groupJson.getString("groupType")));

        GroupEditVO groupEditVO = JSONObject.parseObject(msg,GroupEditVO.class);
        GroupTaskDto groupTaskDto = JSONObject.parseObject(message,GroupTaskDto.class);
        List<UserListDto> userList = new ArrayList<UserListDto>();
        groupTaskDto.setType(GROUP_INVITE_MSG);
        groupTaskDto.setGroupId(zzGroupInfo.getGroupId());
        groupTaskDto.setReviser(zzGroupInfo.getCreator());
        groupTaskDto.setUserList(null);
        groupTaskDto.setTimestamp(zzGroupInfo.getCreateTime());
        groupTaskDto.setZzGroup(zzGroupInfo);
        //通知被邀请人员
        //遍历用户begin
        for (int i = 0; i < userJsonArray.size(); i++) {
            JSONObject userJson = JSONObject.parseObject(userJsonArray.getString(i));
            UserListDto userListDto = new UserListDto();
            userListDto.setUserId(userJson.getString("userId"));
            userListDto.setUserLevels(userJson.getString("userLevels"));
            userListDto.setImg(userJson.getString("img"));
            userList.add(userListDto);
            groupTaskDto.setReviser(userJson.getString("userId"));
            groupEditVO.setCode(GROUP_EDIT);
            groupEditVO.setData(groupTaskDto);
            String res = JSONObject.toJSONString(groupEditVO, SerializerFeature.DisableCircularReferenceDetect);
            //todo 发消息后期改成前端连接信息中心
            SocketMsgVo msgVo = new SocketMsgVo();
            msgVo.setCode(jsonObject.getString("code"));
            msgVo.setSender(userId);
            msgVo.setReceiver(userJson.getString("userId"));
            msgVo.setMsg(res);
            rabbitMqMsgProducer.sendSocketPrivateMsg(msgVo);

            //遍历用户end
            //返回创建结果
            GroupTaskDto groupTaskDto1 = JSONObject.parseObject(message,GroupTaskDto.class);
            groupTaskDto1.setType(CREATE_GROUP_ANS);
            groupTaskDto1.setGroupId(zzGroupInfo.getGroupId());
            groupTaskDto1.setUserList(userList);
            groupTaskDto1.setTimestamp(zzGroupInfo.getCreateTime());
            groupTaskDto1.setZzGroup(zzGroupInfo);
            groupTaskDto1.setReviser(zzGroupInfo.getCreator());
            groupEditVO.setCode(CREATE_GROUP_ANS);
            groupEditVO.setData(groupTaskDto1);
            String res1 = JSONObject.toJSONString(groupEditVO, SerializerFeature.DisableCircularReferenceDetect);
            //todo 发消息后期改成前端连接信息中心
            SocketMsgVo msgVo1 = new SocketMsgVo();
            msgVo1.setCode(jsonObject.getString("code"));
            msgVo1.setSender(userId);
            msgVo1.setReceiver(userJson.getString("userId"));
            msgVo1.setMsg(res1);
            rabbitMqMsgProducer.sendSocketPrivateMsg(msgVo1);
        }

//        groupTaskDto.setUserList(userList);
        /*ArrayList<String> picUrls = new ArrayList<>();
        List<UserListDto> userList = new ArrayList<UserListDto>();
        JSONArray userJsonArray = JSONObject.parseArray(groupJson.getString("userList"));
        for (int i = 0; i < userJsonArray.size(); i++) {
            JSONObject userJson = JSONObject.parseObject(userJsonArray.getString(i));
            picUrls.add(userJson.getString("img"));
            ZzUserGroup zzUserGroup = new ZzUserGroup();
            zzUserGroup.setId(UUIDUtils.generateShortUuid());
            zzUserGroup.setGroupId(zzGroup.getGroupId());
            zzUserGroup.setUserId(userJson.getString("userId"));
            userGroupService.insert(zzUserGroup);
            UserListDto userListDto = new UserListDto();
            userListDto.setUserId(userJson.getString("userId"));
            userListDto.setUserLevels(userJson.getString("userLevels"));
            userListDto.setImg(userJson.getString("img"));
            userList.add(userListDto);
        }
        String newPath = "C:\\Users\\Public\\Pictures\\Sample Pictures\\"+zzGroup.getGroupId()+".jpg";
        ImageUtil.generate(picUrls, newPath);
        zzGroup.setGroupImg(newPath);*/
        // TODO: 2019/6/3 群头像生成
        return msgSendStatusVo;
    }
}
