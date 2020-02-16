package com.workhub.z.servicechat.controller.group;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.entity.group.ZzUserGroupMsgTag;
import com.workhub.z.servicechat.service.ZzUserGroupMsgTagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户标记群消息
 *
 * @author zhuqz
 * @since 2019-06-11
 */
@RestController
@RequestMapping("/zzUserGroupMsgTag")
public class ZzUserGroupMsgTagController{
    /**
     * 服务对象
     */
    @Resource
    private ZzUserGroupMsgTagService zzUserGroupMsgTagService;
    /**
     * 增加群消息标记
     * @param userId 用户id；groupId 群id；msgId 消息id；tagType标记类型，默认0重要消息
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    @PostMapping("/addUserGroupMsgTag")
    public ObjectRestResponse addUserGroupMsgTag(@RequestParam("userId") String userId,
                                                 @RequestParam("groupId") String groupId,
                                                 @RequestParam("msgId") String msgId,
                                                 @RequestParam("tagType") String tagType){
        ZzUserGroupMsgTag entity = new ZzUserGroupMsgTag();
        entity.setId(RandomId.getUUID());
        entity.setCreateTime(new Date());
        entity.setUserId(userId);
        entity.setMesId(msgId);
        entity.setTagType((tagType==null||tagType.equals(""))?"0":tagType);
        entity.setGroupId(groupId);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        String oppRes = "1";
        try {
            zzUserGroupMsgTagService.addUserGroupMsgTag(entity);
        }catch (Exception e){
            e.printStackTrace();
            oppRes="-1";
        }
        if("1".equals(oppRes)){
            objectRestResponse.data("成功");//成功
            objectRestResponse.rel(true);
            objectRestResponse.msg("200");
        }else if("-1".equals(oppRes)){
            objectRestResponse.data("失败");//失败
            objectRestResponse.rel(true);
            objectRestResponse.msg("200");
        }
        return objectRestResponse;
    }
    /**
     * 按照id删除群消息标记
     * @param id 表主键
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    @PostMapping("/delUserGroupMsgTagById")
    public ObjectRestResponse delUserGroupMsgTagById(@RequestParam("id") String id){

        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        String oppRes = "1";
        try {
            zzUserGroupMsgTagService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            oppRes="-1";
        }
        if("1".equals(oppRes)){
            objectRestResponse.data("成功");//成功
            objectRestResponse.rel(true);
            objectRestResponse.msg("200");
        }else if("-1".equals(oppRes)){
            objectRestResponse.data("失败");//失败
            objectRestResponse.rel(true);
            objectRestResponse.msg("200");
        }
        return objectRestResponse;
    }
    /**
     * 按照条件删除群消息标记
     * @param userId 用户id；groupId 群id；msgId 消息id；tagType标记类型，默认0重要消息
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    @PostMapping("/delUserGroupMsgTagByConditions")
    public ObjectRestResponse delUserGroupMsgTagByConditions(@RequestParam("userId") String userId,
                                                             @RequestParam("groupId") String groupId,
                                                             @RequestParam("msgId") String msgId,
                                                             @RequestParam("tagType") String tagType){

        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        ZzUserGroupMsgTag entity = new ZzUserGroupMsgTag();
        entity.setUserId(userId);
        entity.setMesId(msgId);
        entity.setTagType(tagType);
        entity.setGroupId(groupId);
        String oppRes = "1";
        try {
            zzUserGroupMsgTagService.deleteByConditions(entity);
        }catch (Exception e){
            e.printStackTrace();
            oppRes="-1";
        }
        if("1".equals(oppRes)){
            objectRestResponse.data("成功");//成功
            objectRestResponse.rel(true);
            objectRestResponse.msg("200");
        }else if("-1".equals(oppRes)){
            objectRestResponse.data("失败");//失败
            objectRestResponse.rel(true);
            objectRestResponse.msg("200");
        }
        return objectRestResponse;
    }
    /**
     * todo:使用
     * 查询用户标记消息
     * @param userId 用户id；groupId 群id；tagType标记类型
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误；数据列表
     * @author zhuqz
     * @since 2019-06-11
     */
    @GetMapping ("/getUserGroupMsgTagList")
    public TableResultResponse getUserGroupMsgTagList(@RequestParam("userId")String userId,
                                              @RequestParam("groupId")String groupId,
                                              @RequestParam("tagType")String tagType,
                                              @RequestParam(value = "page",defaultValue = "1")Integer page,
                                              @RequestParam(value = "size",defaultValue = "10")Integer size){

        TableResultResponse dataList = null;
        try {
            dataList = this.zzUserGroupMsgTagService.getUserGroupMsgTagList(userId,groupId,tagType,page,size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
