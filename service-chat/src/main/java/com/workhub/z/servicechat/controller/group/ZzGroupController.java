package com.workhub.z.servicechat.controller.group;

import com.alibaba.fastjson.JSONArray;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.vo.rpcvo.ContactVO;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.*;
import com.workhub.z.servicechat.entity.group.ZzGroup;
import com.workhub.z.servicechat.model.GroupEditDto;
import com.workhub.z.servicechat.model.GroupEditUserList;
import com.workhub.z.servicechat.redis.RedisUtil;
import com.workhub.z.servicechat.service.AdminUserService;
import com.workhub.z.servicechat.service.ZzGroupService;
import com.workhub.z.servicechat.service.ZzMessageInfoService;
import com.workhub.z.servicechat.service.ZzUserGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tio.core.ChannelContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;

import static com.workhub.z.servicechat.config.VoToEntity.ZzGroupToGroupInfo;

/**
 * 群组表(ZzGroup)表控制层
 *
 * @author 忠
 * @since 2019-05-10 14:29:33
 */
@RestController
@RequestMapping("/zzGroup")
public class ZzGroupController  {
    private static Logger log = LoggerFactory.getLogger(ZzGroupController.class);
    /**
     * 服务对象
     */
    @Resource
    private ZzGroupService zzGroupService;

    @Resource
    private ZzUserGroupService userGroupService;

    @Resource
    private ZzMessageInfoService messageInfoService;

    @Autowired
    private AdminUserService iUserService;
    @Autowired
    private HttpServletRequest request;
    /**
     * 成功
     */
    private static final String SUCCESS = "1";
    /**
     * 失败
     */
    private static final String FAIL = "-1";
    //gate请求属性
    static String pidInHeaderRequest = GateRequestHeaderParamConfig.getPid();
    static String clientIpInHeaderRequest = GateRequestHeaderParamConfig.getClientIp();
    static String userIdInHeaderRequest = GateRequestHeaderParamConfig.getUserId();
    static String userNameInHeaderRequest = GateRequestHeaderParamConfig.getUserName();
    /**
     * 通过主键查询单条数据
     *
     * @param groupId 主键
     * @return 单条数据
     */
    @GetMapping("/getGroupInfo")
    public ObjectRestResponse selectOne(@RequestParam("groupId")String groupId) throws Exception {
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.msg("200");

        GroupInfoVo groupInfo = new GroupInfoVo();
        groupInfo = (GroupInfoVo)ZzGroupToGroupInfo(this.zzGroupService.queryById(groupId));
        groupInfo.setMemberNum(Math.toIntExact(this.zzGroupService.groupUserListTotal(groupId)));
        Common.putVoNullStringToEmptyString(groupInfo);
        objectRestResponse.data(groupInfo);
        return objectRestResponse;
    }

    @PostMapping("/create")
    public ObjectRestResponse insert(@RequestBody ZzGroup zzGroup,@RequestParam("token")String token) throws Exception{
        zzGroup.setGroupId(RandomId.getUUID());
        zzGroup.setCreateTime(new Date());
        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        try {
            Common.putEntityNullToEmptyString(zzGroup);
            if(zzGroup!=null && zzGroup.getIscross().equals("")){
                zzGroup.setIscross("0");
            }
            zzGroup.setCreator(userId);
            zzGroup.setCreatorName(userName);

            zzGroup.setGroupOwnerId(userId);
            zzGroup.setGroupOwnerName(userName);
        }catch (Exception e){
            log.error(Common.getExceptionMessage(e));
        }
        this.zzGroupService.insert(zzGroup);
//        Integer insert = this.zzGroupService.insert(zzGroup);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
//        if (insert == 0){
//            objectRestResponse.data("失败");
//            return objectRestResponse;
//        }
        objectRestResponse.rel(true);
        objectRestResponse.msg("200");
        objectRestResponse.data("成功");
        return objectRestResponse;
    }

    @PostMapping("/update")
    public ObjectRestResponse update(@RequestBody ZzGroup zzGroup){
        String userId = Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        try {
            Common.putEntityNullToEmptyString(zzGroup);
            zzGroup.setCreateTime(null);
            zzGroup.setUpdator(userId);
        }catch (Exception e){
            e.getStackTrace();
        }
        Integer update = this.zzGroupService.update(zzGroup);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.rel(true);
        objectRestResponse.msg("200");
        if (update == null){
            objectRestResponse.rel(false);
            objectRestResponse.data("失败");
            return objectRestResponse;
        }
        objectRestResponse.data("成功");
        return objectRestResponse;
    }

    /**
     * 删除研讨组
     * @Author: dd
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public ObjectRestResponse delete(@RequestParam("id")String id){
        boolean flag = this.zzGroupService.deleteById(id);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.data(flag);
        return objectRestResponse;
    }
    /**
    *@Description:
    *@Param:
    *@return:
    *@Author: 忠
    *@date: 2019/6/11
    */
    @PostMapping("/querygroupuser")
    public TableResultResponse queryGroupUserList(@RequestParam("id")String id,
                                                  @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                  @RequestParam(value = "size",defaultValue = "10")Integer size){
        PageInfo<GroupUserListVo> groupUserListVoPageInfo = null;
        try {
            groupUserListVoPageInfo = this.zzGroupService.groupUserList(id, page, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //int pageSize, int pageNo, int totalPage, long totalCount, List<T> rows
        return new TableResultResponse(groupUserListVoPageInfo.getPageSize(),
                groupUserListVoPageInfo.getPageNum(),
                groupUserListVoPageInfo.getPages(),
                groupUserListVoPageInfo.getTotal(),
                groupUserListVoPageInfo.getList());
    }

    /**
    *@Description: 根据用户id查询用户所在群组信息
    *@Param: 用户id
    *@return: 群组列表
    *@Author: 忠
    *@date: 2019/6/11
    */
    @GetMapping("/queryGroupListByUserId")
    public ListRestResponse queryGroupListByUserId(@RequestParam("userId")String userId) throws Exception {
        List<ZzGroup> groups = this.zzGroupService.queryGroupListByUserId(userId);
        Common.putVoNullStringToEmptyString(groups);
        return new ListRestResponse("200",groups.size(),groups);
    }

    @GetMapping("/queryContactListById2")
    public ListRestResponse queryContactListById2(@RequestParam("userId")String userId) throws Exception {
        List<ContactVO> contactVOS = userGroupService.getContactVOList2(userId);
        Common.putVoNullStringToEmptyString(contactVOS);
//        List<ZzGroup> groups = this.zzGroupService.queryGroupListByUserId(userId);
        return new ListRestResponse("200", contactVOS.size(), contactVOS);
    }
    @GetMapping("/queryContactListById")
    public ListRestResponse queryContactListById(@RequestParam("userId")String userId) throws Exception {
        List<ContactVO> contactVOS = userGroupService.getContactVOList(userId);
        Common.putVoNullStringToEmptyString(contactVOS);
//        List<ZzGroup> groups = this.zzGroupService.queryGroupListByUserId(userId);
        return new ListRestResponse("200", contactVOS.size(), contactVOS);
    }
    @GetMapping("/queryHistoryMessageById2")
    public ListRestResponse queryHistoryMessageById2(@RequestParam("userId")String userId) throws Exception {
//        List<HistoryMessageVo> query = this.groupMsgService.queryHistoryMessageById(userId);
        String res =  messageInfoService.queryContactsMessage2(userId);
        JSONArray myJsonArray = JSONArray.parseArray(res);
        return new ListRestResponse("200", 0,myJsonArray);
    }
    @GetMapping("/queryHistoryMessageById")
    public ListRestResponse queryHistoryMessageById(@RequestParam("userId")String userId) throws Exception {
//        List<HistoryMessageVo> query = this.groupMsgService.queryHistoryMessageById(userId);
        String res =  messageInfoService.queryContactsMessage(userId);
        JSONArray myJsonArray = JSONArray.parseArray(res);
        return new ListRestResponse("200", myJsonArray.size(),myJsonArray);
    }
    /**
     * @MethodName: queryMessageList
     * @Description:
     * @Param: [type 消息类型 USER私人 GROUP群 MEET会议, receiver 接收人]
     * @Return: com.github.hollykunge.security.Common.msg.ListRestResponse
     * @Author: zhuqz
     * @Date: 2019/10/18
     **/
    @GetMapping("/queryMessageList2")
    public ListRestResponse queryMessageList2(@RequestParam("type")String type,@RequestParam("receiver")String receiver) throws Exception {
        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String res =  messageInfoService.queryMessageList2(type,receiver,userId);
        JSONArray myJsonArray = JSONArray.parseArray(res);
        return new ListRestResponse("200", 0,myJsonArray);
    }
    /**
    * @MethodName: queryMessageList
     * @Description:
     * @Param: [type 消息类型 USER私人 GROUP群 MEET会议, receiver 接收人]
     * @Return: com.github.hollykunge.security.Common.msg.ListRestResponse
     * @Author: zhuqz
     * @Date: 2019/10/18
    **/
    @GetMapping("/queryMessageList")
    public ListRestResponse queryMessageList(@RequestParam("type")String type,@RequestParam("receiver")String receiver) throws Exception {
        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String res =  messageInfoService.queryMessageList(type,receiver,userId);
        JSONArray myJsonArray = JSONArray.parseArray(res);
        return new ListRestResponse("200", myJsonArray.size(),myJsonArray);
    }
    /**
     * 逻辑删除群
     * @param groupId 群id;delFlg：删除标记位，1删除，0 不删
     * @return  1成功；-1失败；
     * @author zhuqz
     * @since 2019-06-11
     */
    @PostMapping("/deleteGroupLogic")
    public ObjectRestResponse deleteGroupLogic(@RequestParam("groupId")String groupId,@RequestParam("delFlg")String delFlg) {
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        String oppRes = "1";
        String userId= Common.nulToEmptyString(request.getHeader("userId"));
        try {
            this.zzGroupService.deleteGroupLogic(groupId,delFlg,userId);
        }catch (Exception e){
            e.printStackTrace();
            oppRes = FAIL;
        }
        if(SUCCESS.equals(oppRes)){
            objectRestResponse.data("成功");
            objectRestResponse.rel(true);
            objectRestResponse.msg("200");
        }else{
            objectRestResponse.data("失败");
            objectRestResponse.rel(false);
            objectRestResponse.msg("200");
        }
        return objectRestResponse;
    }


    @GetMapping("/queryHistoryMessageForSingle2")
    public TableResultResponse queryHistoryMessageForSingle2(@RequestParam("userId")String userId,@RequestParam("contactId")String contactId,@RequestParam("isGroup")String isGroup,@RequestParam("query")String query,@RequestParam("page")String page,@RequestParam("size")String size) throws Exception {
        TableResultResponse resultResponse = messageInfoService.queryHistoryMessageForSingle2(userId,contactId,isGroup,query,page,size);
        return resultResponse;
    }
    /**
     * 当前登录人查询具体某个人或者群的聊天记录,contactId表示个人或者群id
     * query 聊天内容、接收人
     * @param userId
     * @param contactId
     * @param isGroup
     * @param query
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @GetMapping("/queryHistoryMessageForSingle")
    public TableResultResponse queryHistoryMessageForSingle(@RequestParam("userId")String userId,@RequestParam("contactId")String contactId,@RequestParam("isGroup")String isGroup,@RequestParam("query")String query,@RequestParam("page")String page,@RequestParam("size")String size) throws Exception {
        TableResultResponse resultResponse = messageInfoService.queryHistoryMessageForSingle(userId,contactId,isGroup,query,page,size);
        return resultResponse;
    }
    /**
     * 查询群成员列表信息
     *
     * @param groupId 主键
     * @return 单条数据
     */
    @GetMapping("/getGroupUserList")
    public ListRestResponse getGroupUserList(@RequestParam("groupId")String groupId) throws Exception {

        String userIds= this.zzGroupService.getGroupUserList(groupId);
        List<ChatAdminUserVo> list  = iUserService.userList(userIds);
        List<UserInfoVo> dataList = new ArrayList<>();
        for(ChatAdminUserVo userTemp:list){
            UserInfoVo vo = new UserInfoVo();
            vo.setId(userTemp.getId());
            vo.setName(userTemp.getName());
            vo.setSecretLevel(userTemp.getSecretLevel());
            vo.setOrgId(userTemp.getOrgCode());
            vo.setOrgName(userTemp.getOrgName());
            vo.setAvatar(userTemp.getAvatar());
            String online = Common.nulToEmptyString(RedisUtil.getValue(CacheConst.userOnlineCahce+ Common.nulToEmptyString(userTemp.getId())));
            vo.setOnline((MessageType.ONLINE+"").equals(online)?"1":"0");
            vo.setPathName(userTemp.getPathName());
            dataList.add(vo);
        }

//        dataList.sort((lhs,rhs)->{
//            if(lhs.getName().equals(rhs.getName())) {
//                return lhs.getOnline().compareTo(rhs.getOnline());
//            } else {
//                return lhs.getName().compareTo(rhs.getName());
//            }
//        });
        dataList.sort(Comparator.comparing(UserInfoVo::getOnline).reversed().thenComparing(UserInfoVo::getName));
        ListRestResponse res=new ListRestResponse("200",dataList.size(),dataList);
        return res;
    }
    /**
     * 群列表监控
     //param:page 页码 size 每页几条;group_name群组名称；creator创建人姓名；level密级；
     // dateBegin创建时间开始；dateEnd创建时间结束；pname项目名称；isclose是否关闭;groupOwnerName群主名称
     * @return
     */
    @GetMapping("/groupListMonitoring")
    public TableResultResponse<GroupVo> groupListMonitoring(@RequestParam Map<String,String> params){
        TableResultResponse<GroupVo> pageInfo = null;
        try {
            pageInfo = this.zzGroupService.groupListMonitoring(params);
        } catch (Exception e) {
            log.error(Common.getExceptionMessage(e));
        }
        return pageInfo;
    }

    /**
     * 解散本研讨组
     */
    @GetMapping("dissolve")
    public ObjectRestResponse dissolve(@RequestParam("groupId") String groupId,ChannelContext channelContext) throws Exception{
        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        zzGroupService.dissolveGroup(groupId,userId,userName);
        return new ObjectRestResponse().rel(true).msg("研讨组已解散...");
    }

    /**
     * 移除研讨组成员
     */
    @GetMapping("removeMember")
    public ObjectRestResponse removeMember(@RequestParam("groupId") String groupId, @RequestParam("userId") String userId) {
        zzGroupService.removeMember(groupId, userId);
        return new ObjectRestResponse().rel(true).msg("成功移除组成员");
    }

    /**
     * 添加研讨组成员
     */
    @GetMapping("addMember")
    public ObjectRestResponse addMember(@RequestParam("groupId") String groupId, @RequestParam("userIds") String userIds,ChannelContext channelContext) {
        zzGroupService.addMember(groupId, userIds);
        return new ObjectRestResponse().rel(true).msg("成功添加组成员");
    }

    /**
     * 群编辑接口
     */
    @PostMapping("editGroup")
    public ObjectRestResponse groupMemberEdit(@RequestBody GroupEditDto groupInfo) throws Exception{
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.rel(true);
        objectRestResponse.msg("编辑成员成功");
        String userId= Common.nulToEmptyString(request.getHeader(userIdInHeaderRequest));
        String userName = URLDecoder.decode(Common.nulToEmptyString(request.getHeader(userNameInHeaderRequest)),"UTF-8");
        //邀请入群，本人必须在群组内
        List<String>  memberList = zzGroupService.queryGroupUserIdListByGroupId(groupInfo.getGroupId());
        if(!memberList.contains(userId)){
            objectRestResponse.rel(false);
            objectRestResponse.msg("操作失败，操作人不在群组内");
        }
        ZzGroup zzGroupNow = zzGroupService.queryById(groupInfo.getGroupId());
        String groupOwner = zzGroupNow.getGroupOwnerId();
        List<GroupEditUserList> userListDtos = groupInfo.getUserList();
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
            objectRestResponse.rel(false);
            objectRestResponse.msg("操作失败，不能删除群主");
        }
        //如果只有群主自己了，解散群
        if(userListDtos.size()==1){
            this.zzGroupService.dissolveGroup(groupInfo.getGroupId(),userId,userName);
            objectRestResponse.msg("群解散成功");
            return objectRestResponse;
        }


        int res = zzGroupService.groupMemberEdit(groupInfo,userId,userName);

        if(res==-1){
            objectRestResponse.rel(false);
            objectRestResponse.msg("操作失败，群组可能已经不存在");
        }else if(res==0){
            objectRestResponse.rel(false);
            objectRestResponse.msg("人员超过上限");
        }

        return objectRestResponse;
    }


    /**
     * todo:使用
     * 获取全部群组信息，包括人员
     */
    @GetMapping("getAllGroupInfo")
    public ObjectRestResponse getAllGroupInfo(@RequestParam String groupId) throws Exception{
        String userIds= this.zzGroupService.getGroupUserList(groupId);
        List<ChatAdminUserVo> list  = iUserService.userList(userIds);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        ZzGroup groupInfo = new ZzGroup();
        groupInfo = this.zzGroupService.queryById(groupId);
        Common.putVoNullStringToEmptyString(groupInfo);
        GroupAllInfoVo groupAllInfo = new GroupAllInfoVo();
        groupAllInfo.setAdminUserList(list);
        groupAllInfo.setGroupInfo(groupInfo);
        objectRestResponse.msg("200");
        objectRestResponse.data(groupAllInfo);

        return objectRestResponse;
    }
}