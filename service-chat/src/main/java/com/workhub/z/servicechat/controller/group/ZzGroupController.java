package com.workhub.z.servicechat.controller.group;

import com.alibaba.fastjson.JSONArray;
import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.vo.rpcvo.ContactVO;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.*;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.group.ZzGroup;
import com.workhub.z.servicechat.feign.IUserService;
import com.workhub.z.servicechat.model.GroupEditDto;
import com.workhub.z.servicechat.service.ZzGroupMsgService;
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
    private ZzGroupMsgService groupMsgService;

    @Resource
    private ZzMessageInfoService messageInfoService;

    @Autowired
    private IUserService iUserService;
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
    /**
     * todo:使用
     * 通过主键查询单条数据
     *
     * @param groupId 主键
     * @return 单条数据
     */
    @GetMapping("/getGroupInfo")
    public ObjectRestResponse selectOne(@RequestParam("groupId")String groupId) throws Exception {
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.msg("200");

        GroupInfoVO groupInfo = new GroupInfoVO();
        groupInfo = (GroupInfoVO)ZzGroupToGroupInfo(this.zzGroupService.queryById(groupId));
        groupInfo.setMemberNum(Math.toIntExact(this.zzGroupService.groupUserListTotal(groupId)));
        common.putVoNullStringToEmptyString(groupInfo);
        objectRestResponse.data(groupInfo);
        return objectRestResponse;
    }
    @Decrypt
    @PostMapping("/create")
    public ObjectRestResponse insert(@RequestBody ZzGroup zzGroup,@RequestParam("token")String token) throws Exception{
        zzGroup.setGroupId(RandomId.getUUID());
        zzGroup.setCreateTime(new Date());
        String userId=common.nulToEmptyString(request.getHeader("userId"));
        String userName = URLDecoder.decode(common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
        try {
            common.putEntityNullToEmptyString(zzGroup);
            if(zzGroup!=null && zzGroup.getIscross().equals("")){
                zzGroup.setIscross("0");
            }
            zzGroup.setCreator(userId);
            zzGroup.setCreatorName(userName);

            zzGroup.setGroupOwnerId(userId);
            zzGroup.setGroupOwnerName(userName);
        }catch (Exception e){
            log.error(common.getExceptionMessage(e));
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

    @Decrypt
    @PostMapping("/update")
    public ObjectRestResponse update(@RequestBody ZzGroup zzGroup){
        String userId = common.nulToEmptyString(request.getHeader("userId"));
        try {
            common.putEntityNullToEmptyString(zzGroup);
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
    @Decrypt
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
     * todo:使用
    *@Description: 根据用户id查询用户所在群组信息
    *@Param: 用户id
    *@return: 群组列表
    *@Author: 忠
    *@date: 2019/6/11
    */
    @GetMapping("/queryGroupListByUserId")
    public ListRestResponse queryGroupListByUserId(@RequestParam("userId")String userId) throws Exception {
        List<ZzGroup> groups = this.zzGroupService.queryGroupListByUserId(userId);
        common.putVoNullStringToEmptyString(groups);
        return new ListRestResponse("200",groups.size(),groups);
    }

    /**
     * todo:使用
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/queryContactListById")
    public ListRestResponse queryContactListById(@RequestParam("userId")String userId) throws Exception {
        List<ContactVO> contactVOS = userGroupService.getContactVOList(userId);
        common.putVoNullStringToEmptyString(contactVOS);
//        List<ZzGroup> groups = this.zzGroupService.queryGroupListByUserId(userId);
        return new ListRestResponse("200", contactVOS.size(), contactVOS);
    }

    /**
     * todo:使用
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/queryHistoryMessageById")
    public ListRestResponse queryHistoryMessageById(@RequestParam("userId")String userId) throws Exception {
//        List<HistoryMessageVO> query = this.groupMsgService.queryHistoryMessageById(userId);
        String res =  messageInfoService.queryContactsMessage(userId);
        JSONArray myJsonArray = JSONArray.parseArray(res);
        return new ListRestResponse("200", 0,myJsonArray);
    }
    /**
     * todo:使用
    * @MethodName: queryMessageList
     * @Description:
     * @Param: [type 消息类型 USER私人 GROUP群 MEET会议, receiver 接收人]
     * @Return: com.github.hollykunge.security.common.msg.ListRestResponse
     * @Author: zhuqz
     * @Date: 2019/10/18
    **/
    @GetMapping("/queryMessageList")
    public ListRestResponse queryMessageList(@RequestParam("type")String type,@RequestParam("receiver")String receiver) throws Exception {
        String userId=common.nulToEmptyString(request.getHeader("userId"));
        String res =  messageInfoService.queryMessageList(type,receiver,userId);
        JSONArray myJsonArray = JSONArray.parseArray(res);
        return new ListRestResponse("200", 0,myJsonArray);
    }
    /**
     * 逻辑删除群
     * @param groupId 群id;delFlg：删除标记位，1删除，0 不删
     * @return  1成功；-1失败；
     * @author zhuqz
     * @since 2019-06-11
     */
    @Decrypt
    @PostMapping("/deleteGroupLogic")
    public ObjectRestResponse deleteGroupLogic(@RequestParam("groupId")String groupId,@RequestParam("delFlg")String delFlg) {
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        String oppRes = "1";
        try {
            this.zzGroupService.deleteGroupLogic(groupId,delFlg);
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

    /**
     * todo:使用
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
     * todo:使用
     * 查询群成员列表信息
     *
     * @param groupId 主键
     * @return 单条数据
     */
    @GetMapping("/getGroupUserList")
    public ListRestResponse getGroupUserList(@RequestParam("groupId")String groupId) throws Exception {

        String userIds= this.zzGroupService.getGroupUserList(groupId);
        List<AdminUser> list  = iUserService.userList(userIds);
        List<UserInfoVO> dataList = new ArrayList<>();
        for(AdminUser userTemp:list){
            UserInfoVO vo = new UserInfoVO();
            vo.setId(userTemp.getId());
            vo.setName(userTemp.getName());
            vo.setSecretLevel(userTemp.getSecretLevel());
            vo.setOrgId(userTemp.getOrgCode());
            vo.setOrgName(userTemp.getOrgName());
            vo.setAvatar(userTemp.getAvatar());
            vo.setOnline(common.isUserOnSocket(userTemp.getId()));
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
        dataList.sort(Comparator.comparing(UserInfoVO::getOnline).reversed().thenComparing(UserInfoVO::getName));
        ListRestResponse res=new ListRestResponse("200",dataList.size(),dataList);
        return res;
    }
    /**
     * todo:使用
     * 群列表监控
     //param:page 页码 size 每页几条;group_name群组名称；creator创建人姓名；level密级；
     // dateBegin创建时间开始；dateEnd创建时间结束；pname项目名称；isclose是否关闭;groupOwnerName群主名称
     * @return
     */
    @GetMapping("/groupListMonitoring")
    public TableResultResponse<GroupVO> groupListMonitoring(@RequestParam Map<String,String> params){
        TableResultResponse<GroupVO> pageInfo = null;
        try {
            pageInfo = this.zzGroupService.groupListMonitoring(params);
        } catch (Exception e) {
            log.error(common.getExceptionMessage(e));
        }
        return pageInfo;
    }

    /**
     * 解散本研讨组
     */
    @GetMapping("dissolve")
    public ObjectRestResponse dissolve(@RequestParam("groupId") String groupId,ChannelContext channelContext) throws Exception{
        String userId=common.nulToEmptyString(request.getHeader("userId"));
        String userName = URLDecoder.decode(common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
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
     * todo:使用
     * 群编辑接口
     */
    @Decrypt
    @PostMapping("editGroup")
    public ObjectRestResponse groupMemberEdit(@RequestBody GroupEditDto groupInfo) throws Exception{
//    public ObjectRestResponse groupMemberEdit(@RequestBody GroupEditDto groupEditDto) throws Exception{
//        GroupEditDto groupEditDto = JSON.parseObject(groupInfo,GroupEditDto.class);
        String userId=common.nulToEmptyString(request.getHeader("userId"));
        String userName = URLDecoder.decode(common.nulToEmptyString(request.getHeader("userName")),"UTF-8");
        int res = zzGroupService.groupMemberEdit(groupInfo,userId,userName);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.rel(true);
        objectRestResponse.msg("编辑成员成功");
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
        List<AdminUser> list  = iUserService.userList(userIds);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        ZzGroup groupInfo = new ZzGroup();
        groupInfo = this.zzGroupService.queryById(groupId);
        common.putVoNullStringToEmptyString(groupInfo);
        GroupAllInfoVo groupAllInfo = new GroupAllInfoVo();
        groupAllInfo.setAdminUserList(list);
        groupAllInfo.setGroupInfo(groupInfo);
        objectRestResponse.msg("200");
        objectRestResponse.data(groupAllInfo);

        return objectRestResponse;
    }
}