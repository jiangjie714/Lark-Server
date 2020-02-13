package com.workhub.z.servicechat.controller.message;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.PrivateFileVO;
import com.workhub.z.servicechat.config.RandomId;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.entity.ZzPrivateMsg;
import com.workhub.z.servicechat.service.ZzPrivateMsgService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 私人消息(ZzPrivateMsg)表控制层
 *
 * @author 忠
 * @since 2019-05-13 10:57:46
 */
@RestController
@RequestMapping("/zzPrivateMsg")
public class ZzPrivateMsgController{
    /**
     * 服务对象
     */
    @Resource
    private ZzPrivateMsgService zzPrivateMsgService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public ObjectRestResponse selectOne(String id) {
        ZzPrivateMsg zzPrivateMsg = this.zzPrivateMsgService.queryById(id);
        ObjectRestResponse<ZzPrivateMsg> res = new ObjectRestResponse<>();
        res.data(zzPrivateMsg);
        res.msg("200");
        res.rel(true);
        return res;
    }

    /**
     * 文件删除(删记录)
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public ObjectRestResponse delFileInfo(@RequestParam("id") String id){
        boolean flag = this.zzPrivateMsgService.deleteById(id);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        objectRestResponse.data("成功");
        return objectRestResponse;
    }

    /**
     * 创建
     * @param zzPrivateMsg
     * @return
     */
    @PostMapping("/create")
    public ObjectRestResponse insert(@RequestBody ZzPrivateMsg zzPrivateMsg){
        zzPrivateMsg.setMsgId(RandomId.getUUID());
//        Integer insert = this.zzPrivateMsgService.insert(zzPrivateMsg);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        try{
            common.putEntityNullToEmptyString(zzPrivateMsg);
        }catch (Exception e){
            e.printStackTrace();
        }
        if("".equals(zzPrivateMsg.getIsDelete())){
            zzPrivateMsg.setIsDelete("0");
        }
        this.zzPrivateMsgService.insert(zzPrivateMsg);
//        if (insert == null){
//            objectRestResponse.data("失败");
//            return objectRestResponse;
//        }
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        objectRestResponse.data("成功");
        return objectRestResponse;
    }

    /**
     * 修改
     * @param zzPrivateMsg
     * @return
     */
    @PostMapping("/update")
    public ObjectRestResponse update(@RequestBody ZzPrivateMsg zzPrivateMsg){
        try{
            common.putEntityNullToEmptyString(zzPrivateMsg);
        }catch (Exception e){
            e.printStackTrace();
        }
        Integer update = this.zzPrivateMsgService.update(zzPrivateMsg);
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        if (update == null){
            objectRestResponse.msg("200");
            objectRestResponse.rel(true);
            objectRestResponse.data("操作失败");
            return objectRestResponse;
        }
        objectRestResponse.msg("200");
        objectRestResponse.rel(true);
        objectRestResponse.data("成功");
        return objectRestResponse;
    }
    /**
     * 查询消息记录（最近+历史+二者并集）
     * @auther zhuqz
     * @param sender：发送人id；receiver：接收人id；begin_time：发送开始时间yyyy-mm-dd;end_time：发送结束时间
     * @return
     */
    @GetMapping("/queryMsg")
    public TableResultResponse<ZzPrivateMsg> queryMsg(@RequestParam("sender") String sender,
                                                      @RequestParam("receiver") String receiver,
                                                      @RequestParam("begin_time") String begin_time,
                                                      @RequestParam("end_time") String end_time,
                                                      @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                      @RequestParam(value = "size",defaultValue = "10")Integer size
                                                      ){
        TableResultResponse<ZzPrivateMsg> res = null;
        Map<String,String> param = new HashMap<>();
        param.put("sender",sender);
        param.put("receiver",receiver);
        param.put("begin_time",begin_time);
        param.put("end_time",end_time);
        param.put("page",String.valueOf(page));
        param.put("size",String.valueOf(size));
        try {
            res=this.zzPrivateMsgService.queryMsg(param);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     *@Description: 根据消息ID查询发送人详细信息
     *@Param:
     *@return: userid
     *@Author: 忠
     *@date: 2019/6/20
     */
    @GetMapping("/getSenderByMsgId")
    public ObjectRestResponse getSenderByMsgId(String msgId) throws  Exception{
        String data = this.zzPrivateMsgService.getSenderByMsgId(msgId);
        ObjectRestResponse res = new ObjectRestResponse();
        res.data(data);
        res.msg("200");
        res.rel(true);
        return res;
    }
    /**
     *@Description: 根据消息ID查询接收人详细信息（若为群组则返回当前群组内userList）
     *@Param:
     *@return: userid,list<userid>
     *@Author: 忠
     *@date: 2019/6/20
     */
    @GetMapping("/getReceiverByMsgId")
    public ObjectRestResponse getReceiverByMsgId(String msgId) throws  Exception{
        String data = this.zzPrivateMsgService.getReceiverByMsgId(msgId);
        ObjectRestResponse res = new ObjectRestResponse();
        res.data(data);
        res.msg("200");
        res.rel(true);
        return res;
    }
    /**
     * todo:使用
     * 私聊文件查询
     * @param
     * @return
     */
    //query 查询文件名称
    @PostMapping("/privateFile")
    public TableResultResponse<PrivateFileVO> getFileList(@RequestParam("userId")String userId,
                                                          @RequestParam("receiver")String receiver,
                                                          @RequestParam("query")String query,
                                                          @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                          @RequestParam(value = "size",defaultValue = "10")Integer size){
        TableResultResponse<PrivateFileVO> pageInfo = null;
        try {
            pageInfo = this.zzPrivateMsgService.getFileList(userId,receiver,query,page,size);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        T data, int pageSize, int pageNo, int totalPage, int totalCount
        return pageInfo;
    }

}